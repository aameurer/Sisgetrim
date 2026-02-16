-- Tornar a coluna nome opcional em doi_participantes
ALTER TABLE doi_participantes ALTER COLUMN nome DROP NOT NULL;
