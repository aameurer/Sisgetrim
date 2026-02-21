package com.br.sisgetrim.dto;

import com.br.sisgetrim.model.enums.StatusSubstituto;
import com.br.sisgetrim.model.enums.TipoResponsavel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public class ResponsavelCartorioRequestDTO {

        @NotNull
        private TipoResponsavel tipo;

        @NotBlank
        private String nome;

        @NotBlank
        @org.hibernate.validator.constraints.br.CPF
        private String cpf;

        @NotNull
        @org.springframework.format.annotation.DateTimeFormat(pattern = "yyyy-MM-dd")
        private LocalDate dataNomeacao;

        @NotNull
        @org.springframework.format.annotation.DateTimeFormat(pattern = "yyyy-MM-dd")
        private LocalDate dataIngresso;

        @NotNull
        private StatusSubstituto substituto;

        public ResponsavelCartorioRequestDTO() {
        }

        public ResponsavelCartorioRequestDTO(TipoResponsavel tipo, String nome, String cpf, LocalDate dataNomeacao,
                        LocalDate dataIngresso, StatusSubstituto substituto) {
                this.tipo = tipo;
                this.nome = nome;
                this.cpf = cpf;
                this.dataNomeacao = dataNomeacao;
                this.dataIngresso = dataIngresso;
                this.substituto = substituto;
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

        @Override
        public String toString() {
                return "ResponsavelCartorioRequestDTO [nome=" + nome + ", cpf=" + cpf + "]";
        }
}
