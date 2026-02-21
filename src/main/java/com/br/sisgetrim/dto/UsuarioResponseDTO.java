package com.br.sisgetrim.dto;

import java.time.LocalDateTime;

public record UsuarioResponseDTO(
        Long id,
        String nome,
        String email,
        String documento,
        String role,
        String tipoUsuario,
        boolean ativo,
        String status,
        java.util.List<String> entidades,
        java.util.List<String> cartorios,
        String fotoUrl,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {
}
