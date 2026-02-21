package com.br.sisgetrim.repository.ibge;

import com.br.sisgetrim.model.Entidade;
import com.br.sisgetrim.model.ibge.IbgeImportacao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IbgeImportacaoRepository extends JpaRepository<IbgeImportacao, Long> {

    List<IbgeImportacao> findByEntidade(Entidade entidade);

    List<IbgeImportacao> findByEntidadeOrderByCreatedAtDesc(Entidade entidade);

    Page<IbgeImportacao> findByEntidade(Entidade entidade, Pageable pageable);

}
