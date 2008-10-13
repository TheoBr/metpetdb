package edu.rpi.metpetdb.client.model.validation.primitive;

import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.error.validation.number.InvalidShortException;
import edu.rpi.metpetdb.client.model.validation.PropertyConstraint;
import edu.rpi.metpetdb.client.model.validation.interfaces.MaxLengthConstraint;
import edu.rpi.metpetdb.client.model.validation.interfaces.NumberConstraint;

public class ShortConstraint extends PropertyConstraint implements
		MaxLengthConstraint, NumberConstraint<Short> {
	
	private Short minValue;
	private Short maxValue;

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

	public Short getMinValue() {
		return minValue;
	}

	public void setMinValue(Short minValue) {
		this.minValue = minValue;
	}

	public Short getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(Short maxValue) {
		this.maxValue = maxValue;
	}
}
