package edu.rpi.metpetdb.client.ui.input.attributes.specific.search;

import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.model.validation.PropertyConstraint;
import edu.rpi.metpetdb.client.model.validation.primitive.StringConstraint;
import edu.rpi.metpetdb.client.ui.input.attributes.TextAttribute;

public abstract class SearchTextAttribute extends TextAttribute implements
		ClickListener {
	public SearchTextAttribute(final PropertyConstraint pc) {
		super(pc);
	}
	public SearchTextAttribute(final StringConstraint sc) {
		super(sc);
	}

	public void onClick(final Widget sender) {
		onSet(new Label(this.getLabel().toString()));
	}

	public abstract void onSet(final Widget constraint);
}
