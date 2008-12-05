package edu.rpi.metpetdb.server.bulk.upload;

import edu.rpi.metpetdb.client.model.properties.Property;

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
	private Property property;

	public ColumnMapping(final String regularExpression, final Property property) {
		this.regularExpression = regularExpression;
		this.property = property;
	}

	public String getRegularExpression() {
		return regularExpression;
	}

	public void setRegularExpression(String regularExpression) {
		this.regularExpression = regularExpression;
	}

	public Property getProperty() {
		return property;
	}

	public void setProperty(Property property) {
		this.property = property;
	}
}
