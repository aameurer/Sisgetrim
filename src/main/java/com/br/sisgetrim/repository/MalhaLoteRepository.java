package com.br.sisgetrim.repository;

import com.br.sisgetrim.model.Entidade;
import com.br.sisgetrim.model.MalhaLote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MalhaLoteRepository extends JpaRepository<MalhaLote, Long> {

    List<MalhaLote> findByEntidadeOrderByCreatedAtDesc(Entidade entidade);

    List<MalhaLote> findTop10ByEntidadeOrderByCreatedAtDesc(Entidade entidade);
}
