package edu.rpi.metpetdb.client.error.validation;

import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.locale.LocaleHandler;
import edu.rpi.metpetdb.client.model.validation.PropertyConstraint;

/** Indicates a particular property value has already been taken. */
public class DuplicateValueException extends ValidationException {
	private static final long serialVersionUID = 1L;

	private String taken;

	public DuplicateValueException() {
	}
	public DuplicateValueException(final PropertyConstraint p, final String v) {
		super(p);
		taken = v;
	}

	public String format() {
		return LocaleHandler.lc_text.errorDesc_Duplicate(formatPropertyName(), taken);
	}
}
