package edu.rpi.metpetdb.client.error.validation.number;

import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.model.validation.PropertyConstraint;
import edu.rpi.metpetdb.client.model.validation.interfaces.NumberConstraint;

public class NumberTooSmallException  extends ValidationException {
	private static final long serialVersionUID = 1L;

	private Number value;

	public NumberTooSmallException() {
	}

	public NumberTooSmallException(PropertyConstraint pc, Number value) {
		super(pc);
		this.value = value;
	}

	@Override
	public String format() {
		return "The value of " + formatPropertyName()
		+ " is too small, it has to be greater than "
		+ ((NumberConstraint<?>) constraint).getMinValue()
		+ ", current value is " + value;
	}
}
