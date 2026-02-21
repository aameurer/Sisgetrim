-- V33__Create_IBGE_Importacoes_Table.sql

CREATE TABLE ibge_importacoes (
    id BIGSERIAL PRIMARY KEY,
    entidade_id BIGINT NOT NULL,
    usuario_id BIGINT NOT NULL,
    cartorio_id BIGINT,
    nome_arquivo VARCHAR(255) NOT NULL,
    data_importacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(50) DEFAULT 'PROCESSANDO',
    total_registros INTEGER DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_ibge_importacao_entidade FOREIGN KEY (entidade_id) REFERENCES entidades (id) ON DELETE CASCADE,
    CONSTRAINT fk_ibge_importacao_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios (id) ON DELETE CASCADE,
    CONSTRAINT fk_ibge_importacao_cartorio FOREIGN KEY (cartorio_id) REFERENCES cartorios (id) ON DELETE SET NULL
);

-- Adiciona a FK referenciando o lote de importação dentro de IBGE_declaracoes
ALTER TABLE ibge_declaracoes 
ADD COLUMN ibge_importacao_id BIGINT;

ALTER TABLE ibge_declaracoes
ADD CONSTRAINT fk_ibge_declaracao_importacao 
FOREIGN KEY (ibge_importacao_id) 
REFERENCES ibge_importacoes (id) 
ON DELETE CASCADE;

CREATE INDEX idx_ibge_importacao_entidade ON ibge_importacoes (entidade_id);
CREATE INDEX idx_ibge_declaracao_importacao_id ON ibge_declaracoes (ibge_importacao_id);
