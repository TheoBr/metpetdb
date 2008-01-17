package edu.rpi.metpetdb.client.error;

import edu.rpi.metpetdb.client.model.validation.PropertyConstraint;

public class InvalidImageException extends ValidationException {
	private static final long serialVersionUID = 1L;

	private String errorMsg;

	public InvalidImageException() {
	}

	public InvalidImageException(final PropertyConstraint pc) {
		super(pc);
	}

	public InvalidImageException(final String s) {
		errorMsg = s;
	}

	public String format() {
		// return MpDb.lc_text.errorDesc_InvalidFloat(formatPropertyName());
		if (errorMsg != null)
			return errorMsg;
		else
			return "NEED ERROR MESSAGE HERE(Image)";
	}
}
