package com.br.sisgetrim.dto.ibge.analise;

import java.time.LocalDate;

public record IbgeDeclaracaoResumoDTO(
        Long id,
        String protocolo,
        LocalDate dataLavratura,
        String cartorio,
        String transmitente,
        String adquirente,
        String situacao,
        String badgeClass) {
}
