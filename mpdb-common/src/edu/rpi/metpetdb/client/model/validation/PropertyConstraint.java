package edu.rpi.metpetdb.client.model.validation;

import com.google.gwt.user.client.rpc.IsSerializable;

import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.error.validation.PropertyRequiredException;
import edu.rpi.metpetdb.client.model.interfaces.MObject;
import edu.rpi.metpetdb.client.model.properties.Property;

/**
 * Description of the constraints surrounding a single entity property.
 * <p>
 * Typically exposed as part of the singleton {@link DatabaseObjectConstraints},
 * and derived on the fly from the backend database schema by obtaining its
 * column structure. Instances of this type tell the user interface how to
 * display attributes, and how to verify them.
 * </p>
 */
public class PropertyConstraint implements IsSerializable {
	/** Name of the entity this property is a member of. */
	public String entityName;

	/** Name of this property within the entity. */
	public String propertyName;

	/** Unique property in the entity. */
	public Property property;

	/** Is this property required to contain a non-null value? */
	public boolean required;
	
	/** Is this propery a hibernate formula? */
	public boolean formula;
	
	/** if this property is lazy or not, if it is we don't validate it */
	public boolean lazy;

	public void validateEntity(final MObject obj) throws ValidationException {
		if (!formula)
			validateValue(obj.mGet(property));
	}

	public void validateValue(Object value) throws ValidationException {
		if (value == null && required)
			throw new PropertyRequiredException(this);
	}

	public int hashCode() {
		return property.name().hashCode();
	}

	public boolean equals(final Object o) {
		if (!(o instanceof PropertyConstraint))
			return false;
		final PropertyConstraint p = (PropertyConstraint) o;	
		return entityName.equals(p.entityName) && property.equals(p.property);
	}
}
