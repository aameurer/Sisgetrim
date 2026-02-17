-- V14: Correção de Mismatch de Campos e Colunas Faltantes na Estrutura DOI
-- Autor: Antigravity
-- Data: 2026-02-17

-- 1. Renomear matricula para numero_declaracao na doi_declaracao (Sincronizar com JPA)
ALTER TABLE doi_declaracao RENAME COLUMN matricula TO numero_declaracao;

-- 2. Adicionar coluna area_imovel que estava faltando
ALTER TABLE doi_declaracao ADD COLUMN area_imovel DECIMAL(18,4);

-- 3. Atualizar índices para refletir o novo nome da coluna
DROP INDEX IF EXISTS idx_doi_declaracao_ent_mat;
CREATE INDEX idx_doi_declaracao_ent_num ON doi_declaracao (entidade_id, numero_declaracao);
