CREATE TABLE users
(
  user_id INT4 NOT NULL,
  version INT4 NOT NULL,
  first_name VARCHAR(50) NOT NULL,
  last_name VARCHAR(50) NOT NULL,
  email VARCHAR(255) NOT NULL, --email is login id
  password bytea NOT NULL,
  address VARCHAR(200),
  city VARCHAR(50),
  province VARCHAR(100),
  country VARCHAR(100),
  postal_code VARCHAR(15),
  institution VARCHAR(300),
  reference_email VARCHAR(255),
  CONSTRAINT users_sk PRIMARY KEY (user_id),
  CONSTRAINT users_nk_username UNIQUE (email)
) WITHOUT OIDS;

INSERT INTO users VALUES (nextval('user_seq'), 1, 'Anthony', 'Waters', 'watera2@cs.rpi.edu','','','','','','','');
INSERT INTO users VALUES (nextval('user_seq'), 1, 'Sibel', 'Adali', 'sibel@cs.rpi.edu','','','','','','','');
INSERT INTO users VALUES (nextval('user_seq'), 1, 'Boleslaw', 'Szymanski', 'szymansk@cs.rpi.edu','','','','','','','');
INSERT INTO users VALUES (nextval('user_seq'), 1, 'Frank', 'Spear', 'spearf@rpi.edu','','','','','','','');
INSERT INTO users VALUES (nextval('user_seq'), 1, 'Benjamin', 'Hallett', 'halleb3@rpi.edu','','','','','','','');