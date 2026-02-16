-- Migração para expandir colunas de FiscalItbi (47 campos da planilha)
-- Renomeando colunas existentes para o novo padrão e adicionando as faltantes

-- 1. Renomeações
ALTER TABLE fiscal_itbi RENAME COLUMN numero_itbi TO itbi_numero;
ALTER TABLE fiscal_itbi RENAME COLUMN ano_itbi TO itbi_ano;
ALTER TABLE fiscal_itbi RENAME COLUMN data_itbi TO itbi_data;
ALTER TABLE fiscal_itbi RENAME COLUMN tipo_itbi TO itbi_tipo;
ALTER TABLE fiscal_itbi RENAME COLUMN situacao_itbi TO itbi_situacao;
ALTER TABLE fiscal_itbi RENAME COLUMN proprietarios_nomes TO itbi_proprietario_nome;
ALTER TABLE fiscal_itbi RENAME COLUMN proprietarios_cpfs TO itbi_proprietario_cpf;
ALTER TABLE fiscal_itbi RENAME COLUMN transmitentes_nomes TO itbi_transmitente_nome;
ALTER TABLE fiscal_itbi RENAME COLUMN transmitentes_cpfs TO itbi_transmitente_cpf;
ALTER TABLE fiscal_itbi RENAME COLUMN adquirentes_nomes TO itbi_adquirente_nome;
ALTER TABLE fiscal_itbi RENAME COLUMN adquirentes_cpfs TO itbi_adquirente_cpf;
ALTER TABLE fiscal_itbi RENAME COLUMN valor_venal_territorial_calculado TO itbi_valor_venal_calculado_vvt;
ALTER TABLE fiscal_itbi RENAME COLUMN valor_venal_predial_calculado TO itbi_valor_venal_calculado_vvp;
ALTER TABLE fiscal_itbi RENAME COLUMN valor_venal_total_calculado TO itbi_valor_venal_calculado_total;
ALTER TABLE fiscal_itbi RENAME COLUMN valor_venal_territorial_informado TO itbi_valor_venal_informado_vvt;
ALTER TABLE fiscal_itbi RENAME COLUMN valor_venal_predial_informado TO itbi_valor_venal_informado_vvp;
ALTER TABLE fiscal_itbi RENAME COLUMN valor_venal_total_informado TO itbi_valor_venal_informado_total;
ALTER TABLE fiscal_itbi RENAME COLUMN valor_venal_iptu_territorial TO iptu_valor_venal_territorial;
ALTER TABLE fiscal_itbi RENAME COLUMN valor_venal_iptu_predial TO iptu_valor_venal_predial;
ALTER TABLE fiscal_itbi RENAME COLUMN valor_venal_iptu_total TO iptu_valor_venal_total;
ALTER TABLE fiscal_itbi RENAME COLUMN inscricao_imobiliaria TO cadimo_inscricao;
ALTER TABLE fiscal_itbi RENAME COLUMN matricula TO cadimo_matricula;
ALTER TABLE fiscal_itbi RENAME COLUMN cib TO cadimo_cib;
ALTER TABLE fiscal_itbi RENAME COLUMN area_terreno TO cadimo_area_terreno;
ALTER TABLE fiscal_itbi RENAME COLUMN area_construida TO cadimo_area_construida;

-- 2. Adição de novas colunas ITBI
ALTER TABLE fiscal_itbi ADD COLUMN itbi_percentual_vvt DECIMAL(19,4);

-- 3. Adição de novas colunas CADIMO
ALTER TABLE fiscal_itbi ADD COLUMN cadimo_cadastro VARCHAR(50);
ALTER TABLE fiscal_itbi ADD COLUMN cadimo_tipo VARCHAR(50);
ALTER TABLE fiscal_itbi ADD COLUMN cadimo_situacao_cadastral VARCHAR(100);
ALTER TABLE fiscal_itbi ADD COLUMN cadimo_quadra VARCHAR(50);
ALTER TABLE fiscal_itbi ADD COLUMN cadimo_lote VARCHAR(50);
ALTER TABLE fiscal_itbi ADD COLUMN cadimo_bairro_nome VARCHAR(200);
ALTER TABLE fiscal_itbi ADD COLUMN cadimo_logradouro_nome VARCHAR(255);
ALTER TABLE fiscal_itbi ADD COLUMN cadimo_numero VARCHAR(50);
ALTER TABLE fiscal_itbi ADD COLUMN cadimo_complemento VARCHAR(255);
ALTER TABLE fiscal_itbi ADD COLUMN cadimo_apartamento_unidade VARCHAR(100);
ALTER TABLE fiscal_itbi ADD COLUMN cadimo_cep VARCHAR(20);
ALTER TABLE fiscal_itbi ADD COLUMN cadimo_cod_terreno VARCHAR(50);
ALTER TABLE fiscal_itbi ADD COLUMN cadimo_proprietario_nome VARCHAR(255);
ALTER TABLE fiscal_itbi ADD COLUMN cadimo_proprietario_cpf_cnpj VARCHAR(20);
ALTER TABLE fiscal_itbi ADD COLUMN cadimo_responsavel_nome VARCHAR(255);
ALTER TABLE fiscal_itbi ADD COLUMN cadimo_responsavel_cpf_cnpj VARCHAR(20);
ALTER TABLE fiscal_itbi ADD COLUMN cadimo_area_total_construida DECIMAL(19,4);
ALTER TABLE fiscal_itbi ADD COLUMN cadimo_codigo_nacional_matricula VARCHAR(100);
ALTER TABLE fiscal_itbi ADD COLUMN cadimo_nro_imovel_incra VARCHAR(100);
ALTER TABLE fiscal_itbi ADD COLUMN cadimo_nome_propriedade VARCHAR(255);

-- 4. Atualização de Índices (opcional, mas recomendado se mudar o nome da coluna)
DROP INDEX IF EXISTS idx_fiscal_itbi_numero_ano;
CREATE INDEX idx_fiscal_itbi_numero_ano ON fiscal_itbi(itbi_numero, itbi_ano);

DROP INDEX IF EXISTS idx_fiscal_itbi_inscricao;
CREATE INDEX idx_fiscal_itbi_inscricao ON fiscal_itbi(cadimo_inscricao);
