package edu.rpi.metpetdb.client.ui.input.attributes;

import com.google.gwt.dom.client.Document;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.model.interfaces.MObject;
import edu.rpi.metpetdb.client.model.validation.primitive.StringConstraint;
import edu.rpi.metpetdb.client.ui.widgets.MText;

public class PasswordAttribute extends GenericAttribute {
	protected int visibleLength;
	protected String elementId;
	protected PasswordTextBox b;

	public PasswordAttribute(final StringConstraint sc) {
		super(sc);
		this.elementId = "";
		visibleLength = 30;
	}

	public PasswordAttribute(final StringConstraint sc, final String elementId) {
		super(sc);
		this.elementId = elementId;
		visibleLength = 30;
	}
	
	public Widget[] createDisplayWidget(final MObject obj) {
		return new Widget[] {
			new MText()
		};
	}

	public Widget[] createEditWidget(final MObject obj, final String id) {
		b = new PasswordTextBox();
		if (!elementId.equals("")){
			try {
				final PasswordTextBox temp = PasswordTextBox.wrap(Document.get().getElementById(elementId));
				b.setText(temp.getText());
			} catch (Exception e) {
				
			}
		}
		b.setVisibleLength(visibleLength);
		applyStyle(b, true);
		return new Widget[] {
			b
		};
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
	
	public String getText(){
		return b.getText();
	}
}
