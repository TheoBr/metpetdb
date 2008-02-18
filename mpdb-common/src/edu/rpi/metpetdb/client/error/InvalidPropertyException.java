package edu.rpi.metpetdb.client.error;

import edu.rpi.metpetdb.client.model.MObjectDTO;

/** Indicates a bad property constant was given to an {@link MObjectDTO}. */
public class InvalidPropertyException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	public InvalidPropertyException(final int propertyId) {
		super("Property " + propertyId + " not valid.");
	}
}
