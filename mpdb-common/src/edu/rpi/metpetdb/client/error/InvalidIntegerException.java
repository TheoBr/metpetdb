package edu.rpi.metpetdb.client.error;

import edu.rpi.metpetdb.client.model.validation.PropertyConstraint;

public class InvalidIntegerException extends ValidationException {
	private static final long serialVersionUID = 1L;

	public InvalidIntegerException() {
	}

	public InvalidIntegerException(final PropertyConstraint pc) {
		super(pc);
	}

	public String format() {
		// return MpDb.lc_text.errorDesc_InvalidFloat(formatPropertyName());
		return "NEED ERROR MESSAGE HERE";
	}
}
