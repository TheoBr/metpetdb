package edu.rpi.metpetdb.client.ui.widgets;

import com.google.gwt.user.client.ui.CheckBox;

public class MCheckBox extends CheckBox {
	private Object value;

	public MCheckBox() {
		super();
	}

	public MCheckBox(final Object value) {
		super();
		this.value = value;
	}

	public void setValue(final Object value) {
		this.value = value;
	}

	public Object getValue() {
		return value;
	}
}
