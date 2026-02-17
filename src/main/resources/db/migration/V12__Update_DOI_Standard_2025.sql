-- V12: Atualização do Módulo de Importação DOI - Padrão JSON 2025
-- Autor: Antigravity
-- Data: 2026-02-17

-- 1. Nova estrutura de Controle de Importação
CREATE TABLE doi_importacao (
    id BIGSERIAL PRIMARY KEY,
    entidade_id BIGINT NOT NULL,
    usuario_id BIGINT NOT NULL,
    nome_arquivo VARCHAR(255) NOT NULL,
    data_importacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    total_registros INTEGER DEFAULT 0,
    status VARCHAR(50) DEFAULT 'PROCESSANDO', -- PROCESSANDO, CONCLUIDO, ERRO
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_doi_import_entidade FOREIGN KEY (entidade_id) REFERENCES entidades(id),
    CONSTRAINT fk_doi_import_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
);

-- 2. Nova estrutura de Declarações (Dados da Transação)
CREATE TABLE doi_declaracao (
    id BIGSERIAL PRIMARY KEY,
    importacao_id BIGINT NOT NULL,
    entidade_id BIGINT NOT NULL,
    situacao VARCHAR(20) DEFAULT 'RASCUNHO', -- RASCUNHO, INAPTO, PROCESSADO
    tipo_ato VARCHAR(10),
    data_ato DATE,
    matricula VARCHAR(50),
    valor_operacao DECIMAL(19,2),
    log_erros TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_doi_decl_import FOREIGN KEY (importacao_id) REFERENCES doi_importacao(id),
    CONSTRAINT fk_doi_decl_entidade FOREIGN KEY (entidade_id) REFERENCES entidades(id)
);

-- 3. Nova estrutura de Dados Cadastrais do Imóvel
CREATE TABLE doi_imovel (
    id BIGSERIAL PRIMARY KEY,
    declaracao_id BIGINT NOT NULL,
    entidade_id BIGINT NOT NULL,
    cib VARCHAR(50),
    inscricao_municipal VARCHAR(50),
    area_terreno DECIMAL(19,4),
    area_construida DECIMAL(19,4),
    endereco_completo TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_doi_imov_decl FOREIGN KEY (declaracao_id) REFERENCES doi_declaracao(id),
    CONSTRAINT fk_doi_imov_entidade FOREIGN KEY (entidade_id) REFERENCES entidades(id)
);

-- 4. Nova estrutura de Participantes (Alienantes e Adquirentes)
CREATE TABLE doi_participante (
    id BIGSERIAL PRIMARY KEY,
    declaracao_id BIGINT NOT NULL,
    entidade_id BIGINT NOT NULL,
    tipo_participante VARCHAR(20), -- ALIENANTE, ADQUIRENTE
    ni VARCHAR(20), -- CPF/CNPJ
    participacao DECIMAL(5,2),
    indicador_espolio BOOLEAN DEFAULT FALSE,
    cpf_conjuge VARCHAR(11),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_doi_part_decl FOREIGN KEY (declaracao_id) REFERENCES doi_declaracao(id),
    CONSTRAINT fk_doi_part_entidade FOREIGN KEY (entidade_id) REFERENCES entidades(id)
);

-- 5. Índices de Performance Sugeridos para Malha Fiscal e Multi-tenancy
CREATE INDEX idx_doi_participante_ent_ni ON doi_participante (entidade_id, ni);
CREATE INDEX idx_doi_declaracao_ent_mat ON doi_declaracao (entidade_id, matricula);
CREATE INDEX idx_doi_declaracao_import ON doi_declaracao (importacao_id);
CREATE INDEX idx_doi_imovel_declaracao ON doi_imovel (declaracao_id);

-- 6. Tabela de Log de Erros de Importação (Opcional, mas recomendado para detalhamento)
CREATE TABLE doi_importacao_erros (
    id BIGSERIAL PRIMARY KEY,
    importacao_id BIGINT NOT NULL,
    linha_json INTEGER,
    campo_chave VARCHAR(100), -- Ex: Matricula ou NI
    mensagem_erro TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_doi_error_import FOREIGN KEY (importacao_id) REFERENCES doi_importacao(id)
);
