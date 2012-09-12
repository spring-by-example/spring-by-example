\c contact contact

CREATE TABLE users (
	username VARCHAR(50) NOT NULL PRIMARY KEY,
	password VARCHAR(50) NOT NULL,
	enabled BOOLEAN NOT NULL
);

CREATE TABLE authorities (
	username VARCHAR(50) NOT NULL,
	authority VARCHAR(50) NOT NULL
);

CREATE UNIQUE INDEX ix_auth_username ON authorities (username, authority);

ALTER TABLE authorities ADD CONSTRAINT fk_authorities_users foreign key (username) REFERENCES users(username);


INSERT INTO users VALUES ('david', 'newyork', true);
INSERT INTO users VALUES ('alex', 'newjersey', true);
INSERT INTO users VALUES ('tim', 'illinois', true);

INSERT INTO authorities VALUES ('david', 'ROLE_USER');
INSERT INTO authorities VALUES ('david', 'ROLE_ADMIN');
INSERT INTO authorities VALUES ('alex', 'ROLE_USER');
INSERT INTO authorities VALUES ('tim', 'ROLE_USER');
