package com.br.sisgetrim.dto;

import com.br.sisgetrim.model.enums.AtribuicaoCartorio;
import com.br.sisgetrim.model.enums.SituacaoCartorio;
import com.br.sisgetrim.model.enums.SituacaoJuridicaResponsavel;
import com.br.sisgetrim.model.enums.TipoCartorio;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public record CartorioResponseDTO(
        Long id,
        String codigoCns,
        String denominacao,
        LocalDate dataCriacao,
        SituacaoCartorio situacao,
        TipoCartorio tipo,
        SituacaoJuridicaResponsavel situacaoJuridicaResponsavel,
        Set<AtribuicaoCartorio> atribuicoes,
        String bairro,
        String cep,
        String endereco,
        String numero,
        String telefonePrincipal,
        String email,
        String cnpj,
        String razaoSocial,
        String naturezaJuridica,
        List<ResponsavelCartorioResponseDTO> responsaveis) {
}
