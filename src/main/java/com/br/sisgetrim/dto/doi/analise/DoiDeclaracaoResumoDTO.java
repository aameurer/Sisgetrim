package com.br.sisgetrim.dto.doi.analise;

import java.time.LocalDateTime;

public record DoiDeclaracaoResumoDTO(
        Long id,
        String protocolo,
        LocalDateTime dataInsercao,
        String matricula, // Alterado de qtdRegistros para matricula
        String nomeArquivo,
        String status,
        String statusBadgeClass) {
}
