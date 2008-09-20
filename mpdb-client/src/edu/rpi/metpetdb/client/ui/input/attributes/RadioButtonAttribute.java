package edu.rpi.metpetdb.client.ui.input.attributes;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.model.interfaces.MObject;
import edu.rpi.metpetdb.client.model.validation.primitive.BooleanConstraint;
import edu.rpi.metpetdb.client.ui.widgets.MText;

public class RadioButtonAttribute extends GenericAttribute {
	private final Label msg;

	public RadioButtonAttribute(final BooleanConstraint sc) {
		super(sc);
		msg = null;
	}

	public RadioButtonAttribute(final BooleanConstraint sc, final String msg) {
		super(sc);
		this.msg = new Label(msg);
	}

	public Widget[] createDisplayWidget(final MObject obj) {
		return new Widget[] {
			new MText((get(obj)) == true ? "Yes" : "No")
		};
	}

	public Widget[] createEditWidget(final MObject obj, final String id) {
		final FlowPanel fp = new FlowPanel();
		final RadioButton YesButton = new RadioButton("choice", "Yes");
		final RadioButton NoButton = new RadioButton("choice", "No");

		YesButton.addClickListener(new ClickListener() {
			public void onClick(final Widget sender) {
				if (msg != null)
					fp.add(msg);
			}
		});
		NoButton.addClickListener(new ClickListener() {
			public void onClick(final Widget sender) {
				if (msg != null && msg.getParent() == fp)
					fp.remove(msg);
			}
		});
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
		return new Widget[] {
			fp
		};
	}

	public void commitEdit(final MObject obj, final Widget editWidget) {
		set(obj, get(editWidget));
	}

	public Object get(final Widget editWidget) {
		final boolean v = ((RadioButton) (((FlowPanel) editWidget).getWidget(0)))
				.isChecked() ? true : false;
		return new Boolean(v);
	}

	protected boolean get(final MObject obj) {
		if (mGet(obj) == null)
			return false;
		else {
			final boolean v = ((Boolean) (mGet(obj))).booleanValue();
			return v;
		}
	}

	protected void set(final MObject obj, final Object v) {
		mSet(obj, v);
	}
}
