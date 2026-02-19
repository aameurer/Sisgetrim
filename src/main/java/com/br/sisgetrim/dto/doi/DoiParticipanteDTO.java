package com.br.sisgetrim.dto.doi;

import java.math.BigDecimal;
import java.util.List;

public record DoiParticipanteDTO(
                String cpfConjuge,
                String cpfInventariante,
                Boolean indicadorConjuge,
                Boolean indicadorConjugeParticipa,
                Boolean indicadorCpfConjugeIdentificado,
                Boolean indicadorEspolio,
                Boolean indicadorEstrangeiro,
                Boolean indicadorNaoConstaParticipacaoOperacao,
                Boolean indicadorNiIdentificado,
                Boolean indicadorRepresentante,
                String motivoNaoIdentificacaoNi,
                String ni,
                BigDecimal participacao,
                String regimeBens,
                List<DoiRepresentanteDTO> representantes) {
}
