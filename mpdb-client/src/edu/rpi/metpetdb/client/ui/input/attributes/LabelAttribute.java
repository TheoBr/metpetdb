package edu.rpi.metpetdb.client.ui.input.attributes;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.model.interfaces.MObject;
import edu.rpi.metpetdb.client.model.validation.primitive.StringConstraint;

public class LabelAttribute extends TextAttribute {

	public LabelAttribute(String labelString) {
		super(labelString);
		// TODO Auto-generated constructor stub
	}
	
	/*@Override
	public Widget[] createEditWidget(final MObject obj, final String id) {
		Label b = new Label();
		
		b.set
		DOM.setElementAttribute(b.getElement(), "id", id);
		b.setText(get(obj));
		
		applyStyle(b, true);
		return new Widget[] {
			b
		};
	}*/

}
