package edu.rpi.metpetdb.client.error;

import edu.rpi.metpetdb.client.locale.LocaleHandler;
import edu.rpi.metpetdb.client.model.validation.PropertyConstraint;

public class InvalidLatitudeException extends ValidationException {
	private static final long serialVersionUID = 1L;
	private String propertyName;

	public InvalidLatitudeException() {
	}

	public InvalidLatitudeException(final PropertyConstraint pc) {
		super(pc);
	}

	public InvalidLatitudeException(final PropertyConstraint pc,
			final String propertyName) {
		super(pc);
		this.propertyName = propertyName;
	}

	public String format() {
		return LocaleHandler.lc_text
				.errorDesc_InvalidLatitude(propertyName != null ? propertyName
						: formatPropertyName());
	}
}
