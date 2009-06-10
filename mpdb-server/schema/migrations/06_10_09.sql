--Adds the comment values to the image types that have them
--Adds two new image types
insert into image_type values(nextval('image_type_seq'), 'Figure', 'FIG','Miscellaneous figure, may be composite');
insert into image_type values(nextval('image_type_seq'), 'Field Notes', 'FIELDNOTES','Scanned/digital field notes');
update image_type set comments = 'Map (geologic or otherwise) with sample locations' where abbreviation = 'MAP';
update image_type set comments = 'Geologic cross section with sample locations' where abbreviation = 'XC';
update image_type set comments = 'Field photo of outcrop or sample location', abbreviation = 'FIELDPHOTO' where abbreviation = 'FP';
update image_type set comments = 'General sample imagery' where abbreviation = 'PHOTO';
update image_type set comments = 'Special image of entire thin section' where abbreviation = 'TSS';
update image_type set comments = 'Element name (or combo) is required for X-ray maps' where abbreviation = 'XRM';
update image_type set comments = 'Hand sketch or drawing', abbreviation = 'DRAWING' where abbreviation = 'Drawing';