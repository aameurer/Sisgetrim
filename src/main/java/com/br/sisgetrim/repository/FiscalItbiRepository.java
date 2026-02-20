package com.br.sisgetrim.repository;

import com.br.sisgetrim.model.FiscalItbi;
import com.br.sisgetrim.model.Entidade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FiscalItbiRepository extends JpaRepository<FiscalItbi, Long> {
        List<FiscalItbi> findByEntidade(Entidade entidade);

        List<FiscalItbi> findByItbiNumeroAndItbiAnoAndEntidade(String itbiNumero, Integer itbiAno, Entidade entidade);

        @org.springframework.data.jpa.repository.Query("SELECT f FROM FiscalItbi f " +
                        "WHERE f.entidade = :entidade " +
                        "AND f.itbiData BETWEEN :inicio AND :fim")
        List<FiscalItbi> findByEntidadeAndPeriodo(
                        @org.springframework.data.repository.query.Param("entidade") Entidade entidade,
                        @org.springframework.data.repository.query.Param("inicio") java.time.LocalDate inicio,
                        @org.springframework.data.repository.query.Param("fim") java.time.LocalDate fim,
                        org.springframework.data.domain.Sort sort);
}
