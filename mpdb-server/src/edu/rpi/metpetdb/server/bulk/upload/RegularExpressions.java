package edu.rpi.metpetdb.server.bulk.upload;

public class RegularExpressions {
	
	public static final String ROCK_TYPE = "(type)|(rock)";
	public static final String SESAR_NUMBER = "(sesar)|(isgn)";
	public static final String LATITUDE_ERROR = "(latitude error)|(lat error)";
	public static final String LONGITUDE_ERROR = "(longitude error)|(lon error)";
	public static final String REGION = "region";
	public static final String COUNTRY = "country";
	public static final String COLLECTOR = "(collector)|(collected by)";
	public static final String COLLECTION_DATE = "(date of collection)|(collected)|(collection.+date)";
	public static final String LOCATION = "(present.+location)|(current.+location)";
	public static final String METAMORPHIC_GRADES = "(grade)|(facies)";
	public static final String COMMENTS = "(comment)|(note)|(description)";
	public static final String REFERENCES = "(reference)|(ref)";
	public static final String ALIAS = "(sample[ number| name|])|(sample)";
	public static final String MINERALS = "minerals";

}
