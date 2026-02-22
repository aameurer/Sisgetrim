package com.br.sisgetrim.service;

import com.br.sisgetrim.model.Entidade;
import com.br.sisgetrim.model.ParametroMalha;
import com.br.sisgetrim.repository.ParametroMalhaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
public class ParametroMalhaService {

    private static final Logger log = LoggerFactory.getLogger(ParametroMalhaService.class);

    private final ParametroMalhaRepository repository;

    public ParametroMalhaService(ParametroMalhaRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public ParametroMalha buscarOuCriarPadrao(Entidade entidade) {
        return repository.findFirstByEntidadeOrderByIdDesc(entidade)
                .orElseGet(() -> {
                    log.info("Criando parâmetros padrão para entidade ID={}", entidade.getId());
                    return repository.save(criarPadrao(entidade));
                });
    }

    private ParametroMalha criarPadrao(Entidade entidade) {
        ParametroMalha p = new ParametroMalha();
        p.setEntidade(entidade);
        p.setDataInicial(LocalDate.of(LocalDate.now().getYear(), 1, 1));
        p.setDataFinal(LocalDate.of(LocalDate.now().getYear(), 12, 31));
        p.setDiferencaBcDoi(BigDecimal.ONE);
        p.setDiferencaImpostoDoi(BigDecimal.ONE);
        p.setPercentualAbaixoVvi(1.0);
        p.setPercentualAbaixoImpostoDoi(1.0);
        p.setConsiderarIntegralizacaoCapital(true);
        p.setMalhaValorReducaoNominal(new BigDecimal("5000.00"));
        p.setMalhaValorReducaoRelativa(new BigDecimal("50.00"));
        p.setAlertaMalhaReducaoNominal(new BigDecimal("999999.99"));
        p.setAlertaMalhaReducaoRelativa(new BigDecimal("100.00"));
        return p;
    }

    @Transactional
    public ParametroMalha salvar(ParametroMalha parametro) {
        log.info("Salvando parâmetros da malha para entidade ID={}", parametro.getEntidade().getId());
        return repository.save(parametro);
    }
}
