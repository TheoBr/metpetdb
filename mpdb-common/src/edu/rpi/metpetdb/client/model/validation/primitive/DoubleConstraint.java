package edu.rpi.metpetdb.client.model.validation.primitive;

import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.error.validation.number.InvalidFloatException;
import edu.rpi.metpetdb.client.error.validation.number.NumberTooBigException;
import edu.rpi.metpetdb.client.error.validation.number.NumberTooSmallException;
import edu.rpi.metpetdb.client.model.validation.PropertyConstraint;
import edu.rpi.metpetdb.client.model.validation.interfaces.MaxLengthConstraint;
import edu.rpi.metpetdb.client.model.validation.interfaces.NumberConstraint;

public class DoubleConstraint extends PropertyConstraint implements
		MaxLengthConstraint, NumberConstraint<Double> {

	private Double maxValue;
	private Double minValue;

	public void validateValue(Object value) throws ValidationException {
		super.validateValue(value);
		if (value == null)
			return;
		if (!(value instanceof Double)) {
			try {
				if (value instanceof String)
					value = Double.parseDouble((String) value);
				else if (value instanceof Double)
					value = new Double((Double)value);
				else
					throw new InvalidFloatException(this);
			} catch (NumberFormatException nfe) {
				throw new InvalidFloatException(this);
			}
		}
		if (value instanceof Double) {
			// make sure value is within ranges
			if (maxValue != null)
				if ((Double) value > maxValue) {
					throw new NumberTooBigException(this, (Double) value);
				}
			if (minValue != null)
				if ((Double) value < minValue) {
					throw new NumberTooSmallException(this, (Double) value);
				}
		}
		return;
	}

	public int getMaxLength() {
		return 30;
	}

	public Double getMaxValue() {
		return maxValue;
	}

	public Double getMinValue() {
		return minValue;
	}

	public void setMaxValue(Double v) {
		maxValue = v;
	}

	public void setMinValue(Double v) {
		minValue = v;
	}

}
