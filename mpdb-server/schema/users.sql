CREATE SEQUENCE user_seq;
CREATE SEQUENCE admin_user_seq;
CREATE SEQUENCE role_seq;
CREATE SEQUENCE pending_roles_seq;
CREATE SEQUENCE roles_changed_seq;

CREATE TABLE roles
(
  role_id INT2 NOT NULL,
  rank INT2 NOT NULL,
  role_name VARCHAR(50) NOT NULL,
  CONSTRAINT roles_sk PRIMARY KEY (role_id),
  CONSTRAINT roles_nk_rank UNIQUE (rank)
) WITHOUT OIDS;

CREATE TABLE users
(
  user_id INT4 NOT NULL,
  version INT4 NOT NULL,
  name VARCHAR(100) NOT NULL,
  email VARCHAR(255) NOT NULL, --email is login id
  password bytea NOT NULL,
  address VARCHAR(200),
  city VARCHAR(50),
  province VARCHAR(100),
  country VARCHAR(100),
  postal_code VARCHAR(15),
  institution VARCHAR(300),
  reference_email VARCHAR(255),
  confirmation_code CHAR(32),
  enabled CHAR(1) NOT NULL,
  role_id INT2 NOT NULL,
  CONSTRAINT users_sk PRIMARY KEY (user_id),
  CONSTRAINT users_nk_username UNIQUE (email),
  CONSTRAINT users_fk_role FOREIGN KEY (role_id)
    REFERENCES roles(role_id)
) WITHOUT OIDS;

CREATE TABLE admin_users
(
  admin_id INT4 NOT NULL,
  user_id INT4 NOT NULL,
  CONSTRAINT admin_users_sk PRIMARY KEY (admin_id),
  CONSTRAINT admin_users_fk_user FOREIGN KEY (user_id)
    REFERENCES users(user_id)
) WITHOUT OIDS;

--stores pending role requests
CREATE TABLE pending_roles
(
  pending_role_id INT8 NOT NULL,
  user_id INT8 NOT NULL, --receiver of new roll
  sponsor_id INT8 NOT NULL,
  request_date TIMESTAMP NOT NULL,
  role_id INT2 NOT NULL, --the roll they want
  CONSTRAINT pending_roles_sk PRIMARY KEY (pending_role_id),
  CONSTRAINT pending_roles_uk UNIQUE (user_id, role_id),
  CONSTRAINT pending_roles_fk_user FOREIGN KEY (user_id)
    REFERENCES users(user_id),
  CONSTRAINT pending_roles_fk_sponsor FOREIGN KEY (sponsor_id)
    REFERENCES users(user_id),
  CONSTRAINT pending_roles_fk_role FOREIGN KEY (role_id)
    REFERENCES roles(role_id)
) WITHOUT OIDS;

--stores the requests after they have been approved/denied (not implemented)
CREATE TABLE roles_changed
(
  roles_changed_id INT8 NOT NULL,
  user_id INT8 NOT NULL, --receiver of the roll
  sponsor_id INT8 NOT NULL,
  change_date TIMESTAMP NOT NULL, --when the role change was finalized
  role_id INT2 NOT NULL, --the roll they want
  granted CHAR(1) CHECK (granted IN ('Y','N')) NOT NULL, --y/n for granted or not
  reason TEXT, --aprove/deny reason
  CONSTRAINT roles_changed_sk PRIMARY KEY (roles_changed_id),
  CONSTRAINT roles_changed_fk_user FOREIGN KEY (user_id)
    REFERENCES users(user_id),
  CONSTRAINT roles_changed_fk_sponsor FOREIGN KEY (sponsor_id)
    REFERENCES users(user_id),
  CONSTRAINT roles_changed_fk_role FOREIGN KEY (role_id)
    REFERENCES roles(role_id)
) WITHOUT OIDS;

INSERT INTO roles VALUES (nextval('role_seq') ,0, 'Member');
INSERT INTO roles VALUES (nextval('role_seq') ,1, 'Contributor');
INSERT INTO roles VALUES (nextval('role_seq') ,2, 'Fellow');

INSERT INTO users VALUES (nextval('user_seq'), 1, 'Anthony Waters', 'watera2@cs.rpi.edu','','','','','','','','','','Y',(select role_id from roles where rank=1));
INSERT INTO users VALUES (nextval('user_seq'), 1, 'Sibel Adali', 'sibel@cs.rpi.edu','','','','','','','','','','Y',(select role_id from roles where rank=1));
INSERT INTO users VALUES (nextval('user_seq'), 1, 'Boleslaw Szymanski', 'szymansk@cs.rpi.edu','','','','','','','','','','Y',(select role_id from roles where rank=1));
INSERT INTO users VALUES (nextval('user_seq'), 1, 'Frank Spear', 'spearf@rpi.edu','','','','','','','','','','Y',(select role_id from roles where rank=1));
INSERT INTO users VALUES (nextval('user_seq'), 1, 'Benjamin Hallett', 'halleb3@rpi.edu','','','','','','','','','','Y',(select role_id from roles where rank=1));
INSERT INTO users VALUES (nextval('user_seq'), 1, 'Matt Fyffe', 'fyffem@rpi.edu','','','','','','','','','','Y',(select role_id from roles where rank=1));
INSERT INTO users VALUES (nextval('user_seq'), 1, 'Dennis Goldfarb', 'goldfd@rpi.edu','','','','','','','','','','Y',(select role_id from roles where rank=1));
INSERT INTO users VALUES (nextval('user_seq'), 1, 'Zak Linder', 'lindez@rpi.edu','','','','','','','','','','Y',(select role_id from roles where rank=1));