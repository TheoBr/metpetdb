package edu.rpi.metpetdb.client.model.validation;


/**
 * Represents constraints that may or may not exist in the database
 *
 */
public class ObjectConstraints extends DatabaseObjectConstraints  {

	/* Sample */
	public IntegerConstraint Sample_subsampleCount;
	
	/* Subsample */
	public StringConstraint Subsample_sampleName;
	public IntegerConstraint Subsample_imageCount;
	public IntegerConstraint Subsample_analysisCount;
	
	/* Mineral Analysis */
	public StringConstraint MineralAnalysis_sampleName;
	public StringConstraint MineralAnalysis_subsampleName;

	public void finishInitialization() {
		
	}

}
