package com.br.sisgetrim.repository.doi;

import com.br.sisgetrim.model.doi.DoiAlienante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DoiAlienanteRepository extends JpaRepository<DoiAlienante, Long> {
}
