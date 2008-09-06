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

	public void finishInitialization(DatabaseObjectConstraints doc) {
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
