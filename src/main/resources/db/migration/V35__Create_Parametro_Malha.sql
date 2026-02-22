CREATE TABLE parametro_malha (
    id BIGSERIAL PRIMARY KEY,
    entidade_id BIGINT NOT NULL REFERENCES entidades(id) ON DELETE CASCADE,
    data_inicial DATE,
    data_final DATE,
    diferenca_bc_doi NUMERIC(19,2) DEFAULT 1,
    diferenca_imposto_doi NUMERIC(19,2) DEFAULT 1,
    percentual_abaixo_vvi DOUBLE PRECISION DEFAULT 1,
    considerar_integralizacao_capital BOOLEAN DEFAULT TRUE,
    malha_valor_reducao_nominal NUMERIC(19,2) DEFAULT 5000.00,
    malha_valor_reducao_relativa NUMERIC(19,2) DEFAULT 50.00,
    alerta_malha_reducao_nominal NUMERIC(19,2) DEFAULT 999999.99,
    alerta_malha_reducao_relativa NUMERIC(19,2) DEFAULT 100.00,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ativo BOOLEAN DEFAULT TRUE
);

CREATE INDEX idx_parametro_malha_entidade ON parametro_malha(entidade_id);
