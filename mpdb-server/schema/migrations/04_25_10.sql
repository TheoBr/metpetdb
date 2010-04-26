-- converts the spot_id column in the chemical_analyses table from varchar(50) to int
alter table chemical_analyses add column spot_id_temp int8;
update chemical_analyses set spot_id_temp = cast(cast(spot_id as double precision) as int);
alter table chemical_analyses drop column spot_id;
alter table chemical_analyses rename column spot_id_temp to spot_id;
alter table chemical_analyses alter column spot_id set not null;