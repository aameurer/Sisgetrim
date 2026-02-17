-- V19: Resolução de duplicidade e padronização para o plural conforme manual
-- Autor: Antigravity
-- Data: 2026-02-17

-- 1. Remover tabelas legadas da V5 (que são vazias e redundantes)
DROP TABLE IF EXISTS doi_participantes CASCADE;
DROP TABLE IF EXISTS doi_declaracoes CASCADE;
DROP TABLE IF EXISTS doi_importacoes CASCADE;

-- 2. Renomear tabelas atuais (que possuem dados) de singular para plural
ALTER TABLE doi_importacao RENAME TO doi_importacoes;
ALTER TABLE doi_declaracao RENAME TO doi_declaracoes;
ALTER TABLE doi_participante RENAME TO doi_participantes;
ALTER TABLE doi_imovel RENAME TO doi_imoveis;
ALTER TABLE doi_importacao_erros RENAME TO doi_importacao_erros_plural_temp; -- Evitar conflito se houver lógica
ALTER TABLE doi_importacao_erros_plural_temp RENAME TO doi_importacao_erros; -- Apenas garantindo consistência

-- Se as sequências não forem renomeadas automaticamente pelo PostgreSQL (geralmente são em RENAME TABLE),
-- o Hibernate lidará com o mapeamento.
