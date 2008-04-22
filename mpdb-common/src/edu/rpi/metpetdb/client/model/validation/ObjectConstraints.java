package edu.rpi.metpetdb.client.model.validation;

import edu.rpi.metpetdb.client.model.properties.SearchSampleProperty;
import edu.rpi.metpetdb.client.model.validation.primitive.IntegerConstraint;
import edu.rpi.metpetdb.client.model.validation.primitive.StringConstraint;

/**
 * Represents constraints that may or may not exist in the database
 * 
 */
public class ObjectConstraints extends DatabaseObjectConstraints {

	/* Sample */
	public IntegerConstraint Sample_subsampleCount;

	/* Subsample */
	public StringConstraint Subsample_sampleName;
	public IntegerConstraint Subsample_imageCount;
	public IntegerConstraint Subsample_analysisCount;

	/* Mineral Analysis */
	public StringConstraint ChemicalAnalysis_sampleName;
	public StringConstraint ChemicalAnalysis_subsampleName;

	public StringConstraint SearchSample_alias;
	public Sample_sesarNumber SearchSample_sesarNumber;
	// public ValuesInCollectionConstraint<MineralDTO> SearchSample_minerals;
	public GeometryConstraint SearchSample_location;
	public TimestampConstraint SearchSample_collectionDate;
	public MultipleRockTypeConstraint SearchSample_possibleRockTypes;
	public UserConstraint SearchSample_owner;

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

		// SearchSample_minerals.entityName = "SearchSample";
		// SearchSample_minerals.property = SearchSampleProperty.minerals;
		// SearchSample_minerals.propertyName = "Minerals";
		// SearchSample_minerals.required = false;

		SearchSample_location.entityName = "SearchSample";
		SearchSample_location.property = SearchSampleProperty.location;
		SearchSample_location.propertyName = "Location";
		SearchSample_location.required = false;

		SearchSample_collectionDate.entityName = "SearchSample";
		SearchSample_collectionDate.property = SearchSampleProperty.collectionDate;
		SearchSample_collectionDate.propertyName = "Collection Date";
		SearchSample_collectionDate.required = false;

		SearchSample_possibleRockTypes.entityName = "SearchSample";
		SearchSample_possibleRockTypes.property = SearchSampleProperty.possibleRockTypes;
		SearchSample_possibleRockTypes.propertyName = "Rock Type";
		SearchSample_possibleRockTypes.required = false;

		SearchSample_owner.entityName = "SearchSample";
		SearchSample_owner.property = SearchSampleProperty.owner;
		SearchSample_owner.propertyName = "Owner";
		SearchSample_owner.required = false;

	}

}
