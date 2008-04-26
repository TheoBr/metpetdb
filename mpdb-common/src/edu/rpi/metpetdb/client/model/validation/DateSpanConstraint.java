package edu.rpi.metpetdb.client.model.validation;

import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.model.DateSpan;

/** Applies to any SQL timestamp value. */
public class DateSpanConstraint extends PropertyConstraint {
	public void validateValue(final Object value) throws ValidationException {
		super.validateValue(value);
		final DateSpan g = (DateSpan) value;
		if (g == null)
			return;
	}
}
