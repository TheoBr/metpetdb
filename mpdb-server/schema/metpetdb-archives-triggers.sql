CREATE FUNCTION archive_sample() RETURNS trigger AS $archive_sample$
    BEGIN
        INSERT INTO samples_archive SELECT NEW.*;
	RETURN NEW;
    END;
$archive_sample$ LANGUAGE plpgsql;

CREATE TRIGGER tafter AFTER INSERT OR UPDATE ON samples 
    FOR EACH ROW EXECUTE PROCEDURE archive_sample();



CREATE FUNCTION archive_sample_minerals() RETURNS trigger AS $archive_sample_minerals$
    DECLARE
	myrec INTEGER;
    BEGIN
	SELECT INTO myrec count(*) FROM sample_minerals_archive sam inner join samples sample on sam.sample_id = sample.sample_id AND sam.sample_version = sample.version WHERE NEW.sample_id = sample.sample_id;
	IF myrec <> 0
	THEN
	    INSERT INTO sample_minerals_archive SELECT NEW.mineral_id, NEW.sample_id, sample.version, NEW.amount FROM samples sample WHERE sample.sample_id = NEW.sample_id;	
	ELSE
	    INSERT INTO sample_minerals_archive SELECT mineral.mineral_id, NEW.sample_id, sample.version, mineral.amount FROM samples sample inner join sample_minerals mineral on mineral.sample_id = sample.sample_id WHERE sample.sample_id = NEW.sample_id;
	END IF;
    	
	RETURN NEW;
    END;
$archive_sample_minerals$ LANGUAGE plpgsql;

CREATE TRIGGER tafter AFTER INSERT OR UPDATE OR DELETE ON sample_minerals
    FOR EACH ROW EXECUTE PROCEDURE archive_sample_minerals();





CREATE FUNCTION archive_sample_regions() RETURNS trigger AS $archive_sample_regions$
    DECLARE
	myrec INTEGER;
    BEGIN
	SELECT INTO myrec count(*) FROM sample_regions_archive sar inner join samples sample on sar.sample_id = sample.sample_id AND sar.sample_version = sample.version WHERE NEW.sample_id = sample.sample_id;
	IF myrec <> 0
	THEN
	    INSERT INTO sample_regions_archive SELECT NEW.sample_id, NEW.region_id, sample.version FROM samples sample WHERE sample.sample_id = NEW.sample_id;	
	ELSE
	    INSERT INTO sample_regions_archive SELECT region.sample_id, region.region_id, sample.version FROM samples sample inner join sample_regions region on region.sample_id = sample.sample_id WHERE sample.sample_id = NEW.sample_id;
	END IF;
	RETURN NEW;
    END;
$archive_sample_regions$ LANGUAGE plpgsql;

CREATE TRIGGER tafter AFTER INSERT OR UPDATE OR DELETE ON sample_regions
    FOR EACH ROW EXECUTE PROCEDURE archive_sample_regions();






CREATE FUNCTION archive_sample_grades() RETURNS trigger AS $archive_sample_grades$
    DECLARE
	myrec INTEGER;
    BEGIN
	SELECT INTO myrec count(*) FROM sample_metamorphic_grades_archive samg inner join samples sample on samg.sample_id = sample.sample_id AND samg.sample_version = sample.version WHERE NEW.sample_id = sample.sample_id;
	IF myrec <> 0
	THEN
	    INSERT INTO sample_metamorphic_grades_archive SELECT NEW.sample_id, NEW.metamorphic_grade_id, sample.version FROM samples sample WHERE sample.sample_id = NEW.sample_id;	
	ELSE
	    INSERT INTO sample_metamorphic_grades_archive SELECT grade.sample_id, grade.metamorphic_grade_id, sample.version FROM samples sample inner join sample_metamorphic_grades grade on grade.sample_id = sample.sample_id WHERE sample.sample_id = NEW.sample_id;
	END IF;

	RETURN NEW;
    END;
$archive_sample_grades$ LANGUAGE plpgsql;

CREATE TRIGGER tafter AFTER INSERT OR UPDATE OR DELETE ON sample_metamorphic_grades
    FOR EACH ROW EXECUTE PROCEDURE archive_sample_grades();






