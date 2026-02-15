package com.br.sisgetrim.dto;

import java.time.LocalDateTime;

public record UsuarioResponseDTO(
        Long id,
        String nome,
        String email,
        String documento,
        String role,
        boolean ativo,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {
}
