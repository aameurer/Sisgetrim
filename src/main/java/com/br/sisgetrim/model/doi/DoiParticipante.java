package com.br.sisgetrim.model.doi;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "doi_participantes")
public class DoiParticipante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "declaracao_id", nullable = false)
    private DoiDeclaracao declaracao;

    @Column(name = "tipo_participante", nullable = false)
    private String tipoParticipante; // ALIENANTE, ADQUIRENTE

    @Column(nullable = true)
    private String nome;

    @Column(nullable = false)
    private String documento;

    @Column(name = "percentual_participacao")
    private BigDecimal percentualParticipacao;

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

    public DoiParticipante() {
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

    public String getTipoParticipante() {
        return tipoParticipante;
    }

    public void setTipoParticipante(String tipoParticipante) {
        this.tipoParticipante = tipoParticipante;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public BigDecimal getPercentualParticipacao() {
        return percentualParticipacao;
    }

    public void setPercentualParticipacao(BigDecimal percentualParticipacao) {
        this.percentualParticipacao = percentualParticipacao;
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
}
