-- Lotes de Importação DOI
CREATE TABLE doi_importacoes (
    id BIGSERIAL PRIMARY KEY,
    entidade_id BIGINT NOT NULL,
    usuario_id BIGINT NOT NULL,
    nome_arquivo VARCHAR(255) NOT NULL,
    data_importacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(50) DEFAULT 'PROCESSANDO', -- PROCESSANDO, CONCLUIDO, ERRO
    total_registros INTEGER DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_doi_import_entidade FOREIGN KEY (entidade_id) REFERENCES entidades(id),
    CONSTRAINT fk_doi_import_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
);

-- Declarações DOI (Cabeçalho da Transação)
CREATE TABLE doi_declaracoes (
    id BIGSERIAL PRIMARY KEY,
    importacao_id BIGINT NOT NULL,
    entidade_id BIGINT NOT NULL,
    numero_declaracao VARCHAR(50) NOT NULL,
    situacao VARCHAR(20) DEFAULT 'RASCUNHO', -- RASCUNHO, PROCESSADO, REVISAO_MANUAL
    data_operacao DATE NOT NULL,
    valor_operacao DECIMAL(19,2),
    area_imovel DECIMAL(19,4),
    indicador_nao_consta_valor BOOLEAN DEFAULT FALSE,
    tipo_imovel VARCHAR(50),
    municipio_imovel VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_doi_decl_import FOREIGN KEY (importacao_id) REFERENCES doi_importacoes(id),
    CONSTRAINT fk_doi_decl_entidade FOREIGN KEY (entidade_id) REFERENCES entidades(id)
);

-- Participantes da DOI (Alienante/Adquirente)
CREATE TABLE doi_participantes (
    id BIGSERIAL PRIMARY KEY,
    declaracao_id BIGINT NOT NULL,
    tipo_participante VARCHAR(20) NOT NULL, -- ALIENANTE, ADQUIRENTE
    nome VARCHAR(255) NOT NULL,
    documento VARCHAR(20) NOT NULL,
    percentual_participacao DECIMAL(5,2),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_doi_part_decl FOREIGN KEY (declaracao_id) REFERENCES doi_declaracoes(id)
);

-- Índices para Performance e Filtro Multi-tenancy
CREATE INDEX idx_doi_decl_entidade ON doi_declaracoes(entidade_id);
CREATE INDEX idx_doi_decl_import ON doi_declaracoes(importacao_id);
CREATE INDEX idx_doi_part_decl ON doi_participantes(declaracao_id);
CREATE INDEX idx_doi_import_entidade ON doi_importacoes(entidade_id);
