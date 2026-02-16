package com.br.sisgetrim.service;

import com.br.sisgetrim.dto.UsuarioRequestDTO;
import com.br.sisgetrim.dto.UsuarioResponseDTO;
import com.br.sisgetrim.dto.UsuarioUpdateDTO;
import com.br.sisgetrim.dto.SenhaUpdateDTO;
import com.br.sisgetrim.mapper.UsuarioMapper;
import com.br.sisgetrim.model.Usuario;
import com.br.sisgetrim.model.Entidade;
import com.br.sisgetrim.repository.UsuarioRepository;
import com.br.sisgetrim.repository.EntidadeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UsuarioService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(UsuarioService.class);

    private final UsuarioRepository usuarioRepository;
    private final EntidadeRepository entidadeRepository;
    private final PasswordEncoder passwordEncoder;
    private final UsuarioMapper usuarioMapper;
    private final FileService fileService;
    private final org.springframework.security.core.session.SessionRegistry sessionRegistry;

    @Autowired
    public UsuarioService(UsuarioRepository usuarioRepository,
            EntidadeRepository entidadeRepository,
            PasswordEncoder passwordEncoder,
            UsuarioMapper usuarioMapper, FileService fileService,
            org.springframework.security.core.session.SessionRegistry sessionRegistry) {
        this.usuarioRepository = usuarioRepository;
        this.entidadeRepository = entidadeRepository;
        this.passwordEncoder = passwordEncoder;
        this.usuarioMapper = usuarioMapper;
        this.fileService = fileService;
        this.sessionRegistry = sessionRegistry;
    }

    public long contarTotalUsuarios() {
        return usuarioRepository.count();
    }

    public long contarUsuariosOnline() {
        return sessionRegistry.getAllPrincipals().stream()
                .filter(principal -> !sessionRegistry.getAllSessions(principal, false).isEmpty())
                .count();
    }

    @Override
    public UserDetails loadUserByUsername(String documento) throws UsernameNotFoundException {
        String documentoLimpo = limparDocumento(documento);
        return usuarioRepository.findByDocumento(documentoLimpo)
                .orElseThrow(
                        () -> new UsernameNotFoundException("Usuário não encontrado com o documento: " + documento));
    }

    public Usuario buscarPorDocumento(String documento) {
        String documentoLimpo = limparDocumento(documento);
        return usuarioRepository.findByDocumento(documentoLimpo)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));
    }

    @Transactional
    public UsuarioResponseDTO atualizarPerfil(String documento, UsuarioUpdateDTO dto,
            org.springframework.web.multipart.MultipartFile foto, boolean isMaster) {
        Usuario usuario = buscarPorDocumento(documento);

        // Verificar se novo e-mail já existe em outro usuário
        if (!usuario.getEmail().equals(dto.email()) && usuarioRepository.existsByEmail(dto.email())) {
            throw new IllegalStateException("E-mail já cadastrado por outro usuário");
        }

        usuario.setNome(dto.nome());
        usuario.setEmail(dto.email());
        usuario.setTipoUsuario(dto.tipoUsuario());

        // Atualizar Entidade por Link
        if (dto.entidadeId() != null) {
            Entidade entidadeId = entidadeRepository.findById(dto.entidadeId())
                    .orElseThrow(() -> new IllegalArgumentException("Entidade não encontrada"));
            usuario.setEntidade(entidadeId);
        } else {
            usuario.setEntidade(null);
        }

        // Apenas MASTER pode alterar PRIVILÉGIO (Role)
        if (isMaster && dto.role() != null) {
            usuario.setRole(dto.role());
        }

        // Upload de Foto
        if (foto != null && !foto.isEmpty()) {
            // Deletar foto antiga se existir
            if (usuario.getFotoUrl() != null) {
                fileService.deletar(usuario.getFotoUrl());
            }
            String nomeArquivo = fileService.salvar(foto);
            usuario.setFotoUrl(nomeArquivo);
        }

        Usuario salvo = usuarioRepository.save(usuario);

        // Atualizar o contexto de segurança para refletir as mudanças (foto, nome,
        // role) na sessão atual
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            Authentication newAuth = new UsernamePasswordAuthenticationToken(salvo, auth.getCredentials(),
                    salvo.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(newAuth);
        }

        return usuarioMapper.toResponseDTO(salvo);
    }

    @Transactional
    public void alterarSenha(String documento, SenhaUpdateDTO dto) {
        Usuario usuario = buscarPorDocumento(documento);

        // Validar senha atual
        if (!passwordEncoder.matches(dto.senhaAtual(), usuario.getSenha())) {
            throw new IllegalArgumentException("A senha atual está incorreta");
        }

        // Validar se novas senhas coincidem
        if (!dto.novaSenha().equals(dto.confirmarNovaSenha())) {
            throw new IllegalArgumentException("As novas senhas não coincidem");
        }

        usuario.setSenha(passwordEncoder.encode(dto.novaSenha()));
        usuarioRepository.save(usuario);
    }

    @Transactional
    public UsuarioResponseDTO cadastrarUsuario(UsuarioRequestDTO dto) {
        logger.info("Iniciando cadastro de usuário: {}", dto.email());

        // Validar se as senhas coincidem
        if (!dto.senha().equals(dto.confirmarSenha())) {
            throw new IllegalArgumentException("As senhas não coincidem!");
        }

        String documentoLimpo = limparDocumento(dto.documento());

        // Validar CPF/CNPJ (Checksum)
        if (!isDocumentoValido(documentoLimpo)) {
            throw new IllegalArgumentException("CPF ou CNPJ inválido!");
        }

        // Verificar se o documento já existe
        if (usuarioRepository.existsByDocumento(documentoLimpo)) {
            throw new IllegalStateException("CPF/CNPJ já cadastrado");
        }

        // Verificar se o email já existe
        if (usuarioRepository.existsByEmail(dto.email())) {
            throw new IllegalStateException("E-mail já cadastrado");
        }

        Usuario usuario = usuarioMapper.toEntity(dto);
        usuario.setDocumento(documentoLimpo);
        usuario.setSenha(passwordEncoder.encode(dto.senha()));

        // Associar Entidade
        if (dto.entidadeId() != null) {
            Entidade entidade = entidadeRepository.findById(dto.entidadeId())
                    .orElseThrow(() -> new IllegalArgumentException("Entidade não encontrada"));
            usuario.setEntidade(entidade);
        }

        Usuario salvo = usuarioRepository.save(usuario);
        logger.info("Usuário cadastrado com sucesso: ID {}", salvo.getId());

        return usuarioMapper.toResponseDTO(salvo);
    }

    @Transactional
    public void deletarUsuario(Long id) {
        if (id == null)
            return;
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));

        // Deletar foto física se existir
        if (usuario.getFotoUrl() != null) {
            fileService.deletar(usuario.getFotoUrl());
        }

        usuarioRepository.delete(usuario);
    }

    private boolean isDocumentoValido(String documento) {
        if (documento == null)
            return false;
        if (documento.length() == 11)
            return isCpfValido(documento);
        if (documento.length() == 14)
            return isCnpjValido(documento);
        return false;
    }

    private boolean isCpfValido(String cpf) {
        if (cpf.matches("(\\d)\\1{10}"))
            return false;
        try {
            int d1 = 0, d2 = 0;
            int digit1, digit2, rest;
            for (int i = 1; i < 10; i++) {
                int digit = Integer.parseInt(cpf.substring(i - 1, i));
                d1 = d1 + (11 - i) * digit;
                d2 = d2 + (12 - i) * digit;
            }
            rest = (d1 % 11);
            digit1 = (rest < 2) ? 0 : 11 - rest;
            d2 = d2 + 2 * digit1;
            rest = (d2 % 11);
            digit2 = (rest < 2) ? 0 : 11 - rest;
            return Integer.parseInt(cpf.substring(9, 10)) == digit1 &&
                    Integer.parseInt(cpf.substring(10, 11)) == digit2;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isCnpjValido(String cnpj) {
        if (cnpj.matches("(\\d)\\1{13}"))
            return false;
        try {
            int[] weight1 = { 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2 };
            int[] weight2 = { 6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2 };
            int sum = 0;
            for (int i = 0; i < 12; i++)
                sum += Integer.parseInt(cnpj.substring(i, i + 1)) * weight1[i];
            int digit1 = sum % 11 < 2 ? 0 : 11 - (sum % 11);
            sum = 0;
            for (int i = 0; i < 12; i++)
                sum += Integer.parseInt(cnpj.substring(i, i + 1)) * weight2[i];
            sum += digit1 * weight2[12];
            int digit2 = sum % 11 < 2 ? 0 : 11 - (sum % 11);
            return Integer.parseInt(cnpj.substring(12, 13)) == digit1 &&
                    Integer.parseInt(cnpj.substring(13, 14)) == digit2;
        } catch (Exception e) {
            return false;
        }
    }

    private String limparDocumento(String documento) {
        if (documento == null)
            return null;
        return documento.replaceAll("[^0-9]", "");
    }
}
