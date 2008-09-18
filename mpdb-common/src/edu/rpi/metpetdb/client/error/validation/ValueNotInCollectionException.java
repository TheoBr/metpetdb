package edu.rpi.metpetdb.client.error.validation;

import java.util.Collection;

import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.locale.LocaleHandler;
import edu.rpi.metpetdb.client.model.MObjectDTO;
import edu.rpi.metpetdb.client.model.validation.PropertyConstraint;

/**
 * Indicates that the value is not contained in a collection that it is suppose
 * to be in.
 */
public class ValueNotInCollectionException extends ValidationException {
	private static final long serialVersionUID = 1L;

	private String value;
	private Collection<? extends MObjectDTO> collection;

	public ValueNotInCollectionException() {
	}

	public ValueNotInCollectionException(final PropertyConstraint pc) {
		super(pc);
	}

	public ValueNotInCollectionException(final String value,
			final Collection<? extends MObjectDTO> collection) {
		this.value = value == null ? "" : value.toString();
		this.collection = collection;
	}

	public String format() {
		String collectionItems = collection.toString();
		return LocaleHandler.lc_text.errorDesc_ValueNotInCollection(value,
				collectionItems);
	}
}
