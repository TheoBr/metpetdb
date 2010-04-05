package edu.rpi.metpetdb.client.ui.widgets;

import com.google.gwt.user.client.ui.CheckBox;

import edu.rpi.metpetdb.client.ui.CSS;

public class MCheckBox extends CheckBox {
	private Object value;

	public MCheckBox() {
		super();
		setStylePrimaryName(CSS.CHECKBOX);
	}
	
	public MCheckBox(String label) {
		super(label);
		setStylePrimaryName(CSS.CHECKBOX);
	}
	
	public MCheckBox(String label, boolean asHTML) {
		super(label, asHTML);
		setStylePrimaryName(CSS.CHECKBOX);
	}
	
	public MCheckBox(final Object value) {
		this();
		this.value = value;
	}
	
	public void applyCheckedStyle(boolean checked) {
		if (checked) addStyleDependentName(CSS.CHECKED);
	    else removeStyleDependentName(CSS.CHECKED);
	}
	
	public void setChecked(boolean checked) {
	    super.setChecked(checked);
	    applyCheckedStyle(checked);
	}

	public void setObjectValue(final Object value) {
		this.value = value;
	}

	public Object getObjectValue() {
		return value;
	}
}
