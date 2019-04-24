CREATE TABLE IF NOT EXISTS regions (
  id int NOT NULL AUTO_INCREMENT,
  name varchar(128) NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS users (
  id int NOT NULL AUTO_INCREMENT,
  username varchar(128) NOT NULL,
  password varchar(128) NOT NULL,
  email varchar(128) NOT NULL,
  role varchar(128) DEFAULT 'ROLE_USER',
  PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS ads (
  id int NOT NULL AUTO_INCREMENT,
  title varchar(128) NOT NULL,
  description text NOT NULL,
  region_id int NOT NULL,
  user_id int NOT NULL,
  created_at TIMESTAMP NOT NULL,
  updated_at TIMESTAMP NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (region_id) REFERENCES regions(id) ON DELETE  NO ACTION,
  FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE  NO ACTION
);
