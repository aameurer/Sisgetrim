package com.br.sisgetrim.model;

import com.br.sisgetrim.model.enums.AtribuicaoCartorio;
import com.br.sisgetrim.model.enums.SituacaoCartorio;
import com.br.sisgetrim.model.enums.SituacaoJuridicaResponsavel;
import com.br.sisgetrim.model.enums.TipoCartorio;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "cartorios", uniqueConstraints = {
        @UniqueConstraint(columnNames = "codigo_cns")
})
public class Cartorio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(name = "codigo_cns", nullable = false, updatable = false)
    private String codigoCns;

    @NotBlank
    @Column(nullable = false)
    private String denominacao;

    @NotNull
    @Column(name = "data_criacao", nullable = false)
    private LocalDate dataCriacao;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SituacaoCartorio situacao;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoCartorio tipo;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "situacao_juridica_responsavel", nullable = false)
    private SituacaoJuridicaResponsavel situacaoJuridicaResponsavel;

    @ElementCollection(targetClass = AtribuicaoCartorio.class)
    @CollectionTable(name = "cartorio_atribuicoes", joinColumns = @JoinColumn(name = "cartorio_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "atribuicao")
    private Set<AtribuicaoCartorio> atribuicoes = new HashSet<>();

    // Localização
    private String bairro;
    private String cep;
    private String endereco;
    private String numero;
    private String telefonePrincipal;
    private String telefoneSecundario;

    @Email
    private String email;

    // Dados CNPJ
    private String cnpj;
    private String razaoSocial;
    private String atividadePrincipal;

    @Column(name = "natureza_juridica", columnDefinition = "varchar(255) default '303-4 - Serviço Notarial e Registral'")
    private String naturezaJuridica = "303-4 - Serviço Notarial e Registral";

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "entidade_id", nullable = false, updatable = false)
    private Entidade entidade;

    @OneToMany(mappedBy = "cartorio", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ResponsavelCartorio> responsaveis = new ArrayList<>();

    @ManyToMany(mappedBy = "cartorios")
    private Set<Usuario> usuarios = new HashSet<>();

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
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

    public String getNaturezaJuridica() {
        return naturezaJuridica;
    }

    public void setNaturezaJuridica(String naturezaJuridica) {
        this.naturezaJuridica = naturezaJuridica;
    }

    public Entidade getEntidade() {
        return entidade;
    }

    public void setEntidade(Entidade entidade) {
        this.entidade = entidade;
    }

    public List<ResponsavelCartorio> getResponsaveis() {
        return responsaveis;
    }

    public void setResponsaveis(List<ResponsavelCartorio> responsaveis) {
        this.responsaveis = responsaveis;
    }

    public Set<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(Set<Usuario> usuarios) {
        this.usuarios = usuarios;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
