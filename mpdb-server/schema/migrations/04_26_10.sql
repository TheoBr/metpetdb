SELECT AddGeometryColumn('metamorphic_regions', 'label_location', 4326, 'POINT', 2);
update metamorphic_regions set label_location = ST_Centroid(shape);

update metamorphic_regions set label_location = GeometryFromText('POINT (-112.713283 36.320681)',4326) where name = 'Grand Canyon';
update metamorphic_regions set label_location = GeometryFromText('POINT (-121.696711 37.517958)',4326) where name = 'California - Oregon Coast Ranges';
update metamorphic_regions set label_location = GeometryFromText('POINT (122.241383 -33.224819)',4326) where name = 'Albany-Fraser Orogen';
update metamorphic_regions set label_location = GeometryFromText('POINT (-127.183294 57.715981)',4326) where name = 'Canadian Cordillera';
update metamorphic_regions set label_location = GeometryFromText('POINT (-78.591336 49.298814)',4326) where name = 'Superior Province';
update metamorphic_regions set label_location = GeometryFromText('POINT (-85.978317 67.56985)',4326) where name = 'Ray / Hearne Province';
update metamorphic_regions set label_location = GeometryFromText('POINT (-53.296478 69.836825)',4326) where name = 'West Greenland';




	 