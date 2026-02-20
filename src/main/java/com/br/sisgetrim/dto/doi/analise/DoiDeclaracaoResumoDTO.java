package com.br.sisgetrim.dto.doi.analise;

public record DoiDeclaracaoResumoDTO(
                Long id,
                String protocolo,
                java.time.LocalDateTime dataInsercao,
                java.time.LocalDate dataLavratura,
                String matricula,
                String cartorio, // Substituído nomeArquivo por cartorio
                java.math.BigDecimal valorOperacao,
                String status,
                String statusBadgeClass) {
}
