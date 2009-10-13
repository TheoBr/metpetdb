package edu.rpi.metpetdb.client.ui.input.attributes.specific;

import com.google.gwt.dom.client.Document;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.model.interfaces.MObject;
import edu.rpi.metpetdb.client.model.validation.PropertyConstraint;
import edu.rpi.metpetdb.client.model.validation.ValueInCollectionConstraint;
import edu.rpi.metpetdb.client.model.validation.interfaces.MaxLengthConstraint;
import edu.rpi.metpetdb.client.model.validation.primitive.StringConstraint;
import edu.rpi.metpetdb.client.ui.input.attributes.TextAttribute;

public class LoginTextAttribute extends TextAttribute{
	protected String elementId;
	protected TextBox b;
	
	public LoginTextAttribute(final StringConstraint sc, final String elementId){
		super(sc);
		this.elementId = elementId;
	}
	
	public LoginTextAttribute(final PropertyConstraint pc, final String elementId) {
		super(pc);
		this.elementId = elementId;
	}

	
	@Override
	public Object get(final Widget editWidget) {
		final String v = ((HasText) editWidget).getText();
		if (this.getConstraint() instanceof ValueInCollectionConstraint) {
			// Get the real instance of the object instead of the string
			return ((ValueInCollectionConstraint) this.getConstraint())
					.getObjectWithName(v);
		}
		return v != null && v.length() > 0 ? v : null;
	}
	
	@Override
	public Widget[] createEditWidget(final MObject obj, final String id) {
		b = new TextBox();
		/*if (!elementId.equals("")){
			try {
				final TextBox temp = TextBox.wrap(Document.get().getElementById(elementId));
				b.setText(temp.getText());
			} catch (Exception e){
				
			}
		}*/
		b.setVisibleLength(visibleLength);
		if (getConstraint() instanceof MaxLengthConstraint)
			b.setMaxLength(((MaxLengthConstraint) getConstraint())
					.getMaxLength());
		applyStyle(b, true);
		return new Widget[] {
			b
		};
	}
	public String getText(){
		return b.getText();
	}
}
