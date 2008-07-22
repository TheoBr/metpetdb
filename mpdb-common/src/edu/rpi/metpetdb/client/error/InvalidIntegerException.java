package edu.rpi.metpetdb.client.error;

import edu.rpi.metpetdb.client.locale.LocaleHandler;
import edu.rpi.metpetdb.client.model.validation.PropertyConstraint;

public class InvalidIntegerException extends ValidationException {
	private static final long serialVersionUID = 1L;

	public InvalidIntegerException() {
	}

	public InvalidIntegerException(final PropertyConstraint pc) {
		super(pc);
	}

	public String format() {
		return LocaleHandler.lc_text
				.errorDesc_InvalidInteger(formatPropertyName());
	}
}
