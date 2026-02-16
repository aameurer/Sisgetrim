package com.br.sisgetrim.dto;

import com.br.sisgetrim.model.enums.StatusSubstituto;
import com.br.sisgetrim.model.enums.TipoResponsavel;
import java.time.LocalDate;

public record ResponsavelCartorioResponseDTO(
        Long id,
        TipoResponsavel tipo,
        String nome,
        String cpf,
        LocalDate dataNomeacao,
        LocalDate dataIngresso,
        StatusSubstituto substituto) {
}
