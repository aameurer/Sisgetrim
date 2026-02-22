package com.br.sisgetrim.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "malha_resultados")
public class MalhaResultado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lote_id", nullable = false)
    private MalhaLote lote;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "entidade_id", nullable = false)
    private Entidade entidade;

    @Column(name = "doi_id")
    private Long doiId;

    @Column(name = "fiscal_id")
    private Long fiscalId;

    @Column(length = 50)
    private String matricula;

    @Column(length = 50)
    private String cib;

    @Column(name = "cadimo_inscricao", length = 50)
    private String cadimoInscricao;

    @Column(name = "cadimo_quadra", length = 50)
    private String cadimoQuadra;

    @Column(name = "cadimo_lote", length = 50)
    private String cadimoLote;

    @Column(name = "cadimo_bairro_nome", length = 200)
    private String cadimoBairroNome;

    @Column(name = "data_lavratura")
    private LocalDate dataLavratura;

    @Column(name = "situacao_doi", length = 50)
    private String situacaoDoi;

    @Column(name = "valor_base_calculo_doi", precision = 19, scale = 2)
    private BigDecimal valorBaseCalculoDoi;

    @Column(name = "valor_venal_fiscal", precision = 19, scale = 2)
    private BigDecimal valorVenalFiscal;

    @Column(name = "diferenca_valor", precision = 19, scale = 2)
    private BigDecimal diferencaValor;

    @Column(length = 255)
    private String cartorio;

    @Column(name = "parametros_violados", columnDefinition = "TEXT")
    private String parametrosViolados;

    @Column(length = 30)
    private String situacao = "PENDENTE";

    @Column(columnDefinition = "TEXT")
    private String justificativa;

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

    public MalhaLote getLote() {
        return lote;
    }

    public void setLote(MalhaLote lote) {
        this.lote = lote;
    }

    public Entidade getEntidade() {
        return entidade;
    }

    public void setEntidade(Entidade entidade) {
        this.entidade = entidade;
    }

    public Long getDoiId() {
        return doiId;
    }

    public void setDoiId(Long doiId) {
        this.doiId = doiId;
    }

    public Long getFiscalId() {
        return fiscalId;
    }

    public void setFiscalId(Long fiscalId) {
        this.fiscalId = fiscalId;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getCib() {
        return cib;
    }

    public void setCib(String cib) {
        this.cib = cib;
    }

    public String getCadimoInscricao() {
        return cadimoInscricao;
    }

    public void setCadimoInscricao(String cadimoInscricao) {
        this.cadimoInscricao = cadimoInscricao;
    }

    public String getCadimoQuadra() {
        return cadimoQuadra;
    }

    public void setCadimoQuadra(String cadimoQuadra) {
        this.cadimoQuadra = cadimoQuadra;
    }

    public String getCadimoLote() {
        return cadimoLote;
    }

    public void setCadimoLote(String cadimoLote) {
        this.cadimoLote = cadimoLote;
    }

    public String getCadimoBairroNome() {
        return cadimoBairroNome;
    }

    public void setCadimoBairroNome(String cadimoBairroNome) {
        this.cadimoBairroNome = cadimoBairroNome;
    }

    public LocalDate getDataLavratura() {
        return dataLavratura;
    }

    public void setDataLavratura(LocalDate dataLavratura) {
        this.dataLavratura = dataLavratura;
    }

    public String getSituacaoDoi() {
        return situacaoDoi;
    }

    public void setSituacaoDoi(String situacaoDoi) {
        this.situacaoDoi = situacaoDoi;
    }

    public BigDecimal getValorBaseCalculoDoi() {
        return valorBaseCalculoDoi;
    }

    public void setValorBaseCalculoDoi(BigDecimal valorBaseCalculoDoi) {
        this.valorBaseCalculoDoi = valorBaseCalculoDoi;
    }

    public BigDecimal getValorVenalFiscal() {
        return valorVenalFiscal;
    }

    public void setValorVenalFiscal(BigDecimal valorVenalFiscal) {
        this.valorVenalFiscal = valorVenalFiscal;
    }

    public BigDecimal getDiferencaValor() {
        return diferencaValor;
    }

    public void setDiferencaValor(BigDecimal diferencaValor) {
        this.diferencaValor = diferencaValor;
    }

    public String getCartorio() {
        return cartorio;
    }

    public void setCartorio(String cartorio) {
        this.cartorio = cartorio;
    }

    public String getParametrosViolados() {
        return parametrosViolados;
    }

    public void setParametrosViolados(String parametrosViolados) {
        this.parametrosViolados = parametrosViolados;
    }

    public String getSituacao() {
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    public String getJustificativa() {
        return justificativa;
    }

    public void setJustificativa(String justificativa) {
        this.justificativa = justificativa;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
