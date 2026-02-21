package com.br.sisgetrim.dto;

public record EntidadeResponseDTO(
        Long id,
        String cnpj,
        String nomeEmpresarial,
        String nomeFantasia,
        String municipio,
        String uf,
        String logoUrl,
        boolean ativo,
        java.util.List<UsuarioResponseDTO> usuarios) {
}
