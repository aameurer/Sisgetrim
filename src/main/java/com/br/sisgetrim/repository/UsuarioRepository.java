package com.br.sisgetrim.repository;

import com.br.sisgetrim.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByDocumento(String documento);

    Optional<Usuario> findByEmail(String email);

    boolean existsByDocumento(String documento);

    boolean existsByEmail(String email);
}
