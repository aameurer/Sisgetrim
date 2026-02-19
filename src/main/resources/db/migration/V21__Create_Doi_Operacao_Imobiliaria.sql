-- V21: Criação da tabela de operações imobiliárias conforme manual
-- Autor: Antigravity
-- Data: 2026-02-17

CREATE TABLE doi_operacao_imobiliaria (
    id BIGSERIAL PRIMARY KEY,
    -- Relacionamento com a tabela principal
    doi_declaracao_id BIGINT REFERENCES doi_declaracoes(id) ON DELETE CASCADE,

    data_negocio_juridico DATE NOT NULL,
    tipo_operacao_imobiliaria VARCHAR(100) NOT NULL,
    descricao_outras_operacoes VARCHAR(30), -- Preencher se tipo_operacao for "Outras"
    
    -- Valores e Indicadores de Valor
    valor_operacao_imobiliaria NUMERIC(20,2),
    indicador_nao_consta_valor_operacao BOOLEAN DEFAULT FALSE,
    
    valor_base_calculo_itbi_itcmd NUMERIC(20,2),
    indicador_nao_consta_base_calculo BOOLEAN DEFAULT FALSE,
    
    -- Pagamento
    forma_pagamento VARCHAR(50) NOT NULL,
    indicador_alienacao_fiduciaria BOOLEAN, -- Se "A prazo"
    mes_ano_ultima_parcela DATE,           -- Se "A prazo"
    valor_pago_ate_data_ato NUMERIC(20,2),  -- Se "A prazo"
    
    -- Permuta e Espécie
    indicador_permuta_bens BOOLEAN NOT NULL,
    indicador_pagamento_dinheiro BOOLEAN NOT NULL,
    valor_pago_moeda_corrente_data_ato NUMERIC(20,2), -- Se indicador_pagamento_dinheiro for True
    
    -- Parte Transacionada
    tipo_parte_transacionada VARCHAR(50) NOT NULL,
    valor_parte_transacionada NUMERIC(20,2) NOT NULL,

    -- Auditoria
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Índices de busca
CREATE INDEX idx_doi_oper_decl ON doi_operacao_imobiliaria(doi_declaracao_id);
