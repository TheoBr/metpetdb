package edu.rpi.metpetdb.client.ui.widgets;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.SimplePanel;

public class MPagePanel extends FlowPanel {
	private Element headH1 = DOM.createElement("h1");
	private FlowPanel headContainer = new FlowPanel();
	private FlowPanel actionList = new FlowPanel();

	public void addPageHeader() {
		headContainer.setStylePrimaryName("page-header");
		DOM.appendChild(headContainer.getElement(), headH1);
		insert(headContainer, 0);
	}

	public void setPageTitle(String text) {
		DOM.setInnerText(headH1, text);
	}

	public void addPageHeaderActionList() {
		actionList.setStylePrimaryName("page-action-list");
		DOM.appendChild(headContainer.getElement(), actionList.getElement());
	}

	public void addActionListItem(final MLink lnk) {
		insertActionListItem(lnk, actionList.getWidgetCount());
	}

	public void insertActionListItem(final MLink lnk, int pos) {
		final SimplePanel li = new SimplePanel();
		li.setStylePrimaryName("action-list-item");
		li.add(lnk);
		actionList.insert(li, pos);
	}

}
