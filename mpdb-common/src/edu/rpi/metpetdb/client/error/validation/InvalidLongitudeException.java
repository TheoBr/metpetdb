package edu.rpi.metpetdb.client.error.validation;

import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.locale.LocaleHandler;
import edu.rpi.metpetdb.client.model.validation.PropertyConstraint;

public class InvalidLongitudeException extends ValidationException {
	private static final long serialVersionUID = 1L;
	private String propertyName;

	public InvalidLongitudeException() {
	}

	public InvalidLongitudeException(final PropertyConstraint pc) {
		super(pc);
	}

	public InvalidLongitudeException(final PropertyConstraint pc,
			final String propertyName) {
		super(pc);
		this.propertyName = propertyName;
	}

	public String format() {
		return LocaleHandler.lc_text
				.errorDesc_InvalidLongitude(propertyName != null ? propertyName
						: formatPropertyName());
	}
}
