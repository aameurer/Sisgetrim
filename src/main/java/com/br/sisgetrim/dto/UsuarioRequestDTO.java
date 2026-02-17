package com.br.sisgetrim.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UsuarioRequestDTO(
        @NotBlank(message = "O nome é obrigatório") String nome,

        @NotBlank(message = "O e-mail é obrigatório") @Email(message = "E-mail inválido") String email,

        @NotBlank(message = "O documento (CPF/CNPJ) é obrigatório") String documento,

        java.util.List<Long> entidadeIds,
        java.util.List<Long> cartorioIds,
        @NotBlank(message = "A senha é obrigatória") @Size(min = 8, message = "A senha deve ter no mínimo 8 caracteres") @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*(),.?\":{}|<>]).{8,}$", message = "A senha deve conter letras maiúsculas, minúsculas, números e símbolos") String senha,

        @NotBlank(message = "A confirmação de senha é obrigatória") String confirmarSenha,
        String tipoUsuario,
        @NotBlank(message = "A permissão de acesso é obrigatória") String role) {
}
