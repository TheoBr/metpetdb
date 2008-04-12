package edu.rpi.metpetdb.client.model.validation;

import edu.rpi.metpetdb.client.model.properties.SearchSampleProperty;

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

	}

}
