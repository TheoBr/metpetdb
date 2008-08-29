package edu.rpi.metpetdb.client.error;

import com.google.gwt.user.client.rpc.IsSerializable;

import edu.rpi.metpetdb.client.locale.LocaleHandler;
import edu.rpi.metpetdb.client.model.validation.DatabaseObjectConstraints;
import edu.rpi.metpetdb.client.model.validation.PropertyConstraint;

/**
 * Throw when there is an input error detected.
 * 
 * @see PropertyConstraint
 */
public abstract class ValidationException extends MpDbException implements
		IsSerializable {
	private static final long serialVersionUID = 1L;

	protected static String formatPropertyName(final PropertyConstraint c) {
		final String p = c.propertyName;
		try {
			final String r = LocaleHandler.lc_entity.getString(c.entityName + "_"
					+ p);
			return r != null ? r : p;
		} catch(Exception e){ 
			//TODO handle the exception
		}
		return null;
	}

	protected PropertyConstraint constraint;

	public ValidationException() {
	}
	protected ValidationException(final PropertyConstraint pc) {
		constraint = pc;
	}

	/**
	 * Get the constraint that has failed.
	 * <p>
	 * Note that the returned constraint object is probably not reference equal
	 * with one that is a constant in {@link DatabaseObjectConstraints}. So use
	 * the overriden equals methods to test if two constraints are the same, not
	 * ==.
	 * </p>
	 * 
	 * @return the constraint that has failed.
	 */
	public PropertyConstraint getPropertyConstraint() {
		return constraint;
	}

	/**
	 * Name of the database entity (type of object) that had the error.
	 * 
	 * @return internal name of the database entity with the error. This is the
	 *         class name, without the package, that is the model bean.
	 */
	public String getEntityName() {
		return constraint.entityName;
	}

	/**
	 * Name of the property that had the error.
	 * 
	 * @return internal name of the property of the bean that had the invalid
	 *         value within it. This is the property name as known by the data
	 *         storage code, and matches the getter/setter method names in the
	 *         entity bean class.
	 */
	public String getPropertyName() {
		return constraint.propertyName;
	}

	/**
	 * Format the entity name for end-user display.
	 * <p>
	 * <b><i>Only available on the client side.</i></b>
	 * </p>
	 * <p>
	 * This method requires the localication support supplied by GWT, and that
	 * is only available on the client side of the system. Invoking this method
	 * on the server will throw an internal GWT error.
	 * </p>
	 * 
	 * @return a translated version of {@link #getEntityName()} that is more
	 *         suitable for presentation to an end-user.
	 */
	public String formatEntityName() {
		final String e = getEntityName();
		final String r = LocaleHandler.lc_entity.getString(e);
		return r != null ? r : e;
	}

	/**
	 * Format the property name for end-user display.
	 * <p>
	 * <b><i>Only available on the client side.</i></b>
	 * </p>
	 * <p>
	 * This method requires the localication support supplied by GWT, and that
	 * is only available on the client side of the system. Invoking this method
	 * on the server will throw an internal GWT error.
	 * </p>
	 * 
	 * @return a translated version of {@link #getPropertyName()} that is more
	 *         suitable for presentation to an end-user.
	 */
	public String formatPropertyName() {
		return formatPropertyName(constraint);
	}
}
