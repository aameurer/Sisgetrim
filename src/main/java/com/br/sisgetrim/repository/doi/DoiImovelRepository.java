package com.br.sisgetrim.repository.doi;

import com.br.sisgetrim.model.doi.DoiImovel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DoiImovelRepository extends JpaRepository<DoiImovel, Long> {
}
