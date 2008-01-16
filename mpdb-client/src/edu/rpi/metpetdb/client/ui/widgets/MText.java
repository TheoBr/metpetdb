package edu.rpi.metpetdb.client.ui.widgets;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Widget;

public class MText extends Widget implements HasText {
	public MText() {
		this(null);
	}
	public MText(final String text) {
		this(text, "span");
	}
	public MText(final String text, final String elementName) {
		setElement(DOM.createElement(elementName));
		setText(text);
	}

	public String getText() {
		return DOM.getInnerText(getElement());
	}

	public void setText(final String text) {
		DOM.setInnerText(getElement(), text != null ? text : "");
	}
}
