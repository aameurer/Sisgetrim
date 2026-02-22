package com.br.sisgetrim.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "malha_lotes")
public class MalhaLote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "entidade_id", nullable = false)
    private Entidade entidade;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(name = "data_inicial", nullable = false)
    private LocalDate dataInicial;

    @Column(name = "data_final", nullable = false)
    private LocalDate dataFinal;

    @Column(name = "diferenca_bc_doi", precision = 19, scale = 2)
    private BigDecimal diferencaBcDoi;

    @Column(name = "diferenca_imposto_doi", precision = 19, scale = 2)
    private BigDecimal diferencaImpostoDoi;

    @Column(name = "percentual_abaixo_vvi")
    private Double percentualAbaixoVvi;

    @Column(name = "percentual_abaixo_imposto_doi")
    private Double percentualAbaixoImpostoDoi;

    @Column(name = "considerar_integralizacao_capital")
    private Boolean considerarIntegralizacaoCapital;

    @Column(name = "total_analisado")
    private Integer totalAnalisado = 0;

    @Column(name = "total_divergencias")
    private Integer totalDivergencias = 0;

    @Column(length = 20)
    private String status = "PROCESSANDO";

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

    public Entidade getEntidade() {
        return entidade;
    }

    public void setEntidade(Entidade entidade) {
        this.entidade = entidade;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public LocalDate getDataInicial() {
        return dataInicial;
    }

    public void setDataInicial(LocalDate dataInicial) {
        this.dataInicial = dataInicial;
    }

    public LocalDate getDataFinal() {
        return dataFinal;
    }

    public void setDataFinal(LocalDate dataFinal) {
        this.dataFinal = dataFinal;
    }

    public BigDecimal getDiferencaBcDoi() {
        return diferencaBcDoi;
    }

    public void setDiferencaBcDoi(BigDecimal diferencaBcDoi) {
        this.diferencaBcDoi = diferencaBcDoi;
    }

    public BigDecimal getDiferencaImpostoDoi() {
        return diferencaImpostoDoi;
    }

    public void setDiferencaImpostoDoi(BigDecimal diferencaImpostoDoi) {
        this.diferencaImpostoDoi = diferencaImpostoDoi;
    }

    public Double getPercentualAbaixoVvi() {
        return percentualAbaixoVvi;
    }

    public void setPercentualAbaixoVvi(Double percentualAbaixoVvi) {
        this.percentualAbaixoVvi = percentualAbaixoVvi;
    }

    public Double getPercentualAbaixoImpostoDoi() {
        return percentualAbaixoImpostoDoi;
    }

    public void setPercentualAbaixoImpostoDoi(Double percentualAbaixoImpostoDoi) {
        this.percentualAbaixoImpostoDoi = percentualAbaixoImpostoDoi;
    }

    public Boolean getConsiderarIntegralizacaoCapital() {
        return considerarIntegralizacaoCapital;
    }

    public void setConsiderarIntegralizacaoCapital(Boolean considerarIntegralizacaoCapital) {
        this.considerarIntegralizacaoCapital = considerarIntegralizacaoCapital;
    }

    public Integer getTotalAnalisado() {
        return totalAnalisado;
    }

    public void setTotalAnalisado(Integer totalAnalisado) {
        this.totalAnalisado = totalAnalisado;
    }

    public Integer getTotalDivergencias() {
        return totalDivergencias;
    }

    public void setTotalDivergencias(Integer totalDivergencias) {
        this.totalDivergencias = totalDivergencias;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
