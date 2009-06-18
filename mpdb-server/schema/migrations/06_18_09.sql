-- changes sample mineral amounts from a double to a string
alter table sample_minerals alter column amount type varchar(30);