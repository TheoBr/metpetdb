package edu.rpi.metpetdb.client.error;

import edu.rpi.metpetdb.client.locale.LocaleHandler;
import edu.rpi.metpetdb.client.model.validation.PropertyConstraint;

/** Indicates the value is not a properly formatted SESAR number. */
public class InvalidSESARNumberException extends ValidationException {
	private static final long serialVersionUID = 1L;

	public InvalidSESARNumberException() {
	}
	public InvalidSESARNumberException(final PropertyConstraint pc) {
		super(pc);
	}

	public String format() {
		return LocaleHandler.lc_text.errorDesc_InvalidSESARNumber(formatPropertyName());
	}
}
