package edu.rpi.metpetdb.client.ui.widgets;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

public class LeftColWidget extends SimplePanel {

	private final FlowPanel item;

	public LeftColWidget(final String headerText) {
		// super();

		item = new FlowPanel();

		this.setWidget(item);

		final Element header = DOM.createElement("h1");
		DOM.setElementAttribute(header, "class", "header");
		final MLink headerLink = new MLink(headerText, new ClickListener() {
			public void onClick(final Widget sender) {

			}
		});
		DOM.appendChild(header, headerLink.getElement());
		DOM.appendChild(item.getElement(), header);

	}

	public void add(final Widget w) {
		item.add(w);
	}

}
