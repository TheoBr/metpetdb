package edu.rpi.metpetdb.client.model.validation;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.model.interfaces.MObject;
import edu.rpi.metpetdb.client.model.validation.interfaces.HasValues;

public class ObjectConstraint extends PropertyConstraint implements HasValues {

	private PropertyConstraint[] constraints;

	public ObjectConstraint() {

	}

	public ObjectConstraint(final PropertyConstraint[] constraints) {
		this.constraints = constraints;
	}

	public void setConstraints(final PropertyConstraint[] constraints) {
		this.constraints = constraints;
	}

	public PropertyConstraint[] getConstraints() {
		return this.constraints;
	}

	public ValueInCollectionConstraint getValueInCollectionConstraint() {
		for (PropertyConstraint pc : this.constraints) {
			if (pc instanceof ValueInCollectionConstraint)
				return (ValueInCollectionConstraint) pc;
		}
		return null;
	}

	@Override
	public void validateEntity(final MObject obj) throws ValidationException {
		final Object value = obj.mGet(this.property);
		if (value != null) {
			if (value instanceof Collection) {
				final Iterator<?> itr = ((Collection<?>) value).iterator();
				while (itr.hasNext()) {
					DatabaseObjectConstraints.validate((MObject) itr.next(),
							constraints);
				}
			} else
				DatabaseObjectConstraints
						.validate((MObject) value, constraints);
		}
	}

	public Collection<?> getValues() {
		if (getValueInCollectionConstraint() != null)
			return getValueInCollectionConstraint().getValues();
		else
			return new HashSet<Object>();
	}

}
