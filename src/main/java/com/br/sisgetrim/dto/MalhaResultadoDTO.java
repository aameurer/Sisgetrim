package com.br.sisgetrim.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * DTO para resultados do cruzamento da Malha Fiscal ITBI.
 * Transporta dados do JOIN entre DOI e FISCAL para a interface.
 */
public class MalhaResultadoDTO {

    private Long doiId;
    private Long fiscalId;
    private String matricula;
    private String cadimoInscricao;
    private String cadimoCib;
    private String cadimoQuadra;
    private String cadimoLote;
    private String cadimoBairroNome;
    private LocalDate dataLavratura;
    private String situacao;
    private BigDecimal valorBaseCalculoDoi;
    private BigDecimal valorVenalFiscal;
    private BigDecimal diferencaValor;
    private String cartorio;
    private List<String> parametrosViolados = new ArrayList<>();

    public MalhaResultadoDTO() {
    }

    // Getters and Setters

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

    public String getCadimoInscricao() {
        return cadimoInscricao;
    }

    public void setCadimoInscricao(String cadimoInscricao) {
        this.cadimoInscricao = cadimoInscricao;
    }

    public String getCadimoCib() {
        return cadimoCib;
    }

    public void setCadimoCib(String cadimoCib) {
        this.cadimoCib = cadimoCib;
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

    public String getSituacao() {
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
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

    public List<String> getParametrosViolados() {
        return parametrosViolados;
    }

    public void setParametrosViolados(List<String> parametrosViolados) {
        this.parametrosViolados = parametrosViolados;
    }

    public void addParametroViolado(String descricao) {
        this.parametrosViolados.add(descricao);
    }

    public String getQuadraLoteBairro() {
        StringBuilder sb = new StringBuilder();
        if (cadimoQuadra != null)
            sb.append("Q:").append(cadimoQuadra);
        if (cadimoLote != null)
            sb.append(" L:").append(cadimoLote);
        if (cadimoBairroNome != null)
            sb.append(" - ").append(cadimoBairroNome);
        return sb.toString().trim();
    }
}
