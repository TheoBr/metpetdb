insert into image_format values(nextval('image_format_seq'),'image/tiff');
insert into image_format values(nextval('image_format_seq'),'image/gif');
insert into image_format values(nextval('image_format_seq'),'image/jpeg');
insert into image_format values(nextval('image_format_seq'),'image/bmp');
insert into image_format values(nextval('image_format_seq'),'image/png');
--Rock Types
--insert into rock_type values(nextval('rock_type_seq'), 'Gentoo');
insert into rock_type values(nextval('rock_type_seq'), 'Slate');
insert into rock_type values(nextval('rock_type_seq'), 'Phyllite');
insert into rock_type values(nextval('rock_type_seq'), 'Schist');
insert into rock_type values(nextval('rock_type_seq'), 'Gneiss');
insert into rock_type values(nextval('rock_type_seq'), 'Migmatite');
insert into rock_type values(nextval('rock_type_seq'), 'Anatectite');
insert into rock_type values(nextval('rock_type_seq'), 'Marble');
insert into rock_type values(nextval('rock_type_seq'), 'Calc-silicate');
insert into rock_type values(nextval('rock_type_seq'), 'Greenschist');
insert into rock_type values(nextval('rock_type_seq'), 'Amphibolite');
insert into rock_type values(nextval('rock_type_seq'), 'Blueschist');
insert into rock_type values(nextval('rock_type_seq'), 'Eclogite');
insert into rock_type values(nextval('rock_type_seq'), 'Granofels');
insert into rock_type values(nextval('rock_type_seq'), 'Hornfels');
insert into rock_type values(nextval('rock_type_seq'), 'Skarn');
insert into rock_type values(nextval('rock_type_seq'), 'Quartzite');
insert into rock_type values(nextval('rock_type_seq'), 'Jadeitite');
insert into rock_type values(nextval('rock_type_seq'), 'Glaucophanite');
insert into rock_type values(nextval('rock_type_seq'), 'Serpentinite');
insert into rock_type values(nextval('rock_type_seq'), 'Garnetite');
insert into rock_type values(nextval('rock_type_seq'), 'Pyroxenite');
insert into rock_type values(nextval('rock_type_seq'), 'Mylonite');
insert into rock_type values(nextval('rock_type_seq'), 'Cataclasite');
insert into rock_type values(nextval('rock_type_seq'), 'Metapelite');
insert into rock_type values(nextval('rock_type_seq'), 'Metaigneous');
insert into rock_type values(nextval('rock_type_seq'), 'Metaarkose');
insert into rock_type values(nextval('rock_type_seq'), 'Metagreywacke');
insert into rock_type values(nextval('rock_type_seq'), 'Metabasite');
insert into rock_type values(nextval('rock_type_seq'), 'Metacarbonate');
insert into rock_type values(nextval('rock_type_seq'), 'Metagranite');
insert into rock_type values(nextval('rock_type_seq'), 'Cordierite-Anthophyllite');
--Image Types
--insert into image_type values(nextval('image_type_seq'), 'Xfce');
insert into image_type values(nextval('image_type_seq'), 'Map', 'MAP');
insert into image_type values(nextval('image_type_seq'), 'Cross Section', 'XC');
insert into image_type values(nextval('image_type_seq'), 'Field Photo', 'FP');
insert into image_type values(nextval('image_type_seq'), 'Photograph', 'PHOTO');
insert into image_type values(nextval('image_type_seq'), 'Thin Section Scan', 'TSS');
insert into image_type values(nextval('image_type_seq'), 'Photomicrograph-Transmitted Plane Polarized', 'TPPL');
insert into image_type values(nextval('image_type_seq'), 'Photomicrograph-Transmitted Crossed Polars', 'TXPL');
insert into image_type values(nextval('image_type_seq'), 'Photomicrograph-Reflected Plane Polarized', 'RPPL');
insert into image_type values(nextval('image_type_seq'), 'Photomicrograph-Reflected Crossed Polars', 'RXPL');
insert into image_type values(nextval('image_type_seq'), 'Secondary Electron Image', 'SE');
insert into image_type values(nextval('image_type_seq'), 'Back-Scattered Electron Image', 'BSE');
insert into image_type values(nextval('image_type_seq'), 'Cathodoluminescence Image', 'CL');
insert into image_type values(nextval('image_type_seq'), 'X-ray Map', 'XRM');
--Subsample Types
insert into subsample_type values(nextval('subsample_type_seq'), 'Thin Section');
insert into subsample_type values(nextval('subsample_type_seq'), 'Polished Thin Section');
insert into subsample_type values(nextval('subsample_type_seq'), 'Rock Chip');
--insert into subsample_type values(nextval('subsample_type_seq'), 'Bash');
insert into subsample_type values(nextval('subsample_type_seq'), 'Mineral Separate');
--insert into elements values(1,1,1,1,1,1,'Silicates');