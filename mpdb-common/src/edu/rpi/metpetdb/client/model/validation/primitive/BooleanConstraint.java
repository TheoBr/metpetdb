package edu.rpi.metpetdb.client.model.validation.primitive;

import edu.rpi.metpetdb.client.error.InvalidBooleanException;
import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.model.validation.PropertyConstraint;

/**
 * Constraints that only accepts a Boolean true or false
 * 
 * @author anthony
 * 
 */
public class BooleanConstraint extends PropertyConstraint {

	@Override
	public void validateValue(final Object value) throws ValidationException {
		super.validateValue(value);
		if (value instanceof Boolean)
			return;
		else {
			throw new InvalidBooleanException(this);
		}

	}
}
