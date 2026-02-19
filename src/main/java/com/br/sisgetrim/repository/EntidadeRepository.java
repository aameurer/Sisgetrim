package com.br.sisgetrim.repository;

import com.br.sisgetrim.model.Entidade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface EntidadeRepository extends JpaRepository<Entidade, Long> {
    Optional<Entidade> findByCnpj(String cnpj);

    boolean existsByCnpj(String cnpj);
}
