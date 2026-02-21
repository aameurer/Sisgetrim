-- V34__Alter_IBGE_Date_Nullable.sql

-- A data de lavratura/registro pode vir nula em alguns casos de importação IBGE.
-- Tornamos a coluna opcional para evitar erros na persistência de dados incompletos.

ALTER TABLE IBGE_declaracoes ALTER COLUMN IBGE_data_lavratura_registro_averbacao DROP NOT NULL;
