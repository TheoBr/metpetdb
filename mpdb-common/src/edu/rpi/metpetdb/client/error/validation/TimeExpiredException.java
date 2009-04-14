package edu.rpi.metpetdb.client.error.validation;

import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.locale.LocaleHandler;
import edu.rpi.metpetdb.client.model.validation.PropertyConstraint;

public class TimeExpiredException extends ValidationException {
	private static final long serialVersionUID = 1L;

	public TimeExpiredException() {
	}
	public TimeExpiredException(final PropertyConstraint pc,
			final PropertyConstraint oc) {
		super(pc);
	}

	public String format() {
		return LocaleHandler.lc_text.errorDesc_TimeExpired();
	}
}