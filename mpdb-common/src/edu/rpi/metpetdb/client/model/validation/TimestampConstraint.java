package edu.rpi.metpetdb.client.model.validation;

import java.sql.Timestamp;

import edu.rpi.metpetdb.client.error.ValidationException;

/** Applies to any SQL timestamp value. */
public class TimestampConstraint extends PropertyConstraint {
	public void validateValue(final Object value) throws ValidationException {
		super.validateValue(value);
		final Timestamp g = (Timestamp) value;
		if (g == null)
			return;
	}
}
