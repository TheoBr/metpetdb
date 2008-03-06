package edu.rpi.metpetdb.client.model;

public class AttributeDTO {
	private String type;
	private boolean required;

	/**
	 * The base attribute type. It id's if a property is required or not, and
	 * what kind of property it is. Used by Search to understand what it is
	 * looking for
	 */
	public AttributeDTO(String aType, boolean aRequired) {
		type = aType;
		required = aRequired;
	}

	/** The subclass of this attribute */
	public String getType() {
		return type;
	}

	/** Is this property required in search or is it simply preferable? */
	public boolean isRequired() {
		return required;
	}
}
