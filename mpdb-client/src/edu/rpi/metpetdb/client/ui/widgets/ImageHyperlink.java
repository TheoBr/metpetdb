package edu.rpi.metpetdb.client.ui.widgets;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Image;

public class ImageHyperlink extends MLink {

	private Object obj = null;

	public ImageHyperlink(final Image image, final ClickListener whereTo) {
		super();
		setText("");
		addClickListener(whereTo);
		DOM.appendChild(this.getElement(), image.getElement());
		image.unsinkEvents(Event.ONCLICK | Event.MOUSEEVENTS);

	}

	public ImageHyperlink(final Image image, final String text,
			final boolean after) {
		super();
		if (after) {
			setText(getText() + text + " ");
			DOM.appendChild(this.getElement(), image.getElement());
		} else {
			final Element span = DOM.createSpan();
			DOM.setInnerText(span, " " + text);
			DOM.appendChild(this.getElement(), image.getElement());
			DOM.appendChild(this.getElement(), span);
		}
	}

	public ImageHyperlink(final Image image, final String text,
			final ClickListener whereTo, final boolean after) {
		this(image, text, after);
		addClickListener(whereTo);
	}

	public ImageHyperlink(final Image image, final String text,
			final String targetHistoryToken, final boolean after) {
		this(image, text, after);
		setTargetHistoryToken(targetHistoryToken);
	}

	public ImageHyperlink(final Image image, final ClickListener whereTo,
			final Object o) {
		this(image, whereTo);
		obj = o;
	}
	
	public ImageHyperlink(final Image image, final ClickListener whereTo,
			final Object o, final String text) {
		this(image, text, whereTo, true);
		obj = o;
	}

	public Object getObject() {
		return obj;
	}
}
