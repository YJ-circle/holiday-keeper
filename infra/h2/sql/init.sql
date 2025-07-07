CREATE USER IF NOT EXISTS test PASSWORD '1234';
GRANT ALL ON SCHEMA PUBLIC TO test;

CREATE TABLE country
(
    country_code CHAR(2) NOT NULL,
    country_name VARCHAR(100) NOT NULL,
	created_time TIMESTAMP,
    updated_time TIMESTAMP,
    active       BOOLEAN,
    CONSTRAINT pk_country PRIMARY KEY (country_code)
);