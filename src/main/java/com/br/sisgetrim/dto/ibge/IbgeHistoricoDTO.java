package com.br.sisgetrim.dto.ibge;

import java.time.LocalDateTime;

public class IbgeHistoricoDTO {

    private String nomeArquivo;
    private Long totalRegistros;
    private LocalDateTime dataImportacao;
    private String status;

    public IbgeHistoricoDTO(String nomeArquivo, Long totalRegistros, LocalDateTime dataImportacao, String status) {
        this.nomeArquivo = nomeArquivo;
        this.totalRegistros = totalRegistros;
        this.dataImportacao = dataImportacao;
        this.status = status;
    }

    public String getNomeArquivo() {
        return nomeArquivo;
    }

    public void setNomeArquivo(String nomeArquivo) {
        this.nomeArquivo = nomeArquivo;
    }

    public Long getTotalRegistros() {
        return totalRegistros;
    }

    public void setTotalRegistros(Long totalRegistros) {
        this.totalRegistros = totalRegistros;
    }

    public LocalDateTime getDataImportacao() {
        return dataImportacao;
    }

    public void setDataImportacao(LocalDateTime dataImportacao) {
        this.dataImportacao = dataImportacao;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
