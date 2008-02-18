package edu.rpi.metpetdb.client.ui.input.attributes;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.model.MObjectDTO;
import edu.rpi.metpetdb.client.model.validation.BooleanConstraint;
import edu.rpi.metpetdb.client.ui.widgets.MText;

public class RadioButtonAttribute extends GenericAttribute {
	public RadioButtonAttribute(final BooleanConstraint sc) {
		super(sc);
	}

	public Widget[] createDisplayWidget(final MObjectDTO obj) {
		return new Widget[]{new MText((get(obj)) == true ? "Yes" : "No")};
	}

	public Widget[] createEditWidget(final MObjectDTO obj, final String id) {
		final FlowPanel fp = new FlowPanel();
		final RadioButton YesButton = new RadioButton("choice", "Yes");
		final RadioButton NoButton = new RadioButton("choice", "No");

		// Set default value
		boolean radioValue = get(obj);
		if (radioValue)
			YesButton.setChecked(true);
		else
			NoButton.setChecked(true);

		fp.add(YesButton);
		fp.add(NoButton);

		DOM.setElementAttribute(YesButton.getElement(), "id", id);
		DOM.setElementAttribute(NoButton.getElement(), "id", id);

		applyStyle(fp, true);
		return new Widget[]{fp};
	}

	public void commitEdit(final MObjectDTO obj, final Widget editWidget) {
		set(obj, get(editWidget));
	}

	public Object get(final Widget editWidget) {
		final boolean v = ((RadioButton) (((FlowPanel) editWidget).getWidget(0)))
				.isChecked() ? true : false;
		return new Boolean(v);
	}

	protected boolean get(final MObjectDTO obj) {
		if (mGet(obj) == null)
			return false;
		else {
			final boolean v = ((Boolean) (mGet(obj))).booleanValue();
			return v;
		}
	}

	protected void set(final MObjectDTO obj, final Object v) {
		mSet(obj, v);
	}
}