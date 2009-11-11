ALTER TABLE georeference DROP COLUMN misc_info;
ALTER TABLE georeference DROP COLUMN authors;

ALTER TABLE georeference ADD COLUMN first_author VARCHAR(32) NOT NULL;
ALTER TABLE georeference ADD COLUMN second_authors VARCHAR(480);
ALTER TABLE georeference ADD COLUMN full_text VARCHAR(1024) NOT NULL;