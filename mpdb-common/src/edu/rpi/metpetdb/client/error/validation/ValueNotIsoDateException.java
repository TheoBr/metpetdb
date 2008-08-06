package edu.rpi.metpetdb.client.error.validation;

import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.locale.LocaleHandler;
import edu.rpi.metpetdb.client.model.validation.PropertyConstraint;

/** Indicates a particular property must use an ISO date format. */
public class ValueNotIsoDateException extends ValidationException {
	private static final long serialVersionUID = 1L;

	public ValueNotIsoDateException() {
	}
	public ValueNotIsoDateException(final PropertyConstraint pc) {
		super(pc);
	}

	public String format() {
		return LocaleHandler.lc_text.errorDesc_IsoDateFormat(formatPropertyName());
	}
}
