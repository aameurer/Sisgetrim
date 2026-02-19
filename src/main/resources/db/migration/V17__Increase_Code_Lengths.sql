-- V17: Ajuste de tamanho de colunas de código para evitar erros de truncamento
-- Autor: Antigravity
-- Data: 2026-02-17

ALTER TABLE doi_declaracao
    ALTER COLUMN tipo_declaracao TYPE VARCHAR(50),
    ALTER COLUMN forma_pagamento TYPE VARCHAR(50),
    ALTER COLUMN natureza_titulo TYPE VARCHAR(50),
    ALTER COLUMN tipo_operacao_imobiliaria TYPE VARCHAR(50),
    ALTER COLUMN tipo_parte_transacionada TYPE VARCHAR(50),
    ALTER COLUMN tipo_servico TYPE VARCHAR(50);

ALTER TABLE doi_imovel
    ALTER COLUMN destinacao TYPE VARCHAR(50);

ALTER TABLE doi_participante
    ALTER COLUMN motivo_nao_identificacao_ni TYPE VARCHAR(50),
    ALTER COLUMN regime_bens TYPE VARCHAR(50);
