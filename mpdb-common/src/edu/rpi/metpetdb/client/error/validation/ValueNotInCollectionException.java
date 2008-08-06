package edu.rpi.metpetdb.client.error.validation;

import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.locale.LocaleHandler;
import edu.rpi.metpetdb.client.model.validation.PropertyConstraint;

/**
 * Indicates that the value is not contained in a collection that it is suppose
 * to be in.
 */
public class ValueNotInCollectionException extends ValidationException {
	private static final long serialVersionUID = 1L;

	private String value;
	private String collectionName;

	public ValueNotInCollectionException() {
	}

	public ValueNotInCollectionException(final PropertyConstraint pc) {
		super(pc);
	}

	public ValueNotInCollectionException(final String value,
			final String collectionName) {
		this.value = value == null ? "" : value.toString();
		this.collectionName = collectionName == null ? "" : collectionName
				.toString();
	}

	public String format() {
		String collection = LocaleHandler.lc_entity.getString(collectionName);
		return LocaleHandler.lc_text.errorDesc_ValueNotInCollection(value,
				collection);
	}
}
