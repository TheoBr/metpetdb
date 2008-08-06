package edu.rpi.metpetdb.client.error.validation;

import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.locale.LocaleHandler;
import edu.rpi.metpetdb.client.model.validation.PropertyConstraint;

public class InvalidShortException extends ValidationException {
	private static final long serialVersionUID = 1L;

	public InvalidShortException() {
	}

	public InvalidShortException(PropertyConstraint pc) {
		super(pc);
	}

	@Override
	public String format() {
		return LocaleHandler.lc_text
				.errorDesc_InvalidShort(formatPropertyName());
	}
}
