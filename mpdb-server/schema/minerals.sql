CREATE SEQUENCE mineral_seq;

CREATE TABLE minerals
(
  mineral_id INT2 NOT NULL,
  real_mineral_id INT2 NOT NULL, --for alternative minerals this is the default id, else it is mineral_id
  name VARCHAR(100) NOT NULL,
  CONSTRAINT minerals_sk PRIMARY KEY (mineral_id),
  CONSTRAINT minerals_fk_real_mineral_id FOREIGN KEY (real_mineral_id)
    REFERENCES minerals (mineral_id),
  CONSTRAINT minerals_nk UNIQUE (name)
) WITHOUT OIDS;

CREATE TABLE mineral_relationships
(
	parent_mineral_id INT2 NOT NULL,
	child_mineral_id INT2 NOT NULL,
	CONSTRAINT mineral_relationships_sk PRIMARY KEY (parent_mineral_id, child_mineral_id),
	CONSTRAINT mineral_relationships_parent_fk FOREIGN KEY (parent_mineral_id)
    	REFERENCES minerals (mineral_id),
    CONSTRAINT mineral_relationships_child_fk FOREIGN KEY (child_mineral_id)
    	REFERENCES minerals (mineral_id)
) WITHOUT OIDS;