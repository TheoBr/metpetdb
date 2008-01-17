GRANT select,insert               ON minerals        TO @APPUSER@;
GRANT select,insert,update        ON projects        TO @APPUSER@;
GRANT select,insert       ,delete ON project_members TO @APPUSER@;
GRANT select,insert       ,delete ON project_samples TO @APPUSER@;
GRANT select,insert               ON regions         TO @APPUSER@;
GRANT select,insert,update,delete ON samples         TO @APPUSER@;
GRANT select,insert,update,delete ON subsamples      TO @APPUSER@;
GRANT select,insert,update        ON users           TO @APPUSER@;
GRANT select,insert       ,delete ON sample_minerals TO @APPUSER@;
GRANT select,insert,update,delete ON grids           TO @APPUSER@;
GRANT select,insert,update,delete ON images          TO @APPUSER@;
GRANT select,insert,update,delete ON image_on_grid   TO @APPUSER@;
GRANT select,insert,update,delete ON mineral_analyses TO @APPUSER@;
GRANT select,insert,update,delete ON xray_image TO @APPUSER@;
GRANT select,insert,update,delete ON sample_regions TO @APPUSER@;
GRANT select,insert,update,delete ON sample_metamorphic_grades TO @APPUSER@;
GRANT select,insert,update        ON metamorphic_grades TO @APPUSER@;
GRANT select,insert,update        ON elements TO @APPUSER@;
GRANT select,insert,update        ON reference TO @APPUSER@;
GRANT select,insert,update,delete ON sample_reference TO @APPUSER@;

GRANT update ON mineral_seq   TO @APPUSER@;
GRANT update ON project_seq   TO @APPUSER@;
GRANT update ON region_seq    TO @APPUSER@;
GRANT update ON rock_type_seq TO @APPUSER@;
GRANT update ON sample_seq    TO @APPUSER@;
GRANT update ON subsample_seq TO @APPUSER@;
GRANT update ON user_seq      TO @APPUSER@;
GRANT update ON image_seq     TO @APPUSER@;
GRANT update ON image_on_grid_seq TO @APPUSER@;
GRANT update ON grid_seq TO @APPUSER@;
GRANT update ON mineral_analyses_seq TO @APPUSER@;
GRANT update ON metamorphic_grade_seq TO @APPUSER@;
GRANT update ON reference_seq TO @APPUSER@;
