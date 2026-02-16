package com.br.sisgetrim.repository;

import com.br.sisgetrim.model.Cartorio;
import com.br.sisgetrim.model.Entidade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface CartorioRepository extends JpaRepository<Cartorio, Long> {
    List<Cartorio> findByEntidade(Entidade entidade);

    Optional<Cartorio> findByCodigoCnsAndEntidade(String codigoCns, Entidade entidade);

    boolean existsByCodigoCns(String codigoCns);
}
