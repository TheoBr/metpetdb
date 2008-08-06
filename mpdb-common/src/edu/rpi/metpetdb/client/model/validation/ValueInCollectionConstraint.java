package edu.rpi.metpetdb.client.model.validation;

import java.util.Collection;
import java.util.Iterator;

import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.error.validation.ValueNotInCollectionException;
import edu.rpi.metpetdb.client.model.MObjectDTO;
import edu.rpi.metpetdb.client.model.interfaces.HasChildren;
import edu.rpi.metpetdb.client.model.interfaces.IHasName;
import edu.rpi.metpetdb.client.model.validation.interfaces.HasValues;
import edu.rpi.metpetdb.client.model.validation.interfaces.MaxLengthConstraint;

/**
 * Verifies whether the entered/selected value is part of a certain collection.
 * This should be used when the object has only one instance of the value, i.e.
 * for a Sample the user can choose from many rock types (collection) but
 * ultimately can select only one rock type.
 * 
 * @author anthony
 * 
 * @param <T>
 */
public class ValueInCollectionConstraint extends PropertyConstraint implements
		MaxLengthConstraint, HasValues {

	private Collection<? extends MObjectDTO> values;
	private String collectionName;

	public ValueInCollectionConstraint() {

	}

	public void setValues(final Collection<? extends MObjectDTO> c) {
		values = c;
	}

	public Collection<?> getValues() {
		return values;
	}

	public String getCollectionName() {
		return collectionName;
	}

	public void setCollectionName(String collectionName) {
		this.collectionName = collectionName;
	}

	public void validateValue(Object value) throws ValidationException {
		if (value == null && this.required)
			return;
		if (values != null && valueInCollection(value, this.getValues()))
			return;
		else if (value != null)
			throw new ValueNotInCollectionException(value.toString(),
					collectionName);
		else
			throw new ValueNotInCollectionException("", collectionName);
	}

	public boolean valueInCollection(final Object value,
			final Collection<?> values) {
		if (values != null) {
			final Iterator<?> itr = values.iterator();
			while (itr.hasNext()) {
				final Object object = itr.next();
				if (object.equals(value))
					return true;
				if (object instanceof HasChildren) {
					if (valueInCollection(value, ((HasChildren<?>) object)
							.getChildren()))
						return true;
				}
			}
			return false;
		} else {
			return false;
		}
	}
	public int getMaxLength() {
		return 30;
	}

	public Object getObjectWithName(final String name) {
		final Iterator<?> itr = values.iterator();
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
