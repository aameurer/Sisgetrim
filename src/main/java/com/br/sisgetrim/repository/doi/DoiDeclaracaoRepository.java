package com.br.sisgetrim.repository.doi;

import com.br.sisgetrim.model.doi.DoiDeclaracao;
import com.br.sisgetrim.model.Entidade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DoiDeclaracaoRepository extends JpaRepository<DoiDeclaracao, Long> {
    List<DoiDeclaracao> findByEntidade(Entidade entidade);

    List<DoiDeclaracao> findByImportacaoId(Long importacaoId);
}
