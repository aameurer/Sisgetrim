-- V18: Padronização de nomes de colunas conforme JSON de exemplo DOI 2025
-- Autor: Antigravity
-- Data: 2026-02-17

-- Tabela doi_declaracao
ALTER TABLE doi_declaracao RENAME COLUMN numero_declaracao TO matricula;
ALTER TABLE doi_declaracao RENAME COLUMN data_operacao TO data_lavratura_registro_averbacao;
ALTER TABLE doi_declaracao RENAME COLUMN valor_operacao TO valor_operacao_imobiliaria;
ALTER TABLE doi_declaracao RENAME COLUMN descricao_outras_operacoes TO descricao_outras_operacoes_imobiliarias;
ALTER TABLE doi_declaracao RENAME COLUMN municipio_imovel TO codigo_ibge;

-- Tabela doi_imovel
ALTER TABLE doi_imovel RENAME COLUMN municipio_ibge TO codigo_ibge;
ALTER TABLE doi_imovel RENAME COLUMN area_terreno TO area_imovel;
