-- Adds the sample_aliases table to the database
CREATE TABLE sample_aliases
(
  sample_alias_id INT8 NOT NULL,
  sample_id INT8,
  alias VARCHAR(35) NOT NULL,
  CONSTRAINT sample_aliases_sk PRIMARY KEY (sample_alias_id),
  CONSTRAINT sample_aliases_nk UNIQUE (sample_id, alias),
  CONSTRAINT sample_id_fk_sample FOREIGN KEY (sample_id)
  REFERENCES samples (sample_id)
) WITHOUT OIDS;

CREATE SEQUENCE sample_aliases_seq;
