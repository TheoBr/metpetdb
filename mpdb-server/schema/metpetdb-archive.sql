CREATE TABLE samples_archive
(
  sample_id INT8 NOT NULL,
  version INT4 NOT NULL,
  sesar_number CHAR(9),
  public_data CHAR(1) CHECK (public_data IN ('Y','N')) NOT NULL,
  collection_date TIMESTAMP,
  date_precision INT2,
  alias VARCHAR(20) NOT NULL,
  rock_type_id int2 NOT NULL,
  user_id INT4 NOT NULL,
  longitude_error FLOAT4,
  latitude_error FLOAT4,
  country VARCHAR(100),
  description VARCHAR(100),
  collector VARCHAR(50),
  collector_id INT4,
  location_text VARCHAR(50),
  CONSTRAINT samples_archive_sk PRIMARY KEY (sample_id, version),
  CONSTRAINT samples_archive_fk_user FOREIGN KEY (user_id)
    REFERENCES users (user_id),
  CONSTRAINT samples_archive_fk_collector FOREIGN KEY (collector_id)
    REFERENCES users (user_id),
  CONSTRAINT samples_fk_rock_type FOREIGN KEY (rock_type_id)
    REFERENCES rock_type(rock_type_id)
) WITHOUT OIDS;
-- Use WGS 84 (srid = 4326)
SELECT AddGeometryColumn('samples_archive', 'location', 4326, 'POINT', 2);
CREATE INDEX samples_archive_ix_loc ON samples
  USING GIST (location GIST_GEOMETRY_OPS);
ALTER TABLE samples ALTER location SET NOT NULL;

CREATE TABLE sample_minerals_archive
(
  mineral_id INT2 NOT NULL,
  sample_id INT8 NOT NULL,
  sample_version INT4 NOT NULL,
  amount FLOAT4,
  CONSTRAINT mineral_samples_archive_fk_min FOREIGN KEY (mineral_id)
    REFERENCES minerals (mineral_id)
    ON DELETE CASCADE,
  CONSTRAINT mineral_samples_archive_fk_samp FOREIGN KEY (sample_id, sample_version)
    REFERENCES samples_archive (sample_id, version)
) WITHOUT OIDS;

CREATE TABLE sample_regions_archive
(
  sample_id INT8,
  region_id INT2,
  sample_version INT4 NOT NULL,
  CONSTRAINT sample_regions_archive_pk PRIMARY KEY (sample_id, region_id, sample_version),
  CONSTRAINT sample_regions_archive_fk_sample FOREIGN KEY (sample_id, sample_version)
    REFERENCES samples_archive (sample_id, version),
  CONSTRAINT sample_region_archive_fk_region FOREIGN KEY (region_id)
    REFERENCES regions (region_id)
    ON DELETE CASCADE
) WITHOUT OIDS;

CREATE TABLE sample_metamorphic_grades_archive
(
  sample_id INT8 NOT NULL,
  metamorphic_grade_id INT2 NOT NULL,
  sample_version INT4 NOT NULL,
  CONSTRAINT samples_metgrade_archive_pk PRIMARY KEY (sample_id, metamorphic_grade_id, sample_version),
  CONSTRAINT samples_metgrade_archive_fk_samples FOREIGN KEY (sample_id, sample_version)
      REFERENCES samples_archive (sample_id, version)
) WITHOUT OIDS;

CREATE TABLE sample_reference_archive
(
  sample_id INT8 NOT NULL,
  reference_id INT8 NOT NULL,
  sample_version INT4 NOT NULL,
  CONSTRAINT sample_reference_archive_pk PRIMARY KEY (sample_id, reference_id, sample_version),
  CONSTRAINT sample_reference_archive_fk_reference FOREIGN KEY (reference_id)
      REFERENCES reference (reference_id) 
ON DELETE CASCADE,
  CONSTRAINT sample_reference_archive_fk_sample FOREIGN KEY (sample_id, sample_version)
      REFERENCES samples_archive (sample_id, version) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
) WITHOUT OIDS;

