package edu.rpi.metpetdb.client.ui.input.attributes;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.model.MObjectDTO;
import edu.rpi.metpetdb.client.model.validation.primitive.StringConstraint;
import edu.rpi.metpetdb.client.ui.widgets.MText;

public class PasswordAttribute extends GenericAttribute {
	public PasswordAttribute(final StringConstraint sc) {
		super(sc);
	}

	public Widget[] createDisplayWidget(final MObjectDTO obj) {
		return new Widget[]{new MText()};
	}

	public Widget[] createEditWidget(final MObjectDTO obj, final String id) {
		final PasswordTextBox b = new PasswordTextBox();
		DOM.setElementAttribute(b.getElement(), "id", id);
		applyStyle(b, true);
		return new Widget[]{b};
	}

	public void commitEdit(final MObjectDTO obj, final Widget editWidget) {
		set(obj, get(editWidget));
	}

	protected Object get(final Widget editWidget) {
		final String v = ((HasText) editWidget).getText();
		return v != null && v.length() > 0 ? v : null;
	}
	protected void set(final MObjectDTO obj, final Object v) {
		mSet(obj, v != null && ((String) v).length() > 0 ? v : null);
	}
}
