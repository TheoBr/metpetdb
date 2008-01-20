package edu.rpi.metpetdb.client.error;

import edu.rpi.metpetdb.client.locale.LocaleHandler;
import edu.rpi.metpetdb.client.model.validation.PropertyConstraint;

/** Indicates a particular property must have a value. */
public class PropertyRequiredException extends ValidationException {
	private static final long serialVersionUID = 1L;

	public PropertyRequiredException() {
	}
	public PropertyRequiredException(final PropertyConstraint pc) {
		super(pc);
	}

	public String format() {
		return LocaleHandler.lc_text.errorDesc_Required(formatPropertyName());
	}
	
	public String getMessage() {
		return format() + super.getMessage();
	}
}
