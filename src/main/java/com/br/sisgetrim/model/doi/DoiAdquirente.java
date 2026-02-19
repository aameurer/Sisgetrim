package com.br.sisgetrim.model.doi;

import com.br.sisgetrim.model.Entidade;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "doi_adquirentes")
public class DoiAdquirente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doi_declaracao_id", nullable = false)
    private DoiDeclaracao declaracao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "entidade_id", nullable = false)
    private Entidade entidade;

    // Identificação do Adquirente
    @Column(name = "indicador_ni_identificado", nullable = false)
    private boolean indicadorNiIdentificado;

    @Column(name = "motivo_nao_identificacao_ni", length = 2)
    private String motivoNaoIdentificacaoNi;

    @Column(length = 14)
    private String ni; // CPF ou CNPJ

    // Participação na Aquisição
    @Column(precision = 7, scale = 4)
    private BigDecimal participacao;

    @Column(name = "indicador_nao_consta_participacao", nullable = false)
    private boolean indicadorNaoConstaParticipacao = false;

    // Status do Adquirente
    @Column(name = "indicador_estrangeiro", nullable = false)
    private boolean indicadorEstrangeiro = false;

    @Column(name = "indicador_espolio", nullable = false)
    private boolean indicadorEspolio = false;

    @Column(name = "cpf_inventariante", length = 11)
    private String cpfInventariante;

    // Dados do Cônjuge
    @Column(name = "indicador_conjuge", nullable = false)
    private boolean indicadorConjuge = false;

    @Column(name = "indicador_conjuge_participa")
    private Boolean indicadorConjugeParticipa;

    @Column(name = "regime_bens", length = 50)
    private String regimeBens;

    @Column(name = "indicador_cpf_conjuge_identificado")
    private Boolean indicadorCpfConjugeIdentificado;

    @Column(name = "cpf_conjuge", length = 11)
    private String cpfConjuge;

    // Representantes (Mandatos/Procurações)
    @Column(name = "indicador_representante", nullable = false)
    private boolean indicadorRepresentante = false;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "representantes")
    private List<String> representantes;

    @Column(name = "data_criacao", updatable = false)
    private LocalDateTime dataCriacao;

    @PrePersist
    protected void onCreate() {
        dataCriacao = LocalDateTime.now();
    }

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

    public Entidade getEntidade() {
        return entidade;
    }

    public void setEntidade(Entidade entidade) {
        this.entidade = entidade;
    }

    public boolean isIndicadorNiIdentificado() {
        return indicadorNiIdentificado;
    }

    public void setIndicadorNiIdentificado(boolean indicadorNiIdentificado) {
        this.indicadorNiIdentificado = indicadorNiIdentificado;
    }

    public String getMotivoNaoIdentificacaoNi() {
        return motivoNaoIdentificacaoNi;
    }

    public void setMotivoNaoIdentificacaoNi(String motivoNaoIdentificacaoNi) {
        this.motivoNaoIdentificacaoNi = motivoNaoIdentificacaoNi;
    }

    public String getNi() {
        return ni;
    }

    public void setNi(String ni) {
        this.ni = ni;
    }

    public BigDecimal getParticipacao() {
        return participacao;
    }

    public void setParticipacao(BigDecimal participacao) {
        this.participacao = participacao;
    }

    public boolean isIndicadorNaoConstaParticipacao() {
        return indicadorNaoConstaParticipacao;
    }

    public void setIndicadorNaoConstaParticipacao(boolean indicadorNaoConstaParticipacao) {
        this.indicadorNaoConstaParticipacao = indicadorNaoConstaParticipacao;
    }

    public boolean isIndicadorEstrangeiro() {
        return indicadorEstrangeiro;
    }

    public void setIndicadorEstrangeiro(boolean indicadorEstrangeiro) {
        this.indicadorEstrangeiro = indicadorEstrangeiro;
    }

    public boolean isIndicadorEspolio() {
        return indicadorEspolio;
    }

    public void setIndicadorEspolio(boolean indicadorEspolio) {
        this.indicadorEspolio = indicadorEspolio;
    }

    public String getCpfInventariante() {
        return cpfInventariante;
    }

    public void setCpfInventariante(String cpfInventariante) {
        this.cpfInventariante = cpfInventariante;
    }

    public boolean isIndicadorConjuge() {
        return indicadorConjuge;
    }

    public void setIndicadorConjuge(boolean indicadorConjuge) {
        this.indicadorConjuge = indicadorConjuge;
    }

    public Boolean getIndicadorConjugeParticipa() {
        return indicadorConjugeParticipa;
    }

    public void setIndicadorConjugeParticipa(Boolean indicadorConjugeParticipa) {
        this.indicadorConjugeParticipa = indicadorConjugeParticipa;
    }

    public String getRegimeBens() {
        return regimeBens;
    }

    public void setRegimeBens(String regimeBens) {
        this.regimeBens = regimeBens;
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

    public boolean isIndicadorRepresentante() {
        return indicadorRepresentante;
    }

    public void setIndicadorRepresentante(boolean indicadorRepresentante) {
        this.indicadorRepresentante = indicadorRepresentante;
    }

    public List<String> getRepresentantes() {
        return representantes;
    }

    public void setRepresentantes(List<String> representantes) {
        this.representantes = representantes;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }
}
