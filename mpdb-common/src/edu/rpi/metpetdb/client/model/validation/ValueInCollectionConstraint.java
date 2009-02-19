package edu.rpi.metpetdb.client.model.validation;

import java.util.Collection;
import java.util.Iterator;

import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.error.validation.ValueNotInCollectionException;
import edu.rpi.metpetdb.client.model.interfaces.HasChildren;
import edu.rpi.metpetdb.client.model.interfaces.IHasName;
import edu.rpi.metpetdb.client.model.interfaces.MObject;
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
 * @param < T>
 */
public class ValueInCollectionConstraint<T extends MObject> extends
		PropertyConstraint implements MaxLengthConstraint, HasValues {

	private Collection<T> values;
	private String collectionName;

	public ValueInCollectionConstraint() {

	}

	public void setValues(final Collection<T> c) {
		values = c;
	}

	public Collection<T> getValues() {
		return values;
	}

	public String getCollectionName() {
		return collectionName;
	}

	public void setCollectionName(String collectionName) {
		this.collectionName = collectionName;
	}

	public void validateValue(Object value) throws ValidationException {
		super.validateValue(value);
		if (value != null && value.toString().length() > 0) {
			if (values != null && valueInCollection(value, this.getValues()))
				return;
			else if (value != null)
				throw new ValueNotInCollectionException(this, value.toString(),
						values);
			else
				throw new ValueNotInCollectionException(this, "", values);
		}
	}

	private boolean valuesInCollection(final Collection<?> value,
			final Collection<T> values) throws ValueNotInCollectionException {
		final Iterator<?> itrVal = value.iterator();
		while (itrVal.hasNext()) {
			final Object val = itrVal.next();
			if (!valueInCollection(val, values)) {
				throw new ValueNotInCollectionException(this, val.toString(), values);
			}
		}
		return true;
	}

	private boolean valueInCollection(final Object value,
			final Collection<T> values) throws ValueNotInCollectionException {
		if (values != null) {
			if (value instanceof Collection) {
				return valuesInCollection((Collection<?>) value, values);
			}
			final Iterator<?> itr = values.iterator();
			while (itr.hasNext()) {
				final Object object = itr.next();
				if (object.equals(value))
					return true;
				if (object instanceof HasChildren) {
					if (valueInCollection(value, ((HasChildren<T>) object)
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
