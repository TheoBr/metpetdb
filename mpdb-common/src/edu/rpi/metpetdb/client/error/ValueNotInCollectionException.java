package edu.rpi.metpetdb.client.error;

import java.util.Collection;
import java.util.HashSet;

import edu.rpi.metpetdb.client.model.MObjectDTO;
import edu.rpi.metpetdb.client.model.validation.PropertyConstraint;

/**
 * Indicates that the value is not contained in a collection that it is suppose
 * to be in.
 */
public class ValueNotInCollectionException extends ValidationException {
	private static final long serialVersionUID = 1L;
	
	private Collection<MObjectDTO> c;
	private String value;

	public ValueNotInCollectionException() {
	}
	public ValueNotInCollectionException(final PropertyConstraint pc) {
		super(pc);
	}
	
	public ValueNotInCollectionException(final String value, final Collection<MObjectDTO> c) {
		this.c = c;
		this.value = value == null ? "" : value.toString();
		if (this.c == null)
			this.c = new HashSet<MObjectDTO>();
	}

	public String format() {
		return value + " is not in the collection " + c.toString();
	}
}
