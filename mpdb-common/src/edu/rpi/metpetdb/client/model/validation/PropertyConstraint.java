package edu.rpi.metpetdb.client.model.validation;

import com.google.gwt.user.client.rpc.IsSerializable;

import edu.rpi.metpetdb.client.error.PropertyRequiredException;
import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.model.MObject;

/**
 * Description of the constraints surrounding a single entity property.
 * <p>
 * Typically exposed as part of the singleton {@link DatabaseObjectConstraints}, and
 * derived on the fly from the backend database schema by obtaining its column
 * structure. Instances of this type tell the user interface how to display
 * attributes, and how to verify them.
 * </p>
 */
public class PropertyConstraint implements IsSerializable {
	/** Name of the entity this property is a member of. */
	public String entityName;

	/** Name of this property within the entity. */
	public String propertyName;

	/** Unique id number of this property in the entity. */
	public int propertyId;

	/** Is this property required to contain a non-null value? */
	public boolean required;

	public void validateEntity(final MObject obj) throws ValidationException {
		validateValue(obj.mGet(propertyId));
	}

	public void validateValue(Object value) throws ValidationException {
		if (value == null && required)
			throw new PropertyRequiredException(this);
	}

	public int hashCode() {
		return propertyId;
	}

	public boolean equals(final Object o) {
		if (!(o instanceof PropertyConstraint))
			return false;
		final PropertyConstraint p = (PropertyConstraint) o;
		return entityName.equals(p.entityName) && propertyId == p.propertyId;
	}
}
