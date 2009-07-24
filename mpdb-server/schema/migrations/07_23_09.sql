--remake the project_invites table
--no easy way to change primary key without rebuilding it
DROP TABLE project_invites;

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

CREATE SEQUENCE invite_seq;

GRANT update ON invite_seq TO @APPUSER@;
GRANT select,insert,update,delete ON project_invites to @APPUSER@;