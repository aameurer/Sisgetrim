package com.br.sisgetrim.dto.doi;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record DoiDeclaracaoDTO(
        String numeroDeclaracao,

        @JsonFormat(pattern = "yyyy-MM-dd") LocalDate dataLavraturaRegistroAverbacao,

        BigDecimal valorOperacaoImobiliaria,
        BigDecimal areaImovel,
        boolean indicadorNaoConstaValorOperacaoImobiliaria,
        String tipoImovel,
        String codigoIbge,
        List<DoiParticipanteDTO> alienantes,
        List<DoiParticipanteDTO> adquirentes) {
}
