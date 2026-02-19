package com.br.sisgetrim.repository;

import com.br.sisgetrim.model.ResponsavelCartorio;
import com.br.sisgetrim.model.Cartorio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ResponsavelCartorioRepository extends JpaRepository<ResponsavelCartorio, Long> {
    List<ResponsavelCartorio> findByCartorio(Cartorio cartorio);
}
