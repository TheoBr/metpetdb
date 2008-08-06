package edu.rpi.metpetdb.client.model.validation.primitive;

import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.error.validation.InvalidShortException;
import edu.rpi.metpetdb.client.model.validation.PropertyConstraint;
import edu.rpi.metpetdb.client.model.validation.interfaces.MaxLengthConstraint;

public class ShortConstraint extends PropertyConstraint implements
		MaxLengthConstraint {

	public void validateValue(final Object value) throws ValidationException {
		super.validateValue(value);
		if (value == null || value instanceof Short)
			return;
		try {
			Short.parseShort((String) value);
		} catch (NumberFormatException nfe) {
			throw new InvalidShortException(this);
		}
	}

	public int getMaxLength() {
		return 30;
	}

}
