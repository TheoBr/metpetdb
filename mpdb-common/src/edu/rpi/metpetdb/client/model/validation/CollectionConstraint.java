package edu.rpi.metpetdb.client.model.validation;

import java.util.Collection;
import java.util.Iterator;

import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.error.ValueNotInCollectionException;
import edu.rpi.metpetdb.client.model.MObjectDTO;
import edu.rpi.metpetdb.client.model.interfaces.IHasName;

/**
 * The constraint verifies that whatever value the user entered is contained
 * inside of the collection
 * 
 */
public class CollectionConstraint<T extends MObjectDTO> extends
		PropertyConstraint implements MaxLengthConstraint {

	private Collection<T> values;

	public CollectionConstraint() {

	}

	public void setValues(final Collection<T> c) {
		values = c;
	}

	public Collection<T> getValues() {
		return values;
	}

	public void validateValue(Object value) throws ValidationException {
		if (values != null && values.contains(value))
			return;
		else if (value != null)
			throw new ValueNotInCollectionException(value.toString());
		else
			throw new ValueNotInCollectionException("");
	}

	public int getMaxLength() {
		return 30;
	}

	public Object getObjectWithName(final String name) {
		final Iterator<T> itr = values.iterator();
		while (itr.hasNext()) {
			final Object o = itr.next();
			if (o instanceof IHasName) {
				if (((IHasName) o).getName().equals(name))
					return o;
			}
		}
		return name;
	}

}
