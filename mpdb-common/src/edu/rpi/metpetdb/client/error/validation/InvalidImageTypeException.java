package edu.rpi.metpetdb.client.error.validation;

import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.locale.LocaleHandler;
import edu.rpi.metpetdb.client.model.validation.PropertyConstraint;

public class InvalidImageTypeException extends ValidationException {
	private static final long serialVersionUID = 1L;
	private String value;

	public InvalidImageTypeException() {
	}

	public InvalidImageTypeException(PropertyConstraint pc) {
		super(pc);
	}

	public InvalidImageTypeException(String value) {
		this.value = value;
	}

	@Override
	public String format() {
		return LocaleHandler.lc_text.errorDesc_InvalidImageType(value);
	}

}
