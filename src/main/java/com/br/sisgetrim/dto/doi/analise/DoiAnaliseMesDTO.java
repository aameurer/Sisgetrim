package com.br.sisgetrim.dto.doi.analise;

import java.util.List;

public record DoiAnaliseMesDTO(
                String nomeMes,
                Integer numeroMes,
                String statusMes, // ATRASO, CONCLUIDO, FUTURO
                String statusCor, // text-red-600, text-green-600, etc.
                List<DoiDeclaracaoResumoDTO> importacoes) {
}
