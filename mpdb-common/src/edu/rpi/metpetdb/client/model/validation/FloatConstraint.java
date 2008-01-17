package edu.rpi.metpetdb.client.model.validation;

import edu.rpi.metpetdb.client.error.InvalidFloatException;
import edu.rpi.metpetdb.client.error.ValidationException;

public class FloatConstraint extends PropertyConstraint
		implements
			MaxLengthConstraint {

	public void validateValue(Object value) throws ValidationException {
		super.validateValue(value);
		if (value == null || value instanceof Float)
			return;
		try {
			if (value instanceof String)
				Float.parseFloat((String) value);
		} catch (NumberFormatException nfe) {
			throw new InvalidFloatException(this);
		}
	}

	public int getMaxLength() {
		return 30;
	}

}