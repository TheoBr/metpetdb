
-- (add lines table)

CREATE TABLE grids
(
   grid_id INT8 NOT NULL,
   version INT4 NOT NULL,
   subsample_id INT8 NOT NULL,
   width INT2 NOT NULL,
   height INT2 NOT NULL,
   CONSTRAINT grids_sk PRIMARY KEY (grid_id),
   CONSTRAINT grids_fk FOREIGN KEY (subsample_id) 
      REFERENCES subsamples(subsample_id) ON DELETE CASCADE
) WITHOUT OIDS;

-- TIFF, GIF, JPEG, BMP, PNG,
CREATE TABLE image_format
(
   image_format_id INT2 NOT NULL,
   name VARCHAR(100) NOT NULL UNIQUE, -- mime type image/x
   CONSTRAINT image_format_sk PRIMARY KEY (image_format_id)
) WITHOUT OIDS;

-- can a subsample have multiple images? yes
-- are all images of subsamples? no

CREATE TABLE images
(
   image_id INT8 NOT NULL,
   checksum CHAR(50) NOT NULL,
   version INT4 NOT NULL,
   sample_id INT8,
   subsample_id INT8,
   image_format_id INT2,
   image_type VARCHAR(100),
   width INT2 NOT NULL,
   height INT2 NOT NULL,
   pixel_size INT2,
   contrast INT2,
   brightness INT2,
   lut INT2,
   collector VARCHAR(50),
   --user_id INT4 NOT NULL,
   checksum_64x64 CHAR(50) NOT NULL,
   checksum_half CHAR(50) NOT NULL,
   filename VARCHAR(256) NOT NULL,
   CONSTRAINT images_sk PRIMARY KEY (image_id),
   CONSTRAINT images_fk_image_format FOREIGN KEY (image_format_id)
     REFERENCES image_format(image_format_id),
   --CONSTRAINT images_fk_user FOREIGN KEY (user_id)
   --  REFERENCES users (user_id),
   CONSTRAINT images_fk_subsample FOREIGN KEY (subsample_id)
     REFERENCES subsamples(subsample_id) ON DELETE CASCADE,
   CONSTRAINT images_fk_sample FOREIGN KEY (sample_id)
     REFERENCES samples(sample_id) ON DELETE CASCADE,
   CONSTRAINT images_ck_nonzero CHECK (
     width > 0 AND height > 0)
) WITHOUT OIDS;


CREATE TABLE image_reference
(
  image_id INT8 NOT NULL,
  reference_id INT8 NOT NULL,
  CONSTRAINT image_reference_pk PRIMARY KEY (image_id, reference_id),
  CONSTRAINT image_reference_fk_reference FOREIGN KEY (reference_id)
      REFERENCES reference (reference_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT image_reference_fk_image FOREIGN KEY (image_id)
      REFERENCES images (image_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
) WITHOUT OIDS;

CREATE TABLE xray_image
(
   image_id INT8 NOT NULL,
   element_id INT2 NOT NULL,
   radiation BOOLEAN,
   lines VARCHAR(100),
   dwelltime INT2 NOT NULL, -- msec
   current INT2 NOT NULL, -- nano amps
   voltage INT2, --kilo watts
   CONSTRAINT xray_image_sk PRIMARY KEY (image_id),
   CONSTRAINT xray_image_fk_image FOREIGN KEY (image_id)
     REFERENCES images(image_id), --does not need ON DELET CASCADE, hibernate takes care of it
   CONSTRAINT xray_image_fk_element FOREIGN KEY (element_id)
     REFERENCES elements(element_id)
) WITHOUT OIDS;


 
CREATE TABLE image_on_grid 
(
   image_on_grid_id INT8 NOT NULL,
   grid_id INT8 NOT NULL,
   image_id INT8 NOT NULL,
   top_left_x INT2 NOT NULL,
   top_left_y INT2 NOT NULL,
   z_order   INT2 NOT NULL,
   opacity  INT2 NOT NULL DEFAULT 100,
   resize_ratio FLOAT4 NOT NULL DEFAULT 100,
   width INT2 NOT NULL,
   height INT2 NOT NULL,
   checksum CHAR(50) NOT NULL,
   checksum_64x64 CHAR(50) NOT NULL,
   checksum_half CHAR(50) NOT NULL,
   CONSTRAINT image_on_grid_sk PRIMARY KEY (image_on_grid_id),
   CONSTRAINT image_on_grid_fk_grids FOREIGN KEY (grid_id)
      REFERENCES grids(grid_id) ON DELETE CASCADE,
   CONSTRAINT image_on_grid_fk_images FOREIGN KEY (image_id)
      REFERENCES images(image_id) ON DELETE CASCADE,
   CONSTRAINT image_on_grid_ck_opacity CHECK (
     opacity >= 0 AND opacity <= 100)
) WITHOUT OIDS;

-- analysis_id will later link to the mineral analysis 

CREATE SEQUENCE image_seq;
CREATE SEQUENCE grid_seq;
CREATE SEQUENCE image_type_seq;
CREATE SEQUENCE image_on_grid_seq;
CREATE SEQUENCE image_format_seq;

-- ask about what image types there will be
-- add image format
