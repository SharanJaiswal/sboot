-- Every .sql file must contain at least one sql script inside it. Empty or all commented will throw an error. This will be auto picked by embedded databases, but not by external DBs.
-- To makes external DBs pick these .sql files, we need to add configuration.
-- This file name "data.sql" is if found, ideally used to run the insertion, update, deletion scripts of SQL.
INSERT INTO Run (id, title, started_on, completed_on, miles, location)
VALUES (12, 'Morning Run', '2017-01-01 06:00:00', '2017-01-01 07:00:00', 5, 'OUTDOOR');