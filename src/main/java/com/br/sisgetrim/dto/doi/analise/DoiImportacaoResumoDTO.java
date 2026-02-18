package com.br.sisgetrim.dto.doi.analise;

import java.time.LocalDateTime;

public record DoiImportacaoResumoDTO(
        Long id,
        String protocolo,
        LocalDateTime dataInsercao,
        Integer qtdRegistros,
        String nomeArquivo,
        String status,
        String statusBadgeClass // Para facilitar o frontend
) {
}
