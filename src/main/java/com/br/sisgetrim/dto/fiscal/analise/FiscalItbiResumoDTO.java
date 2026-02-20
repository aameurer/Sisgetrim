package com.br.sisgetrim.dto.fiscal.analise;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO para exibir o resumo dos dados ITBI na grid de análise.
 */
public record FiscalItbiResumoDTO(
        Long id,
        String itbiNumeroAno,
        LocalDate itbiData,
        String contribuinte,
        String adquirente,
        BigDecimal valorVenalTotal,
        String situacao,
        String badgeClass) {
}
