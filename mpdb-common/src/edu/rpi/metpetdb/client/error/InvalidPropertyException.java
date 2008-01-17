package edu.rpi.metpetdb.client.error;

import edu.rpi.metpetdb.client.model.MObject;

/** Indicates a bad property constant was given to an {@link MObject}. */
public class InvalidPropertyException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	public InvalidPropertyException(final int propertyId) {
		super("Property " + propertyId + " not valid.");
	}
}
