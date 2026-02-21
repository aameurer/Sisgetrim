-- V32__Alter_IBGE_Tables_Nullable.sql

-- O arquivo JSON do IBGE pode enviar nulos em diversos campos de endereço que o DOI enviava preenchido.
-- Para prevenir erros no Flyway/JPA na importação do Sisgetrim, removemos a restrição NOT NULL dessas colunas.

ALTER TABLE IBGE_declaracoes ALTER COLUMN IBGE_destinacao DROP NOT NULL;
ALTER TABLE IBGE_declaracoes ALTER COLUMN IBGE_tipo_logradouro DROP NOT NULL;
ALTER TABLE IBGE_declaracoes ALTER COLUMN IBGE_nome_logradouro DROP NOT NULL;
ALTER TABLE IBGE_declaracoes ALTER COLUMN IBGE_numero_imovel DROP NOT NULL;
ALTER TABLE IBGE_declaracoes ALTER COLUMN IBGE_complemento_endereco DROP NOT NULL;
ALTER TABLE IBGE_declaracoes ALTER COLUMN IBGE_bairro DROP NOT NULL;
ALTER TABLE IBGE_declaracoes ALTER COLUMN IBGE_inscricao_municipal DROP NOT NULL;
ALTER TABLE IBGE_declaracoes ALTER COLUMN IBGE_codigo_ibge DROP NOT NULL;
