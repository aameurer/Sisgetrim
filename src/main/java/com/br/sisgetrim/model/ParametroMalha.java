package com.br.sisgetrim.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "parametro_malha")
public class ParametroMalha {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "entidade_id", nullable = false)
    private Entidade entidade;

    @Column(name = "parametro_malha_data_inicial")
    private LocalDate dataInicial;

    @Column(name = "parametro_malha_data_final")
    private LocalDate dataFinal;

    @Column(name = "parametro_malha_diferenca_bc_doi", precision = 19, scale = 2)
    private BigDecimal diferencaBcDoi = BigDecimal.ONE;

    @Column(name = "parametro_malha_diferenca_imposto_doi", precision = 19, scale = 2)
    private BigDecimal diferencaImpostoDoi = BigDecimal.ONE;

    @Column(name = "parametro_malha_percentual_abaixo_vvi")
    private Double percentualAbaixoVvi = 1.0;

    @Column(name = "parametro_malha_percentual_abaixo_imposto_doi")
    private Double percentualAbaixoImpostoDoi = 1.0;

    @Column(name = "parametro_malha_considerar_integralizacao_capital")
    private Boolean considerarIntegralizacaoCapital = true;

    @Column(name = "parametro_malha_malha_valor_reducao_nominal", precision = 19, scale = 2)
    private BigDecimal malhaValorReducaoNominal = new BigDecimal("5000.00");

    @Column(name = "parametro_malha_malha_valor_reducao_relativa", precision = 19, scale = 2)
    private BigDecimal malhaValorReducaoRelativa = new BigDecimal("50.00");

    @Column(name = "parametro_malha_alerta_malha_reducao_nominal", precision = 19, scale = 2)
    private BigDecimal alertaMalhaReducaoNominal = new BigDecimal("999999.99");

    @Column(name = "parametro_malha_alerta_malha_reducao_relativa", precision = 19, scale = 2)
    private BigDecimal alertaMalhaReducaoRelativa = new BigDecimal("100.00");

    @Column(updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private Boolean ativo = true;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
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

    public BigDecimal getMalhaValorReducaoNominal() {
        return malhaValorReducaoNominal;
    }

    public void setMalhaValorReducaoNominal(BigDecimal malhaValorReducaoNominal) {
        this.malhaValorReducaoNominal = malhaValorReducaoNominal;
    }

    public BigDecimal getMalhaValorReducaoRelativa() {
        return malhaValorReducaoRelativa;
    }

    public void setMalhaValorReducaoRelativa(BigDecimal malhaValorReducaoRelativa) {
        this.malhaValorReducaoRelativa = malhaValorReducaoRelativa;
    }

    public BigDecimal getAlertaMalhaReducaoNominal() {
        return alertaMalhaReducaoNominal;
    }

    public void setAlertaMalhaReducaoNominal(BigDecimal alertaMalhaReducaoNominal) {
        this.alertaMalhaReducaoNominal = alertaMalhaReducaoNominal;
    }

    public BigDecimal getAlertaMalhaReducaoRelativa() {
        return alertaMalhaReducaoRelativa;
    }

    public void setAlertaMalhaReducaoRelativa(BigDecimal alertaMalhaReducaoRelativa) {
        this.alertaMalhaReducaoRelativa = alertaMalhaReducaoRelativa;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }
}
