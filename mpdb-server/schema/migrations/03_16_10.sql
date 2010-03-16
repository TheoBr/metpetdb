--Adds the locked column to the image_on_grid table so that locked images on the image map can stay locked
alter table image_on_grid add column locked CHAR(1) CHECK (locked IN ('Y','N'));
update image_on_grid set locked = 'N';
alter table image_on_grid alter column locked set NOT NULL;
