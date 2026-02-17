-- Migração para adicionar controle de status de usuários
-- Permite que Administradores aprovem novos cadastros

ALTER TABLE usuarios ADD COLUMN IF NOT EXISTS status VARCHAR(20) DEFAULT 'PENDENTE' NOT NULL;

-- Atualiza usuários existentes para VERIFICADO (para não bloquear acesso legado)
UPDATE usuarios SET status = 'VERIFICADO' WHERE status = 'PENDENTE';
