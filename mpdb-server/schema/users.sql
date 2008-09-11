CREATE SEQUENCE user_seq;
CREATE SEQUENCE admin_user_seq;

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
  CONSTRAINT users_sk PRIMARY KEY (user_id),
  CONSTRAINT users_nk_username UNIQUE (email)
) WITHOUT OIDS;

CREATE TABLE admin_users
(
  admin_id INT4 NOT NULL,
  user_id INT4 NOT NULL,
  CONSTRAINT users_sk PRIMARY KEY (admin_id),
  CONSTRAINT admin_users_fk_user FOREIGN KEY (user_id)
    REFERENCES users(user_id)
) WITHOUT OIDS;

INSERT INTO users VALUES (nextval('user_seq'), 1, 'Anthony Waters', 'watera2@cs.rpi.edu','','','','','','','');
INSERT INTO users VALUES (nextval('user_seq'), 1, 'Sibel Adali', 'sibel@cs.rpi.edu','','','','','','','');
INSERT INTO users VALUES (nextval('user_seq'), 1, 'Boleslaw Szymanski', 'szymansk@cs.rpi.edu','','','','','','','');
INSERT INTO users VALUES (nextval('user_seq'), 1, 'Frank Spear', 'spearf@rpi.edu','','','','','','','');
INSERT INTO users VALUES (nextval('user_seq'), 1, 'Benjamin Hallett', 'halleb3@rpi.edu','','','','','','','');