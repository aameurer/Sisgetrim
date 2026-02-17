-- Migração para suporte a múltiplos vínculos de Entidades e Cartórios por Usuário

-- 1. Criar tabela de junção para Entidades
CREATE TABLE usuario_entidades (
    usuario_id BIGINT NOT NULL,
    entidade_id BIGINT NOT NULL,
    PRIMARY KEY (usuario_id, entidade_id),
    CONSTRAINT fk_usuario_entidade_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios (id) ON DELETE CASCADE,
    CONSTRAINT fk_usuario_entidade_entidade FOREIGN KEY (entidade_id) REFERENCES entidades (id) ON DELETE CASCADE
);

-- 2. Criar tabela de junção para Cartórios
CREATE TABLE usuario_cartorios (
    usuario_id BIGINT NOT NULL,
    cartorio_id BIGINT NOT NULL,
    PRIMARY KEY (usuario_id, cartorio_id),
    CONSTRAINT fk_usuario_cartorio_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios (id) ON DELETE CASCADE,
    CONSTRAINT fk_usuario_cartorio_cartorio FOREIGN KEY (cartorio_id) REFERENCES cartorios (id) ON DELETE CASCADE
);

-- 3. Migrar dados existentes de usuarios.entidade_id para usuario_entidades
INSERT INTO usuario_entidades (usuario_id, entidade_id)
SELECT id, entidade_id FROM usuarios WHERE entidade_id IS NOT NULL;

-- 4. Remover a coluna antiga da tabela usuarios
ALTER TABLE usuarios DROP COLUMN entidade_id;
