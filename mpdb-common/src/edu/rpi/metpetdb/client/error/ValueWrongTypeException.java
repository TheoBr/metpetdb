package edu.rpi.metpetdb.client.error;

import edu.rpi.metpetdb.client.model.validation.MObjectConstraint;
import edu.rpi.metpetdb.client.ui.LocaleHandler;

/**
 * Indicates the value for a property is the wrong Java object type.
 * <p>
 * This exception shouldn't be possible if the Java objects are declared
 * properly, as the JVM should have caught the ClassCastExceptions much earlier
 * when it tried to stick an incompatible type into the field.
 * </p>
 */
public class ValueWrongTypeException extends ValidationException {
	private static final long serialVersionUID = 1L;

	public ValueWrongTypeException() {
	}
	public ValueWrongTypeException(final MObjectConstraint pc) {
		super(pc);
	}

	public String format() {
		return LocaleHandler.lc_text.errorDesc_WrongType(formatPropertyName());
	}
}
