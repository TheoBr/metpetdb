package edu.rpi.metpetdb.client.error;

import edu.rpi.metpetdb.client.model.validation.PropertyConstraint;
import edu.rpi.metpetdb.client.ui.LocaleHandler;

/** Indicates a Geometry object instance is non-conforming. */
public class InvalidGeometryException extends ValidationException {
	private static final long serialVersionUID = 1L;

	private String propertyName;

	public InvalidGeometryException() {
		
	}
	public InvalidGeometryException(final PropertyConstraint pc) {
		super(pc);
	}
	public InvalidGeometryException(final PropertyConstraint pc,
			final String propertyName) {
		super(pc);
		this.propertyName = propertyName;
	}

	public String format() {
		return LocaleHandler.lc_text.errorDesc_InvalidGeometry(propertyName != null
				? propertyName
				: formatPropertyName());
	}
}
