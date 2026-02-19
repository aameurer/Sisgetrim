package com.br.sisgetrim.model;

import com.br.sisgetrim.model.enums.StatusSubstituto;
import com.br.sisgetrim.model.enums.TipoResponsavel;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "responsavel_cartorio")
public class ResponsavelCartorio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoResponsavel tipo;

    @NotBlank
    @Column(nullable = false)
    private String nome;

    @NotBlank
    @Column(nullable = false)
    private String cpf;

    @NotNull
    @Column(name = "data_nomeacao", nullable = false)
    private LocalDate dataNomeacao;

    @NotNull
    @Column(name = "data_ingresso", nullable = false)
    private LocalDate dataIngresso;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusSubstituto substituto;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cartorio_id", nullable = false)
    private Cartorio cartorio;

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

    public TipoResponsavel getTipo() {
        return tipo;
    }

    public void setTipo(TipoResponsavel tipo) {
        this.tipo = tipo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public LocalDate getDataNomeacao() {
        return dataNomeacao;
    }

    public void setDataNomeacao(LocalDate dataNomeacao) {
        this.dataNomeacao = dataNomeacao;
    }

    public LocalDate getDataIngresso() {
        return dataIngresso;
    }

    public void setDataIngresso(LocalDate dataIngresso) {
        this.dataIngresso = dataIngresso;
    }

    public StatusSubstituto getSubstituto() {
        return substituto;
    }

    public void setSubstituto(StatusSubstituto substituto) {
        this.substituto = substituto;
    }

    public Cartorio getCartorio() {
        return cartorio;
    }

    public void setCartorio(Cartorio cartorio) {
        this.cartorio = cartorio;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
