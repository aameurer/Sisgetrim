package com.br.sisgetrim.repository;

import com.br.sisgetrim.model.MalhaLote;
import com.br.sisgetrim.model.MalhaResultado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MalhaResultadoRepository extends JpaRepository<MalhaResultado, Long> {

    List<MalhaResultado> findByLoteOrderByDiferencaValorDesc(MalhaLote lote);

    List<MalhaResultado> findByLoteIdOrderByDiferencaValorDesc(Long loteId);

    long countByLoteAndSituacao(MalhaLote lote, String situacao);
}
