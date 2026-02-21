-- V26: Aumentar o tamanho das colunas motivo_nao_identificacao_ni e regime_bens
-- Objetivo: Evitar erro de "valor muito longo" na importação de arquivos DOI
-- Autor: Antigravity
-- Data: 2026-02-20

-- Tabela doi_alienantes
ALTER TABLE doi_alienantes 
    ALTER COLUMN motivo_nao_identificacao_ni TYPE VARCHAR(255),
    ALTER COLUMN regime_bens TYPE VARCHAR(255);

-- Tabela doi_adquirentes
ALTER TABLE doi_adquirentes 
    ALTER COLUMN motivo_nao_identificacao_ni TYPE VARCHAR(255),
    ALTER COLUMN regime_bens TYPE VARCHAR(255);
