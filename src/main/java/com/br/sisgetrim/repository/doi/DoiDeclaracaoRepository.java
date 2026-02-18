package com.br.sisgetrim.repository.doi;

import com.br.sisgetrim.model.doi.DoiDeclaracao;
import com.br.sisgetrim.model.Entidade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface DoiDeclaracaoRepository extends JpaRepository<DoiDeclaracao, Long> {
    List<DoiDeclaracao> findByEntidade(Entidade entidade);

    List<DoiDeclaracao> findByImportacaoId(Long importacaoId);

    java.util.Optional<DoiDeclaracao> findByMatricula(String matricula);

    @org.springframework.data.jpa.repository.Query("SELECT d FROM DoiDeclaracao d JOIN FETCH d.importacao WHERE d.entidade = :entidade AND YEAR(d.dataCadastro) = :ano ORDER BY d.dataCadastro DESC")
    List<DoiDeclaracao> findByEntidadeAndAno(Entidade entidade, int ano);

    @Query("SELECT d FROM DoiDeclaracao d " +
            "LEFT JOIN FETCH d.importacao " +
            "LEFT JOIN FETCH d.entidade " +
            "LEFT JOIN FETCH d.dadosImovel " +
            "LEFT JOIN FETCH d.operacao " +
            "WHERE d.id = :id")
    Optional<DoiDeclaracao> findByIdWithDetails(@Param("id") Long id);
}
