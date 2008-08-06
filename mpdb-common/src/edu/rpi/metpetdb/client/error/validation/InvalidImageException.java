package edu.rpi.metpetdb.client.error.validation;

import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.locale.LocaleHandler;
import edu.rpi.metpetdb.client.model.validation.PropertyConstraint;

public class InvalidImageException extends ValidationException {
	private static final long serialVersionUID = 1L;

	private String imgName;

	public InvalidImageException() {
	}

	public InvalidImageException(final PropertyConstraint pc) {
		super(pc);
	}

	public InvalidImageException(final String s) {
		imgName = s;
	}

	public String format() {
		return LocaleHandler.lc_text.errorDesc_InvalidImage(imgName);
	}
}
