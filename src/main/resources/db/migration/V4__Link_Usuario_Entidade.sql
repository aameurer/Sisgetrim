-- Migração para vincular usuários a entidades
-- Remove o campo antigo de texto e adiciona a coluna de ID com chave estrangeira

-- 1. Cria a nova coluna
ALTER TABLE usuarios ADD COLUMN IF NOT EXISTS entidade_id BIGINT;

-- 2. Tenta migrar dados básicos se o nome da entidade bater com o nome_empresarial
-- (Opcional, mas útil para manter dados em desenvolvimento)
UPDATE usuarios u
SET entidade_id = e.id
FROM entidades e
WHERE u.entidade = e.nome_empresarial OR u.entidade = e.nome_fantasia;

-- 3. Remove a coluna antiga
ALTER TABLE usuarios DROP COLUMN entidade;

-- 4. Adiciona a constraint de FK
ALTER TABLE usuarios 
ADD CONSTRAINT fk_usuario_entidade 
FOREIGN KEY (entidade_id) 
REFERENCES entidades(id)
ON DELETE SET NULL;
