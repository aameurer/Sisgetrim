-- V26: Criação das tabelas de domínio conforme o manual DOI Abril/2025
-- Autor: Antigravity
-- Data: 2026-02-17

-- 1. Tabela de Tipo da Declaração
CREATE TABLE dom_tipo_declaracao (codigo INT PRIMARY KEY, descricao VARCHAR(50));
INSERT INTO dom_tipo_declaracao (codigo, descricao) VALUES (0,'Original'), (1,'Retificadora'), (3,'Canceladora');

-- 2. Tabela de Tipo do Serviço
CREATE TABLE dom_tipo_servico (codigo INT PRIMARY KEY, descricao VARCHAR(100));
INSERT INTO dom_tipo_servico (codigo, descricao) VALUES (1,'Notarial'), (2,'Registro de Imóveis'), (3,'Registro de Títulos e Documentos');

-- 3. Tabela de Tipo do Ato
CREATE TABLE dom_tipo_ato (codigo INT PRIMARY KEY, descricao VARCHAR(100));
INSERT INTO dom_tipo_ato (codigo, descricao) VALUES (1,'Escritura'), (2,'Procuração'), (3,'Averbação'), (4,'Registro'), (5,'Registros para fins de publicidade'), (6,'Registro para fins de conservação');

-- 4. Tabela de Tipo do Livro
CREATE TABLE dom_tipo_livro (codigo INT PRIMARY KEY, descricao VARCHAR(100));
INSERT INTO dom_tipo_livro (codigo, descricao) VALUES (1,'Lv.2-Registro Geral(matrícula)'), (2,'Transcrição das Transmissões');

-- 5. Tabela de Natureza do Título
CREATE TABLE dom_natureza_titulo (codigo INT PRIMARY KEY, descricao VARCHAR(150));
INSERT INTO dom_natureza_titulo (codigo, descricao) VALUES (1,'Instrumento particular com força de escritura pública'), (2,'Escritura Pública'), (3,'Título Judicial'), (4,'Contratos ou termos administrativos'), (5,'Atos autênticos de países estrangeiros');

-- 6. Tabela de Tipo da Operação Imobiliária
CREATE TABLE dom_tipo_operacao (codigo INT PRIMARY KEY, descricao VARCHAR(150));
INSERT INTO dom_tipo_operacao (codigo, descricao) VALUES 
(11,'Compra e Venda'), (13,'Permuta'), (15,'Adjudicação'), (19,'Dação em Pagamento'), (21,'Distrato de Negócio'), 
(31,'Procuração em Causa Própria'), (33,'Promessa de Compra e Venda'), (35,'Promessa de Cessão de Direitos'), 
(37,'Cessão de Direitos'), (39,'Outras operações imobiliárias'), (41,'Alienação por iniciativa particular ou leilão judicial'), 
(45,'Incorporação e loteamento'), (47,'Integralização/Subscrição de capital'), (55,'Doação em adiantamento da legítima'), 
(56,'Aforamento'), (57,'Casamento em comunhão universal de bens'), (58,'Cisão total ou parcial'), 
(59,'Compra e venda de imóvel gravado por enfiteuse'), (60,'Concessão de Direito Real de Uso (CDRU)'), 
(61,'Concessão de Uso Especial para Fins de Moradia (CUEM)'), (62,'Consolidação da Propriedade em Nome do Fiduciário'), 
(63,'Desapropriação para fins de Reforma Agrária'), (64,'Desapropriação, exceto para Reforma Agrária'), 
(65,'Direito de laje'), (66,'Direito de superfície'), (67,'Doação, exceto em Adiantamento de Legítima'), 
(68,'Incorporação'), (69,'Inventário'), (70,'Part. Separação/Divórcio/União Estável'), 
(71,'Retorno de Capital Próprio na Extinção de Pessoa Jurídica'), (72,'Retorno de Capital Próprio, exceto na Extinção de Pessoa Jurídica'), 
(73,'Título de Domínio - TD'), (74,'Usucapião');

-- 7. Tabela de Forma de Pagamento
CREATE TABLE dom_forma_pagamento (codigo INT PRIMARY KEY, descricao VARCHAR(100));
INSERT INTO dom_forma_pagamento (codigo, descricao) VALUES (5,'Quitado à vista'), (10,'Quitado a prazo'), (11,'Quitado sem informação da forma de pagamento'), (7,'A prazo'), (9,'Não se aplica');

-- 8. Tabela de Medida da Parte Transacionada
CREATE TABLE dom_medida_parte (codigo INT PRIMARY KEY, descricao VARCHAR(20));
INSERT INTO dom_medida_parte (codigo, descricao) VALUES (1,'%'), (2,'ha/m²');

-- 9. Tabela de Destinação
CREATE TABLE dom_destinacao (codigo INT PRIMARY KEY, descricao VARCHAR(50));
INSERT INTO dom_destinacao (codigo, descricao) VALUES (1,'Urbano'), (3,'Rural');

-- 10. Tabela de Motivo da Não Identificação do NI
CREATE TABLE dom_motivo_nao_ni (codigo INT PRIMARY KEY, descricao VARCHAR(100));
INSERT INTO dom_motivo_nao_ni (codigo, descricao) VALUES (1,'Sem CPF/CNPJ - Decisão Judicial'), (2,'Não consta no documento');

-- 11. Tabela de Regime de Bens
CREATE TABLE dom_regime_bens (codigo INT PRIMARY KEY, descricao VARCHAR(100));
INSERT INTO dom_regime_bens (codigo, descricao) VALUES (1,'Separação de Bens'), (2,'Comunhão Parcial de Bens'), (3,'Comunhão Universal de Bens'), (4,'Participação Final nos Aquestos');

-- 12. Tabela de Tipo do Imóvel
CREATE TABLE dom_tipo_imovel (codigo INT PRIMARY KEY, descricao VARCHAR(100));
INSERT INTO dom_tipo_imovel (codigo, descricao) VALUES 
(15,'Loja'), (31,'Galpão'), (65,'Apartamento'), (67,'Casa'), (69,'Fazenda/Sítio/Chácara'), 
(71,'Terreno/Fração'), (89,'Outros'), (90,'Sala'), (91,'Conjunto de salas'), (92,'Sobreloja'), 
(93,'Vaga de Garagem'), (94,'Laje'), (95,'Estacionamento'), (96,'Barraco');
