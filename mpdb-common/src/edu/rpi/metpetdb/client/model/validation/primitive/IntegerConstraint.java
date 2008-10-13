package edu.rpi.metpetdb.client.model.validation.primitive;

import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.error.validation.number.InvalidIntegerException;
import edu.rpi.metpetdb.client.model.validation.PropertyConstraint;
import edu.rpi.metpetdb.client.model.validation.interfaces.MaxLengthConstraint;
import edu.rpi.metpetdb.client.model.validation.interfaces.NumberConstraint;

public class IntegerConstraint extends PropertyConstraint implements
		MaxLengthConstraint, NumberConstraint<Integer> {
	
	private Integer minValue;
	private Integer maxValue;

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

	public Integer getMinValue() {
		return minValue;
	}

	public void setMinValue(Integer minValue) {
		this.minValue = minValue;
	}

	public Integer getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(Integer maxValue) {
		this.maxValue = maxValue;
	}
}
