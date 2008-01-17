package edu.rpi.metpetdb.client.error;

import edu.rpi.metpetdb.client.model.validation.PropertyConstraint;
import edu.rpi.metpetdb.client.ui.LocaleHandler;

/** Indicates one value does not match the other. */
public class ValueNotEqualException extends ValidationException {
	private static final long serialVersionUID = 1L;

	private PropertyConstraint otherConstraint;

	public ValueNotEqualException() {
	}
	public ValueNotEqualException(final PropertyConstraint pc,
			final PropertyConstraint oc) {
		super(pc);
		otherConstraint = oc;
	}

	public String format() {
		final String a = formatPropertyName();
		final String b = formatPropertyName(otherConstraint);
		return LocaleHandler.lc_text.errorDesc_NotEqual(a, b);
	}
}
