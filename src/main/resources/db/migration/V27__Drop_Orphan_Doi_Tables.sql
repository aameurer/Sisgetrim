-- Migração para remover tabelas órfãs e legadas do módulo DOI
-- Essas tabelas foram substituídas pela nova estrutura DOI 2025 ou nunca foram utilizadas.

-- 1. Remover tabela órfã (sem uso no código JPA)
DROP TABLE IF EXISTS doi_imovel_municipio;

-- 2. Remover tabelas legadas de participantes (substituídas por alienantes/adquirentes com JSON)
DROP TABLE IF EXISTS doi_participante_representante;
DROP TABLE IF EXISTS doi_participantes;
