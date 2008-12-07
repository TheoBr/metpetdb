package edu.rpi.metpetdb.client.error.bulk.upload;

import edu.rpi.metpetdb.client.error.MpDbException;

public class InvalidSpreadSheetException extends MpDbException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String message;
	
	public InvalidSpreadSheetException(final String message) {
		this.message = message;
	}

	@Override
	public String format() {
		return message;
	}
	

}
