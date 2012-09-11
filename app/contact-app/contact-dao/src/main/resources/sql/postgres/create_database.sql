-- drop database contact;
-- drop user contact;
create user contact;

ALTER ROLE contact WITH ENCRYPTED PASSWORD 'contact' VALID UNTIL 'infinity';
   
ALTER Role contact  CREATEDB;

create database contact ENCODING = 'UTF-8' LC_CTYPE = 'en_US.UTF-8' LC_COLLATE = 'en_US.UTF-8' TEMPLATE = template0;
