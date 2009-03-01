package edu.rpi.metpetdb.client.model.interfaces;

import edu.rpi.metpetdb.client.model.properties.Property;

public interface MObject {
	
	/**
	 * Get the value of any supported property.
	 * 
	 * @param propertyId
	 * 		unique identifier of the property, within this concrete class. See
	 * 		the P_* constants in the relevant concrete class for valid values.
	 * @return the value of the property; null if the property value is null and
	 * 	allows nulls (it is not a primitive).
	 */
	public Object mGet(final Property<?> property);

	/**
	 * Set the value of any supported property.
	 * 
	 * @param propertyId
	 * 		unique identifier of the property, within this concrete class. See
	 * 		the P_* constants in the relevant concrete class for valid values.
	 * @param newVal
	 * 		the new value to set. Type must match that of the property, and the
	 * 		value must not be null if the property is a primitive type.
	 * @throws ClassCastException
	 * 		the property is of a different type than that of the new value.
	 * @throws NullPointerException
	 * 		the property is a primitive type and cannot accept a null, but the
	 * 		new value was null.
	 */
	public void mSet(final Property<?> property, final Object newVal);

	/**
	 * Is this a new object instance?
	 * 
	 * @return true if this object has not yet been written to the database.
	 */
	public abstract boolean mIsNew();

}
