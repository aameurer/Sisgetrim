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
                usuario.setTipoUsuario(
                                dto.tipoUsuario() != null && !dto.tipoUsuario().isBlank() ? dto.tipoUsuario()
                                                : "Pendente");

                // Segurança: Se for cadastro público (não autenticado como Admin), força
                // ROLE_USER
                org.springframework.security.core.Authentication auth = org.springframework.security.core.context.SecurityContextHolder
                                .getContext().getAuthentication();
                boolean isAdmin = auth != null && auth.getAuthorities().stream()
                                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")
                                                || a.getAuthority().equals("ROLE_MASTER"));

                if (isAdmin) {
                        usuario.setRole(dto.role());
                } else {
                        usuario.setRole("ROLE_USER");
                }

                return usuario;
        }

        public UsuarioResponseDTO toResponseDTO(Usuario entity) {
                java.util.List<String> entidades = entity.getEntidades().stream()
                                .map(e -> e.getNomeEmpresarial())
                                .collect(java.util.stream.Collectors.toList());

                java.util.List<String> cartorios = entity.getCartorios().stream()
                                .map(c -> c.getDenominacao())
                                .collect(java.util.stream.Collectors.toList());

                return new UsuarioResponseDTO(
                                entity.getId(),
                                entity.getNome(),
                                entity.getEmail(),
                                entity.getDocumento(),
                                entity.getRole(),
                                entity.getTipoUsuario(),
                                entity.isAtivo(),
                                entity.getStatus(),
                                entidades,
                                cartorios,
                                entity.getCreatedAt(),
                                entity.getUpdatedAt());
        }
}
