package com.br.sisgetrim.repository.doi;

import com.br.sisgetrim.model.doi.DoiAdquirente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DoiAdquirenteRepository extends JpaRepository<DoiAdquirente, Long> {
}
