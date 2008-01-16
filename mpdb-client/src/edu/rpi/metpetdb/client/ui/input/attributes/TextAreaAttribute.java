package edu.rpi.metpetdb.client.ui.input.attributes;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.model.MObject;
import edu.rpi.metpetdb.client.model.validation.IntegerConstraint;
import edu.rpi.metpetdb.client.model.validation.StringConstraint;
import edu.rpi.metpetdb.client.ui.widgets.MText;

public class TextAreaAttribute extends GenericAttribute {
	protected int visibleLength;

	public TextAreaAttribute(final StringConstraint sc) {
		super(sc);
		visibleLength = sc.maxLength < 30 ? sc.maxLength : 30;
	}

	public TextAreaAttribute(final IntegerConstraint ic) {
		super(ic);
		visibleLength = 30;
	}

	public Widget[] createDisplayWidget(final MObject obj) {
		return new Widget[]{new MText(get(obj))};
	}

	public Widget[] createEditWidget(final MObject obj, final String id) {
		final TextArea b = new TextArea();
		DOM.setElementAttribute(b.getElement(), "id", id);
		b.setText(get(obj));
		b.setCharacterWidth(25);
		b.setVisibleLines(3);
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
	protected String get(final MObject obj) {
		final String v = (String) mGet(obj);
		return v != null ? v : "";
	}
	protected void set(final MObject obj, final Object v) {
		mSet(obj, v != null && ((String) v).length() > 0 ? v : null);
	}
}