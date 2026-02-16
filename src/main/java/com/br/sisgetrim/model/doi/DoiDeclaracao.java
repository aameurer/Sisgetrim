package com.br.sisgetrim.model.doi;

import com.br.sisgetrim.model.Entidade;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "doi_declaracoes")
public class DoiDeclaracao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "importacao_id", nullable = false)
    private DoiImportacao importacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "entidade_id", nullable = false)
    private Entidade entidade;

    @Column(name = "numero_declaracao", nullable = false)
    private String numeroDeclaracao;

    private String situacao = "RASCUNHO";

    @Column(name = "data_operacao", nullable = false)
    private LocalDate dataOperacao;

    @Column(name = "valor_operacao")
    private BigDecimal valorOperacao;

    @Column(name = "area_imovel")
    private BigDecimal areaImovel;

    @Column(name = "indicador_nao_consta_valor")
    private boolean indicadorNaoConstaValor = false;

    @Column(name = "tipo_imovel")
    private String tipoImovel;

    @Column(name = "municipio_imovel")
    private String municipioImovel;

    @OneToMany(mappedBy = "declaracao", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DoiParticipante> participantes = new ArrayList<>();

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

    public DoiDeclaracao() {
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DoiImportacao getImportacao() {
        return importacao;
    }

    public void setImportacao(DoiImportacao importacao) {
        this.importacao = importacao;
    }

    public Entidade getEntidade() {
        return entidade;
    }

    public void setEntidade(Entidade entidade) {
        this.entidade = entidade;
    }

    public String getNumeroDeclaracao() {
        return numeroDeclaracao;
    }

    public void setNumeroDeclaracao(String numeroDeclaracao) {
        this.numeroDeclaracao = numeroDeclaracao;
    }

    public String getSituacao() {
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    public LocalDate getDataOperacao() {
        return dataOperacao;
    }

    public void setDataOperacao(LocalDate dataOperacao) {
        this.dataOperacao = dataOperacao;
    }

    public BigDecimal getValorOperacao() {
        return valorOperacao;
    }

    public void setValorOperacao(BigDecimal valorOperacao) {
        this.valorOperacao = valorOperacao;
    }

    public BigDecimal getAreaImovel() {
        return areaImovel;
    }

    public void setAreaImovel(BigDecimal areaImovel) {
        this.areaImovel = areaImovel;
    }

    public boolean isIndicadorNaoConstaValor() {
        return indicadorNaoConstaValor;
    }

    public void setIndicadorNaoConstaValor(boolean indicadorNaoConstaValor) {
        this.indicadorNaoConstaValor = indicadorNaoConstaValor;
    }

    public String getTipoImovel() {
        return tipoImovel;
    }

    public void setTipoImovel(String tipoImovel) {
        this.tipoImovel = tipoImovel;
    }

    public String getMunicipioImovel() {
        return municipioImovel;
    }

    public void setMunicipioImovel(String municipioImovel) {
        this.municipioImovel = municipioImovel;
    }

    public List<DoiParticipante> getParticipantes() {
        return participantes;
    }

    public void setParticipantes(List<DoiParticipante> participantes) {
        this.participantes = participantes;
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

    public void addParticipante(DoiParticipante participante) {
        participantes.add(participante);
        participante.setDeclaracao(this);
    }
}
