package edu.rpi.metpetdb.client.error.bulk.upload;

import edu.rpi.metpetdb.client.error.MpDbException;

public class MissingMeasurementUnitsException extends MpDbException{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String message;
	
	public MissingMeasurementUnitsException(){
		message = "Missing measurement units";
	}
	
	public MissingMeasurementUnitsException(final String who) {
		this.message = who + " is missing a measurement unit header in the cell below";
	}

	@Override
	public String format() {
		return message;
	}
	

}