package edu.rpi.metpetdb.client.error.validation.number;

import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.model.validation.PropertyConstraint;
import edu.rpi.metpetdb.client.model.validation.interfaces.NumberConstraint;

public class NumberTooBigException extends ValidationException {
	private static final long serialVersionUID = 1L;

	private Number value;

	public NumberTooBigException() {
	}

	public NumberTooBigException(PropertyConstraint pc, Number value) {
		super(pc);
		this.value = value;
	}

	@Override
	public String format() {
		return value.toString() + " is greater than "
				+ ((NumberConstraint<?>) constraint).getMaxValue();
	}
}
