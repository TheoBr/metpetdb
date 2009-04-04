package edu.rpi.metpetdb.client.error.bulk.upload;

import edu.rpi.metpetdb.client.error.ValidationException;

public class ExpectedStringColumnTypeException extends ValidationException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String format() {
		return "Expected this cell to be a String type";
	}

}
