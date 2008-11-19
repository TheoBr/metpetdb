DROP SEQUENCE mineral_seq;
DROP SEQUENCE project_seq;
DROP SEQUENCE region_seq;
DROP SEQUENCE rock_type_seq;
DROP SEQUENCE sample_seq;
DROP SEQUENCE subsample_seq;
--DROP SEQUENCE user_seq;
DROP SEQUENCE metamorphic_grade_seq;
DROP SEQUENCE reference_seq;
DROP SEQUENCE image_seq;
DROP SEQUENCE grid_seq;
DROP SEQUENCE image_type_seq;
DROP SEQUENCE image_on_grid_seq;
DROP SEQUENCE image_format_seq;
DROP SEQUENCE chemical_analyses_seq;
DROP SEQUENCE element_seq;
DROP SEQUENCE oxide_seq;
DROP SEQUENCE chemical_analysis_oxides_seq;
DROP SEQUENCE chemical_analysis_elements_seq;
DROP SEQUENCE uploaded_files_seq;
DROP SEQUENCE sample_comments_seq;
DROP SEQUENCE mineral_types_seq;
DROP SEQUENCE subsample_type_seq;
DROP SEQUENCE admin_user_seq;



DROP TABLE chemical_analysis_oxides_archive CASCADE;
DROP TABLE chemical_analysis_elements_archive CASCADE;
DROP TABLE chemical_analyses_archive CASCADE;
DROP TABLE subsamples_archive CASCADE;
DROP TABLE sample_reference_archive CASCADE;
DROP TABLE sample_metamorphic_grades_archive CASCADE;
DROP TABLE sample_regions_archive CASCADE;
DROP TABLE sample_minerals_archive CASCADE;
DROP TABLE samples_archive CASCADE;

DROP TABLE chemical_analysis_elements CASCADE;
DROP TABLE chemical_analysis_oxides CASCADE;
DROP TABLE elements CASCADE;
DROP TABLE grids CASCADE;
DROP TABLE image_format CASCADE;
DROP TABLE image_on_grid CASCADE;
DROP TABLE images CASCADE;
DROP TABLE metamorphic_grades CASCADE;
DROP TABLE chemical_analyses CASCADE;
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
DROP TABLE sample_comments CASCADE;
DROP TABLE mineral_types CASCADE;
DROP TABLE oxide_mineral_types CASCADE;
DROP TABLE element_mineral_types CASCADE;
DROP TABLE admin_users CASCADE;
DROP TABLE image_comments CASCADE;
DROP TABLE image_reference CASCADE;
DROP TABLE image_type CASCADE;
DROP TABLE rock_type CASCADE;
DROP TABLE subsample_type CASCADE;
