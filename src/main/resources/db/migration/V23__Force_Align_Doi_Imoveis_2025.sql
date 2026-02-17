-- V23: Forçar alinhamento da tabela doi_imoveis com o manual SQL 2025 (Idempotente)
-- Autor: Antigravity
-- Data: 2026-02-17

DO $$ 
BEGIN 
    -- Renomear created_at se existir
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'doi_imoveis' AND column_name = 'created_at') THEN
        ALTER TABLE doi_imoveis RENAME COLUMN created_at TO data_criacao;
    END IF;

    -- Renomear updated_at se existir
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'doi_imoveis' AND column_name = 'updated_at') THEN
        ALTER TABLE doi_imoveis RENAME COLUMN updated_at TO data_atualizacao;
    END IF;

    -- Renomear registro_imobiliario_patrimonial se existir
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'doi_imoveis' AND column_name = 'registro_imobiliario_patrimonial') THEN
        ALTER TABLE doi_imoveis RENAME COLUMN registro_imobiliario_patrimonial TO registro_imobiliario_patrimonial_rip;
    END IF;

    -- Renomear certidao_autorizacao_transferencia se existir
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'doi_imoveis' AND column_name = 'certidao_autorizacao_transferencia') THEN
        ALTER TABLE doi_imoveis RENAME COLUMN certidao_autorizacao_transferencia TO certidao_autorizacao_transferencia_cat;
    END IF;

    -- Renomear denominacao se existir
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'doi_imoveis' AND column_name = 'denominacao') THEN
        ALTER TABLE doi_imoveis RENAME COLUMN denominacao TO denominacao_rural;
    END IF;

    -- Renomear localizacao se existir
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'doi_imoveis' AND column_name = 'localizacao') THEN
        ALTER TABLE doi_imoveis RENAME COLUMN localizacao TO localizacao_detalhada;
    END IF;

    -- Renomear codigo_ibge se existir
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'doi_imoveis' AND column_name = 'codigo_ibge') THEN
        ALTER TABLE doi_imoveis RENAME COLUMN codigo_ibge TO codigo_ibge_municipio;
    END IF;

    -- Renomear complemento_numero_imovel se existir
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = 'doi_imoveis' AND column_name = 'complemento_numero_imovel') THEN
        ALTER TABLE doi_imoveis RENAME COLUMN complemento_numero_imovel TO complemento_numero;
    END IF;
END $$;

-- Adicionar colunas faltantes de forma segura
ALTER TABLE doi_imoveis 
    ADD COLUMN IF NOT EXISTS indicador_imovel_publico_uniao BOOLEAN NOT NULL DEFAULT FALSE,
    ADD COLUMN IF NOT EXISTS indicador_area_lote_nao_consta BOOLEAN NOT NULL DEFAULT FALSE,
    ADD COLUMN IF NOT EXISTS indicador_area_construida_nao_consta BOOLEAN DEFAULT FALSE,
    ADD COLUMN IF NOT EXISTS tipo_imovel VARCHAR(50),
    ADD COLUMN IF NOT EXISTS matricula VARCHAR(15),
    ADD COLUMN IF NOT EXISTS transcricao INTEGER,
    ADD COLUMN IF NOT EXISTS municipios_uf_lista TEXT;

-- Ajustar a coluna tipo_imovel para NOT NULL (se for nova, preenchemos primeiro)
UPDATE doi_imoveis SET tipo_imovel = 'N/A' WHERE tipo_imovel IS NULL;
ALTER TABLE doi_imoveis ALTER COLUMN tipo_imovel SET NOT NULL;

-- Preencher nulos para permitir NOT NULL nas colunas obrigatórias
UPDATE doi_imoveis SET area_imovel = 0 WHERE area_imovel IS NULL;
UPDATE doi_imoveis SET destinacao = 'URBANO' WHERE destinacao IS NULL;
UPDATE doi_imoveis SET tipo_logradouro = 'N/A' WHERE tipo_logradouro IS NULL;
UPDATE doi_imoveis SET nome_logradouro = 'N/A' WHERE nome_logradouro IS NULL;
UPDATE doi_imoveis SET numero_imovel = 'SN' WHERE numero_imovel IS NULL;
UPDATE doi_imoveis SET bairro = 'N/A' WHERE bairro IS NULL;
UPDATE doi_imoveis SET cep = '00000000' WHERE cep IS NULL;

-- Aplicar tipos e constraints
ALTER TABLE doi_imoveis 
    ALTER COLUMN area_imovel TYPE NUMERIC(15, 4),
    ALTER COLUMN area_imovel SET NOT NULL,
    ALTER COLUMN area_construida TYPE NUMERIC(16, 4),
    ALTER COLUMN destinacao SET NOT NULL,
    ALTER COLUMN tipo_logradouro SET NOT NULL,
    ALTER COLUMN nome_logradouro SET NOT NULL,
    ALTER COLUMN numero_imovel SET NOT NULL,
    ALTER COLUMN bairro SET NOT NULL,
    ALTER COLUMN cep SET NOT NULL;

-- Criar índices conforme manual
CREATE INDEX IF NOT EXISTS idx_imovel_cib ON doi_imoveis(cib);
CREATE INDEX IF NOT EXISTS idx_imovel_cep ON doi_imoveis(cep);
CREATE INDEX IF NOT EXISTS idx_imovel_incra ON doi_imoveis(codigo_incra);
