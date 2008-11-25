package edu.rpi.metpetdb.client.error.validation;

import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.locale.LocaleHandler;
import edu.rpi.metpetdb.client.model.validation.PropertyConstraint;

/** Indicates the value is not a properly formatted SESAR number. */
public class InvalidDateStringException extends ValidationException {
	private static final long serialVersionUID = 1L;
	
	private String date;

	public InvalidDateStringException() {
	}
	public InvalidDateStringException(final PropertyConstraint pc) {
		super(pc);
	}

	public InvalidDateStringException(String string) {
		date = string;
	}
	public String format() {
		return LocaleHandler.lc_text.errorDesc_InvalidDateString() + ":" + date;
	}
}
