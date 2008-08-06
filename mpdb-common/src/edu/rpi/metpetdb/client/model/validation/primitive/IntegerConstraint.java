package edu.rpi.metpetdb.client.model.validation.primitive;

import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.error.validation.InvalidIntegerException;
import edu.rpi.metpetdb.client.model.validation.PropertyConstraint;
import edu.rpi.metpetdb.client.model.validation.interfaces.MaxLengthConstraint;

public class IntegerConstraint extends PropertyConstraint
		implements
			MaxLengthConstraint {

	public void validateValue(final Object value) throws ValidationException {
		super.validateValue(value);
		if (value == null || value instanceof Integer)
			return;
		try {
			Integer.parseInt((String) value);
		} catch (NumberFormatException nfe) {
			throw new InvalidIntegerException();
		}
	}

	public int getMaxLength() {
		return 30;
	}

}
