package edu.rpi.metpetdb.client.model.validation;

import java.util.Collection;
import java.util.Iterator;

import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.model.MObjectDTO;

public class ObjectConstraint extends PropertyConstraint {

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
	public void validateEntity(final MObjectDTO obj) throws ValidationException {
		final Object value = obj.mGet(this.property);
		if (value != null) {
			if (value instanceof Collection) {
				final Iterator<?> itr = ((Collection<?>) value).iterator();
				while (itr.hasNext()) {
					DatabaseObjectConstraints.validate((MObjectDTO) itr.next(),
							constraints);
				}
			} else
				DatabaseObjectConstraints.validate((MObjectDTO) value,
						constraints);
		}
	}

}
