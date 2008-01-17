package edu.rpi.metpetdb.client.model.validation;

import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.error.ValueWrongTypeException;
import edu.rpi.metpetdb.client.model.User;

public class UserConstraint extends MObjectConstraint {
	public void validateValue(final Object value) throws ValidationException {
		super.validateValue(value);
		if (value != null && !(value instanceof User))
			throw new ValueWrongTypeException(this);
	}
}
