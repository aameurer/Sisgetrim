-- V24: Criação da tabela doi_alienantes conforme o manual SQL 2025
-- Autor: Antigravity
-- Data: 2026-02-17

CREATE TABLE doi_alienantes (
    id BIGSERIAL PRIMARY KEY,
    doi_declaracao_id BIGINT REFERENCES doi_declaracoes(id),
    entidade_id BIGINT REFERENCES entidades(id),

    -- Identificação do Alienante
    indicador_ni_identificado BOOLEAN NOT NULL,
    motivo_nao_identificacao_ni VARCHAR(2), -- Preencher se indicador_ni_identificado for False
    ni VARCHAR(14), -- CPF ou CNPJ
    
    -- Participação
    participacao NUMERIC(7, 4), -- Soma deve ser entre 99.00 e 100.00
    indicador_nao_consta_participacao BOOLEAN NOT NULL DEFAULT FALSE,
    
    -- Status
    indicador_estrangeiro BOOLEAN NOT NULL DEFAULT FALSE,
    indicador_espolio BOOLEAN NOT NULL DEFAULT FALSE,
    cpf_inventariante VARCHAR(11), -- Se indicador_espolio for True
    
    -- Dados do Cônjuge
    indicador_conjuge BOOLEAN NOT NULL DEFAULT FALSE,
    indicador_conjuge_participa BOOLEAN, -- Se indicador_conjuge for True
    regime_bens VARCHAR(50),             -- Se indicador_conjuge for True
    indicador_cpf_conjuge_identificado BOOLEAN,
    cpf_conjuge VARCHAR(11),
    
    -- Representantes (JSONB para suportar a lista de objetos exigida)
    indicador_representante BOOLEAN NOT NULL DEFAULT FALSE,
    representantes JSONB, -- Lista de CPFs/CNPJs dos representantes

    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Índices para busca por documento
CREATE INDEX idx_alienante_ni ON doi_alienantes(ni);
CREATE INDEX idx_alienante_cpf_conjuge ON doi_alienantes(cpf_conjuge);
CREATE INDEX idx_alienante_decl_id ON doi_alienantes(doi_declaracao_id);
