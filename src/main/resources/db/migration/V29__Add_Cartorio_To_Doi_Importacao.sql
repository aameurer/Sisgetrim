ALTER TABLE doi_importacoes ADD COLUMN cartorio_id BIGINT;

ALTER TABLE doi_importacoes 
ADD CONSTRAINT fk_doi_importacoes_cartorios 
FOREIGN KEY (cartorio_id) REFERENCES cartorios (id);

CREATE INDEX idx_doi_importacoes_cartorio ON doi_importacoes (cartorio_id);
