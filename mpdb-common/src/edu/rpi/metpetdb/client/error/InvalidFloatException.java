package edu.rpi.metpetdb.client.error;

import edu.rpi.metpetdb.client.model.validation.PropertyConstraint;
import edu.rpi.metpetdb.client.ui.LocaleHandler;

public class InvalidFloatException extends ValidationException {
	private static final long serialVersionUID = 1L;

	public InvalidFloatException() {
	}

	public InvalidFloatException(final PropertyConstraint pc) {
		super(pc);
	}

	public String format() {
		return LocaleHandler.lc_text.errorDesc_InvalidFloat(formatPropertyName());
	}
}
