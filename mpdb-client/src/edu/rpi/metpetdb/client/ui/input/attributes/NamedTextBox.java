package edu.rpi.metpetdb.client.ui.input.attributes;

import com.google.gwt.user.client.ui.TextBox;

public class NamedTextBox extends TextBox {
	protected String attributeName;

	public NamedTextBox(String attrName) {
		super();
		attributeName = attrName;
	}

	public String getNameOfTextBox() {
		return attributeName;
	}
}
