-- Migração para criar o histórico de importações de ITBI (Excel)
CREATE TABLE fiscal_itbi_importacoes (
    id BIGSERIAL PRIMARY KEY,
    entidade_id BIGINT NOT NULL,
    usuario_id BIGINT NOT NULL,
    nome_arquivo VARCHAR(255) NOT NULL,
    data_importacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(50) DEFAULT 'PROCESSANDO',
    total_registros INTEGER DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_fiscal_itbi_imp_entidade FOREIGN KEY (entidade_id) REFERENCES entidades(id),
    CONSTRAINT fk_fiscal_itbi_imp_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
);

-- Adicionar coluna de vínculo na tabela de registros
ALTER TABLE fiscal_itbi ADD COLUMN importacao_id BIGINT;
ALTER TABLE fiscal_itbi ADD CONSTRAINT fk_fiscal_itbi_importacao FOREIGN KEY (importacao_id) REFERENCES fiscal_itbi_importacoes(id);

CREATE INDEX idx_fiscal_itbi_importacao_id ON fiscal_itbi(importacao_id);
