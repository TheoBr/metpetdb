package edu.rpi.metpetdb.client.model;

import java.util.Set;


import edu.rpi.metpetdb.client.error.InvalidPropertyException;
import edu.rpi.metpetdb.client.model.interfaces.IHasChildren;
import edu.rpi.metpetdb.client.model.interfaces.IHasName;

public class AttributeDTO 
{
	private String attribute;
	private String value;

	public String getAttribute() {
		return attribute;
	}

	public void setAttribute(final String s) {
		attribute = s;
	}

	public String getValue() {
		return value;
	}

	public void setValue(final String s) {
		value = s;
	}
}
