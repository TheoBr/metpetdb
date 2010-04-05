CREATE TABLE regions
(
  region_id INT2 NOT NULL,
  name VARCHAR(100) NOT NULL UNIQUE,
  CONSTRAINT regions_sk PRIMARY KEY (region_id)
) WITHOUT OIDS;

CREATE TABLE metamorphic_grades
(
  metamorphic_grade_id INT2 NOT NULL,
  name VARCHAR(100) NOT NULL,
  CONSTRAINT metamorphic_grades_sk PRIMARY KEY (metamorphic_grade_id),
  CONSTRAINT metamorphic_grades_name_key UNIQUE (name)
) WITHOUT OIDS;

CREATE TABLE reference
(
  reference_id INT8 NOT NULL,
  name VARCHAR(100) NOT NULL,
  CONSTRAINT references_sk PRIMARY KEY (reference_id),
  CONSTRAINT references_name_key UNIQUE (name)
) WITHOUT OIDS;

CREATE TABLE rock_type
(
	rock_type_id int2 NOT NULL,
	rock_type VARCHAR(100) NOT NULL,
	CONSTRAINT rock_types_sk PRIMARY KEY (rock_type_id),
    CONSTRAINT rock_types_rock_type_key UNIQUE (rock_type)
) WITHOUT OIDS;

CREATE TABLE subsample_type
(
	subsample_type_id int2 NOT NULL,
	subsample_type VARCHAR(100) NOT NULL,
	CONSTRAINT subsample_types_sk PRIMARY KEY (subsample_type_id),
    CONSTRAINT subsample_types_subsample_type_key UNIQUE (subsample_type)
) WITHOUT OIDS;

CREATE TABLE metamorphic_regions
(
  metamorphic_region_id INT8 NOT NULL,
  name VARCHAR(50) NOT NULL UNIQUE,
  CONSTRAINT metamorphic_regions_sk PRIMARY KEY (metamorphic_region_id),
  CONSTRAINT metamorphic_regions_name_key UNIQUE (name)
)WITHOUT OIDS;

--add a column of gis polygons to the metamorhpic_regions table
SELECT AddGeometryColumn('metamorphic_regions', 'shape', 4326, 'POLYGON', 2);
  

CREATE TABLE samples
(
  sample_id INT8 NOT NULL,
  version INT4 NOT NULL,
  sesar_number CHAR(9),
  public_data CHAR(1) CHECK (public_data IN ('Y','N')) NOT NULL,
  collection_date TIMESTAMP,
  date_precision INT2,
  number VARCHAR(35) NOT NULL,
  rock_type_id int2 NOT NULL,
  user_id INT4 NOT NULL,
  location_error FLOAT4,
  country VARCHAR(100),
  description text,
  collector VARCHAR(50),
  metamorphic_region_id INT8,
  collector_id INT4,
  location_text VARCHAR(50),
  CONSTRAINT samples_sk PRIMARY KEY (sample_id),
  CONSTRAINT samples_fk_user FOREIGN KEY (user_id)
    REFERENCES users (user_id),
  CONSTRAINT samples_fk_collector FOREIGN KEY (collector_id)
    REFERENCES users (user_id),
  CONSTRAINT samples_fk_rock_type FOREIGN KEY (rock_type_id)
    REFERENCES rock_type(rock_type_id)
) WITHOUT OIDS;

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

-- Use WGS 84 (srid = 4326)
SELECT AddGeometryColumn('samples', 'location', 4326, 'POINT', 2);
CREATE INDEX samples_ix_loc ON samples
  USING GIST (location GIST_GEOMETRY_OPS);
ALTER TABLE samples ALTER location SET NOT NULL;
CREATE UNIQUE INDEX samples_nk_number on samples (user_id, lower(number));