CREATE TABLE subsamples_archive
(
  subsample_id INT8 NOT NULL,
  version INT4 NOT NULL,
  sample_id INT8 NOT NULL,
  sample_version INT8 NOT NULL,
  --user_id INT4 NOT NULL,
  grid_id INT8,
  name VARCHAR(100) NOT NULL,
  subsample_type_id int2 NOT NULL,
  CONSTRAINT subsamples_archive_sk PRIMARY KEY (subsample_id, version),
  CONSTRAINT subsamples_archive_fk_sample FOREIGN KEY (sample_id, sample_version)
    REFERENCES samples_archive (sample_id, version),
  CONSTRAINT subsamples_archive_fk_subsample_type FOREIGN KEY (subsample_type_id)
    REFERENCES subsample_type (subsample_type_id)
    ON DELETE CASCADE
) WITHOUT OIDS;


CREATE TABLE chemical_analyses_archive
(
   chemical_analysis_id INT8 NOT NULL,
   version INT4 NOT NULL,
   spot_id VARCHAR(50) NOT NULL,
   subsample_id INT8 NOT NULL,
   subsample_version INT8 NOT NULL,
   point_x INT2,
   point_y INT2,
   image_id INT8,
   analysis_method VARCHAR(50),
   where_done VARCHAR(50),
   analyst VARCHAR(50),
   analysis_date TIMESTAMP,
   date_precision INT2,
   reference_id INT8,
   description VARCHAR(1024),
   mineral_id int2,
   -- user_id INT4 NOT NULL,
   large_rock CHAR(1) CHECK (large_rock IN ('Y','N')) NOT NULL,
   total real,
   CONSTRAINT chemical_analyses_archive_sk PRIMARY KEY (chemical_analysis_id, version),
   CONSTRAINT chemical_analyses_archive_fk_subsamples FOREIGN KEY (subsample_id, subsample_version)
      REFERENCES subsamples_archive(subsample_id, version),
  CONSTRAINT chemical_analyses_archive_fk_reference FOREIGN KEY (reference_id)
     REFERENCES reference(reference_id)
	ON DELETE SET NULL,
   CONSTRAINT chemical_analyses_archive_fk_images FOREIGN KEY (image_id)
      REFERENCES images(image_id) ON DELETE SET NULL,
   CONSTRAINT chemical_analyses_archive_fk_mineral FOREIGN KEY (mineral_id)
      REFERENCES minerals(mineral_id)
      ON DELETE SET NULL
) WITHOUT OIDS;

CREATE TABLE chemical_analysis_elements_archive
(
    chemical_analysis_id INT8 NOT NULL,
    chemical_analysis_version INT4 NOT NULL,
    element_id INT2 NOT NULL,
    amount     FLOAT4 NOT NULL,
    precision  FLOAT4,
    precision_type  VARCHAR(3),
    measurement_unit VARCHAR(4),
    min_amount FLOAT4,
    max_amount FLOAT(4),
    CONSTRAINT analysis_elements_archive_sk PRIMARY KEY (chemical_analysis_id, element_id, chemical_analysis_version),
    CONSTRAINT analysis_elements_fk_chemical_analyses_archive FOREIGN KEY (chemical_analysis_id, chemical_analysis_version) REFERENCES chemical_analyses_archive(chemical_analysis_id, version),
    CONSTRAINT analysis_elements_fk_elements_archive FOREIGN KEY (element_id) REFERENCES elements(element_id),
    CONSTRAINT analysis_elements_ck_archive CHECK (precision_type in ('ABS', 'REL'))
) WITHOUT OIDS ;

CREATE TABLE chemical_analysis_oxides_archive
(
    chemical_analysis_id INT8 NOT NULL,
    chemical_analysis_version INT4 NOT NULL,
    oxide_id INT2 NOT NULL,
    amount     FLOAT4 NOT NULL,
    precision  FLOAT4,
    precision_type  VARCHAR(3),
    measurement_unit VARCHAR(4),
    min_amount FLOAT4,
    max_amount FLOAT(4),
    CONSTRAINT analysis_oxides_archive_sk PRIMARY KEY (chemical_analysis_id, oxide_id, chemical_analysis_version),
    CONSTRAINT analysis_oxides_fk_chemical_analyses_archive FOREIGN KEY (chemical_analysis_id, chemical_analysis_version) REFERENCES chemical_analyses_archive(chemical_analysis_id, version),
    CONSTRAINT analysis_oxides_fk_oxides_archive FOREIGN KEY (oxide_id) REFERENCES oxides(oxide_id),
    CONSTRAINT analysis_oxides_ck_archive CHECK (precision_type in ('ABS', 'REL'))
) WITHOUT OIDS ;
