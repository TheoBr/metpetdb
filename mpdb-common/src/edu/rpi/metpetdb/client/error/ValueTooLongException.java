package edu.rpi.metpetdb.client.error;

import edu.rpi.metpetdb.client.locale.LocaleHandler;
import edu.rpi.metpetdb.client.model.validation.StringConstraint;

/** Indicates the value for a property is too long (and would truncate). */
public class ValueTooLongException extends ValidationException {
	private static final long serialVersionUID = 1L;

	public ValueTooLongException() {
	}
	public ValueTooLongException(final StringConstraint pc) {
		super(pc);
	}

	public String format() {
		final int n = ((StringConstraint) constraint).maxLength;
		return LocaleHandler.lc_text.errorDesc_TooLong(formatPropertyName(), n);
	}
}
