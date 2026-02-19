package com.br.sisgetrim.dto;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;

public record EntidadeRequestDTO(
        @NotBlank(message = "O CNPJ é obrigatório") String cnpj,
        String tipoEstabelecimento,
        @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE) LocalDate dataAbertura,
        @NotBlank(message = "O Nome Empresarial é obrigatório") String nomeEmpresarial,
        String nomeFantasia,
        String porte,
        String cnaePrincipal,
        String cnaeSecundario,
        String naturezaJuridica,
        String logradouro,
        String numero,
        String complemento,
        String cep,
        String bairro,
        String municipio,
        String uf,
        String email,
        String telefone,
        String enteFederativo,
        String situacaoCadastral,
        @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE) LocalDate dataSituacaoCadastral,
        String motivoSituacaoCadastral,
        String situacaoEspecial,
        @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE) LocalDate dataSituacaoEspecial,
        String logoUrl) {
}
