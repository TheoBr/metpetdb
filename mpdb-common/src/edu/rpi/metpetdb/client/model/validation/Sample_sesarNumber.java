package edu.rpi.metpetdb.client.model.validation;

import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.error.validation.InvalidSESARNumberException;
import edu.rpi.metpetdb.client.model.Sample;
import edu.rpi.metpetdb.client.model.validation.primitive.StringConstraint;

/** Applies only to {@link Sample#getSesarNumber()}. */
public class Sample_sesarNumber extends StringConstraint {
	public void validateValue(final Object value) throws ValidationException {
		super.validateValue(value);
		if (value == null)
			return;
		final String s = (String) value;
		if (!s.matches("^[A-Z0-9]{" + minLength + "," + maxLength + "}$"))
			throw new InvalidSESARNumberException(this);
	}
}
