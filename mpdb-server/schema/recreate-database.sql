-- Creates a new MetPetDB database with PostGIS installed.
--

DROP DATABASE @DATABASE@;
CREATE DATABASE @DATABASE@ TEMPLATE = template0;
\c @DATABASE@
CREATE LANGUAGE plpgsql;

CREATE USER @APPUSER@;
ALTER USER @APPUSER@ WITH PASSWORD '@APPPASS@';

\i @postgis@/lwpostgis.sql
\i @postgis@/spatial_ref_sys.sql
\i all.sql
