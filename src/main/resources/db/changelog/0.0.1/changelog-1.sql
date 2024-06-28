--liquibase formatted sql

--changeset BondarevM:1
--comment first migration
CREATE TABLE Customer(
                         id SERIAL PRIMARY KEY,
                         login VARCHAR(64) UNIQUE NOT NULL,
                         password VARCHAR(64) NOT NULL
);
--rollback truncate table demo;

