package edu.rpi.metpetdb.client.error;

import edu.rpi.metpetdb.client.locale.LocaleHandler;
import edu.rpi.metpetdb.client.model.validation.PropertyConstraint;

public class InvalidBooleanException extends ValidationException {
	private static final long serialVersionUID = 1L;

	public InvalidBooleanException() {
	}

	public InvalidBooleanException(final PropertyConstraint pc) {
		super(pc);
	}

	@Override
	public String format() {
		return LocaleHandler.lc_text
				.errorDesc_InvalidBoolean(formatPropertyName());
	}

}
