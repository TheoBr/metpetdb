-- Grants privileges on sample_aliases table
GRANT update ON sample_aliases_seq TO @APPUSER@;
GRANT select,insert,update,delete ON sample_aliases to @APPUSER@;