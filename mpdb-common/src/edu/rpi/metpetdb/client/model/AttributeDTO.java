package edu.rpi.metpetdb.client.model;

public class AttributeDTO 
{
	private String attribute;
	private String value;

	public AttributeDTO(String aAttribute, String aValue)
	{
		attribute = aAttribute;
		value = aValue;
	}
	
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
