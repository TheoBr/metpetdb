package edu.rpi.metpetdb.client.ui.widgets;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.ClickListenerCollection;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.SourcesClickEvents;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.ui.TokenHandler;
import edu.rpi.metpetdb.client.ui.TokenSpace;

public class MLink extends Widget implements HasText, SourcesClickEvents {
	private String targetHistoryToken;
	private ClickListenerCollection clickListeners;
	private boolean usePureHref = false;

	public MLink() {
		setElement(DOM.createAnchor());
		sinkEvents(Event.ONCLICK);
	}
	
	public MLink(final String text, final TokenHandler whereTo) {
		this(text, whereTo.makeToken(null));
	}
	
	public MLink(final String text, final ClickListener whereTo) {
		this();
		setText(text);
		addClickListener(whereTo);
	}
	
	public MLink(final String text, final String targetHistoryToken) {
		this(text, targetHistoryToken, false);
	}
	
	public MLink(final String text, final String href, boolean pureHref) {
		this();
		usePureHref = pureHref;
		setText(text);
		setTargetHistoryToken(href);
	}

	public String getText() {
		return DOM.getInnerText(getElement());
	}
	public void setText(final String text) {
		DOM.setInnerText(getElement(), text);
		if (getTitle() == null || getTitle() == "") setTitle(text);
	}

	public String getTargetHistoryToken() {
		return targetHistoryToken;
	}
	public void setTargetHistoryToken(final String tok) {
		targetHistoryToken = tok;
		String prepend = "#";
		if (usePureHref) prepend = ""; 
		getElement().setAttribute("href", prepend + tok);
	}

	public void addClickListener(final ClickListener listener) {
		if (clickListeners == null)
			clickListeners = new ClickListenerCollection();
		clickListeners.add(listener);
	}
	public void removeClickListener(final ClickListener listener) {
		if (clickListeners != null)
			clickListeners.remove(listener);
	}

	public void onBrowserEvent(Event event) {
		if (DOM.eventGetType(event) == Event.ONCLICK) {
			if (!usePureHref) {
				final String me = getTargetHistoryToken();
				if (clickListeners != null)
					clickListeners.fireClick(this);
				if (me != null) {
					// We are already are at this history token. The
					// browser won't issue a history changed event for
					// this click, but the user clicked and expects us
					// to react. Forcefully refiring the history event
					// gets us going again.
					//
					if (me.equals(History.getToken()))
						TokenSpace.dispatch(me);
					else
						History.newItem(me);
				}
				DOM.eventPreventDefault(event);
			}
		}
	}
}
