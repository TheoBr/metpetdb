package edu.rpi.metpetdb.client.error;

import edu.rpi.metpetdb.client.model.validation.PropertyConstraint;
import edu.rpi.metpetdb.client.ui.LocaleHandler;

/** Indicates one value does naturally sort after another value. */
public class ValueNotAfterException extends ValidationException {
	private static final long serialVersionUID = 1L;

	private PropertyConstraint otherConstraint;

	public ValueNotAfterException() {
	}
	public ValueNotAfterException(final PropertyConstraint pc,
			final PropertyConstraint oc) {
		super(pc);
		otherConstraint = oc;
	}

	public String format() {
		final String a = formatPropertyName();
		final String b = formatPropertyName(otherConstraint);
		return LocaleHandler.lc_text.errorDesc_NotAfter(a, b);
	}
}
