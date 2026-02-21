package com.br.sisgetrim.dto;

import com.br.sisgetrim.model.enums.AtribuicaoCartorio;
import com.br.sisgetrim.model.enums.SituacaoCartorio;
import com.br.sisgetrim.model.enums.SituacaoJuridicaResponsavel;
import com.br.sisgetrim.model.enums.TipoCartorio;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public class CartorioRequestDTO {

    private Long id;

    @NotBlank
    private String codigoCns;

    @NotBlank
    private String denominacao;

    @NotNull
    @org.springframework.format.annotation.DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataCriacao;

    @NotNull
    private SituacaoCartorio situacao;

    @NotNull
    private TipoCartorio tipo;

    @NotNull
    private SituacaoJuridicaResponsavel situacaoJuridicaResponsavel;

    @NotNull
    private Set<AtribuicaoCartorio> atribuicoes;

    private String bairro;
    private String cep;
    private String endereco;
    private String numero;
    private String telefonePrincipal;
    private String telefoneSecundario;

    @Email
    private String email;

    @org.hibernate.validator.constraints.br.CNPJ
    private String cnpj;
    private String razaoSocial;
    private String atividadePrincipal;

    private List<ResponsavelCartorioRequestDTO> responsaveis;

    public CartorioRequestDTO() {
    }

    public CartorioRequestDTO(Long id, String codigoCns, String denominacao, LocalDate dataCriacao,
            SituacaoCartorio situacao, TipoCartorio tipo, SituacaoJuridicaResponsavel situacaoJuridicaResponsavel,
            Set<AtribuicaoCartorio> atribuicoes, String bairro, String cep, String endereco, String numero,
            String telefonePrincipal, String telefoneSecundario, String email, String cnpj, String razaoSocial,
            String atividadePrincipal, List<ResponsavelCartorioRequestDTO> responsaveis) {
        this.id = id;
        this.codigoCns = codigoCns;
        this.denominacao = denominacao;
        this.dataCriacao = dataCriacao;
        this.situacao = situacao;
        this.tipo = tipo;
        this.situacaoJuridicaResponsavel = situacaoJuridicaResponsavel;
        this.atribuicoes = atribuicoes;
        this.bairro = bairro;
        this.cep = cep;
        this.endereco = endereco;
        this.numero = numero;
        this.telefonePrincipal = telefonePrincipal;
        this.telefoneSecundario = telefoneSecundario;
        this.email = email;
        this.cnpj = cnpj;
        this.razaoSocial = razaoSocial;
        this.atividadePrincipal = atividadePrincipal;
        this.responsaveis = responsaveis;
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigoCns() {
        return codigoCns;
    }

    public void setCodigoCns(String codigoCns) {
        this.codigoCns = codigoCns;
    }

    public String getDenominacao() {
        return denominacao;
    }

    public void setDenominacao(String denominacao) {
        this.denominacao = denominacao;
    }

    public LocalDate getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDate dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public SituacaoCartorio getSituacao() {
        return situacao;
    }

    public void setSituacao(SituacaoCartorio situacao) {
        this.situacao = situacao;
    }

    public TipoCartorio getTipo() {
        return tipo;
    }

    public void setTipo(TipoCartorio tipo) {
        this.tipo = tipo;
    }

    public SituacaoJuridicaResponsavel getSituacaoJuridicaResponsavel() {
        return situacaoJuridicaResponsavel;
    }

    public void setSituacaoJuridicaResponsavel(SituacaoJuridicaResponsavel situacaoJuridicaResponsavel) {
        this.situacaoJuridicaResponsavel = situacaoJuridicaResponsavel;
    }

    public Set<AtribuicaoCartorio> getAtribuicoes() {
        return atribuicoes;
    }

    public void setAtribuicoes(Set<AtribuicaoCartorio> atribuicoes) {
        this.atribuicoes = atribuicoes;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getTelefonePrincipal() {
        return telefonePrincipal;
    }

    public void setTelefonePrincipal(String telefonePrincipal) {
        this.telefonePrincipal = telefonePrincipal;
    }

    public String getTelefoneSecundario() {
        return telefoneSecundario;
    }

    public void setTelefoneSecundario(String telefoneSecundario) {
        this.telefoneSecundario = telefoneSecundario;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getRazaoSocial() {
        return razaoSocial;
    }

    public void setRazaoSocial(String razaoSocial) {
        this.razaoSocial = razaoSocial;
    }

    public String getAtividadePrincipal() {
        return atividadePrincipal;
    }

    public void setAtividadePrincipal(String atividadePrincipal) {
        this.atividadePrincipal = atividadePrincipal;
    }

    public List<ResponsavelCartorioRequestDTO> getResponsaveis() {
        return responsaveis;
    }

    public void setResponsaveis(List<ResponsavelCartorioRequestDTO> responsaveis) {
        this.responsaveis = responsaveis;
    }

    @Override
    public String toString() {
        return "CartorioRequestDTO [codigoCns=" + codigoCns + ", denominacao=" + denominacao + "]";
    }
}
