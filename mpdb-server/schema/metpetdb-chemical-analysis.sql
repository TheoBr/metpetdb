CREATE TABLE chemical_analyses
(
   chemical_analysis_id INT8 NOT NULL,
   version INT4 NOT NULL,
   spot_id VARCHAR(50) NOT NULL,
   subsample_id INT8 NOT NULL,
   point_x INT2,
   point_y INT2,
   image_id INT8,
   analysis_method VARCHAR(50),
   where_done VARCHAR(50),
   analyst VARCHAR(50),
   analysis_date TIMESTAMP,
   date_precision INT2 DEFAULT(0),
   reference_id INT8,
   description VARCHAR(1024),
   mineral_id int2,
   -- user_id INT4 NOT NULL,
   large_rock CHAR(1) CHECK (large_rock IN ('Y','N')) NOT NULL,
   total real,
   CONSTRAINT chemical_analyses_sk PRIMARY KEY (chemical_analysis_id),
   CONSTRAINT chemical_analyses_nk_spot_id UNIQUE(subsample_id, spot_id),
   CONSTRAINT chemical_analyses_fk_subsamples FOREIGN KEY (subsample_id)
      REFERENCES subsamples(subsample_id),
   --CONSTRAINT chemical_analyses_fk_user FOREIGN KEY (user_id)
  --   REFERENCES users (user_id),
  CONSTRAINT chemical_analyses_fk_reference FOREIGN KEY (reference_id)
     REFERENCES reference (reference_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
   CONSTRAINT chemical_analyses_fk_images FOREIGN KEY (image_id)
      REFERENCES images(image_id) ON DELETE SET NULL,
   CONSTRAINT chemical_analyses_fk_mineral FOREIGN KEY (mineral_id)
      REFERENCES minerals(mineral_id)
) WITHOUT OIDS;



CREATE TABLE oxides
(
   oxide_id INT2 NOT NULL,
   element_id INT2 NOT NULL,
   oxidation_state INT2,
   species  VARCHAR(20),
   weight FLOAT4,
   cations_per_oxide INT2,
   conversion_factor  FLOAT8 NOT NULL,
   CONSTRAINT oxides_sk PRIMARY KEY (oxide_id),
   CONSTRAINT oxides_fk FOREIGN KEY (element_id)
        REFERENCES elements(element_id),
   CONSTRAINT oxides_nk UNIQUE (species)
) WITHOUT OIDS ;

CREATE TABLE chemical_analysis_elements
(
    chemical_analysis_id INT8 NOT NULL,
    element_id INT2 NOT NULL,
    amount     FLOAT4 NOT NULL,
    precision  FLOAT4,
    precision_type  VARCHAR(3),
    measurement_unit VARCHAR(3),
    min_amount FLOAT4,
    max_amount FLOAT(4),
    CONSTRAINT analysis_elements_sk PRIMARY KEY (chemical_analysis_id, element_id),
    CONSTRAINT analysis_elements_fk_chemical_analyses FOREIGN KEY (chemical_analysis_id) REFERENCES chemical_analyses(chemical_analysis_id),
    CONSTRAINT analysis_elements_fk_elements FOREIGN KEY (element_id) REFERENCES elements(element_id),
    CONSTRAINT analysis_elements_ck CHECK (precision_type in ('ABS', 'REL'))
) WITHOUT OIDS ;

CREATE TABLE chemical_analysis_oxides
(
    chemical_analysis_id INT8 NOT NULL,
    oxide_id INT2 NOT NULL,
    amount     FLOAT4 NOT NULL,
    precision  FLOAT4,
    precision_type  VARCHAR(3),
    measurement_unit VARCHAR(3),
    min_amount FLOAT4,
    max_amount FLOAT(4),
    CONSTRAINT analysis_oxides_sk PRIMARY KEY (chemical_analysis_id, oxide_id),
    CONSTRAINT analysis_oxides_fk_chemical_analyses FOREIGN KEY (chemical_analysis_id) REFERENCES chemical_analyses(chemical_analysis_id),
    CONSTRAINT analysis_oxides_fk_oxides FOREIGN KEY (oxide_id) REFERENCES oxides(oxide_id),
    CONSTRAINT analysis_oxides_ck CHECK (precision_type in ('ABS', 'REL'))
) WITHOUT OIDS ;

CREATE TABLE mineral_types
(
	mineral_type_id INT2 NOT NULL,
	name VARCHAR(50) NOT NULL,
	CONSTRAINT mineral_types_sk PRIMARY KEY(mineral_type_id)
) WITHOUT OIDS ;

CREATE TABLE oxide_mineral_types
(
	oxide_id INT2 NOT NULL,
	mineral_type_id INT2 NOT NULL,
	CONSTRAINT oxide_mineral_types_sk PRIMARY KEY (oxide_id, mineral_type_id),
	CONSTRAINT oxide_mineral_types_fk_oxide FOREIGN KEY (oxide_id) REFERENCES oxides (oxide_id),
	CONSTRAINT oxide_mineral_types_fk_mineral_type FOREIGN KEY (mineral_type_id) REFERENCES mineral_types (mineral_type_id)
) WITHOUT OIDS ;

CREATE TABLE element_mineral_types
(
	element_id INT2 NOT NULL,
	mineral_type_id INT2 NOT NULL,
	CONSTRAINT element_mineral_types_sk PRIMARY KEY (element_id, mineral_type_id),
	CONSTRAINT element_mineral_types_fk_element FOREIGN KEY (element_id) REFERENCES elements (element_id),
	CONSTRAINT element_mineral_types_fk_mineral_type FOREIGN KEY (mineral_type_id) REFERENCES mineral_types (mineral_type_id)
) WITHOUT OIDS ;

insert into mineral_types values(1,'Silicates');
insert into mineral_types values(2,'Oxides');
insert into mineral_types values(3,'Carbonates');
insert into mineral_types values(4,'Phosphates');
insert into mineral_types values(5,'Other');

CREATE SEQUENCE chemical_analyses_seq;
CREATE SEQUENCE element_seq;
CREATE SEQUENCE oxide_seq;
CREATE SEQUENCE chemical_analysis_oxides_seq;
CREATE SEQUENCE chemical_analysis_elements_seq;
CREATE SEQUENCE mineral_types_seq;
