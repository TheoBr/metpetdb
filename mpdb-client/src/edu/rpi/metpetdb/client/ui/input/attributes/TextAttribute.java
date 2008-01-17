package edu.rpi.metpetdb.client.ui.input.attributes;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.model.MObject;
import edu.rpi.metpetdb.client.model.validation.CollectionConstraint;
import edu.rpi.metpetdb.client.model.validation.MaxLengthConstraint;
import edu.rpi.metpetdb.client.model.validation.PropertyConstraint;
import edu.rpi.metpetdb.client.model.validation.StringConstraint;
import edu.rpi.metpetdb.client.ui.widgets.MText;

public class TextAttribute extends GenericAttribute {
	protected int visibleLength;

	public TextAttribute(final StringConstraint sc) {
		super(sc);
		visibleLength = sc.maxLength < 30 ? sc.maxLength : 30;
	}
	
	public TextAttribute(final PropertyConstraint pc) {
		super(pc);
		visibleLength = 30;
	}

	public Widget[] createDisplayWidget(final MObject obj) {
		return new Widget[]{new MText(get(obj))};
	}

	public Widget[] createEditWidget(final MObject obj, final String id) {
		final TextBox b = new TextBox();
		DOM.setElementAttribute(b.getElement(), "id", id);
		b.setText(get(obj));
		b.setVisibleLength(visibleLength);
		b.setMaxLength(((MaxLengthConstraint) getConstraint()).getMaxLength());
		applyStyle(b, true);
		return new Widget[]{b};
	}

	protected Object get(final Widget editWidget) {
		final String v = ((HasText) editWidget).getText();
		if (this.getConstraint() instanceof CollectionConstraint) {
			//Get the real instance of the object instead of the string
			return ((CollectionConstraint)this.getConstraint()).getObjectWithName(v);
		}
		return v != null && v.length() > 0 ? v : null;
	}
	protected String get(final MObject obj) {
		final Object value = mGet(obj);
		if (value != null)
			return value.toString();
		else
			return "";
	}
	protected void set(final MObject obj, final Object v) {
		mSet(obj, v);
	}
}