CREATE FUNCTION archive_sample_references() RETURNS trigger AS $archive_sample_references$
    DECLARE
	myrec INTEGER;
    BEGIN
	SELECT INTO myrec count(*) FROM sample_reference_archive sar inner join samples sample on sar.sample_id = sample.sample_id AND sar.sample_version = sample.version WHERE NEW.sample_id = sample.sample_id;
	IF myrec <> 0
	THEN
	    INSERT INTO sample_reference_archive SELECT NEW.sample_id, NEW.reference_id, sample.version FROM samples sample WHERE sample.sample_id = NEW.sample_id;	
	ELSE
	    INSERT INTO sample_reference_archive SELECT reference.sample_id, reference.reference_id, sample.version FROM samples sample inner join sample_references reference on reference.sample_id = sample.sample_id WHERE sample.sample_id = NEW.sample_id;
	END IF;

	RETURN NEW;
    END;
$archive_sample_references$ LANGUAGE plpgsql;

CREATE TRIGGER tafter AFTER INSERT OR UPDATE OR DELETE ON sample_reference
    FOR EACH ROW EXECUTE PROCEDURE archive_sample_references();




CREATE FUNCTION archive_subsample() RETURNS trigger AS $archive_subsample$
    DECLARE
	myrec INTEGER;
    BEGIN
	SELECT INTO myrec count(*) FROM subsamples_archive ssa inner join samples sample on ssa.sample_id = sample.sample_id AND ssa.sample_version = sample.version WHERE NEW.sample_id = sample.sample_id;
	IF myrec <> 0
	THEN
	    INSERT INTO subsamples_archive SELECT NEW.subsample_id, NEW.version, NEW.sample_id, sample.version, NEW.grid_id, NEW.name, NEW.subsample_type_id FROM samples sample WHERE sample.sample_id = NEW.sample_id;
	ELSE
	    INSERT INTO subsamples_archive SELECT ss.subsample_id, ss.version, NEW.sample_id, sample.version, ss.grid_id, ss.name, ss.subsample_type_id FROM samples sample inner join subsamples ss on ss.sample_id = sample.sample_id WHERE sample.sample_id = NEW.sample_id;
	END IF;

	RETURN NEW;
    END;
$archive_subsample$ LANGUAGE plpgsql;

CREATE TRIGGER tafter AFTER INSERT OR UPDATE ON subsamples 
    FOR EACH ROW EXECUTE PROCEDURE archive_subsample();



CREATE FUNCTION archive_chemical_analysis() RETURNS trigger AS $archive_chemical_analysis$
    DECLARE
	myrec INTEGER;
    BEGIN
	SELECT INTO myrec count(*) FROM chemical_analyses_archive caa inner join subsamples subsample on caa.subsample_id = subsample.subsample_id AND caa.subsample_version = subsample.version WHERE NEW.subsample_id = subsample.subsample_id;
	IF myrec <> 0
	THEN
	    INSERT INTO chemical_analyses_archive SELECT NEW.chemical_analysis_id, NEW.version, NEW.spot_id, NEW.subsample_id, subsample.version, NEW.point_x, NEW.point_y, NEW.image_id, NEW.analysis_method, NEW.where_done, NEW.analyst, NEW.analysis_date, NEW.date_precision, NEW.reference_id, NEW.description, NEW.mineral_id, NEW.large_rock, NEW.total FROM subsamples subsample WHERE subsample.subsample_id = NEW.subsample_id;
	ELSE
	    INSERT INTO chemical_analyses_archive SELECT ca.chemical_analysis_id, ca.version, ca.spot_id, NEW.subsample_id, subsample.version, ca.point_x, ca.point_y, ca.image_id, ca.analysis_method, ca.where_done, ca.analyst, ca.analysis_date, ca.date_precision, ca.reference_id, ca.description, ca.mineral_id, ca.large_rock, ca.total FROM chemical_analyses ca inner join subsamples subsample on ca.subsample_id = subsample.subsample_id WHERE subsample.subsample_id = NEW.subsample_id;
	END IF;

	RETURN NEW;
    END;
$archive_chemical_analysis$ LANGUAGE plpgsql;

CREATE TRIGGER tafter AFTER INSERT OR UPDATE ON chemical_analyses
    FOR EACH ROW EXECUTE PROCEDURE archive_chemical_analysis();