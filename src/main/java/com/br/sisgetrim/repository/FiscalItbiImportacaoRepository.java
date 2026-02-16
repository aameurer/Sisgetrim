package com.br.sisgetrim.repository;

import com.br.sisgetrim.model.FiscalItbiImportacao;
import com.br.sisgetrim.model.Entidade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface FiscalItbiImportacaoRepository extends JpaRepository<FiscalItbiImportacao, Long> {
    List<FiscalItbiImportacao> findByEntidadeOrderByCreatedAtDesc(Entidade entidade);
}
