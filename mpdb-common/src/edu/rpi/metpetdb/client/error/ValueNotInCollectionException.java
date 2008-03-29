package edu.rpi.metpetdb.client.error;

import edu.rpi.metpetdb.client.model.validation.PropertyConstraint;

/**
 * Indicates that the value is not contained in a collection that it is suppose
 * to be in.
 */
public class ValueNotInCollectionException extends ValidationException {
	private static final long serialVersionUID = 1L;

	private String value;

	public ValueNotInCollectionException() {
	}

	public ValueNotInCollectionException(final PropertyConstraint pc) {
		super(pc);
	}

	public ValueNotInCollectionException(final String value) {
		this.value = value == null ? "" : value.toString();
	}

	public String format() {
		return value + " is not in the collection";
	}
}
