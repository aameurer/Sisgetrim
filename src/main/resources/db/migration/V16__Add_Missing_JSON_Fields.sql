-- V16: Sincronização de campos identificados no JSON de exemplo
-- Autor: Antigravity
-- Data: 2026-02-17

ALTER TABLE doi_declaracao
    ADD COLUMN tipo_declaracao VARCHAR(1),
    ADD COLUMN descricao_outras_operacoes TEXT,
    ADD COLUMN forma_pagamento VARCHAR(1);
