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
	public static final String REGION = "\\s*region\\s*";
	public static final String COUNTRY = "\\s*country\\s*";
	public static final String ALIAS = "\\s*alias\\s*";

	public static final String METAMORPHIC_GRADES = "(grade)|(facies)";
	public static final String LATITUDE = "(\\s*latitude\\s*$)|(lat\\s*$)";
	public static final String LONGITUDE = "(\\s*longitude\\s*$)|(lon\\s*$)";

	/** Chemical Analysis */
	public static final String SUBSAMPLE = "^\\s*subsample\\s*$";
	public static final String ANALYSIS_METHOD = "(method)|(analytical method)|(analysis method)|(^\\s*type\\s*$)";
	public static final String SPOT_ID = "(point)|(spot)";
	public static final String TOTAL = "(total)|(wt%tot)|(wt%total)";
	public static final String X_COORDINATE = "(x position)|(x pos)|(x coordinate)|(x coord)|(x reference)";
	public static final String Y_COORDINATE = "(y position)|(y pos)|(y coordinate)|(y coord)|(y reference)";
	public static final String X_STAGE = "(x stage)";
	public static final String Y_STAGE = "(y stage)";
	public static final String ANALYST = "\\s*analyst\\s*";
	public static final String SUBSAMPLE_TYPE = "\\s*subsample type\\s*";
	public static final String PRECISION_UNIT = "\\s*precision unit\\s*";
	public static final String PRECISION = "\\s*precision\\s*";
	public static final String WHOLE_ROCK = "\\s*whole rock\\s*";
	public static final String IMAGE_REFERENCE = "(image reference)|(reference image)";
	
	/** Image */
	public static final String FILENAME = "(file)|(path)";
	public static final String IMAGE_TYPE = "\\s*image type\\s*";
	public static final String DWELL_TIME = "\\s*dwell time\\s*";
	public static final String CURRENT = "\\s*current\\s*";
	public static final String VOLTAGE = "\\s*voltage\\s*";
	public static final String ELEMENT = "\\s*element\\s*";
	public static final String SCALE = "\\s*scale\\s*";
	public static final String PARENT_LOC_X = "^\\s*grid location x\\s*";
	public static final String PARENT_LOC_Y = "^\\s*grid location y\\s*";
	
	/** Common */
	public static final String MINERALS = "(minerals)|(mineral)";
	public static final String LOCATION = "(present.+location)|(current.+location)|(analytical facility)|(analysis location)|(where done)|(wheredone)";
	public static final String REFERENCES = "(^reference$)|(publication reference)";
	public static final String COLLECTION_DATE = "(date of collection)|(collected)|(collection.+date)|(analysis Date)|(date)";
	public static final String COMMENTS = "(comment)|(note)|(description)";
	public static final String SAMPLE = "(^sample[ number| name|])|(^sample$)";
	public static final String COLLECTOR = "(collector)|(collected by)";
	public static final String REFERENCE_FILENAME = "(reference path)";
}
