package com.br.sisgetrim.service.doi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class DoiLookupService {

    private final JdbcTemplate jdbcTemplate;
    private final Map<String, Map<Integer, String>> cache = new ConcurrentHashMap<>();

    @Autowired
    public DoiLookupService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostConstruct
    public void init() {
        carregarDominios();
    }

    public synchronized void carregarDominios() {
        String[] tabelas = {
                "dom_tipo_declaracao", "dom_tipo_servico", "dom_tipo_ato",
                "dom_tipo_livro", "dom_natureza_titulo", "dom_tipo_operacao",
                "dom_forma_pagamento", "dom_medida_parte", "dom_destinacao",
                "dom_motivo_nao_ni", "dom_regime_bens", "dom_tipo_imovel"
        };

        for (String tabela : tabelas) {
            Map<Integer, String> valores = new HashMap<>();
            jdbcTemplate.query("SELECT codigo, descricao FROM " + tabela, rs -> {
                valores.put(rs.getInt("codigo"), rs.getString("descricao"));
            });
            cache.put(tabela, valores);
        }
    }

    public String getDescricao(String dominio, Integer codigo) {
        Map<Integer, String> valores = cache.get(dominio);
        if (valores != null) {
            return valores.get(codigo);
        }
        return null;
    }

    public boolean validar(String dominio, Integer codigo) {
        if (codigo == null)
            return false;
        Map<Integer, String> valores = cache.get(dominio);
        return valores != null && valores.containsKey(codigo);
    }

    /**
     * Tenta converter o código para descrição. Se não encontrar, retorna null.
     */
    public String traduzir(String dominio, String codigoStr) {
        if (codigoStr == null || codigoStr.isEmpty())
            return null;
        try {
            Integer codigo = Integer.parseInt(codigoStr);
            String descricao = getDescricao(dominio, codigo);
            return (descricao != null) ? descricao : codigoStr;
        } catch (NumberFormatException e) {
            return codigoStr;
        }
    }
}
