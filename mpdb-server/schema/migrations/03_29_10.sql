--Adds the angle column to the image_on_grid table so that we can keep track of the angle an image was rotated
alter table image_on_grid add column angle double precision default 0;