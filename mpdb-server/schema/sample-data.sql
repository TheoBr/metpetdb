insert into image_format values(nextval('image_format_seq'),'image/tiff');
insert into image_format values(nextval('image_format_seq'),'image/gif');
insert into image_format values(nextval('image_format_seq'),'image/jpeg');
insert into image_format values(nextval('image_format_seq'),'image/bmp');
insert into image_format values(nextval('image_format_seq'),'image/png');

insert into rock_type values(nextval('rock_type_seq'), 'gentoo');
insert into rock_type values(nextval('rock_type_seq'), 'Mylonite');
insert into rock_type values(nextval('rock_type_seq'), 'Gneiss');
insert into rock_type values(nextval('rock_type_seq'), 'Amphibolite');
insert into image_type values(nextval('image_type_seq'), 'xfce');
insert into image_type values(nextval('image_type_seq'), 'X-ray');
insert into image_type values(nextval('image_type_seq'), 'Transmitted Plane polarized');
insert into subsample_type values(nextval('subsample_type_seq'), 'linux');

--insert into elements values(1,1,1,1,1,1,'Silicates');