package com.br.sisgetrim.model.doi;

import com.br.sisgetrim.model.Entidade;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;
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

    // --- CAMPOS DO MANUAL SQL ---
    @Column(name = "tipo_declaracao", length = 20, nullable = false)
    private String tipoDeclaracao;

    @Column(name = "tipo_servico", length = 50, nullable = false)
    private String tipoServico;

    @Column(name = "data_lavratura", nullable = false)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dataLavratura;

    @Column(name = "tipo_ato", length = 100, nullable = false)
    private String tipoAto;

    @Column(name = "tipo_livro", length = 50)
    private String tipoLivro;

    @Column(name = "numero_livro", length = 7)
    private String numeroLivro;

    @Column(nullable = false, length = 7)
    private String folha;

    @Column(name = "mne_eletronica", length = 24)
    private String mneEletronica;

    @Column(unique = true, length = 15)
    private String matricula;

    private Integer transcricao;

    @Column(name = "cnm_codigo_nacional", length = 20)
    private String cnmCodigoNacional;

    @Column(name = "num_registro_averbacao", length = 7)
    private String numRegistroAverbacao;

    @Column(name = "natureza_titulo", length = 100)
    private String naturezaTitulo;

    @Column(name = "numero_registro_td", length = 30)
    private String numeroRegistroTd;

    @Column(name = "existe_doi_anterior", nullable = false)
    private boolean existeDoiAnterior = false;

    @Column(name = "data_cadastro", updatable = false)
    private LocalDateTime dataCadastro;

    // --- CAMPOS DE APOIO AO SISTEMA ---
    @Column(name = "area_imovel")
    private BigDecimal areaImovel;

    @Column(name = "codigo_ibge")
    private String codigoIbge;

    @Column(name = "log_erros", columnDefinition = "TEXT")
    private String logErros;

    @Column(name = "situacao")
    private String situacao = "RASCUNHO";

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Campos complementares da DOI 2025 (Permanecem na declaração para controle de
    // ato)
    @Column(name = "retificacao_ato")
    private boolean retificacaoAto = false;

    @Column(name = "indicador_imovel_publico_uniao")
    private boolean indicadorImovelPublicoUniao = false;

    @Column(name = "indicador_area_construida_nao_consta")
    private boolean indicadorAreaConstruidaNaoConsta = false;

    @Column(name = "indicador_area_lote_nao_consta")
    private boolean indicadorAreaLoteNaoConsta = false;

    // Relacionamento com a nova estrutura de operação (DOI 2025)
    @OneToOne(mappedBy = "declaracao", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private DoiOperacaoImobiliaria operacao;

    @OneToOne(mappedBy = "declaracao", cascade = CascadeType.ALL, orphanRemoval = true)
    private DoiImovel dadosImovel;

    @OneToMany(mappedBy = "declaracao", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DoiAlienante> alienantes = new ArrayList<>();

    @OneToMany(mappedBy = "declaracao", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DoiAdquirente> adquirentes = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        dataCadastro = LocalDateTime.now();
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

    public String getTipoDeclaracao() {
        return tipoDeclaracao;
    }

    public void setTipoDeclaracao(String tipoDeclaracao) {
        this.tipoDeclaracao = tipoDeclaracao;
    }

    public String getTipoServico() {
        return tipoServico;
    }

    public void setTipoServico(String tipoServico) {
        this.tipoServico = tipoServico;
    }

    public LocalDate getDataLavratura() {
        return dataLavratura;
    }

    public void setDataLavratura(LocalDate dataLavratura) {
        this.dataLavratura = dataLavratura;
    }

    public String getTipoAto() {
        return tipoAto;
    }

    public void setTipoAto(String tipoAto) {
        this.tipoAto = tipoAto;
    }

    public String getTipoLivro() {
        return tipoLivro;
    }

    public void setTipoLivro(String tipoLivro) {
        this.tipoLivro = tipoLivro;
    }

    public String getNumeroLivro() {
        return numeroLivro;
    }

    public void setNumeroLivro(String numeroLivro) {
        this.numeroLivro = numeroLivro;
    }

    public String getFolha() {
        return folha;
    }

    public void setFolha(String folha) {
        this.folha = folha;
    }

    public String getMneEletronica() {
        return mneEletronica;
    }

    public void setMneEletronica(String mneEletronica) {
        this.mneEletronica = mneEletronica;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public Integer getTranscricao() {
        return transcricao;
    }

    public void setTranscricao(Integer transcricao) {
        this.transcricao = transcricao;
    }

    public String getCnmCodigoNacional() {
        return cnmCodigoNacional;
    }

    public void setCnmCodigoNacional(String cnmCodigoNacional) {
        this.cnmCodigoNacional = cnmCodigoNacional;
    }

    public String getNumRegistroAverbacao() {
        return numRegistroAverbacao;
    }

    public void setNumRegistroAverbacao(String numRegistroAverbacao) {
        this.numRegistroAverbacao = numRegistroAverbacao;
    }

    public String getNaturezaTitulo() {
        return naturezaTitulo;
    }

    public void setNaturezaTitulo(String naturezaTitulo) {
        this.naturezaTitulo = naturezaTitulo;
    }

    public String getNumeroRegistroTd() {
        return numeroRegistroTd;
    }

    public void setNumeroRegistroTd(String numeroRegistroTd) {
        this.numeroRegistroTd = numeroRegistroTd;
    }

    public boolean isExisteDoiAnterior() {
        return existeDoiAnterior;
    }

    public void setExisteDoiAnterior(boolean existeDoiAnterior) {
        this.existeDoiAnterior = existeDoiAnterior;
    }

    public LocalDateTime getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(LocalDateTime dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public BigDecimal getAreaImovel() {
        return areaImovel;
    }

    public void setAreaImovel(BigDecimal areaImovel) {
        this.areaImovel = areaImovel;
    }

    public String getCodigoIbge() {
        return codigoIbge;
    }

    public void setCodigoIbge(String codigoIbge) {
        this.codigoIbge = codigoIbge;
    }

    public String getSituacao() {
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    public String getLogErros() {
        return logErros;
    }

    public void setLogErros(String logErros) {
        this.logErros = logErros;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public DoiOperacaoImobiliaria getOperacao() {
        return operacao;
    }

    public void setOperacao(DoiOperacaoImobiliaria operacao) {
        this.operacao = operacao;
        if (operacao != null)
            operacao.setDeclaracao(this);
    }

    public boolean isRetificacaoAto() {
        return retificacaoAto;
    }

    public void setRetificacaoAto(boolean retificacaoAto) {
        this.retificacaoAto = retificacaoAto;
    }

    public boolean isIndicadorImovelPublicoUniao() {
        return indicadorImovelPublicoUniao;
    }

    public void setIndicadorImovelPublicoUniao(boolean indicadorImovelPublicoUniao) {
        this.indicadorImovelPublicoUniao = indicadorImovelPublicoUniao;
    }

    public boolean isIndicadorAreaConstruidaNaoConsta() {
        return indicadorAreaConstruidaNaoConsta;
    }

    public void setIndicadorAreaConstruidaNaoConsta(boolean indicadorAreaConstruidaNaoConsta) {
        this.indicadorAreaConstruidaNaoConsta = indicadorAreaConstruidaNaoConsta;
    }

    public boolean isIndicadorAreaLoteNaoConsta() {
        return indicadorAreaLoteNaoConsta;
    }

    public void setIndicadorAreaLoteNaoConsta(boolean indicadorAreaLoteNaoConsta) {
        this.indicadorAreaLoteNaoConsta = indicadorAreaLoteNaoConsta;
    }

    public DoiImovel getDadosImovel() {
        return dadosImovel;
    }

    public void setDadosImovel(DoiImovel dadosImovel) {
        this.dadosImovel = dadosImovel;
        if (dadosImovel != null)
            dadosImovel.setDeclaracao(this);
    }

    public List<DoiAlienante> getAlienantes() {
        return alienantes;
    }

    public void setAlienantes(List<DoiAlienante> alienantes) {
        this.alienantes = alienantes;
    }

    public void addAlienante(DoiAlienante alienante) {
        alienantes.add(alienante);
        alienante.setDeclaracao(this);
    }

    public List<DoiAdquirente> getAdquirentes() {
        return adquirentes;
    }

    public void setAdquirentes(List<DoiAdquirente> adquirentes) {
        this.adquirentes = adquirentes;
    }

    public void addAdquirente(DoiAdquirente adquirente) {
        adquirentes.add(adquirente);
        adquirente.setDeclaracao(this);
    }
}
