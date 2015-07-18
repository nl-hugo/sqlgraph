-- http://www.postgresql.org/docs/8.3/static/sql-createtableas.html
-- CREATE TABLE AS -- define a new table from the results of a query

-- Create a new table films_recent consisting of only recent entries from the table films:
CREATE TABLE films_recent AS
  SELECT * FROM films WHERE date_prod >= '2002-01-01';
  
-- Create a new temporary table films_recent, consisting of only recent entries from the table 
-- films, using a prepared statement. The new table has OIDs and will be dropped at commit:
--PREPARE recentfilms(date) AS
--  SELECT * FROM films WHERE date_prod > $1;
--CREATE TEMP TABLE films_recent WITH (OIDS) ON COMMIT DROP AS
--  EXECUTE recentfilms('2002-01-01');

-- (2) create table with left join
create table table1 as
select *
from table2 t0
left join table3 t01
on t0.colA = t01.colA
;
  