package com.br.sisgetrim.repository.doi;

import com.br.sisgetrim.model.doi.DoiOperacaoImobiliaria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DoiOperacaoImobiliariaRepository extends JpaRepository<DoiOperacaoImobiliaria, Long> {
}
