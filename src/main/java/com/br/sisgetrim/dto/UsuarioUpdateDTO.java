package com.br.sisgetrim.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UsuarioUpdateDTO(
                @NotBlank(message = "O nome é obrigatório") String nome,
                @NotBlank(message = "O e-mail é obrigatório") @Email(message = "E-mail inválido") String email,
                Long entidadeId,
                String tipoUsuario,
                String role) {
}
