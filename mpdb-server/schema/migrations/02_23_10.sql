--remaking georeference table with TEXT data types
DROP TABLE sample_georeferences;
DROP TABLE georeference;

CREATE TABLE georeference
(
  georef_id INT8 NOT NULL,
  title TEXT NOT NULL,
  first_author TEXT NOT NULL,
  second_authors TEXT,
  journal_name TEXT NOT NULL,
  full_text TEXT NOT NULL,
  reference_number TEXT,
  CONSTRAINT georeference_sk PRIMARY KEY (georef_id),
  CONSTRAINT ref_num_unq UNIQUE (reference_number)
) WITHOUT OIDS;

CREATE TABLE sample_georeferences
(
  sample_id INT8 NOT NULL,
  georef_id INT8 NOT NULL,
  CONSTRAINT sample_georeferences_pk PRIMARY KEY (sample_id, georef_id),
  CONSTRAINT sample_georeferences_fk_georeferenc FOREIGN KEY (georef_id)
      REFERENCES georeference (georef_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT sample_georeferences_fk_sample FOREIGN KEY (sample_id)
      REFERENCES samples (sample_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
) WITHOUT OIDS;

GRANT select,insert,update,delete ON georeference to @APPUSER@;
GRANT select,insert,update,delete ON sample_georeferences to @APPUSER@;
GRANT update ON georeference_seq TO @APPUSER@;