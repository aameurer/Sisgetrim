package com.br.sisgetrim.service;

import com.br.sisgetrim.model.Usuario;
import com.br.sisgetrim.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UsuarioService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String documento) throws UsernameNotFoundException {
        String documentoLimpo = limparDocumento(documento);
        return usuarioRepository.findByDocumento(documentoLimpo)
                .orElseThrow(
                        () -> new UsernameNotFoundException("Usuário não encontrado com o documento: " + documento));
    }

    @Transactional
    public Usuario cadastrarUsuario(Usuario usuario) {
        // Normalizar documento (remover pontos, traços, etc)
        usuario.setDocumento(limparDocumento(usuario.getDocumento()));

        // Verificar se o documento já existe
        if (usuarioRepository.existsByDocumento(usuario.getDocumento())) {
            throw new RuntimeException("CPF/CNPJ já cadastrado");
        }

        // Verificar se o email já existe
        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            throw new RuntimeException("E-mail já cadastrado");
        }

        // Criptografar a senha
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));

        return usuarioRepository.save(usuario);
    }

    public Usuario buscarPorDocumento(String documento) {
        String documentoLimpo = limparDocumento(documento);
        return usuarioRepository.findByDocumento(documentoLimpo)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }

    private String limparDocumento(String documento) {
        if (documento == null)
            return null;
        return documento.replaceAll("[^0-9]", "");
    }

    public Usuario buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }
}
