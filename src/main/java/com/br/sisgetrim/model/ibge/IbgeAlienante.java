package com.br.sisgetrim.model.ibge;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "ibge_alienantes")
public class IbgeAlienante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ibge_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ibge_declaracao_id", nullable = false)
    private IbgeDeclaracao declaracao;

    @Column(name = "ibge_indicador_ni_identificado", nullable = false)
    private Boolean indicadorNiIdentificado;

    @Column(name = "ibge_ni", nullable = false, length = 20)
    private String ni;

    @Column(name = "ibge_motivo_nao_identificacao_ni")
    private String motivoNaoIdentificacaoNi;

    @Column(name = "ibge_indicador_cpf_conjuge_identificado")
    private Boolean indicadorCpfConjugeIdentificado;

    @Column(name = "ibge_cpf_conjuge", length = 20)
    private String cpfConjuge;

    @CreationTimestamp
    @Column(name = "ibge_created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "ibge_updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "ibge_ativo")
    private Boolean ativo = true;

    public IbgeAlienante() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public IbgeDeclaracao getDeclaracao() {
        return declaracao;
    }

    public void setDeclaracao(IbgeDeclaracao declaracao) {
        this.declaracao = declaracao;
    }

    public Boolean getIndicadorNiIdentificado() {
        return indicadorNiIdentificado;
    }

    public void setIndicadorNiIdentificado(Boolean indicadorNiIdentificado) {
        this.indicadorNiIdentificado = indicadorNiIdentificado;
    }

    public String getNi() {
        return ni;
    }

    public void setNi(String ni) {
        this.ni = ni;
    }

    public String getMotivoNaoIdentificacaoNi() {
        return motivoNaoIdentificacaoNi;
    }

    public void setMotivoNaoIdentificacaoNi(String motivoNaoIdentificacaoNi) {
        this.motivoNaoIdentificacaoNi = motivoNaoIdentificacaoNi;
    }

    public Boolean getIndicadorCpfConjugeIdentificado() {
        return indicadorCpfConjugeIdentificado;
    }

    public void setIndicadorCpfConjugeIdentificado(Boolean indicadorCpfConjugeIdentificado) {
        this.indicadorCpfConjugeIdentificado = indicadorCpfConjugeIdentificado;
    }

    public String getCpfConjuge() {
        return cpfConjuge;
    }

    public void setCpfConjuge(String cpfConjuge) {
        this.cpfConjuge = cpfConjuge;
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

    @Override
    public String toString() {
        return "IbgeAlienante{" +
                "id=" + id +
                ", ni='" + ni + '\'' +
                '}';
    }
}
