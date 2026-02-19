package com.br.sisgetrim.repository.doi;

import com.br.sisgetrim.model.doi.DoiImportacaoErros;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DoiImportacaoErrosRepository extends JpaRepository<DoiImportacaoErros, Long> {
}