CREATE TABLE sample_comments
(
	comment_id INT8 NOT NULL,
	sample_id INT8 NOT NULL,
	user_id INT8 NOT NULL,
	comment_text TEXT NOT NULL,
	date_added TIMESTAMP,
	CONSTRAINT sample_comments_sk PRIMARY KEY (comment_id),
	CONSTRAINT sample_comments_fk_sample FOREIGN KEY (sample_id)
    REFERENCES samples (sample_id),
    CONSTRAINT sample_comments_fk_user FOREIGN KEY (user_id)
    REFERENCES users (user_id)
) WITHOUT OIDS;

CREATE TABLE subsamples
(
  subsample_id INT8 NOT NULL,
  version INT4 NOT NULL,
  public_data CHAR(1) CHECK (public_data IN ('Y','N')) NOT NULL,
  sample_id INT8 NOT NULL,
  user_id INT4 NOT NULL,
  grid_id INT8,
  name VARCHAR(100) NOT NULL,
  subsample_type_id int2 NOT NULL,
  CONSTRAINT subsamples_fk_user FOREIGN KEY (user_id)
    REFERENCES users (user_id),
  CONSTRAINT subsamples_sk PRIMARY KEY (subsample_id),
  CONSTRAINT subsamples_fk_sample FOREIGN KEY (sample_id)
    REFERENCES samples (sample_id),
  CONSTRAINT subsamples_fk_subsample_type FOREIGN KEY (subsample_type_id)
    REFERENCES subsample_type (subsample_type_id)
) WITHOUT OIDS;
CREATE UNIQUE INDEX subsamples_nk_name on subsamples (sample_id, lower(name));

CREATE TABLE projects
(
  project_id INT4 NOT NULL,
  version INT4 NOT NULL,
  user_id INT4 NOT NULL,
  name VARCHAR(50) NOT NULL,
  description VARCHAR(300),
  CONSTRAINT projects_sk PRIMARY KEY (project_id),
  CONSTRAINT projects_nk UNIQUE (user_id, name),
  CONSTRAINT projects_fk_user FOREIGN KEY (user_id)
    REFERENCES users (user_id)
) WITHOUT OIDS;

CREATE TABLE project_members
(
  project_id INT4 NOT NULL,
  user_id INT4 NOT NULL,
  CONSTRAINT project_members_nk PRIMARY KEY (project_id, user_id),
  CONSTRAINT project_members_fk_proj FOREIGN KEY (project_id)
    REFERENCES projects (project_id),
  CONSTRAINT project_members_fk_user FOREIGN KEY (user_id)
    REFERENCES users (user_id)
) WITHOUT OIDS;

CREATE TABLE project_samples
(
  project_id INT4 NOT NULL,
  sample_id INT8 NOT NULL,
  CONSTRAINT project_samples_fk_proj FOREIGN KEY (project_id)
    REFERENCES projects (project_id)
    ON DELETE CASCADE,
  CONSTRAINT project_samples_fk_samp FOREIGN KEY (sample_id)
    REFERENCES samples (sample_id)
) WITHOUT OIDS;

CREATE TABLE project_invites
(
  invite_id INT4 NOT NULL,
  project_id INT4 NOT NULL,
  user_id INT4 NOT NULL,
  action_timestamp TIMESTAMP NOT NULL,
  status VARCHAR(32),
  CONSTRAINT project_invites_nk PRIMARY KEY (invite_id),
  CONSTRAINT project_invites_fk_proj FOREIGN KEY (project_id)
    REFERENCES projects (project_id),
  CONSTRAINT project_invites_fk_user FOREIGN KEY (user_id)
    REFERENCES users (user_id)
) WITHOUT OIDS;

CREATE TABLE sample_minerals
(
  mineral_id INT2 NOT NULL,
  sample_id INT8 NOT NULL,
  amount VARCHAR(30),
  CONSTRAINT sample_minerals_nk PRIMARY KEY (mineral_id, sample_id),
  CONSTRAINT mineral_samples_fk_min FOREIGN KEY (mineral_id)
    REFERENCES minerals (mineral_id)
    ON DELETE CASCADE,
  CONSTRAINT mineral_samples_fk_samp FOREIGN KEY (sample_id)
    REFERENCES samples (sample_id)
) WITHOUT OIDS;

