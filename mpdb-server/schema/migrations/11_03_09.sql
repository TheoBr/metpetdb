CREATE TABLE georeference
(
  georef_id INT8 NOT NULL,
  title VARCHAR(100) NOT NULL,
  authors VARCHAR(512) NOT NULL,
  journal_name VARCHAR(128) NOT NULL,
  misc_info VARCHAR(128),
  CONSTRAINT georeference_sk PRIMARY KEY (georef_id)
) WITHOUT OIDS;

CREATE SEQUENCE georeference_seq;

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

GRANT update ON georeference_seq TO @APPUSER@;
GRANT select,insert,update,delete ON georeference to @APPUSER@;
GRANT select,insert,update,delete ON sample_georeferences to @APPUSER@;