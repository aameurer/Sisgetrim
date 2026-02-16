package com.br.sisgetrim.repository.doi;

import com.br.sisgetrim.model.doi.DoiParticipante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DoiParticipanteRepository extends JpaRepository<DoiParticipante, Long> {
}
