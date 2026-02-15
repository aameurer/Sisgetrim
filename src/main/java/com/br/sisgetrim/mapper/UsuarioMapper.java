package com.br.sisgetrim.mapper;

import com.br.sisgetrim.dto.UsuarioRequestDTO;
import com.br.sisgetrim.dto.UsuarioResponseDTO;
import com.br.sisgetrim.model.Usuario;
import org.springframework.stereotype.Component;

@Component
public class UsuarioMapper {

    public Usuario toEntity(UsuarioRequestDTO dto) {
        Usuario usuario = new Usuario();
        usuario.setNome(dto.nome());
        usuario.setEmail(dto.email());
        usuario.setDocumento(dto.documento());
        usuario.setSenha(dto.senha());
        return usuario;
    }

    public UsuarioResponseDTO toResponseDTO(Usuario entity) {
        return new UsuarioResponseDTO(
                entity.getId(),
                entity.getNome(),
                entity.getEmail(),
                entity.getDocumento(),
                entity.getRole(),
                entity.isAtivo(),
                entity.getCreatedAt(),
                entity.getUpdatedAt());
    }
}
