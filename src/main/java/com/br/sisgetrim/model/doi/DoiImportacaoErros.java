package com.br.sisgetrim.model.doi;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "doi_importacao_erros")
public class DoiImportacaoErros {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "importacao_id", nullable = false)
    private DoiImportacao importacao;

    @Column(name = "linha_json")
    private Integer linhaJson;

    @Column(name = "campo_chave", length = 100)
    private String campoChave;

    @Column(name = "mensagem_erro", columnDefinition = "TEXT")
    private String mensagemErro;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public DoiImportacaoErros() {
    }

    // Getters and Setters
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

    public Integer getLinhaJson() {
        return linhaJson;
    }

    public void setLinhaJson(Integer linhaJson) {
        this.linhaJson = linhaJson;
    }

    public String getCampoChave() {
        return campoChave;
    }

    public void setCampoChave(String campoChave) {
        this.campoChave = campoChave;
    }

    public String getMensagemErro() {
        return mensagemErro;
    }

    public void setMensagemErro(String mensagemErro) {
        this.mensagemErro = mensagemErro;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
