package edu.rpi.metpetdb.client.ui.widgets;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.SimplePanel;

public class MPagePanel extends FlowPanel {
	private final Element headH1 = DOM.createElement("h1");
	private final FlowPanel headContainer = new FlowPanel();
	private final FlowPanel actionList = new FlowPanel();

	protected void addPageHeader() {
		headContainer.setStylePrimaryName("page-header");
		DOM.appendChild(headContainer.getElement(), headH1);
		insert(headContainer, 0);
	}

	public void setPageTitle(String text) {
		DOM.setInnerText(headH1, text);
	}

	public void addPageHeaderActionList() {
		actionList.setStylePrimaryName("page-action-list");
		headContainer.add(actionList);
	}

	public void addActionListItem(final MLink lnk) {
		insertActionListItem(lnk, actionList.getWidgetCount());
	}

	public void insertActionListItem(final MLink lnk, int pos) {
		if (!DOM.isOrHasChild(headContainer.getElement(), actionList
				.getElement()))
			addPageHeaderActionList();

		final SimplePanel li = new SimplePanel();
		li.setStylePrimaryName("action-list-item");
		li.add(lnk);
		actionList.insert(li, pos);
	}

}
