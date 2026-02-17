-- V20: Alinhamento da tabela doi_declaracoes com o Manual SQL oficial
-- Autor: Antigravity
-- Data: 2026-02-17

-- 1. Renomeação de Colunas
ALTER TABLE doi_declaracoes RENAME COLUMN data_lavratura_registro_averbacao TO data_lavratura;
ALTER TABLE doi_declaracoes RENAME COLUMN matricula_notarial_eletronica TO mne_eletronica;
ALTER TABLE doi_declaracoes RENAME COLUMN numero_registro_averbacao TO num_registro_averbacao;
ALTER TABLE doi_declaracoes RENAME COLUMN numero_registro TO numero_registro_td;
ALTER TABLE doi_declaracoes RENAME COLUMN created_at TO data_cadastro;

-- 2. Ajuste de Tipos, Constraints e Colunas Faltantes
-- Preencher valores nulos para permitir NOT NULL
UPDATE doi_declaracoes SET tipo_declaracao = 'G' WHERE tipo_declaracao IS NULL;
UPDATE doi_declaracoes SET tipo_servico = 'N/A' WHERE tipo_servico IS NULL;
UPDATE doi_declaracoes SET data_lavratura = '2000-01-01' WHERE data_lavratura IS NULL;
UPDATE doi_declaracoes SET tipo_ato = 'OUTROS' WHERE tipo_ato IS NULL;
UPDATE doi_declaracoes SET folha = '0' WHERE folha IS NULL;

ALTER TABLE doi_declaracoes 
    ALTER COLUMN tipo_declaracao TYPE VARCHAR(20),
    ALTER COLUMN tipo_declaracao SET NOT NULL,
    ALTER COLUMN tipo_servico TYPE VARCHAR(50),
    ALTER COLUMN tipo_servico SET NOT NULL,
    ALTER COLUMN data_lavratura SET NOT NULL,
    ALTER COLUMN tipo_ato TYPE VARCHAR(100),
    ALTER COLUMN tipo_ato SET NOT NULL,
    ALTER COLUMN folha SET NOT NULL,
    ALTER COLUMN matricula TYPE VARCHAR(15);

-- Adicionar restrição UNIQUE na matrícula
-- Primeiro limpamos duplicatas mantendo apenas a mais recente por data de cadastro/id
-- Precisamos limpar as tabelas dependentes primeiro devido às FKs
DELETE FROM doi_participantes WHERE declaracao_id IN (
    SELECT id FROM doi_declaracoes WHERE id NOT IN (
        SELECT MAX(id) FROM doi_declaracoes GROUP BY matricula
    )
);

DELETE FROM doi_imoveis WHERE declaracao_id IN (
    SELECT id FROM doi_declaracoes WHERE id NOT IN (
        SELECT MAX(id) FROM doi_declaracoes GROUP BY matricula
    )
);

-- Agora limpamos a tabela principal
DELETE FROM doi_declaracoes WHERE id NOT IN (
    SELECT MAX(id) FROM doi_declaracoes GROUP BY matricula
);

ALTER TABLE doi_declaracoes ADD CONSTRAINT doi_declaracoes_matricula_key UNIQUE (matricula);

-- Adicionar colunas faltantes
ALTER TABLE doi_declaracoes 
    ADD COLUMN IF NOT EXISTS tipo_livro VARCHAR(50),
    ADD COLUMN IF NOT EXISTS cnm_codigo_nacional VARCHAR(20);

-- Ajustar natureza_titulo
ALTER TABLE doi_declaracoes ALTER COLUMN natureza_titulo TYPE VARCHAR(100);

-- Garante NOT NULL na existe_doi_anterior (já deve ter default do JPA, mas fixamos aqui)
ALTER TABLE doi_declaracoes ALTER COLUMN existe_doi_anterior SET NOT NULL;

-- 3. Criação de Índices Adicionais
CREATE INDEX IF NOT EXISTS idx_doi_cnm ON doi_declaracoes(cnm_codigo_nacional);
CREATE INDEX IF NOT EXISTS idx_doi_mne ON doi_declaracoes(mne_eletronica);
