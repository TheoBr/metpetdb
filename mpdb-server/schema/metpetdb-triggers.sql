CREATE FUNCTION clear_pointx_pointy () RETURNS trigger AS '
  DECLARE
  
  BEGIN
    
    IF (OLD.image_id = NULL) THEN
    	NEW.point_x = NULL;
    	NEW.point_y = NULL;
    END IF;     
    RETURN NEW;
  END;
' LANGUAGE 'plpgsql';