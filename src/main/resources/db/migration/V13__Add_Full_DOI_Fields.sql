-- Adição de campos faltantes na doi_declaracao
ALTER TABLE doi_declaracao
    ADD COLUMN data_negocio_juridico DATE,
    ADD COLUMN existe_doi_anterior BOOLEAN DEFAULT FALSE,
    ADD COLUMN folha VARCHAR(7),
    ADD COLUMN indicador_pagamento_dinheiro BOOLEAN DEFAULT FALSE,
    ADD COLUMN indicador_permuta_bens BOOLEAN DEFAULT FALSE,
    ADD COLUMN matricula_notarial_eletronica VARCHAR(24),
    ADD COLUMN mes_ano_ultima_parcela DATE,
    ADD COLUMN natureza_titulo VARCHAR(2),
    ADD COLUMN numero_livro VARCHAR(7),
    ADD COLUMN numero_registro VARCHAR(30),
    ADD COLUMN numero_registro_averbacao VARCHAR(7),
    ADD COLUMN retificacao_ato BOOLEAN DEFAULT FALSE,
    ADD COLUMN tipo_operacao_imobiliaria VARCHAR(2),
    ADD COLUMN tipo_parte_transacionada VARCHAR(2),
    ADD COLUMN tipo_servico VARCHAR(2),
    ADD COLUMN transcricao INTEGER,
    ADD COLUMN valor_base_calculo_itbi_itcmd DECIMAL(18,2),
    ADD COLUMN valor_pago_ate_data_ato DECIMAL(18,2),
    ADD COLUMN valor_pago_moeda_corrente_data_ato DECIMAL(18,2),
    ADD COLUMN valor_parte_transacionada DECIMAL(18,2),
    ADD COLUMN indicador_alienacao_fiduciaria BOOLEAN DEFAULT FALSE,
    ADD COLUMN indicador_area_construida_nao_consta BOOLEAN DEFAULT FALSE,
    ADD COLUMN indicador_area_lote_nao_consta BOOLEAN DEFAULT FALSE,
    ADD COLUMN indicador_imovel_publico_uniao BOOLEAN DEFAULT FALSE,
    ADD COLUMN indicador_nao_consta_valor_base_calculo BOOLEAN DEFAULT FALSE,
    ADD COLUMN indicador_nao_consta_valor_operacao BOOLEAN DEFAULT FALSE;

-- Adição de campos faltantes na doi_imovel
ALTER TABLE doi_imovel
    ADD COLUMN certidao_autorizacao_transferencia VARCHAR(11),
    ADD COLUMN codigo_incra VARCHAR(13),
    ADD COLUMN codigo_nacional_matricula VARCHAR(16),
    ADD COLUMN destinacao VARCHAR(1),
    ADD COLUMN registro_imobiliario_patrimonial VARCHAR(13),
    ADD COLUMN denominacao VARCHAR(200),
    ADD COLUMN localizacao VARCHAR(200),
    ADD COLUMN cep VARCHAR(8),
    ADD COLUMN bairro VARCHAR(150),
    ADD COLUMN municipio_ibge VARCHAR(7),
    ADD COLUMN tipo_logradouro VARCHAR(30),
    ADD COLUMN nome_logradouro VARCHAR(150),
    ADD COLUMN numero_imovel VARCHAR(10),
    ADD COLUMN complemento_endereco VARCHAR(100),
    ADD COLUMN complemento_numero_imovel VARCHAR(10);

-- Adição de campos faltantes na doi_participante
ALTER TABLE doi_participante
    ADD COLUMN cpf_inventariante VARCHAR(11),
    ADD COLUMN indicador_conjuge BOOLEAN DEFAULT FALSE,
    ADD COLUMN indicador_conjuge_participa BOOLEAN DEFAULT FALSE,
    ADD COLUMN indicador_cpf_conjuge_identificado BOOLEAN DEFAULT FALSE,
    ADD COLUMN indicador_estrangeiro BOOLEAN DEFAULT FALSE,
    ADD COLUMN indicador_nao_consta_participacao BOOLEAN DEFAULT FALSE,
    ADD COLUMN indicador_ni_identificado BOOLEAN DEFAULT FALSE,
    ADD COLUMN indicador_representante BOOLEAN DEFAULT FALSE,
    ADD COLUMN motivo_nao_identificacao_ni VARCHAR(1),
    ADD COLUMN regime_bens VARCHAR(1);

-- Novas tabelas auxiliares para DOI 2025
CREATE TABLE doi_participante_representante (
    id BIGSERIAL PRIMARY KEY,
    participante_id BIGINT NOT NULL REFERENCES doi_participante(id),
    ni VARCHAR(14) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE doi_imovel_municipio (
    id BIGSERIAL PRIMARY KEY,
    imovel_id BIGINT NOT NULL REFERENCES doi_imovel(id),
    codigo_ibge VARCHAR(7) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
