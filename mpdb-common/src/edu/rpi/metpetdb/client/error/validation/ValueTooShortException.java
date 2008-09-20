package edu.rpi.metpetdb.client.error.validation;

import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.locale.LocaleHandler;
import edu.rpi.metpetdb.client.model.validation.primitive.StringConstraint;

/** Indicates the value for a property is too short (not enough characters). */
public class ValueTooShortException extends ValidationException {
	private static final long serialVersionUID = 1L;

	public ValueTooShortException() {
	}
	public ValueTooShortException(final StringConstraint pc) {
		super(pc);
	}

	public String format() {
		final int n = ((StringConstraint) constraint).minLength;
		return LocaleHandler.lc_text
				.errorDesc_TooShort(formatPropertyName(), n);
	}

	public String getMessage() {
		return format();
	}
}
