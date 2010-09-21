package edu.rpi.metpetdb.client.ui.input.attributes.specific.sample;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.model.interfaces.MObject;
import edu.rpi.metpetdb.client.model.validation.PropertyConstraint;
import edu.rpi.metpetdb.client.model.validation.interfaces.MaxLengthConstraint;
import edu.rpi.metpetdb.client.ui.Util;
import edu.rpi.metpetdb.client.ui.input.attributes.TextAttribute;
import edu.rpi.metpetdb.client.ui.widgets.NumericKeyboardListener;

/**
 * Alternate TextAttribute that displays a string representing the 
 * units the field's value is in.
 * 
 * @author Brenden
 *
 */
public class UnitTextAttribute extends TextAttribute {

	protected String unitString;
	
	public UnitTextAttribute(PropertyConstraint pc, String units) {
		super(pc, true, false, false);
		unitString = units;
	}
	
	public Widget[] createDisplayWidget(final MObject obj) {
		String value  = Util.convertForWeb(get(obj));
		if (!value.equals("")) {
			value += unitString;
		}		
		return new Widget[] { 
			new HTML(value)
		};
	}

	public Widget[] createEditWidget(final MObject obj, final String id) {
		final FlowPanel fp = new FlowPanel();
		final TextBox b = new TextBox();
		if (numeric)
			b.addKeyboardListener(new NumericKeyboardListener(integer, negative));

		DOM.setElementAttribute(b.getElement(), "id", id);
		b.setText(get(obj));
		b.setVisibleLength(visibleLength);
		if (getConstraint() instanceof MaxLengthConstraint)
			b.setMaxLength(((MaxLengthConstraint) getConstraint())
					.getMaxLength());
		applyStyle(b, true);
		fp.add(b);
		fp.add(new Label("(" + unitString + ")"));
		return new Widget[] {
			fp
		};
	}

}
