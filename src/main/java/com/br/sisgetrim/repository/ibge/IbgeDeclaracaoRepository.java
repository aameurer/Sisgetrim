package com.br.sisgetrim.repository.ibge;

import com.br.sisgetrim.model.Entidade;
import com.br.sisgetrim.model.ibge.IbgeDeclaracao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface IbgeDeclaracaoRepository extends JpaRepository<IbgeDeclaracao, Long> {

    List<IbgeDeclaracao> findByEntidade(Entidade entidade);

    Page<IbgeDeclaracao> findByEntidade(Entidade entidade, Pageable pageable);

    @Query("SELECT d FROM IbgeDeclaracao d WHERE d.entidade = :entidade AND d.dataLavraturaRegistroAverbacao BETWEEN :inicio AND :fim")
    List<IbgeDeclaracao> findByEntidadeAndPeriodo(
            @Param("entidade") Entidade entidade,
            @Param("inicio") LocalDate inicio,
            @Param("fim") LocalDate fim,
            Sort sort);

    Optional<IbgeDeclaracao> findByMatricula(String matricula);
}
