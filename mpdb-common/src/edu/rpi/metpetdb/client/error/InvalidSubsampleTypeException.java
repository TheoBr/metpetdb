package edu.rpi.metpetdb.client.error;

import edu.rpi.metpetdb.client.locale.LocaleHandler;
import edu.rpi.metpetdb.client.model.validation.PropertyConstraint;

public class InvalidSubsampleTypeException extends ValidationException {
	private static final long serialVersionUID = 1L;

	private String value;

	public InvalidSubsampleTypeException() {
	}

	public InvalidSubsampleTypeException(PropertyConstraint pc) {
		super(pc);
	}

	public InvalidSubsampleTypeException(String value) {
		this.value = value;
	}

	@Override
	public String format() {
		return LocaleHandler.lc_text.errorDesc_InvalidSubsampleType(value);
	}

}
