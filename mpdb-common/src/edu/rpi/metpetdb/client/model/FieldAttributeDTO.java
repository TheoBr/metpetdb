package edu.rpi.metpetdb.client.model;

import edu.rpi.metpetdb.client.error.PropertyRequiredException;
import edu.rpi.metpetdb.client.model.validation.PropertyConstraint;

public class FieldAttributeDTO extends AttributeDTO {
	private PropertyConstraint constraint;
	private String value;

	/**
	 * FieldAttributeDTO is a searchable Attribute that is required, has a
	 * constraint applicable to it, and has a string value associated with it.
	 */
	public FieldAttributeDTO(PropertyConstraint aConstraint, String aValue) {
		super("FieldAttribute", true);
		constraint = aConstraint;
		value = aValue;
	}

	/**
	 * Validate is called by search, it makes sure that the provided data is
	 * correct and prevents the user from doing anything mischievous (looking
	 * for column Password)
	 */
	public boolean validate() {
		try {
			constraint.validateValue(value);
		} catch (PropertyRequiredException PRE) {
			return true;
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	/**
	 * getColumnName would be the way in which the search finds what column this
	 * search applies to. It would get this value from the constraint's name
	 */
	public String getColumnName(String baseType) {
		return "";
	}
}
