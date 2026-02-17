package com.br.sisgetrim.model.doi;

import com.br.sisgetrim.model.Entidade;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "doi_imoveis")
public class DoiImovel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "declaracao_id", nullable = false)
    private DoiDeclaracao declaracao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "entidade_id", nullable = false)
    private Entidade entidade;

    // --- IDENTIFICAÇÃO GERAL ---
    @Column(length = 8)
    private String cib;

    @Column(nullable = false, length = 20)
    private String destinacao; // Urbano / Rural

    @Column(name = "indicador_imovel_publico_uniao", nullable = false)
    private boolean indicadorImovelPublicoUniao = false;

    @Column(name = "registro_imobiliario_patrimonial_rip", length = 13)
    private String registroImobiliarioPatrimonialRip;

    @Column(name = "certidao_autorizacao_transferencia_cat", length = 11)
    private String certidaoAutorizacaoTransferenciaCat;

    // --- DOCUMENTAÇÃO ---
    @Column(length = 15)
    private String matricula;

    private Integer transcricao;

    // --- DADOS DE LOCALIZAÇÃO ---
    @Column(name = "inscricao_municipal", length = 45)
    private String inscricaoMunicipal;

    @Column(name = "codigo_ibge_municipio", nullable = false, length = 7)
    private String codigoIbgeMunicipio;

    // --- ÁREAS ---
    @Column(name = "area_imovel", nullable = false, precision = 15, scale = 4)
    private BigDecimal areaImovel;

    @Column(name = "indicador_area_lote_nao_consta", nullable = false)
    private boolean indicadorAreaLoteNaoConsta = false;

    @Column(name = "area_construida", precision = 16, scale = 4)
    private BigDecimal areaConstruida;

    @Column(name = "indicador_area_construida_nao_consta")
    private boolean indicadorAreaConstruidaNaoConsta = false;

    // --- ENDEREÇAMENTO DETALHADO ---
    @Column(name = "tipo_imovel", nullable = false, length = 50)
    private String tipoImovel;

    @Column(name = "tipo_logradouro", nullable = false, length = 30)
    private String tipoLogradouro;

    @Column(name = "nome_logradouro", nullable = false, length = 255)
    private String nomeLogradouro;

    @Column(name = "numero_imovel", nullable = false, length = 10)
    private String numeroImovel;

    @Column(name = "complemento_numero", length = 10)
    private String complementoNumero;

    @Column(name = "complemento_endereco", length = 100)
    private String complementoEndereco;

    @Column(nullable = false, length = 150)
    private String bairro;

    @Column(nullable = false, length = 8)
    private String cep;

    // --- DADOS ESPECÍFICOS RURAL ---
    @Column(name = "codigo_incra", length = 13)
    private String codigoIncra;

    @Column(name = "denominacao_rural", length = 200)
    private String denominacaoRural;

    @Column(name = "localizacao_detalhada", length = 200)
    private String localizacaoDetalhada;

    @Column(name = "municipios_uf_lista", columnDefinition = "TEXT")
    private String municipiosUfLista;

    @Column(name = "data_criacao", updatable = false)
    private LocalDateTime dataCriacao;

    @PrePersist
    protected void onCreate() {
        dataCriacao = LocalDateTime.now();
    }

    public DoiImovel() {
    }

    // --- GETTERS E SETTERS ---
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DoiDeclaracao getDeclaracao() {
        return declaracao;
    }

    public void setDeclaracao(DoiDeclaracao declaracao) {
        this.declaracao = declaracao;
    }

    public Entidade getEntidade() {
        return entidade;
    }

    public void setEntidade(Entidade entidade) {
        this.entidade = entidade;
    }

    public String getCib() {
        return cib;
    }

    public void setCib(String cib) {
        this.cib = cib;
    }

    public String getDestinacao() {
        return destinacao;
    }

    public void setDestinacao(String destinacao) {
        this.destinacao = destinacao;
    }

    public boolean isIndicadorImovelPublicoUniao() {
        return indicadorImovelPublicoUniao;
    }

    public void setIndicadorImovelPublicoUniao(boolean indicadorImovelPublicoUniao) {
        this.indicadorImovelPublicoUniao = indicadorImovelPublicoUniao;
    }

    public String getRegistroImobiliarioPatrimonialRip() {
        return registroImobiliarioPatrimonialRip;
    }

    public void setRegistroImobiliarioPatrimonialRip(String registroImobiliarioPatrimonialRip) {
        this.registroImobiliarioPatrimonialRip = registroImobiliarioPatrimonialRip;
    }

    public String getCertidaoAutorizacaoTransferenciaCat() {
        return certidaoAutorizacaoTransferenciaCat;
    }

    public void setCertidaoAutorizacaoTransferenciaCat(String certidaoAutorizacaoTransferenciaCat) {
        this.certidaoAutorizacaoTransferenciaCat = certidaoAutorizacaoTransferenciaCat;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public Integer getTranscricao() {
        return transcricao;
    }

    public void setTranscricao(Integer transcricao) {
        this.transcricao = transcricao;
    }

    public String getInscricaoMunicipal() {
        return inscricaoMunicipal;
    }

    public void setInscricaoMunicipal(String inscricaoMunicipal) {
        this.inscricaoMunicipal = inscricaoMunicipal;
    }

    public String getCodigoIbgeMunicipio() {
        return codigoIbgeMunicipio;
    }

    public void setCodigoIbgeMunicipio(String codigoIbgeMunicipio) {
        this.codigoIbgeMunicipio = codigoIbgeMunicipio;
    }

    public BigDecimal getAreaImovel() {
        return areaImovel;
    }

    public void setAreaImovel(BigDecimal areaImovel) {
        this.areaImovel = areaImovel;
    }

    public boolean isIndicadorAreaLoteNaoConsta() {
        return indicadorAreaLoteNaoConsta;
    }

    public void setIndicadorAreaLoteNaoConsta(boolean indicadorAreaLoteNaoConsta) {
        this.indicadorAreaLoteNaoConsta = indicadorAreaLoteNaoConsta;
    }

    public BigDecimal getAreaConstruida() {
        return areaConstruida;
    }

    public void setAreaConstruida(BigDecimal areaConstruida) {
        this.areaConstruida = areaConstruida;
    }

    public boolean isIndicadorAreaConstruidaNaoConsta() {
        return indicadorAreaConstruidaNaoConsta;
    }

    public void setIndicadorAreaConstruidaNaoConsta(boolean indicadorAreaConstruidaNaoConsta) {
        this.indicadorAreaConstruidaNaoConsta = indicadorAreaConstruidaNaoConsta;
    }

    public String getTipoImovel() {
        return tipoImovel;
    }

    public void setTipoImovel(String tipoImovel) {
        this.tipoImovel = tipoImovel;
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

    public String getComplementoNumero() {
        return complementoNumero;
    }

    public void setComplementoNumero(String complementoNumero) {
        this.complementoNumero = complementoNumero;
    }

    public String getComplementoEndereco() {
        return complementoEndereco;
    }

    public void setComplementoEndereco(String complementoEndereco) {
        this.complementoEndereco = complementoEndereco;
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

    public String getCodigoIncra() {
        return codigoIncra;
    }

    public void setCodigoIncra(String codigoIncra) {
        this.codigoIncra = codigoIncra;
    }

    public String getDenominacaoRural() {
        return denominacaoRural;
    }

    public void setDenominacaoRural(String denominacaoRural) {
        this.denominacaoRural = denominacaoRural;
    }

    public String getLocalizacaoDetalhada() {
        return localizacaoDetalhada;
    }

    public void setLocalizacaoDetalhada(String localizacaoDetalhada) {
        this.localizacaoDetalhada = localizacaoDetalhada;
    }

    public String getMunicipiosUfLista() {
        return municipiosUfLista;
    }

    public void setMunicipiosUfLista(String municipiosUfLista) {
        this.municipiosUfLista = municipiosUfLista;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }
}
