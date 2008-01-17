package edu.rpi.metpetdb.client.ui.input.attributes;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.model.MObject;
import edu.rpi.metpetdb.client.model.validation.StringConstraint;
import edu.rpi.metpetdb.client.ui.widgets.MText;

public class PasswordAttribute extends GenericAttribute {
	public PasswordAttribute(final StringConstraint sc) {
		super(sc);
	}

	public Widget[] createDisplayWidget(final MObject obj) {
		return new Widget[]{new MText()};
	}

	public Widget[] createEditWidget(final MObject obj, final String id) {
		final PasswordTextBox b = new PasswordTextBox();
		DOM.setElementAttribute(b.getElement(), "id", id);
		applyStyle(b, true);
		return new Widget[]{b};
	}

	public void commitEdit(final MObject obj, final Widget editWidget) {
		set(obj, get(editWidget));
	}

	protected Object get(final Widget editWidget) {
		final String v = ((HasText) editWidget).getText();
		return v != null && v.length() > 0 ? v : null;
	}
	protected void set(final MObject obj, final Object v) {
		mSet(obj, v != null && ((String) v).length() > 0 ? v : null);
	}
}
