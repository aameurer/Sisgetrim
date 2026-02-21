package com.br.sisgetrim.model.ibge;

import com.br.sisgetrim.model.Entidade;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ibge_declaracoes")
public class IbgeDeclaracao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ibge_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ibge_entidade_id", nullable = false)
    private Entidade entidade;

    @Column(name = "ibge_nome_cartorio", nullable = false)
    private String nomeCartorio;

    @Column(name = "ibge_cns", nullable = false)
    private Integer cns;

    @Column(name = "ibge_tipo_servico", nullable = false)
    private String tipoServico;

    @Column(name = "ibge_tipo_declaracao", nullable = false)
    private String tipoDeclaracao;

    @Column(name = "ibge_matricula", nullable = false)
    private String matricula;

    @Column(name = "ibge_transcricao")
    private String transcricao;

    @Column(name = "ibge_codigo_nacional_matricula")
    private String codigoNacionalMatricula;

    @Column(name = "ibge_matricula_notarial_eletronica")
    private String matriculaNotarialEletronica;

    @Column(name = "ibge_tipo_operacao_imobiliaria", nullable = false)
    private String tipoOperacaoImobiliaria;

    @Column(name = "ibge_descricao_outras_operacoes_imobiliarias")
    private String descricaoOutrasOperacoesImobiliarias;

    @Column(name = "ibge_data_lavratura_registro_averbacao")
    private LocalDate dataLavraturaRegistroAverbacao;

    @Column(name = "ibge_destinacao")
    private String destinacao;

    @Column(name = "ibge_tipo_logradouro")
    private String tipoLogradouro;

    @Column(name = "ibge_nome_logradouro")
    private String nomeLogradouro;

    @Column(name = "ibge_numero_imovel")
    private String numeroImovel;

    @Column(name = "ibge_complemento_endereco")
    private String complementoEndereco;

    @Column(name = "ibge_complemento_numero_imovel")
    private String complementoNumeroImovel;

    @Column(name = "ibge_bairro")
    private String bairro;

    @Column(name = "ibge_inscricao_municipal")
    private String inscricaoMunicipal;

    @Column(name = "ibge_codigo_ibge", length = 20)
    private String codigoIbge;

    @Column(name = "ibge_denominacao")
    private String denominacao;

    @Column(name = "ibge_localizacao")
    private String localizacao;

    @Column(name = "ibge_cib", length = 20)
    private String cib;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ibge_importacao_id")
    private IbgeImportacao ibgeImportacao;

    @OneToMany(mappedBy = "declaracao", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<IbgeAlienante> alienantes = new ArrayList<>();

    @OneToMany(mappedBy = "declaracao", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<IbgeAdquirente> adquirentes = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "ibge_created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "ibge_updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "ibge_ativo")
    private Boolean ativo = true;

    public IbgeDeclaracao() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Entidade getEntidade() {
        return entidade;
    }

    public void setEntidade(Entidade entidade) {
        this.entidade = entidade;
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

    public LocalDate getDataLavraturaRegistroAverbacao() {
        return dataLavraturaRegistroAverbacao;
    }

    public void setDataLavraturaRegistroAverbacao(LocalDate dataLavraturaRegistroAverbacao) {
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

    public IbgeImportacao getIbgeImportacao() {
        return ibgeImportacao;
    }

    public void setIbgeImportacao(IbgeImportacao ibgeImportacao) {
        this.ibgeImportacao = ibgeImportacao;
    }

    public List<IbgeAlienante> getAlienantes() {
        return alienantes;
    }

    public void setAlienantes(List<IbgeAlienante> alienantes) {
        this.alienantes = alienantes;
    }

    public List<IbgeAdquirente> getAdquirentes() {
        return adquirentes;
    }

    public void setAdquirentes(List<IbgeAdquirente> adquirentes) {
        this.adquirentes = adquirentes;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    // Helper methods
    public void addAlienante(IbgeAlienante alienante) {
        alienantes.add(alienante);
        alienante.setDeclaracao(this);
    }

    public void removeAlienante(IbgeAlienante alienante) {
        alienantes.remove(alienante);
        alienante.setDeclaracao(null);
    }

    public void addAdquirente(IbgeAdquirente adquirente) {
        adquirentes.add(adquirente);
        adquirente.setDeclaracao(this);
    }

    public void removeAdquirente(IbgeAdquirente adquirente) {
        adquirentes.remove(adquirente);
        adquirente.setDeclaracao(null);
    }
}
