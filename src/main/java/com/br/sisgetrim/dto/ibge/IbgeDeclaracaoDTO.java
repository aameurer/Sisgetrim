package com.br.sisgetrim.dto.ibge;

import com.fasterxml.jackson.annotation.JsonAlias;
import java.util.List;

public class IbgeDeclaracaoDTO {

    @JsonAlias({ "nomeCartorio", "NomeCartorio" })
    private String nomeCartorio;

    @JsonAlias({ "cns", "Cns" })
    private Integer cns;

    @JsonAlias({ "tipoServico", "TipoServico" })
    private String tipoServico;

    @JsonAlias({ "tipoDeclaracao", "TipoDeclaracao" })
    private String tipoDeclaracao;

    @JsonAlias({ "matricula", "Matricula" })
    private String matricula;

    @JsonAlias({ "transcricao", "Transcricao" })
    private String transcricao;

    @JsonAlias({ "codigoNacionalMatricula", "CodigoNacionalMatricula" })
    private String codigoNacionalMatricula;

    @JsonAlias({ "matriculaNotarialEletronica", "MatriculaNotarialEletronica" })
    private String matriculaNotarialEletronica;

    @JsonAlias({ "tipoOperacaoImobiliaria", "TipoOperacaoImobiliaria" })
    private String tipoOperacaoImobiliaria;

    @JsonAlias({ "descricaoOutrasOperacoesImobiliarias", "DescricaoOutrasOperacoesImobiliarias" })
    private String descricaoOutrasOperacoesImobiliarias;

    @JsonAlias({ "dataLavraturaRegistroAverbacao", "DataLavraturaRegistroAverbacao" })
    private String dataLavraturaRegistroAverbacao;

    @JsonAlias({ "destinacao", "Destinacao" })
    private String destinacao;

    @JsonAlias({ "tipoLogradouro", "TipoLogradouro" })
    private String tipoLogradouro;

    @JsonAlias({ "nomeLogradouro", "NomeLogradouro" })
    private String nomeLogradouro;

    @JsonAlias({ "numeroImovel", "NumeroImovel" })
    private String numeroImovel;

    @JsonAlias({ "complementoEndereco", "ComplementoEndereco" })
    private String complementoEndereco;

    @JsonAlias({ "complementoNumeroImovel", "ComplementoNumeroImovel" })
    private String complementoNumeroImovel;

    @JsonAlias({ "bairro", "Bairro" })
    private String bairro;

    @JsonAlias({ "inscricaoMunicipal", "InscricaoMunicipal" })
    private String inscricaoMunicipal;

    @JsonAlias({ "codigoIbge", "CodigoIbge" })
    private String codigoIbge;

    @JsonAlias({ "denominacao", "Denominacao" })
    private String denominacao;

    @JsonAlias({ "localizacao", "Localizacao" })
    private String localizacao;

    @JsonAlias({ "cib", "Cib" })
    private String cib;

    @JsonAlias({ "alienantes", "Alienantes" })
    private List<IbgePessoaDTO> alienantes;

    @JsonAlias({ "adquirentes", "Adquirentes" })
    private List<IbgePessoaDTO> adquirentes;

    public IbgeDeclaracaoDTO() {
    }

    public String getNomeCartorio() {
        return nomeCartorio;
    }

    public void setNomeCartorio(String nomeCartorio) {
        this.nomeCartorio = nomeCartorio;
    }

    public Integer getCns() {
        return cns;
    }

    public void setCns(Integer cns) {
        this.cns = cns;
    }

    public String getTipoServico() {
        return tipoServico;
    }

    public void setTipoServico(String tipoServico) {
        this.tipoServico = tipoServico;
    }

    public String getTipoDeclaracao() {
        return tipoDeclaracao;
    }

