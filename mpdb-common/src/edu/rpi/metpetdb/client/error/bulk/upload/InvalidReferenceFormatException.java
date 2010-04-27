package edu.rpi.metpetdb.client.error.bulk.upload;

import edu.rpi.metpetdb.client.error.MpDbException;

public class InvalidReferenceFormatException extends MpDbException{
	private static final long serialVersionUID = 1L;
	
	private String message;
	
	public InvalidReferenceFormatException(){
		this.message = "Error parsing reference file";
	}
	
	public InvalidReferenceFormatException(final String message){
		this.message = message;
	}

	@Override
	public String format() {
		return message;
	}

}
