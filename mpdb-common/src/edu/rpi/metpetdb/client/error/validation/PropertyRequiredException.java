package edu.rpi.metpetdb.client.error.validation;

import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.locale.LocaleHandler;
import edu.rpi.metpetdb.client.model.validation.PropertyConstraint;

/** Indicates a particular property must have a value. */
public class PropertyRequiredException extends ValidationException {
	private static final long serialVersionUID = 1L;

	private String propertyName;

	public PropertyRequiredException() {
	}
	public PropertyRequiredException(final PropertyConstraint pc) {
		super(pc);
	}

	public PropertyRequiredException(final PropertyConstraint pc,
			final String propertyName) {
		super(pc);
		this.propertyName = propertyName;
	}

	public String format() {
		// return
		// LocaleHandler.lc_text.errorDesc_Required(formatPropertyName());
		return LocaleHandler.lc_text
				.errorDesc_Required(propertyName != null ? propertyName
						: formatPropertyName());
	}
}
