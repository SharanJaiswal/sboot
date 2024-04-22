-- Every .sql file must contain at least one sql script inside it. Empty or all commented will throw an error. This will be auto picked by embedded databases, but not by external DBs.
-- To makes external DBs pick these .sql files, we need to add configuration.
-- When there is an embedded database, spring gonna look this file named "schema.sql" in resources, and run the query in it inside the embedded db server.

CREATE TABLE IF NOT EXISTS Run (
    id INT NOT NULL,
    title varchar(250) NOT NULL,
    started_on timestamp NOT NULL,
    completed_on timestamp NOT NULL,
    miles INT NOT NULL,
    location varchar(10) NOT NULL,
    version INT,
    primary key (ID)
);