package edu.rpi.metpetdb.client.model.validation;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.model.interfaces.MObject;
import edu.rpi.metpetdb.client.model.validation.interfaces.HasValues;

public class ObjectConstraint<T extends MObject> extends PropertyConstraint implements HasValues {

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

	public ValueInCollectionConstraint<T> getValueInCollectionConstraint() {
		for (PropertyConstraint pc : this.constraints) {
			if (pc instanceof ValueInCollectionConstraint)
				return (ValueInCollectionConstraint<T>) pc;
		}
		return null;
	}

	public void validateValue(Object value) throws ValidationException {
		super.validateValue(value);
		if (value instanceof Set) {
			final Iterator itr = ((Set) value).iterator();
			while (itr.hasNext()) {
				final Object val = itr.next();
				if (val instanceof MObject) {
					DatabaseObjectConstraints.validate((MObject)val,
							constraints);
				}
			}
		}
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

	public Collection<T> getValues() {
		if (getValueInCollectionConstraint() != null)
			return getValueInCollectionConstraint().getValues();
		else
			return new HashSet<T>();
	}

}
