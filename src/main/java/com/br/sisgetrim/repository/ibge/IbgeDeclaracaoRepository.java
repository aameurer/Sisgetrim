package com.br.sisgetrim.repository.ibge;

import com.br.sisgetrim.model.Entidade;
import com.br.sisgetrim.model.ibge.IbgeDeclaracao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IbgeDeclaracaoRepository extends JpaRepository<IbgeDeclaracao, Long> {

    List<IbgeDeclaracao> findByEntidade(Entidade entidade);

    Page<IbgeDeclaracao> findByEntidade(Entidade entidade, Pageable pageable);

    Optional<IbgeDeclaracao> findByMatricula(String matricula);
}
