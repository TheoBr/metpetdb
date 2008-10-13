package edu.rpi.metpetdb.client.error.validation.number;

import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.locale.LocaleHandler;
import edu.rpi.metpetdb.client.model.validation.PropertyConstraint;

public class InvalidFloatException extends ValidationException {
	private static final long serialVersionUID = 1L;

	public InvalidFloatException() {
	}

	public InvalidFloatException(final PropertyConstraint pc) {
		super(pc);
	}

	public String format() {
		return LocaleHandler.lc_text
				.errorDesc_InvalidFloat(formatPropertyName());
	}
}