CREATE TABLE sample_regions
(
  sample_id INT8,
  region_id INT2,
  CONSTRAINT sample_region_pk PRIMARY KEY (sample_id, region_id),
  CONSTRAINT sample_regions_fk_sample FOREIGN KEY (sample_id)
    REFERENCES samples (sample_id),
  CONSTRAINT sample_region_fk_region FOREIGN KEY (region_id)
    REFERENCES regions (region_id)
) WITHOUT OIDS;

CREATE TABLE sample_metamorphic_grades
(
  sample_id INT8 NOT NULL,
  metamorphic_grade_id INT2 NOT NULL,
  CONSTRAINT samples_metgrade_pk PRIMARY KEY (sample_id, metamorphic_grade_id),
  CONSTRAINT samples_metgrade_fk_samples FOREIGN KEY (sample_id)
      REFERENCES samples (sample_id),
CONSTRAINT samples_metgrade_fk_metgrade FOREIGN KEY (metamorphic_grade_id)
    REFERENCES metamorphic_grades (metamorphic_grade_id)
) WITHOUT OIDS;

CREATE TABLE sample_metamorphic_regions
( 
  sample_id INT8 NOT NULL,
  metamorphic_region_id INT2 NOT NULL,
  CONSTRAINT samples_metregion_pk PRIMARY KEY (sample_id, metamorphic_region_id),
  CONSTRAINT samples_metregion_fk_samples FOREIGN KEY (sample_id)
     REFERENCES samples (sample_id),
CONSTRAINT samples_metgrade_fk_metgrade FOREIGN KEY (metamorphic_region_id)
    REFERENCES metamorphic_regions (metamorphic_region_id)
) WITHOUT OIDS;

CREATE TABLE sample_reference
(
  sample_id INT8 NOT NULL,
  reference_id INT8 NOT NULL,
  CONSTRAINT sample_reference_pk PRIMARY KEY (sample_id, reference_id),
  CONSTRAINT sample_reference_fk_reference FOREIGN KEY (reference_id)
      REFERENCES reference (reference_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT sample_reference_fk_sample FOREIGN KEY (sample_id)
      REFERENCES samples (sample_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
) WITHOUT OIDS;

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

CREATE TABLE elements
(
   element_id INT2 NOT NULL,
   name       VARCHAR(100) NOT NULL,
   alternate_name VARCHAR(100),
   symbol     VARCHAR(4)  NOT NULL,
   atomic_number INT4 NOT NULL,
   weight        FLOAT4,
   CONSTRAINT elements_sk PRIMARY KEY (element_id),
   CONSTRAINT elements_nk1 UNIQUE (name),
   CONSTRAINT elements_nk2 UNIQUE (symbol)
) WITHOUT OIDS;

CREATE TABLE uploaded_files
(
   uploaded_file_id INT8 NOT NULL,
   hash      CHAR(50) NOT NULL,
   filename  VARCHAR(255) NOT NULL,
   time      TIMESTAMP NOT NULL,
   user_id   INT4,
   CONSTRAINT uploaded_files_pk PRIMARY KEY (uploaded_file_id),
   CONSTRAINT uploaded_files_fk_user FOREIGN KEY(user_id)
      REFERENCES users (user_id)
) WITHOUT OIDS;

CREATE SEQUENCE georeference_seq;
CREATE SEQUENCE invite_seq;
CREATE SEQUENCE project_seq;
CREATE SEQUENCE region_seq;
CREATE SEQUENCE rock_type_seq;
CREATE SEQUENCE sample_seq;
CREATE SEQUENCE subsample_seq;
CREATE SEQUENCE metamorphic_grade_seq;
CREATE SEQUENCE reference_seq;
CREATE SEQUENCE uploaded_files_seq;
CREATE SEQUENCE sample_comments_seq;
CREATE SEQUENCE subsample_type_seq;
CREATE SEQUENCE sample_aliases_seq;
CREATE SEQUENCE metamorphic_regions_seq;
CREATE SEQUENCE sample_metamorphic_regions_seq;