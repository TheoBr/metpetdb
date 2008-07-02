package edu.rpi.metpetdb.client.ui.widgets;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.ClickListenerCollection;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.SourcesClickEvents;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.ui.TokenHandler;
import edu.rpi.metpetdb.client.ui.TokenSpace;

public class MLinkandText extends Widget implements SourcesClickEvents {
	private String targetHistoryToken;
	private ClickListenerCollection clickListeners;
	private HTML preText;
	private HTML postText;
	private MLink link;

	public MLinkandText(final String preText, final String text,
			final String postText) {
		this();
		setPreText(preText);
		setPostText(postText);
		link.setText(text);
	}

	public MLinkandText(final String preText, final String text,
			final String postText, final TokenHandler whereTo) {
		this(preText, text, postText, whereTo.makeToken(null));
	}

	public MLinkandText(final String preText, final String text,
			final String postText, final ClickListener whereTo) {
		this(preText, text, postText);
		addClickListener(whereTo);
	}

	public MLinkandText(final String preText, final String text,
			final String postText, final String targetHistoryToken) {
		this(preText, text, postText);
		setTargetHistoryToken(targetHistoryToken);
	}

	public MLinkandText() {
		preText = new HTML();
		postText = new HTML();
		link = new MLink();
		final Element table = DOM.createTable();
		final Element tbody = DOM.createTBody();
		DOM.appendChild(table, tbody);
		final Element tr = DOM.createTR();
		final Element labelTD1 = DOM.createTD();
		final Element labelTD2 = DOM.createTD();
		final Element labelTD3 = DOM.createTD();
		DOM.appendChild(tbody, tr);
		DOM.appendChild(tr, labelTD1);
		DOM.appendChild(tr, labelTD2);
		DOM.appendChild(tr, labelTD3);
		DOM.appendChild(labelTD1, this.preText.getElement());
		DOM.appendChild(labelTD2, this.link.getElement());
		DOM.appendChild(labelTD3, this.postText.getElement());
		setElement(table);
		sinkEvents(Event.ONCLICK);
	}

	public String getPreText() {
		return DOM.getInnerText(preText.getElement());
	}
	public void setPreText(final String text) {
		if (!text.equals("") && text.charAt(text.length() - 1) == ' ')
			preText.setHTML(text + "&nbsp;");
		else
			preText.setHTML(text);

	}
	public String getPostText() {
		return DOM.getInnerText(postText.getElement());
	}
	public void setPostText(final String text) {
		if (!text.equals("") && text.charAt(0) == ' ')
			postText.setHTML("&nbsp;" + text);
		else
			postText.setHTML(text);
	}
	public String getLinkText() {
		return DOM.getInnerText(link.getElement());
	}
	public void setLinkText(final String text) {
		DOM.setInnerText(link.getElement(), text);
	}

	public String getTargetHistoryToken() {
		return targetHistoryToken;
	}
	public void setTargetHistoryToken(final String tok) {
		targetHistoryToken = tok;
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
