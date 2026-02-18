package com.br.sisgetrim.dto.doi;

import java.util.List;

public record DoiJsonDTO(
        Long cartorioId,
        String nomeArquivo,
        List<DoiDeclaracaoDTO> declaracoes) {
}
