package edu.rpi.metpetdb.client.error;

import edu.rpi.metpetdb.client.locale.LocaleHandler;
import edu.rpi.metpetdb.client.model.validation.PropertyConstraint;

public class InvalidLocationException extends ValidationException {
	private static final long serialVersionUID = 1L;
	private String propertyName;

	public InvalidLocationException() {
	}

	public InvalidLocationException(final PropertyConstraint pc) {
		super(pc);
	}

	public InvalidLocationException(final PropertyConstraint pc,
			final String propertyName) {
		super(pc);
		this.propertyName = propertyName;
	}

	public String format() {
		return LocaleHandler.lc_text
				.errorDesc_InvalidLocation(propertyName != null ? propertyName
						: formatPropertyName());
	}
}
