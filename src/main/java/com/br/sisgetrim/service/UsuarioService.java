package com.br.sisgetrim.service;

import com.br.sisgetrim.dto.UsuarioRequestDTO;
import com.br.sisgetrim.dto.UsuarioResponseDTO;
import com.br.sisgetrim.mapper.UsuarioMapper;
import com.br.sisgetrim.model.Usuario;
import com.br.sisgetrim.repository.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
    private final PasswordEncoder passwordEncoder;
    private final UsuarioMapper usuarioMapper;

    @Autowired
    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder,
            UsuarioMapper usuarioMapper) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.usuarioMapper = usuarioMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String documento) throws UsernameNotFoundException {
        String documentoLimpo = limparDocumento(documento);
        return usuarioRepository.findByDocumento(documentoLimpo)
                .orElseThrow(
                        () -> new UsernameNotFoundException("Usuário não encontrado com o documento: " + documento));
    }

    @Transactional
    public UsuarioResponseDTO cadastrarUsuario(UsuarioRequestDTO dto) {
        logger.info("Iniciando cadastro de usuário: {}", dto.email());

        // Validar se as senhas coincidem
        if (!dto.senha().equals(dto.confirmarSenha())) {
            throw new IllegalArgumentException("As senhas não coincidem!");
        }

        String documentoLimpo = limparDocumento(dto.documento());

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

        Usuario salvo = usuarioRepository.save(usuario);
        logger.info("Usuário cadastrado com sucesso: ID {}", salvo.getId());

        return usuarioMapper.toResponseDTO(salvo);
    }

    public UsuarioResponseDTO buscarPorDocumento(String documento) {
        String documentoLimpo = limparDocumento(documento);
        return usuarioRepository.findByDocumento(documentoLimpo)
                .map(usuarioMapper::toResponseDTO)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));
    }

    public UsuarioResponseDTO buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email)
                .map(usuarioMapper::toResponseDTO)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));
    }

    private String limparDocumento(String documento) {
        if (documento == null)
            return null;
        return documento.replaceAll("[^0-9]", "");
    }
}
