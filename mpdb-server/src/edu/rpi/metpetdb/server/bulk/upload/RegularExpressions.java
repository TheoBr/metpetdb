package edu.rpi.metpetdb.server.bulk.upload;

/**
 * Regular expressions that are defined for the headers of a bulk upload
 * spreadsheet
 * 
 * @author anthony
 * 
 */
public class RegularExpressions {

	/** Sample */
	public static final String ROCK_TYPE = "(type)|(rock)";
	public static final String SESAR_NUMBER = "(sesar)|(igsn)";
	public static final String LOCATION_ERROR = "(lat/lon error)|(Location Error)";
	public static final String REGION = "region";
	public static final String COUNTRY = "country";

	public static final String METAMORPHIC_GRADES = "(grade)|(facies)";
	public static final String LATITUDE = "(latitude$)|(lat\\s*$)";
	public static final String LONGITUDE = "(longitude$)|(lon\\s*$)";

	/** Chemical Analysis */
	public static final String SUBSAMPLE = "^\\s*subsample\\s*$";
	public static final String ANALYSIS_METHOD = "(method)|(analytical method)|(analysis method)|(^\\s*type\\s*$)";
	public static final String SPOT_ID = "(point)|(spot)|(analysis location)";
	public static final String TOTAL = "(total)|(wt%tot)|(wt%total)";
	public static final String X_COORDINATE = "(x position)|(x pos)|(x coordinate)|(x coord)|(x reference)";
	public static final String Y_COORDINATE = "(y position)|(y pos)|(y coordinate)|(y coord)|(x reference)";
	public static final String X_STAGE = "(x stage)";
	public static final String Y_STAGE = "(y stage)";
	public static final String ANALYST = "analyst";
	public static final String SUBSAMPLE_TYPE = "^subsample type$";
	public static final String PRECISION_UNIT = "precision unit";
	public static final String PRECISION = "precision";
	public static final String WHOLE_ROCK = "whole rock";
	
	/** Image */
	public static final String FILENAME = "(file)|(path)";
	public static final String IMAGE_TYPE = "image type";
	public static final String DWELL_TIME = "dwell time";
	public static final String CURRENT = "current";
	public static final String VOLTAGE = "voltage";
	public static final String ELEMENT = "element";
	public static final String SCALE = "scale";
	public static final String PARENT_LOC_X = "^\\s*grid location x\\s*$";
	public static final String PARENT_LOC_Y = "^\\s*grid location y\\s*$";
	
	/** Common */
	public static final String MINERALS = "(minerals)|(mineral)";
	public static final String LOCATION = "(present.+location)|(current.+location)|(analytical facility)";
	public static final String REFERENCES = "(reference$)|(ref$)";
	public static final String COLLECTION_DATE = "(date of collection)|(collected)|(collection.+date)|(analysis Date)";
	public static final String COMMENTS = "(comment)|(note)|(description)";
	public static final String SAMPLE = "(^sample[ number| name|])|(^sample$)";
	public static final String COLLECTOR = "(collector)|(collected by)";
}
