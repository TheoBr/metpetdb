DROP SEQUENCE mineral_seq;
DROP SEQUENCE project_seq;
DROP SEQUENCE region_seq;
DROP SEQUENCE rock_type_seq;
DROP SEQUENCE sample_seq;
DROP SEQUENCE subsample_seq;
DROP SEQUENCE user_seq;
DROP SEQUENCE metamorphic_grade_seq;
DROP SEQUENCE reference_seq;
DROP SEQUENCE image_seq;
DROP SEQUENCE grid_seq;
DROP SEQUENCE image_type_seq;
DROP SEQUENCE image_on_grid_seq;
DROP SEQUENCE image_format_seq;
DROP SEQUENCE mineral_analyses_seq;
DROP SEQUENCE element_seq;
DROP SEQUENCE oxide_seq;
DROP SEQUENCE analysis_oxides_seq;
DROP SEQUENCE analysis_elements_seq;


DROP TABLE analysis_elements CASCADE;
DROP TABLE analysis_oxides CASCADE;
DROP TABLE elements CASCADE;
DROP TABLE grids CASCADE;
DROP TABLE image_format CASCADE;
DROP TABLE image_on_grid CASCADE;
DROP TABLE images CASCADE;
DROP TABLE metamorphic_grades CASCADE;
DROP TABLE mineral_analyses CASCADE;
DROP TABLE oxides CASCADE;
DROP TABLE project_samples CASCADE; 
DROP TABLE project_members CASCADE;
DROP TABLE projects CASCADE;
DROP TABLE reference CASCADE;
DROP TABLE sample_metamorphic_grades CASCADE;
DROP TABLE sample_minerals CASCADE;
DROP TABLE sample_reference CASCADE;
DROP TABLE sample_regions CASCADE; 
DROP TABLE subsamples CASCADE;
DROP TABLE samples CASCADE;
--DROP TABLE users CASCADE;
DROP TABLE regions CASCADE;
DROP TABLE minerals CASCADE;
DROP TABLE xray_image CASCADE;
DROP TABLE uploaded_files CASCADE;
