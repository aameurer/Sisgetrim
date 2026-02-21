-- V31__Create_IBGE_Tables.sql
-- Criação da tabela principal de Declarações
CREATE TABLE IBGE_declaracoes (
    IBGE_id BIGSERIAL PRIMARY KEY,
    IBGE_entidade_id BIGINT NOT NULL,
    IBGE_nome_cartorio VARCHAR(255) NOT NULL,
    IBGE_cns INTEGER NOT NULL,
    IBGE_tipo_servico VARCHAR(255) NOT NULL,
    IBGE_tipo_declaracao VARCHAR(255) NOT NULL,
    IBGE_matricula VARCHAR(255) NOT NULL,
    IBGE_transcricao VARCHAR(255),
    IBGE_codigo_nacional_matricula VARCHAR(255),
    IBGE_matricula_notarial_eletronica VARCHAR(255),
    IBGE_tipo_operacao_imobiliaria VARCHAR(255) NOT NULL,
    IBGE_descricao_outras_operacoes_imobiliarias VARCHAR(255),
    IBGE_data_lavratura_registro_averbacao DATE NOT NULL,
    IBGE_destinacao VARCHAR(255) NOT NULL,
    IBGE_tipo_logradouro VARCHAR(255) NOT NULL,
    IBGE_nome_logradouro VARCHAR(255) NOT NULL,
    IBGE_numero_imovel VARCHAR(255) NOT NULL,
    IBGE_complemento_endereco VARCHAR(255) NOT NULL,
    IBGE_complemento_numero_imovel VARCHAR(255),
    IBGE_bairro VARCHAR(255) NOT NULL,
    IBGE_inscricao_municipal VARCHAR(255) NOT NULL,
    IBGE_codigo_ibge VARCHAR(20) NOT NULL,
    IBGE_denominacao VARCHAR(255),
    IBGE_localizacao VARCHAR(255),
    IBGE_cib VARCHAR(20),
    
    -- Campos de auditoria padrão
    IBGE_created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    IBGE_updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    IBGE_ativo BOOLEAN DEFAULT TRUE,

    CONSTRAINT fk_ibge_declaracao_entidade FOREIGN KEY (IBGE_entidade_id) REFERENCES entidades (id) ON DELETE CASCADE
);

-- Criação da tabela filha de Alienantes
CREATE TABLE IBGE_alienantes (
    IBGE_id BIGSERIAL PRIMARY KEY,
    IBGE_declaracao_id BIGINT NOT NULL,
    IBGE_indicador_ni_identificado BOOLEAN NOT NULL,
    IBGE_ni VARCHAR(20) NOT NULL,
    IBGE_motivo_nao_identificacao_ni VARCHAR(255),
    IBGE_indicador_cpf_conjuge_identificado BOOLEAN,
    IBGE_cpf_conjuge VARCHAR(20),
    
    -- Campos de auditoria padrão
    IBGE_created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    IBGE_updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    IBGE_ativo BOOLEAN DEFAULT TRUE,

    CONSTRAINT fk_ibge_alienante_declaracao FOREIGN KEY (IBGE_declaracao_id) REFERENCES IBGE_declaracoes (IBGE_id) ON DELETE CASCADE
);

-- Criação da tabela filha de Adquirentes
CREATE TABLE IBGE_adquirentes (
    IBGE_id BIGSERIAL PRIMARY KEY,
    IBGE_declaracao_id BIGINT NOT NULL,
    IBGE_indicador_ni_identificado BOOLEAN NOT NULL,
    IBGE_ni VARCHAR(20) NOT NULL,
    IBGE_motivo_nao_identificacao_ni VARCHAR(255),
    IBGE_indicador_cpf_conjuge_identificado BOOLEAN,
    IBGE_cpf_conjuge VARCHAR(20),
    
    -- Campos de auditoria padrão
    IBGE_created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    IBGE_updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    IBGE_ativo BOOLEAN DEFAULT TRUE,

    CONSTRAINT fk_ibge_adquirente_declaracao FOREIGN KEY (IBGE_declaracao_id) REFERENCES IBGE_declaracoes (IBGE_id) ON DELETE CASCADE
);

-- Índices obrigatórios
CREATE INDEX idx_ibge_codigo_ibge ON IBGE_declaracoes (IBGE_codigo_ibge);
CREATE INDEX idx_ibge_inscricao_municipal ON IBGE_declaracoes (IBGE_inscricao_municipal);
CREATE INDEX idx_ibge_alienantes_ni ON IBGE_alienantes (IBGE_ni);
CREATE INDEX idx_ibge_adquirentes_ni ON IBGE_adquirentes (IBGE_ni);
CREATE INDEX idx_ibge_declaracoes_entidade ON IBGE_declaracoes (IBGE_entidade_id);
