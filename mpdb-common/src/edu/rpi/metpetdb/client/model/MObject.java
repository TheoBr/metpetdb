package edu.rpi.metpetdb.client.model;

import net.sf.gilead.pojo.gwt.LightEntity;

import com.google.gwt.user.client.rpc.IsSerializable;

import edu.rpi.metpetdb.client.model.properties.Property;

/**
 * Base class for all data model beans.
 * <p>
 * All data model beans must derive from this class, allowing validation
 * routines to access all relevant properties through the {@link #mGet(int)} and
 * {@link #mSet(int, Object)} reflection-work-a-like methods. This strategy is
 * required as GWT does not support reflection natively and we don't want to
 * bother with writing our own GWT class generator implementations.
 * </p>
 */
public abstract class MObject extends LightEntity implements IsSerializable, edu.rpi.metpetdb.client.model.interfaces.MObject {

	/**
     * 
     */
	private static final long serialVersionUID = 1L;
	/**
	 * Marker to tell {@link #mSetGet(int, Object)} not to set.
	 * <p>
	 * This exact reference is sometimes passed as the <code>newValue</code>
	 * parameter to {@link #mSetGet(int, Object)} to tell it to <b>not</b>
	 * perform the implicit set operation it normally does.
	 * </p>
	 */
	protected static final Object GET_ONLY = new Object();

	/**
	 * Get the value of any supported property.
	 * 
	 * @param propertyId
	 * 		unique identifier of the property, within this concrete class. See
	 * 		the P_* constants in the relevant concrete class for valid values.
	 * @return the value of the property; null if the property value is null and
	 * 	allows nulls (it is not a primitive).
	 */
	public Object mGet(final Property property) {
		return property.get(this);
	}

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
	public void mSet(final Property property, final Object newVal) {
		property.set(this, newVal);
	}

	/**
	 * Is this a new object instance?
	 * 
	 * @return true if this object has not yet been written to the database.
	 */
	public abstract boolean mIsNew();

	protected Integer setIntegerValue(final Object newValue) {
		if (newValue == null)
			return null;
		else
			return new Integer((String) newValue);
	}

	protected Float setFloatValue(final Object newValue) {
		if (newValue == null)
			return null;
		else
			return new Float((String) newValue);
	}

}
