package com.br.sisgetrim.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record SenhaUpdateDTO(
                @NotBlank(message = "A senha atual é obrigatória") String senhaAtual,
                @NotBlank(message = "A nova senha é obrigatória") @Size(min = 8, message = "A nova senha deve ter no mínimo 8 caracteres") @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*(),.?\":{}|<>]).{8,}$", message = "A nova senha deve conter letras maiúsculas, minúsculas, números e símbolos") String novaSenha,
                @NotBlank(message = "A confirmação da nova senha é obrigatória") String confirmarNovaSenha) {
}
