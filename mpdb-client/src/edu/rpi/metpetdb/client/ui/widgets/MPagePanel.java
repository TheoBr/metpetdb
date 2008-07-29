package edu.rpi.metpetdb.client.ui.widgets;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.FlowPanel;

public class MPagePanel extends FlowPanel {
	private Element headH1 = DOM.createElement("h1");
	private FlowPanel headContainer = new FlowPanel();

	public void addPageHeader() {
		headContainer.setStylePrimaryName("page-header");
		DOM.appendChild(headContainer.getElement(), headH1);
		insert(headContainer, 0);
	}

	public void setPageHeader(String text) {
		DOM.setInnerText(headH1, text);
	}
}
