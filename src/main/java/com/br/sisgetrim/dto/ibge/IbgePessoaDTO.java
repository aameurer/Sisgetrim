package com.br.sisgetrim.dto.ibge;

import com.fasterxml.jackson.annotation.JsonAlias;

public class IbgePessoaDTO {

    @JsonAlias({ "indicadorNiIdentificado", "IndicadorNiIdentificado" })
    private Boolean indicadorNiIdentificado;

    @JsonAlias({ "ni", "Ni" })
    private String ni;

    @JsonAlias({ "motivoNaoIdentificacaoNi", "MotivoNaoIdentificacaoNi" })
    private String motivoNaoIdentificacaoNi;

    @JsonAlias({ "indicadorCpfConjugeIdentificado", "IndicadorCpfConjugeIdentificado" })
    private Boolean indicadorCpfConjugeIdentificado;

    @JsonAlias({ "cpfConjuge", "CpfConjuge" })
    private String cpfConjuge;

    public IbgePessoaDTO() {
    }

    public Boolean getIndicadorNiIdentificado() {
        return indicadorNiIdentificado;
    }

    public void setIndicadorNiIdentificado(Boolean indicadorNiIdentificado) {
        this.indicadorNiIdentificado = indicadorNiIdentificado;
    }

    public String getNi() {
        return ni;
    }

    public void setNi(String ni) {
        this.ni = ni;
    }

    public String getMotivoNaoIdentificacaoNi() {
        return motivoNaoIdentificacaoNi;
    }

    public void setMotivoNaoIdentificacaoNi(String motivoNaoIdentificacaoNi) {
        this.motivoNaoIdentificacaoNi = motivoNaoIdentificacaoNi;
    }

    public Boolean getIndicadorCpfConjugeIdentificado() {
        return indicadorCpfConjugeIdentificado;
    }

    public void setIndicadorCpfConjugeIdentificado(Boolean indicadorCpfConjugeIdentificado) {
        this.indicadorCpfConjugeIdentificado = indicadorCpfConjugeIdentificado;
    }

    public String getCpfConjuge() {
        return cpfConjuge;
    }

    public void setCpfConjuge(String cpfConjuge) {
        this.cpfConjuge = cpfConjuge;
    }
}
