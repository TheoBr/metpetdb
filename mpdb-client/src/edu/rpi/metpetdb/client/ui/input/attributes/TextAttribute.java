package edu.rpi.metpetdb.client.ui.input.attributes;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.model.interfaces.MObject;
import edu.rpi.metpetdb.client.model.validation.PropertyConstraint;
import edu.rpi.metpetdb.client.model.validation.ValueInCollectionConstraint;
import edu.rpi.metpetdb.client.model.validation.interfaces.MaxLengthConstraint;
import edu.rpi.metpetdb.client.model.validation.primitive.DoubleConstraint;
import edu.rpi.metpetdb.client.model.validation.primitive.IntegerConstraint;
import edu.rpi.metpetdb.client.model.validation.primitive.StringConstraint;
import edu.rpi.metpetdb.client.ui.Util;
import edu.rpi.metpetdb.client.ui.widgets.NumericKeyboardListener;

public class TextAttribute extends GenericAttribute {
	protected int visibleLength;
	protected boolean numeric = false;
	protected boolean integer = false;
	protected boolean negative = false;

	public TextAttribute(final StringConstraint sc) {
		super(sc);
		visibleLength = sc.maxLength < 30 ? sc.maxLength : 30;
	}

	public TextAttribute(final PropertyConstraint pc) {
		super(pc);
		visibleLength = 30;
	}
	
	public TextAttribute(final PropertyConstraint pc, final boolean numeric, final boolean integer, final boolean negative) {
		this(pc);
		this.numeric = numeric;
		this.integer = integer;
		this.negative = negative;
	}

	public int getVisibleLength() {
		return visibleLength;
	}

	public void setVisibleLength(final int length) {
		visibleLength = length;
	}

	public Widget[] createDisplayWidget(final MObject obj) {
		return new Widget[] {
			new HTML(Util.convertForWeb(get(obj)))
		};
	}

	public Widget[] createEditWidget(final MObject obj, final String id) {
		final TextBox b = new TextBox();
		if (numeric) b.addKeyboardListener(new NumericKeyboardListener(integer,negative));

		DOM.setElementAttribute(b.getElement(), "id", id);
		b.setText(get(obj));
		b.setVisibleLength(visibleLength);
		if (getConstraint() instanceof MaxLengthConstraint)
			b.setMaxLength(((MaxLengthConstraint) getConstraint())
					.getMaxLength());
		applyStyle(b, true);
		return new Widget[] {
			b
		};
	}

	protected Object get(final Widget editWidget) {
		final String text = ((HasText) editWidget).getText();
		Object val = text;
		if (text != null && text.length() > 0) {
			if (this.getConstraints()[0] instanceof IntegerConstraint) {
				val = Integer.parseInt(text);
			} else if (this.getConstraints()[0] instanceof DoubleConstraint) {
				val = Double.parseDouble(text);
			} 
		}
		if (this.getConstraint() instanceof ValueInCollectionConstraint) {
			// Get the real instance of the object instead of the string
			return ((ValueInCollectionConstraint) this.getConstraint())
					.getObjectWithName(text);
		}
		return val != null && text.length() > 0 ? val : null;
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
