package com.br.sisgetrim.model.doi;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "doi_operacao_imobiliaria")
public class DoiOperacaoImobiliaria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doi_declaracao_id")
    private DoiDeclaracao declaracao;

    @Column(name = "data_negocio_juridico", nullable = false)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dataNegocioJuridico;

    @Column(name = "tipo_operacao_imobiliaria", length = 100, nullable = false)
    private String tipoOperacaoImobiliaria;

    @Column(name = "descricao_outras_operacoes", length = 30)
    private String descricaoOutrasOperacoes;

    @Column(name = "valor_operacao_imobiliaria", precision = 20, scale = 2)
    private BigDecimal valorOperacaoImobiliaria;

    @Column(name = "indicador_nao_consta_valor_operacao")
    private Boolean indicadorNaoConstaValorOperacao = false;

    @Column(name = "valor_base_calculo_itbi_itcmd", precision = 20, scale = 2)
    private BigDecimal valorBaseCalculoItbiItcmd;

    @Column(name = "indicador_nao_consta_base_calculo")
    private Boolean indicadorNaoConstaBaseCalculo = false;

    @Column(name = "forma_pagamento", length = 50, nullable = false)
    private String formaPagamento;

    @Column(name = "indicador_alienacao_fiduciaria")
    private Boolean indicadorAlienacaoFiduciaria;

    @Column(name = "mes_ano_ultima_parcela")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate mesAnoUltimaParcela;

    @Column(name = "valor_pago_ate_data_ato", precision = 20, scale = 2)
    private BigDecimal valorPagoAteDataAto;

    @Column(name = "indicador_permuta_bens", nullable = false)
    private Boolean indicadorPermutaBens = false;

    @Column(name = "indicador_pagamento_dinheiro", nullable = false)
    private Boolean indicadorPagamentoDinheiro = false;

    @Column(name = "valor_pago_moeda_corrente_data_ato", precision = 20, scale = 2)
    private BigDecimal valorPagoMoedaCorrenteDataAto;

    @Column(name = "tipo_parte_transacionada", length = 50, nullable = false)
    private String tipoParteTransacionada;

    @Column(name = "valor_parte_transacionada", precision = 20, scale = 2, nullable = false)
    private BigDecimal valorParteTransacionada;

    @Column(name = "data_criacao", updatable = false)
    private LocalDateTime dataCriacao;

    @PrePersist
    protected void onCreate() {
        dataCriacao = LocalDateTime.now();
    }

    public DoiOperacaoImobiliaria() {
    }

    // Getters e Setters
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

    public LocalDate getDataNegocioJuridico() {
        return dataNegocioJuridico;
    }

    public void setDataNegocioJuridico(LocalDate dataNegocioJuridico) {
        this.dataNegocioJuridico = dataNegocioJuridico;
    }

    public String getTipoOperacaoImobiliaria() {
        return tipoOperacaoImobiliaria;
    }

    public void setTipoOperacaoImobiliaria(String tipoOperacaoImobiliaria) {
        this.tipoOperacaoImobiliaria = tipoOperacaoImobiliaria;
    }

    public String getDescricaoOutrasOperacoes() {
        return descricaoOutrasOperacoes;
    }

    public void setDescricaoOutrasOperacoes(String descricaoOutrasOperacoes) {
        this.descricaoOutrasOperacoes = descricaoOutrasOperacoes;
    }

    public BigDecimal getValorOperacaoImobiliaria() {
        return valorOperacaoImobiliaria;
    }

    public void setValorOperacaoImobiliaria(BigDecimal valorOperacaoImobiliaria) {
        this.valorOperacaoImobiliaria = valorOperacaoImobiliaria;
    }

    public Boolean getIndicadorNaoConstaValorOperacao() {
        return indicadorNaoConstaValorOperacao;
    }

    public void setIndicadorNaoConstaValorOperacao(Boolean indicadorNaoConstaValorOperacao) {
        this.indicadorNaoConstaValorOperacao = indicadorNaoConstaValorOperacao;
    }

    public BigDecimal getValorBaseCalculoItbiItcmd() {
        return valorBaseCalculoItbiItcmd;
    }

    public void setValorBaseCalculoItbiItcmd(BigDecimal valorBaseCalculoItbiItcmd) {
        this.valorBaseCalculoItbiItcmd = valorBaseCalculoItbiItcmd;
    }

    public Boolean getIndicadorNaoConstaBaseCalculo() {
        return indicadorNaoConstaBaseCalculo;
    }

    public void setIndicadorNaoConstaBaseCalculo(Boolean indicadorNaoConstaBaseCalculo) {
        this.indicadorNaoConstaBaseCalculo = indicadorNaoConstaBaseCalculo;
    }

    public String getFormaPagamento() {
        return formaPagamento;
    }

    public void setFormaPagamento(String formaPagamento) {
        this.formaPagamento = formaPagamento;
    }

    public Boolean getIndicadorAlienacaoFiduciaria() {
        return indicadorAlienacaoFiduciaria;
    }

    public void setIndicadorAlienacaoFiduciaria(Boolean indicadorAlienacaoFiduciaria) {
        this.indicadorAlienacaoFiduciaria = indicadorAlienacaoFiduciaria;
    }

    public LocalDate getMesAnoUltimaParcela() {
        return mesAnoUltimaParcela;
    }

    public void setMesAnoUltimaParcela(LocalDate mesAnoUltimaParcela) {
        this.mesAnoUltimaParcela = mesAnoUltimaParcela;
    }

    public BigDecimal getValorPagoAteDataAto() {
        return valorPagoAteDataAto;
    }

    public void setValorPagoAteDataAto(BigDecimal valorPagoAteDataAto) {
        this.valorPagoAteDataAto = valorPagoAteDataAto;
    }

    public Boolean getIndicadorPermutaBens() {
        return indicadorPermutaBens;
    }

    public void setIndicadorPermutaBens(Boolean indicadorPermutaBens) {
        this.indicadorPermutaBens = indicadorPermutaBens;
    }

    public Boolean getIndicadorPagamentoDinheiro() {
        return indicadorPagamentoDinheiro;
    }

    public void setIndicadorPagamentoDinheiro(Boolean indicadorPagamentoDinheiro) {
        this.indicadorPagamentoDinheiro = indicadorPagamentoDinheiro;
    }

    public BigDecimal getValorPagoMoedaCorrenteDataAto() {
        return valorPagoMoedaCorrenteDataAto;
    }

    public void setValorPagoMoedaCorrenteDataAto(BigDecimal valorPagoMoedaCorrenteDataAto) {
        this.valorPagoMoedaCorrenteDataAto = valorPagoMoedaCorrenteDataAto;
    }

    public String getTipoParteTransacionada() {
        return tipoParteTransacionada;
    }

    public void setTipoParteTransacionada(String tipoParteTransacionada) {
        this.tipoParteTransacionada = tipoParteTransacionada;
    }

    public BigDecimal getValorParteTransacionada() {
        return valorParteTransacionada;
    }

    public void setValorParteTransacionada(BigDecimal valorParteTransacionada) {
        this.valorParteTransacionada = valorParteTransacionada;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }
}
