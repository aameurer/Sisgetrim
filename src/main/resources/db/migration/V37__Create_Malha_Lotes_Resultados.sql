-- Tabela de Lotes de Execução da Malha Fiscal
CREATE TABLE malha_lotes (
    id BIGSERIAL PRIMARY KEY,
    entidade_id BIGINT NOT NULL,
    usuario_id BIGINT NOT NULL,
    data_inicial DATE NOT NULL,
    data_final DATE NOT NULL,
    diferenca_bc_doi DECIMAL(19,2) DEFAULT 1.00,
    diferenca_imposto_doi DECIMAL(19,2) DEFAULT 1.00,
    percentual_abaixo_vvi DOUBLE PRECISION DEFAULT 1.0,
    percentual_abaixo_imposto_doi DOUBLE PRECISION DEFAULT 1.0,
    considerar_integralizacao_capital BOOLEAN DEFAULT FALSE,
    total_analisado INTEGER DEFAULT 0,
    total_divergencias INTEGER DEFAULT 0,
    status VARCHAR(20) DEFAULT 'PROCESSANDO',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_malha_lote_entidade FOREIGN KEY (entidade_id) REFERENCES entidades(id),
    CONSTRAINT fk_malha_lote_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
);

-- Tabela de Resultados (Divergências) da Malha Fiscal
CREATE TABLE malha_resultados (
    id BIGSERIAL PRIMARY KEY,
    lote_id BIGINT NOT NULL,
    entidade_id BIGINT NOT NULL,
    doi_id BIGINT,
    fiscal_id BIGINT,
    matricula VARCHAR(50),
    cib VARCHAR(50),
    cadimo_inscricao VARCHAR(50),
    cadimo_quadra VARCHAR(50),
    cadimo_lote VARCHAR(50),
    cadimo_bairro_nome VARCHAR(200),
    data_lavratura DATE,
    situacao_doi VARCHAR(50),
    valor_base_calculo_doi DECIMAL(19,2) DEFAULT 0,
    valor_venal_fiscal DECIMAL(19,2) DEFAULT 0,
    diferenca_valor DECIMAL(19,2) DEFAULT 0,
    cartorio VARCHAR(255),
    parametros_violados TEXT,
    situacao VARCHAR(30) DEFAULT 'PENDENTE',
    justificativa TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_malha_resultado_lote FOREIGN KEY (lote_id) REFERENCES malha_lotes(id) ON DELETE CASCADE,
    CONSTRAINT fk_malha_resultado_entidade FOREIGN KEY (entidade_id) REFERENCES entidades(id)
);

-- Índices para performance
CREATE INDEX idx_malha_lotes_entidade ON malha_lotes(entidade_id);
CREATE INDEX idx_malha_lotes_usuario ON malha_lotes(usuario_id);
CREATE INDEX idx_malha_lotes_status ON malha_lotes(status);
CREATE INDEX idx_malha_resultados_lote ON malha_resultados(lote_id);
CREATE INDEX idx_malha_resultados_entidade ON malha_resultados(entidade_id);
CREATE INDEX idx_malha_resultados_situacao ON malha_resultados(situacao);
CREATE INDEX idx_malha_resultados_matricula ON malha_resultados(matricula);
CREATE INDEX idx_malha_resultados_cib ON malha_resultados(cib);
