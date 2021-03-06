package edu.rpi.metpetdb.client.model.validation;

import edu.rpi.metpetdb.client.model.properties.SampleProperty;
import edu.rpi.metpetdb.client.model.properties.SearchSampleProperty;
import edu.rpi.metpetdb.client.model.validation.primitive.BooleanConstraint;
import edu.rpi.metpetdb.client.model.validation.primitive.FloatConstraint;
import edu.rpi.metpetdb.client.model.validation.primitive.IntegerConstraint;
import edu.rpi.metpetdb.client.model.validation.primitive.StringConstraint;

/**
 * Represents constraints that may or may not exist in the database
 * 
 */
public class ObjectConstraints extends DatabaseObjectConstraints {

	public StringConstraint SearchSample_number;
	public Sample_sesarNumber SearchSample_sesarNumber;
	public GeometryConstraint SearchSample_boundingBox;
	public DateSpanConstraint SearchSample_collectionDateRange;
	public ObjectConstraint SearchSample_owner;
	public ObjectConstraint SearchSample_collector;
	public ObjectConstraint SearchSample_country;
	public ObjectConstraint SearchSample_region;
	public ObjectConstraint SearchSample_references;
	public ObjectConstraint SearchSample_metamorphicGrades;
	public BooleanConstraint SearchSample_wholeRock;
	public IntegerConstraint SearchSample_getPublic;

	public FloatConstraint Sample_longitude;
	public FloatConstraint Sample_latitude;

	// public StringConstraint ChemicalAnalysis_subsampleType;

	public void finishInitialization(DatabaseObjectConstraints doc) {

		try {
			SearchSample_wholeRock.entityName = "SearchSample";
			SearchSample_wholeRock.property = SearchSampleProperty.wholeRock;
			SearchSample_wholeRock.propertyName = "wholeRock";
			SearchSample_wholeRock.required = false;
			
			SearchSample_getPublic.entityName = "SearchSample";
			SearchSample_getPublic.property = SearchSampleProperty.getPublic;
			SearchSample_getPublic.propertyName = "getPublic";
			SearchSample_getPublic.required = true;
			
			SearchSample_collector.entityName = "SearchSample";
			SearchSample_collector.property = SearchSampleProperty.collector;
			SearchSample_collector.propertyName = "Collector";
			SearchSample_collector.required = false;

			SearchSample_country.entityName = "SearchSample";
			SearchSample_country.property = SearchSampleProperty.country;
			SearchSample_country.propertyName = "Country";
			SearchSample_country.required = false;

			SearchSample_number.entityName = "SearchSample";
			SearchSample_number.property = SearchSampleProperty.number;
			SearchSample_number.propertyName = "Number";
			SearchSample_number.maxLength = doc.Sample_number.maxLength;
			SearchSample_number.minLength = doc.Sample_number.minLength;
			SearchSample_number.required = false;

			SearchSample_sesarNumber.entityName = "SearchSample";
			SearchSample_sesarNumber.property = SearchSampleProperty.sesarNumber;
			SearchSample_sesarNumber.propertyName = "IGSN";
			SearchSample_sesarNumber.maxLength = doc.Sample_sesarNumber.maxLength;
			SearchSample_sesarNumber.minLength = doc.Sample_sesarNumber.minLength;
			SearchSample_sesarNumber.required = false;

			SearchSample_boundingBox.entityName = "SearchSample";
			SearchSample_boundingBox.property = SearchSampleProperty.boundingBox;
			SearchSample_boundingBox.propertyName = "Location";
			SearchSample_boundingBox.required = false;

			SearchSample_region.entityName = "SearchSample";
			SearchSample_region.property = SearchSampleProperty.region;
			SearchSample_region.propertyName = "Region";
			SearchSample_region.required = false;

			SearchSample_references.entityName = "SearchSample";
			SearchSample_references.property = SearchSampleProperty.references;
			SearchSample_references.propertyName = "References";
			SearchSample_references.required = false;
		

			SearchSample_metamorphicGrades.entityName = "SearchSample";
			SearchSample_metamorphicGrades.property = SearchSampleProperty.metamorphicGrades;
			SearchSample_metamorphicGrades.propertyName = "MetamorphicGrades";
			SearchSample_metamorphicGrades.required = false;


			SearchSample_collectionDateRange.entityName = "SearchSample";
			SearchSample_collectionDateRange.property = SearchSampleProperty.collectionDateRange;
			SearchSample_collectionDateRange.propertyName = "Collection Date Range";
			SearchSample_collectionDateRange.required = false;

			// Should be a user constraint, but needs a max length... what
			// should
			// max length be?
			SearchSample_owner.entityName = "SearchSample";
			SearchSample_owner.property = SearchSampleProperty.owner;
			SearchSample_owner.propertyName = "Owner";
			SearchSample_owner.required = false;

			// For Bulk Upload
			Sample_longitude.entityName = "Sample";
			Sample_longitude.property = SampleProperty.longitude;
			Sample_longitude.propertyName = "longitude";
			Sample_longitude.required = true;
			Sample_longitude.setMinValue(-180f);
			Sample_longitude.setMaxValue(180f);

			Sample_latitude.entityName = "Sample";
			Sample_latitude.property = SampleProperty.latitude;
			Sample_latitude.propertyName = "latitude";
			Sample_latitude.required = true;
			Sample_latitude.setMinValue(-90f);
			Sample_latitude.setMaxValue(90f);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
