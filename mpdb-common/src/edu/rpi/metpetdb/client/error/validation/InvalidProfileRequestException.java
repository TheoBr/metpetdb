package edu.rpi.metpetdb.client.error.validation;

import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.locale.LocaleHandler;


public class InvalidProfileRequestException extends ValidationException {
	private static final long serialVersionUID = 1L;


	private String value;
	
	public InvalidProfileRequestException()
	{
		
	}
	
	public InvalidProfileRequestException(String value) {
		this.value = value;
	}
	
	public String format() {
		return LocaleHandler.lc_text.errorDesc_InvalidProfileRequest(value);
	}
}
