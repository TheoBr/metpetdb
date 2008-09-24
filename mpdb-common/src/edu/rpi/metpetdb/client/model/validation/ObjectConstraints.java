package edu.rpi.metpetdb.client.model.validation;

import edu.rpi.metpetdb.client.model.properties.SearchSampleProperty;
import edu.rpi.metpetdb.client.model.validation.primitive.StringConstraint;

/**
 * Represents constraints that may or may not exist in the database
 * 
 */
public class ObjectConstraints extends DatabaseObjectConstraints {

	public StringConstraint SearchSample_alias;
	public Sample_sesarNumber SearchSample_sesarNumber;
	public GeometryConstraint SearchSample_boundingBox;
	public DateSpanConstraint SearchSample_collectionDateRange;
	public StringConstraint SearchSample_owner;
	public StringConstraint SearchSample_collector;
	public StringConstraint SearchSample_country;
	public StringConstraint SearchSample_region;
	public StringConstraint SearchSample_references;
	public StringConstraint SearchSample_metamorphicGrades;
	
	public void finishInitialization(DatabaseObjectConstraints doc) {
		
		SearchSample_collector.entityName = "SearchSample";
		SearchSample_collector.property = SearchSampleProperty.collector;
		SearchSample_collector.propertyName = "Collector";
		SearchSample_collector.maxLength = doc.Sample_collector.maxLength;
		SearchSample_collector.minLength = doc.Sample_collector.minLength;
		SearchSample_collector.required = false;
		
		SearchSample_country.entityName = "SearchSample";
		SearchSample_country.property = SearchSampleProperty.country;
		SearchSample_country.propertyName = "Country";
		SearchSample_country.maxLength = doc.Sample_country.maxLength;
		SearchSample_country.minLength = doc.Sample_country.minLength;
		SearchSample_country.required = false;
		
		SearchSample_alias.entityName = "SearchSample";
		SearchSample_alias.property = SearchSampleProperty.alias;
		SearchSample_alias.propertyName = "Alias";
		SearchSample_alias.maxLength = doc.Sample_alias.maxLength;
		SearchSample_alias.minLength = doc.Sample_alias.minLength;
		SearchSample_alias.required = false;

		SearchSample_sesarNumber.entityName = "SearchSample";
		SearchSample_sesarNumber.property = SearchSampleProperty.sesarNumber;
		SearchSample_sesarNumber.propertyName = "Sesar Number";
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

		// Should be a user constraint, but needs a max length... what should
		// max length be?
		SearchSample_owner.entityName = "SearchSample";
		SearchSample_owner.property = SearchSampleProperty.owner;
		SearchSample_owner.minLength = 0;
		SearchSample_owner.maxLength = 50;
		SearchSample_owner.propertyName = "Owner";
		SearchSample_owner.required = false;
		
	}

}
