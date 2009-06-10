--adds the project_invites table to the database
CREATE TABLE project_invites
(
  project_id INT4 NOT NULL,
  user_id INT4 NOT NULL,
  CONSTRAINT project_invites_nk PRIMARY KEY (project_id, user_id),
  CONSTRAINT project_invites_fk_proj FOREIGN KEY (project_id)
    REFERENCES projects (project_id),
  CONSTRAINT project_invites_fk_user FOREIGN KEY (user_id)
    REFERENCES users (user_id)
) WITHOUT OIDS;