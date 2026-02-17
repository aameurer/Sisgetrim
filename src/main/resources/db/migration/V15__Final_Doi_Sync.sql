-- V15: Finalização da Sincronização de Campos DOI e Indicadores Faltantes
-- Autor: Antigravity
-- Data: 2026-02-17

-- 1. Renomeação de data_ato para data_operacao para sincronizar com JPA
ALTER TABLE doi_declaracao RENAME COLUMN data_ato TO data_operacao;

-- 2. Adicionar colunas de indicadores e tipos que faltavam
ALTER TABLE doi_declaracao 
    ADD COLUMN indicador_nao_consta_valor BOOLEAN DEFAULT FALSE,
    ADD COLUMN tipo_imovel VARCHAR(50),
    ADD COLUMN municipio_imovel VARCHAR(100),
    ADD COLUMN tipo_ato VARCHAR(10);
