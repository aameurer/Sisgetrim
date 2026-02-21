package com.br.sisgetrim.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "fiscal_itbi")
public class FiscalItbi {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "entidade_id", nullable = false)
    private Entidade entidade;

    // --- SEÇÃO ITBI ---
    @Column(name = "itbi_numero", nullable = false, length = 20)
    private String itbiNumero;

    @Column(name = "itbi_ano", nullable = false)
    private Integer itbiAno;

    @Column(name = "itbi_data")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate itbiData;

    @Column(name = "itbi_tipo", length = 100)
    private String itbiTipo;

    @Column(name = "itbi_proprietario_nome", columnDefinition = "TEXT")
    private String itbiProprietarioNome;

    @Column(name = "itbi_proprietario_cpf", columnDefinition = "TEXT")
    private String itbiProprietarioCpf;

    @Column(name = "itbi_transmitente_nome", columnDefinition = "TEXT")
    private String itbiTransmitenteNome;

    @Column(name = "itbi_transmitente_cpf", columnDefinition = "TEXT")
    private String itbiTransmitenteCpf;

    @Column(name = "itbi_situacao", length = 100)
    private String itbiSituacao;

    @Column(name = "itbi_valor_venal_calculado_vvt", precision = 19, scale = 2)
    private BigDecimal itbiValorVenalCalculadoVvt;

    @Column(name = "itbi_valor_venal_calculado_vvp", precision = 19, scale = 2)
    private BigDecimal itbiValorVenalCalculadoVvp;

    @Column(name = "itbi_valor_venal_calculado_total", precision = 19, scale = 2)
    private BigDecimal itbiValorVenalCalculadoTotal;

    @Column(name = "itbi_valor_venal_informado_vvt", precision = 19, scale = 2)
    private BigDecimal itbiValorVenalInformadoVvt;

    @Column(name = "itbi_valor_venal_informado_vvp", precision = 19, scale = 2)
    private BigDecimal itbiValorVenalInformadoVvp;

    @Column(name = "itbi_valor_venal_informado_total", precision = 19, scale = 2)
    private BigDecimal itbiValorVenalInformadoTotal;

    @Column(name = "itbi_percentual_vvt", precision = 19, scale = 4)
    private BigDecimal itbiPercentualVvt;

    @Column(name = "itbi_adquirente_nome", columnDefinition = "TEXT")
    private String itbiAdquirenteNome;

    @Column(name = "itbi_adquirente_cpf", columnDefinition = "TEXT")
    private String itbiAdquirenteCpf;

    // --- SEÇÃO VALOR VENAL - IPTU ---
    @Column(name = "iptu_valor_venal_territorial", precision = 19, scale = 2)
    private BigDecimal iptuValorVenalTerritorial;

    @Column(name = "iptu_valor_venal_predial", precision = 19, scale = 2)
    private BigDecimal iptuValorVenalPredial;

    @Column(name = "iptu_valor_venal_total", precision = 19, scale = 2)
    private BigDecimal iptuValorVenalTotal;

    // --- SEÇÃO CADIMO ---
    @Column(name = "cadimo_cadastro", length = 50)
    private String cadimoCadastro;

    @Column(name = "cadimo_tipo", length = 50)
    private String cadimoTipo;

    @Column(name = "cadimo_situacao_cadastral", length = 100)
    private String cadimoSituacaoCadastral;

    @Column(name = "cadimo_inscricao", length = 50)
    private String cadimoInscricao;

    @Column(name = "cadimo_quadra", length = 50)
    private String cadimoQuadra;

    @Column(name = "cadimo_lote", length = 50)
    private String cadimoLote;

    @Column(name = "cadimo_bairro_nome", length = 200)
    private String cadimoBairroNome;

    @Column(name = "cadimo_logradouro_nome", length = 255)
    private String cadimoLogradouroNome;

    @Column(name = "cadimo_numero", length = 50)
    private String cadimoNumero;

    @Column(name = "cadimo_complemento", length = 255)
    private String cadimoComplemento;

    @Column(name = "cadimo_apartamento_unidade", length = 100)
    private String cadimoApartamentoUnidade;

    @Column(name = "cadimo_cep", length = 20)
    private String cadimoCep;

    @Column(name = "cadimo_cod_terreno", length = 50)
    private String cadimoCodTerreno;

    @Column(name = "cadimo_proprietario_nome", length = 255)
    private String cadimoProprietarioNome;

    @Column(name = "cadimo_proprietario_cpf_cnpj", length = 20)
    private String cadimoProprietarioCpfCnpj;

    @Column(name = "cadimo_responsavel_nome", length = 255)
    private String cadimoResponsavelNome;

    @Column(name = "cadimo_responsavel_cpf_cnpj", length = 20)
    private String cadimoResponsavelCpfCnpj;

    @Column(name = "cadimo_area_terreno", precision = 19, scale = 4)
    private BigDecimal cadimoAreaTerreno;

    @Column(name = "cadimo_area_construida", precision = 19, scale = 4)
    private BigDecimal cadimoAreaConstruida;

    @Column(name = "cadimo_area_total_construida", precision = 19, scale = 4)
    private BigDecimal cadimoAreaTotalConstruida;

    @Column(name = "cadimo_matricula", length = 50)
    private String cadimoMatricula;

    @Column(name = "cadimo_codigo_nacional_matricula", length = 100)
    private String cadimoCodigoNacionalMatricula;

    @Column(name = "cadimo_cib", length = 50)
    private String cadimoCib;

    @Column(name = "cadimo_nro_imovel_incra", length = 100)
    private String cadimoNroImovelIncra;

    @Column(name = "cadimo_nome_propriedade", length = 255)
    private String cadimoNomePropriedade;

    // --- METADADOS ---
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "importacao_id")
    private FiscalItbiImportacao importacao;

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

    public Entidade getEntidade() {
        return entidade;
    }

    public void setEntidade(Entidade entidade) {
        this.entidade = entidade;
    }

    public String getItbiNumero() {
        return itbiNumero;
    }

    public void setItbiNumero(String itbiNumero) {
        this.itbiNumero = itbiNumero;
    }

    public Integer getItbiAno() {
        return itbiAno;
    }

    public void setItbiAno(Integer itbiAno) {
        this.itbiAno = itbiAno;
    }

    public LocalDate getItbiData() {
        return itbiData;
    }

    public void setItbiData(LocalDate itbiData) {
        this.itbiData = itbiData;
    }

    public String getItbiTipo() {
        return itbiTipo;
    }

    public void setItbiTipo(String itbiTipo) {
        this.itbiTipo = itbiTipo;
    }

    public String getItbiProprietarioNome() {
        return itbiProprietarioNome;
    }

    public void setItbiProprietarioNome(String itbiProprietarioNome) {
        this.itbiProprietarioNome = itbiProprietarioNome;
    }

    public String getItbiProprietarioCpf() {
        return itbiProprietarioCpf;
    }

    public void setItbiProprietarioCpf(String itbiProprietarioCpf) {
        this.itbiProprietarioCpf = itbiProprietarioCpf;
    }

    public String getItbiTransmitenteNome() {
        return itbiTransmitenteNome;
    }

    public void setItbiTransmitenteNome(String itbiTransmitenteNome) {
        this.itbiTransmitenteNome = itbiTransmitenteNome;
    }

    public String getItbiTransmitenteCpf() {
        return itbiTransmitenteCpf;
    }

    public void setItbiTransmitenteCpf(String itbiTransmitenteCpf) {
        this.itbiTransmitenteCpf = itbiTransmitenteCpf;
    }

    public String getItbiSituacao() {
        return itbiSituacao;
    }

    public void setItbiSituacao(String itbiSituacao) {
        this.itbiSituacao = itbiSituacao;
    }

    public BigDecimal getItbiValorVenalCalculadoVvt() {
        return itbiValorVenalCalculadoVvt;
    }

    public void setItbiValorVenalCalculadoVvt(BigDecimal itbiValorVenalCalculadoVvt) {
        this.itbiValorVenalCalculadoVvt = itbiValorVenalCalculadoVvt;
    }

    public BigDecimal getItbiValorVenalCalculadoVvp() {
        return itbiValorVenalCalculadoVvp;
    }

    public void setItbiValorVenalCalculadoVvp(BigDecimal itbiValorVenalCalculadoVvp) {
        this.itbiValorVenalCalculadoVvp = itbiValorVenalCalculadoVvp;
    }

    public BigDecimal getItbiValorVenalCalculadoTotal() {
        return itbiValorVenalCalculadoTotal;
    }

    public void setItbiValorVenalCalculadoTotal(BigDecimal itbiValorVenalCalculadoTotal) {
        this.itbiValorVenalCalculadoTotal = itbiValorVenalCalculadoTotal;
    }

    public BigDecimal getItbiValorVenalInformadoVvt() {
        return itbiValorVenalInformadoVvt;
    }

    public void setItbiValorVenalInformadoVvt(BigDecimal itbiValorVenalInformadoVvt) {
        this.itbiValorVenalInformadoVvt = itbiValorVenalInformadoVvt;
    }

    public BigDecimal getItbiValorVenalInformadoVvp() {
        return itbiValorVenalInformadoVvp;
    }

    public void setItbiValorVenalInformadoVvp(BigDecimal itbiValorVenalInformadoVvp) {
        this.itbiValorVenalInformadoVvp = itbiValorVenalInformadoVvp;
    }

    public BigDecimal getItbiValorVenalInformadoTotal() {
        return itbiValorVenalInformadoTotal;
    }

    public void setItbiValorVenalInformadoTotal(BigDecimal itbiValorVenalInformadoTotal) {
        this.itbiValorVenalInformadoTotal = itbiValorVenalInformadoTotal;
    }

    public BigDecimal getItbiPercentualVvt() {
        return itbiPercentualVvt;
    }

    public void setItbiPercentualVvt(BigDecimal itbiPercentualVvt) {
        this.itbiPercentualVvt = itbiPercentualVvt;
    }

    public String getItbiAdquirenteNome() {
        return itbiAdquirenteNome;
    }

    public void setItbiAdquirenteNome(String itbiAdquirenteNome) {
        this.itbiAdquirenteNome = itbiAdquirenteNome;
    }

    public String getItbiAdquirenteCpf() {
        return itbiAdquirenteCpf;
    }

    public void setItbiAdquirenteCpf(String itbiAdquirenteCpf) {
        this.itbiAdquirenteCpf = itbiAdquirenteCpf;
    }

    public BigDecimal getIptuValorVenalTerritorial() {
        return iptuValorVenalTerritorial;
    }

    public void setIptuValorVenalTerritorial(BigDecimal iptuValorVenalTerritorial) {
        this.iptuValorVenalTerritorial = iptuValorVenalTerritorial;
    }

    public BigDecimal getIptuValorVenalPredial() {
        return iptuValorVenalPredial;
    }

    public void setIptuValorVenalPredial(BigDecimal iptuValorVenalPredial) {
        this.iptuValorVenalPredial = iptuValorVenalPredial;
    }

    public BigDecimal getIptuValorVenalTotal() {
        return iptuValorVenalTotal;
    }

    public void setIptuValorVenalTotal(BigDecimal iptuValorVenalTotal) {
        this.iptuValorVenalTotal = iptuValorVenalTotal;
    }

    public String getCadimoCadastro() {
        return cadimoCadastro;
    }

    public void setCadimoCadastro(String cadimoCadastro) {
        this.cadimoCadastro = cadimoCadastro;
    }

    public String getCadimoTipo() {
        return cadimoTipo;
    }

    public void setCadimoTipo(String cadimoTipo) {
        this.cadimoTipo = cadimoTipo;
    }

    public String getCadimoSituacaoCadastral() {
        return cadimoSituacaoCadastral;
    }

    public void setCadimoSituacaoCadastral(String cadimoSituacaoCadastral) {
        this.cadimoSituacaoCadastral = cadimoSituacaoCadastral;
    }

    public String getCadimoInscricao() {
        return cadimoInscricao;
    }

    public void setCadimoInscricao(String cadimoInscricao) {
        this.cadimoInscricao = cadimoInscricao;
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

    public String getCadimoLogradouroNome() {
        return cadimoLogradouroNome;
    }

    public void setCadimoLogradouroNome(String cadimoLogradouroNome) {
        this.cadimoLogradouroNome = cadimoLogradouroNome;
    }

    public String getCadimoNumero() {
        return cadimoNumero;
    }

    public void setCadimoNumero(String cadimoNumero) {
        this.cadimoNumero = cadimoNumero;
    }

    public String getCadimoComplemento() {
        return cadimoComplemento;
    }

    public void setCadimoComplemento(String cadimoComplemento) {
        this.cadimoComplemento = cadimoComplemento;
    }

    public String getCadimoApartamentoUnidade() {
        return cadimoApartamentoUnidade;
    }

    public void setCadimoApartamentoUnidade(String cadimoApartamentoUnidade) {
        this.cadimoApartamentoUnidade = cadimoApartamentoUnidade;
    }

    public String getCadimoCep() {
        return cadimoCep;
    }

    public void setCadimoCep(String cadimoCep) {
        this.cadimoCep = cadimoCep;
    }

    public String getCadimoCodTerreno() {
        return cadimoCodTerreno;
    }

    public void setCadimoCodTerreno(String cadimoCodTerreno) {
        this.cadimoCodTerreno = cadimoCodTerreno;
    }

    public String getCadimoProprietarioNome() {
        return cadimoProprietarioNome;
    }

    public void setCadimoProprietarioNome(String cadimoProprietarioNome) {
        this.cadimoProprietarioNome = cadimoProprietarioNome;
    }

    public String getCadimoProprietarioCpfCnpj() {
        return cadimoProprietarioCpfCnpj;
    }

    public void setCadimoProprietarioCpfCnpj(String cadimoProprietarioCpfCnpj) {
        this.cadimoProprietarioCpfCnpj = cadimoProprietarioCpfCnpj;
    }

    public String getCadimoResponsavelNome() {
        return cadimoResponsavelNome;
    }

    public void setCadimoResponsavelNome(String cadimoResponsavelNome) {
        this.cadimoResponsavelNome = cadimoResponsavelNome;
    }

    public String getCadimoResponsavelCpfCnpj() {
        return cadimoResponsavelCpfCnpj;
    }

    public void setCadimoResponsavelCpfCnpj(String cadimoResponsavelCpfCnpj) {
        this.cadimoResponsavelCpfCnpj = cadimoResponsavelCpfCnpj;
    }

    public BigDecimal getCadimoAreaTerreno() {
        return cadimoAreaTerreno;
    }

    public void setCadimoAreaTerreno(BigDecimal cadimoAreaTerreno) {
        this.cadimoAreaTerreno = cadimoAreaTerreno;
    }

    public BigDecimal getCadimoAreaConstruida() {
        return cadimoAreaConstruida;
    }

    public void setCadimoAreaConstruida(BigDecimal cadimoAreaConstruida) {
        this.cadimoAreaConstruida = cadimoAreaConstruida;
    }

    public BigDecimal getCadimoAreaTotalConstruida() {
        return cadimoAreaTotalConstruida;
    }

    public void setCadimoAreaTotalConstruida(BigDecimal cadimoAreaTotalConstruida) {
        this.cadimoAreaTotalConstruida = cadimoAreaTotalConstruida;
    }

    public String getCadimoMatricula() {
        return cadimoMatricula;
    }

    public void setCadimoMatricula(String cadimoMatricula) {
        this.cadimoMatricula = cadimoMatricula;
    }

    public String getCadimoCodigoNacionalMatricula() {
        return cadimoCodigoNacionalMatricula;
    }

    public void setCadimoCodigoNacionalMatricula(String cadimoCodigoNacionalMatricula) {
        this.cadimoCodigoNacionalMatricula = cadimoCodigoNacionalMatricula;
    }

    public String getCadimoCib() {
        return cadimoCib;
    }

    public void setCadimoCib(String cadimoCib) {
        this.cadimoCib = cadimoCib;
    }

    public String getCadimoNroImovelIncra() {
        return cadimoNroImovelIncra;
    }

    public void setCadimoNroImovelIncra(String cadimoNroImovelIncra) {
        this.cadimoNroImovelIncra = cadimoNroImovelIncra;
    }

    public String getCadimoNomePropriedade() {
        return cadimoNomePropriedade;
    }

    public void setCadimoNomePropriedade(String cadimoNomePropriedade) {
        this.cadimoNomePropriedade = cadimoNomePropriedade;
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

    public FiscalItbiImportacao getImportacao() {
        return importacao;
    }

    public void setImportacao(FiscalItbiImportacao importacao) {
        this.importacao = importacao;
    }
}
