-- V25: Criação da tabela doi_adquirentes conforme o manual SQL 2025
-- Autor: Antigravity
-- Data: 2026-02-17

CREATE TABLE doi_adquirentes (
    id BIGSERIAL PRIMARY KEY,
    doi_declaracao_id BIGINT REFERENCES doi_declaracoes(id),
    entidade_id BIGINT REFERENCES entidades(id),

    -- Identificação do Adquirente
    indicador_ni_identificado BOOLEAN NOT NULL,
    motivo_nao_identificacao_ni VARCHAR(2), -- Opcional se indicador_ni_identificado for False
    ni VARCHAR(14), -- CPF ou CNPJ (11 ou 14 dígitos)
    
    -- Participação na Aquisição
    participacao NUMERIC(7, 4), -- Soma total dos adquirentes deve ser ~100%
    indicador_nao_consta_participacao BOOLEAN NOT NULL DEFAULT FALSE,
    
    -- Status do Adquirente
    indicador_estrangeiro BOOLEAN NOT NULL DEFAULT FALSE,
    indicador_espolio BOOLEAN NOT NULL DEFAULT FALSE,
    cpf_inventariante VARCHAR(11), -- Preencher se for Espólio
    
    -- Dados do Cônjuge
    indicador_conjuge BOOLEAN NOT NULL DEFAULT FALSE,
    indicador_conjuge_participa BOOLEAN, 
    regime_bens VARCHAR(50),             
    indicador_cpf_conjuge_identificado BOOLEAN,
    cpf_conjuge VARCHAR(11),
    
    -- Representantes (Mandatos/Procurações)
    indicador_representante BOOLEAN NOT NULL DEFAULT FALSE,
    representantes JSONB, -- Lista de objetos [{ "ni": "...", "nome": "..." }]

    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Índices para agilizar buscas por CPF/CNPJ do comprador
CREATE INDEX idx_adquirente_ni ON doi_adquirentes(ni);
CREATE INDEX idx_adquirente_decl_id ON doi_adquirentes(doi_declaracao_id);
