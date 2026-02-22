package com.br.sisgetrim.repository;

import com.br.sisgetrim.model.Entidade;
import com.br.sisgetrim.model.ParametroMalha;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ParametroMalhaRepository extends JpaRepository<ParametroMalha, Long> {

    Optional<ParametroMalha> findFirstByEntidadeOrderByIdDesc(Entidade entidade);
}
