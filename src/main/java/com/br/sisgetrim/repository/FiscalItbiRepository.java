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
}
