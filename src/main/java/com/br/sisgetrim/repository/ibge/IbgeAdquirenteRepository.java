package com.br.sisgetrim.repository.ibge;

import com.br.sisgetrim.model.ibge.IbgeAdquirente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IbgeAdquirenteRepository extends JpaRepository<IbgeAdquirente, Long> {

    List<IbgeAdquirente> findByDeclaracaoId(Long declaracaoId);
}
