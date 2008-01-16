package edu.rpi.metpetdb.client.ui.input;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Image;

import edu.rpi.metpetdb.client.ui.Styles;

public class CurrentError extends FlowPanel implements HasText {
	static {
		Image.prefetch(Styles.ICON_WARNING);
	}

	private final Element text;

	CurrentError() {
		setStyleName(Styles.INVALID_FIELD_ERR);
		setVisible(false);

		final Element icon = DOM.createImg();
		DOM.setElementAttribute(icon, "src", Styles.ICON_WARNING);
		DOM.appendChild(getElement(), icon);

		text = DOM.createSpan();
		DOM.appendChild(getElement(), text);
	}

	public String getText() {
		return DOM.getInnerText(text);
	}

	public void setText(final String msg) {
		if (msg == null || msg.length() == 0) {
			setVisible(false);
		} else {
			DOM.setInnerText(text, " " + msg);
			setVisible(true);
		}
	}
}
