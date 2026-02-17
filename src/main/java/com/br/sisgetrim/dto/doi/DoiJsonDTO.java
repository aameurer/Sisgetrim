package com.br.sisgetrim.dto.doi;

import java.util.List;

public record DoiJsonDTO(
                String nomeArquivo,
                List<DoiDeclaracaoDTO> declaracoes) {
}
