alter table admin_users owner to metpetdb_dev;
alter table chemical_analyses owner to metpetdb_dev;
alter table chemical_analysis_elements owner to metpetdb_dev;
alter table chemical_analysis_oxides owner to metpetdb_dev;
alter table element_mineral_types owner to metpetdb_dev;
alter table elements owner to metpetdb_dev;
alter table geometry_columns owner to metpetdb_dev;
alter table grids owner to metpetdb_dev;
alter table image_comments owner to metpetdb_dev;
alter table image_format owner to metpetdb_dev;
alter table image_on_grid owner to metpetdb_dev;
alter table image_reference owner to metpetdb_dev;
alter table image_type owner to metpetdb_dev;
alter table images owner to metpetdb_dev;
alter table metamorphic_grades owner to metpetdb_dev;
alter table mineral_types owner to metpetdb_dev;
alter table minerals owner to metpetdb_dev;
alter table oxide_mineral_types owner to metpetdb_dev;
alter table oxides owner to metpetdb_dev;
alter table project_members owner to metpetdb_dev;
alter table project_samples owner to metpetdb_dev;
alter table projects owner to metpetdb_dev;
alter table reference owner to metpetdb_dev;
alter table regions owner to metpetdb_dev;
alter table rock_type owner to metpetdb_dev;
alter table sample_comments owner to metpetdb_dev;
alter table sample_metamorphic_grades owner to metpetdb_dev;
alter table sample_minerals owner to metpetdb_dev;
alter table sample_reference owner to metpetdb_dev;
alter table sample_regions owner to metpetdb_dev;
alter table samples owner to metpetdb_dev;
alter table spatial_ref_sys owner to metpetdb_dev;
alter table subsample_type owner to metpetdb_dev;
alter table subsamples owner to metpetdb_dev;
alter table uploaded_files owner to metpetdb_dev;
alter table users owner to metpetdb_dev;
alter table xray_image owner to metpetdb_dev;
--alter table sample_comments_archive owner to metpetdb_dev;
alter table sample_metamorphic_grades_archive owner to metpetdb_dev;
alter table sample_minerals_archive owner to metpetdb_dev;
alter table sample_reference_archive owner to metpetdb_dev;
alter table sample_regions_archive owner to metpetdb_dev;
alter table samples_archive owner to metpetdb_dev;
alter table chemical_analyses_archive  owner to metpetdb_dev;
alter table chemical_analysis_elements_archive  owner to metpetdb_dev;
alter table chemical_analysis_oxides_archive  owner to metpetdb_dev;
alter table subsamples_archive owner to metpetdb_dev;
alter table roles owner to metpetdb_dev;
alter table role_changes owner to metpetdb_dev;
alter table mineral_relationships owner to metpetdb_dev;
alter table project_invites owner to metpetdb_dev;
alter table sample_aliases owner to metpetdb_dev;
alter table metamorphic_regions owner to metpetdb_dev;
alter table sample_metamorphic_regions owner to metpetdb_dev;

alter table georeference owner to metpetdb_dev;
alter table sample_georeferences owner to metpetdb_dev;

--triggers
alter function archive_sample() owner to metpetdb_dev;
alter function archive_sample_minerals() owner to metpetdb_dev;
alter function archive_sample_regions() owner to metpetdb_dev;
alter function archive_sample_grades() owner to metpetdb_dev;
alter function archive_sample_references() owner to metpetdb_dev;
alter function archive_subsample() owner to metpetdb_dev;
alter function archive_chemical_analysis() owner to metpetdb_dev;
alter function archive_chemical_analysis_elements() owner to metpetdb_dev;
alter function archive_chemical_analysis_oxides() owner to metpetdb_dev;

grant all PRIVILEGES on admin_user_seq to metpetdb_dev; 
grant all PRIVILEGES on chemical_analysis_elements_seq to metpetdb_dev; 
grant all PRIVILEGES on chemical_analysis_oxides_seq to metpetdb_dev;
grant all PRIVILEGES on chemical_analyses_seq to metpetdb_dev;
grant all PRIVILEGES on element_seq to metpetdb_dev; 
grant all PRIVILEGES on grid_seq to metpetdb_dev; 
grant all PRIVILEGES on image_comment_seq to metpetdb_dev; 
grant all PRIVILEGES on image_format_seq to metpetdb_dev; 
grant all PRIVILEGES on image_on_grid_seq to metpetdb_dev; 
grant all PRIVILEGES on image_seq to metpetdb_dev; 
grant all PRIVILEGES on image_type_seq to metpetdb_dev; 
grant all PRIVILEGES on metamorphic_grade_seq to metpetdb_dev; 
grant all PRIVILEGES on mineral_seq to metpetdb_dev; 
grant all PRIVILEGES on mineral_types_seq to metpetdb_dev; 
grant all PRIVILEGES on oxide_seq to metpetdb_dev; 
grant all PRIVILEGES on project_seq to metpetdb_dev; 
grant all PRIVILEGES on mineral_seq to metpetdb_dev; 
grant all PRIVILEGES on reference_seq to metpetdb_dev; 
grant all PRIVILEGES on region_seq to metpetdb_dev; 
grant all PRIVILEGES on rock_type_seq to metpetdb_dev; 
grant all PRIVILEGES on sample_comments_seq to metpetdb_dev; 
grant all PRIVILEGES on sample_seq to metpetdb_dev; 
grant all PRIVILEGES on subsample_seq to metpetdb_dev;
grant all PRIVILEGES on subsample_type_seq to metpetdb_dev; 
grant all PRIVILEGES on uploaded_files_seq to metpetdb_dev; 
grant all PRIVILEGES on user_seq to metpetdb_dev;
grant all PRIVILEGES on role_seq to metpetdb_dev; 
grant all PRIVILEGES on sample_aliases_seq to metpetdb_dev;
grant all PRIVILEGES on georeference_seq to metpetdb_dev;

grant create on database mpdb_test to metpetdb_dev;
grant all privileges on database mpdb_test to metpetdb_dev;
grant all privileges on table geometry_columns to metpetdb_dev;
grant all privileges on metamorphic_regions_seq to metpetdb_dev;
grant all privileges on sample_metamorphic_regions_seq to metpetdb_dev;