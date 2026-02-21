package com.br.sisgetrim.repository.ibge;

import com.br.sisgetrim.model.ibge.IbgeAlienante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IbgeAlienanteRepository extends JpaRepository<IbgeAlienante, Long> {

    List<IbgeAlienante> findByDeclaracaoId(Long declaracaoId);
}
