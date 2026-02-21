package com.br.sisgetrim.dto.ibge;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class IbgeImportRequestDTO {

    private String nomeArquivo;
    private Long cartorioId;

    @JsonProperty("declaracoes")
    private List<IbgeDeclaracaoDTO> declaracoes;

    public IbgeImportRequestDTO() {
    }

    public String getNomeArquivo() {
        return nomeArquivo;
    }

    public void setNomeArquivo(String nomeArquivo) {
        this.nomeArquivo = nomeArquivo;
    }

    public Long getCartorioId() {
        return cartorioId;
    }

    public void setCartorioId(Long cartorioId) {
        this.cartorioId = cartorioId;
    }

    public List<IbgeDeclaracaoDTO> getDeclaracoes() {
        return declaracoes;
    }

    public void setDeclaracoes(List<IbgeDeclaracaoDTO> declaracoes) {
        this.declaracoes = declaracoes;
    }
}