    public void setTipoDeclaracao(String tipoDeclaracao) {
        this.tipoDeclaracao = tipoDeclaracao;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getTranscricao() {
        return transcricao;
    }

    public void setTranscricao(String transcricao) {
        this.transcricao = transcricao;
    }

    public String getCodigoNacionalMatricula() {
        return codigoNacionalMatricula;
    }

    public void setCodigoNacionalMatricula(String codigoNacionalMatricula) {
        this.codigoNacionalMatricula = codigoNacionalMatricula;
    }

    public String getMatriculaNotarialEletronica() {
        return matriculaNotarialEletronica;
    }

    public void setMatriculaNotarialEletronica(String matriculaNotarialEletronica) {
        this.matriculaNotarialEletronica = matriculaNotarialEletronica;
    }

    public String getTipoOperacaoImobiliaria() {
        return tipoOperacaoImobiliaria;
    }

    public void setTipoOperacaoImobiliaria(String tipoOperacaoImobiliaria) {
        this.tipoOperacaoImobiliaria = tipoOperacaoImobiliaria;
    }

    public String getDescricaoOutrasOperacoesImobiliarias() {
        return descricaoOutrasOperacoesImobiliarias;
    }

    public void setDescricaoOutrasOperacoesImobiliarias(String descricaoOutrasOperacoesImobiliarias) {
        this.descricaoOutrasOperacoesImobiliarias = descricaoOutrasOperacoesImobiliarias;
    }

    public String getDataLavraturaRegistroAverbacao() {
        return dataLavraturaRegistroAverbacao;
    }

    public void setDataLavraturaRegistroAverbacao(String dataLavraturaRegistroAverbacao) {
        this.dataLavraturaRegistroAverbacao = dataLavraturaRegistroAverbacao;
    }

    public String getDestinacao() {
        return destinacao;
    }

    public void setDestinacao(String destinacao) {
        this.destinacao = destinacao;
    }

    public String getTipoLogradouro() {
        return tipoLogradouro;
    }

    public void setTipoLogradouro(String tipoLogradouro) {
        this.tipoLogradouro = tipoLogradouro;
    }

    public String getNomeLogradouro() {
        return nomeLogradouro;
    }

    public void setNomeLogradouro(String nomeLogradouro) {
        this.nomeLogradouro = nomeLogradouro;
    }

    public String getNumeroImovel() {
        return numeroImovel;
    }

    public void setNumeroImovel(String numeroImovel) {
        this.numeroImovel = numeroImovel;
    }

    public String getComplementoEndereco() {
        return complementoEndereco;
    }

    public void setComplementoEndereco(String complementoEndereco) {
        this.complementoEndereco = complementoEndereco;
    }

    public String getComplementoNumeroImovel() {
        return complementoNumeroImovel;
    }

    public void setComplementoNumeroImovel(String complementoNumeroImovel) {
        this.complementoNumeroImovel = complementoNumeroImovel;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getInscricaoMunicipal() {
        return inscricaoMunicipal;
    }

    public void setInscricaoMunicipal(String inscricaoMunicipal) {
        this.inscricaoMunicipal = inscricaoMunicipal;
    }

    public String getCodigoIbge() {
        return codigoIbge;
    }

    public void setCodigoIbge(String codigoIbge) {
        this.codigoIbge = codigoIbge;
    }

    public String getDenominacao() {
        return denominacao;
    }

    public void setDenominacao(String denominacao) {
        this.denominacao = denominacao;
    }

    public String getLocalizacao() {
        return localizacao;
    }

    public void setLocalizacao(String localizacao) {
        this.localizacao = localizacao;
    }

    public String getCib() {
        return cib;
    }

    public void setCib(String cib) {
        this.cib = cib;
    }

    public List<IbgePessoaDTO> getAlienantes() {
        return alienantes;
    }

    public void setAlienantes(List<IbgePessoaDTO> alienantes) {
        this.alienantes = alienantes;
    }

    public List<IbgePessoaDTO> getAdquirentes() {
        return adquirentes;
    }

    public void setAdquirentes(List<IbgePessoaDTO> adquirentes) {
        this.adquirentes = adquirentes;
    }
}
