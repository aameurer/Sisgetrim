-- Criação da tabela de Arrecadação de ITBI (Fiscal)
CREATE TABLE fiscal_itbi (
    id BIGSERIAL PRIMARY KEY,
    entidade_id BIGINT NOT NULL,
    -- Identificação
    numero_itbi VARCHAR(20) NOT NULL,
    ano_itbi INTEGER NOT NULL,
    data_itbi DATE,
    tipo_itbi VARCHAR(100),
    situacao_itbi VARCHAR(100),
    -- Partes
    proprietarios_nomes TEXT,
    proprietarios_cpfs TEXT,
    transmitentes_nomes TEXT,
    transmitentes_cpfs TEXT,
    adquirentes_nomes TEXT,
    adquirentes_cpfs TEXT,
    -- Valores Financeiros
    valor_venal_territorial_calculado DECIMAL(19,2),
    valor_venal_predial_calculado DECIMAL(19,2),
    valor_venal_total_calculado DECIMAL(19,2),
    valor_venal_territorial_informado DECIMAL(19,2),
    valor_venal_predial_informado DECIMAL(19,2),
    valor_venal_total_informado DECIMAL(19,2),
    valor_venal_iptu_territorial DECIMAL(19,2),
    valor_venal_iptu_predial DECIMAL(19,2),
    valor_venal_iptu_total DECIMAL(19,2),
    -- Dados Cadastrais (CADIMO)
    inscricao_imobiliaria VARCHAR(50),
    matricula VARCHAR(50),
    cib VARCHAR(50),
    area_terreno DECIMAL(19,4),
    area_construida DECIMAL(19,4),
    -- Metadados
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_fiscal_itbi_entidade FOREIGN KEY (entidade_id) REFERENCES entidades(id)
);

-- Índices para Performance e Filtro Multi-tenancy
CREATE INDEX idx_fiscal_itbi_entidade ON fiscal_itbi(entidade_id);
CREATE INDEX idx_fiscal_itbi_numero_ano ON fiscal_itbi(numero_itbi, ano_itbi);
CREATE INDEX idx_fiscal_itbi_inscricao ON fiscal_itbi(inscricao_imobiliaria);
