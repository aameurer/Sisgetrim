package com.br.sisgetrim.repository.doi;

import com.br.sisgetrim.model.doi.DoiImportacao;
import com.br.sisgetrim.model.Entidade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DoiImportacaoRepository extends JpaRepository<DoiImportacao, Long> {
    @org.springframework.data.jpa.repository.Query("SELECT i FROM DoiImportacao i JOIN FETCH i.usuario WHERE i.entidade = :entidade ORDER BY i.id DESC")
    List<DoiImportacao> findByEntidade(Entidade entidade);

    boolean existsByEntidade(Entidade entidade);
}
