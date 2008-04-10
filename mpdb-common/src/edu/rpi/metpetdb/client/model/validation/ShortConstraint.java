package edu.rpi.metpetdb.client.model.validation;

import edu.rpi.metpetdb.client.error.InvalidIntegerException;
import edu.rpi.metpetdb.client.error.ValidationException;

public class ShortConstraint extends PropertyConstraint implements
		MaxLengthConstraint {

	public void validateValue(final Object value) throws ValidationException {
		super.validateValue(value);
		if (value == null || value instanceof Short)
			return;
		try {
			Short.parseShort((String) value);
		} catch (NumberFormatException nfe) {
			// TODO change
			throw new InvalidIntegerException();
		}
	}

	public int getMaxLength() {
		return 30;
	}

}
