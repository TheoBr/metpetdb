package edu.rpi.metpetdb.server.bulk.upload;

import java.util.regex.Pattern;

import edu.rpi.metpetdb.client.model.validation.PropertyConstraint;

/**
 * Represents a column mapping for a bulk upload spreadsheet
 * 
 * @author anthony
 * 
 */
public class ColumnMapping {

	/** The regular expression to match the column header */
	private String regularExpression;
	/** The property related to what data is in the column */
	private PropertyConstraint property;
	private Pattern pattern;

	public ColumnMapping(final String regularExpression, final PropertyConstraint property) {
		this.regularExpression = regularExpression;
		this.property = property;
		this.pattern = Pattern.compile(regularExpression, Pattern.CASE_INSENSITIVE);
	}

	public boolean matches(final String input) {
		return pattern.matcher(input).find();
	}

	public String getRegularExpression() {
		return regularExpression;
	}

	public void setRegularExpression(String regularExpression) {
		this.regularExpression = regularExpression;
	}

	public PropertyConstraint getProperty() {
		return property;
	}

	public void setProperty(PropertyConstraint property) {
		this.property = property;
	}
}
