package edu.rpi.metpetdb.client.model.validation.primitive;

import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.error.validation.number.InvalidFloatException;
import edu.rpi.metpetdb.client.error.validation.number.NumberTooBigException;
import edu.rpi.metpetdb.client.error.validation.number.NumberTooSmallException;
import edu.rpi.metpetdb.client.model.validation.PropertyConstraint;
import edu.rpi.metpetdb.client.model.validation.interfaces.MaxLengthConstraint;
import edu.rpi.metpetdb.client.model.validation.interfaces.NumberConstraint;

public class FloatConstraint extends PropertyConstraint implements
		MaxLengthConstraint, NumberConstraint<Float> {

	private Float maxValue;
	private Float minValue;

	public void validateValue(Object value) throws ValidationException {
		super.validateValue(value);
		if (value == null)
			return;
		if (!(value instanceof Float)) {
			try {
				if (value instanceof String)
					value = Float.parseFloat((String) value);
				else if (value instanceof Double)
					value = new Float((Double)value);
				else
					throw new InvalidFloatException(this);
			} catch (NumberFormatException nfe) {
				throw new InvalidFloatException(this);
			}
		}
		if (value instanceof Float) {
			// make sure value is within ranges
			if (maxValue != null)
				if ((Float) value > maxValue) {
					throw new NumberTooBigException(this, (Float) value);
				}
			if (minValue != null)
				if ((Float) value < minValue) {
					throw new NumberTooSmallException(this, (Float) value);
				}
		}
		return;
	}

	public int getMaxLength() {
		return 30;
	}

	public Float getMaxValue() {
		return maxValue;
	}

	public Float getMinValue() {
		return minValue;
	}

	public void setMaxValue(Float v) {
		maxValue = v;
	}

	public void setMinValue(Float v) {
		minValue = v;
	}

}
