-- Migração para criação das tabelas de Cadastro de Cartório

CREATE TABLE cartorios (
    id BIGSERIAL PRIMARY KEY,
    codigo_cns VARCHAR(255) NOT NULL UNIQUE,
    denominacao VARCHAR(255) NOT NULL,
    data_criacao DATE NOT NULL,
    situacao VARCHAR(50) NOT NULL,
    tipo VARCHAR(50) NOT NULL,
    situacao_juridica_responsavel VARCHAR(50) NOT NULL,
    bairro VARCHAR(255),
    cep VARCHAR(20),
    endereco VARCHAR(255),
    numero VARCHAR(50),
    telefone_principal VARCHAR(50),
    telefone_secundario VARCHAR(50),
    email VARCHAR(255),
    cnpj VARCHAR(20),
    razao_social VARCHAR(255),
    atividade_principal VARCHAR(255),
    natureza_juridica VARCHAR(255) DEFAULT '303-4 - Serviço Notarial e Registral',
    entidade_id BIGINT NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_cartorio_entidade FOREIGN KEY (entidade_id) REFERENCES entidades(id)
);

CREATE TABLE cartorio_atribuicoes (
    cartorio_id BIGINT NOT NULL,
    atribuicao VARCHAR(100) NOT NULL,
    PRIMARY KEY (cartorio_id, atribuicao),
    CONSTRAINT fk_atribuicao_cartorio FOREIGN KEY (cartorio_id) REFERENCES cartorios(id)
);

CREATE TABLE responsavel_cartorio (
    id BIGSERIAL PRIMARY KEY,
    tipo VARCHAR(50) NOT NULL,
    nome VARCHAR(255) NOT NULL,
    cpf VARCHAR(20) NOT NULL,
    data_nomeacao DATE NOT NULL,
    data_ingresso DATE NOT NULL,
    substituto VARCHAR(50) NOT NULL,
    cartorio_id BIGINT NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_responsavel_cartorio FOREIGN KEY (cartorio_id) REFERENCES cartorios(id)
);

CREATE INDEX idx_cartorio_entidade ON cartorios(entidade_id);
CREATE INDEX idx_cartorio_cns ON cartorios(codigo_cns);
CREATE INDEX idx_responsavel_cartorio ON responsavel_cartorio(cartorio_id);
