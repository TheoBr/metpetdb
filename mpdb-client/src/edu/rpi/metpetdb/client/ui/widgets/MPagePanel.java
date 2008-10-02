package edu.rpi.metpetdb.client.ui.widgets;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.ui.CSS;

public class MPagePanel extends FlowPanel {
	private final MText header = new MText("","h1");
	private final MText category = new MText("","h4");
	private final FlowPanel headContainer = new FlowPanel();
	private final FlowPanel actionList = new FlowPanel();

	protected void addPageHeader() {
		headContainer.setStylePrimaryName("page-header");
		headContainer.add(category);
		hide(category);
		headContainer.add(header);
		insert(headContainer, 0);
	}
	
	public void setPageTitle(String text) {
		setPageTitle(text, "");
	}

	public void setPageTitle(String title, String cat) {
		if (!DOM.isOrHasChild(getElement(), headContainer.getElement()))
			addPageHeader();
		header.setText(title);
		category.setText(cat);
		if (cat != "" && cat != null) show(category);
	}
	
	public void addPageHeaderActionList() {
		actionList.setStylePrimaryName("page-action-list");
		headContainer.add(actionList);
	}

	public void addPageHeaderListItem(final MLink lnk) {
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
	
	public void hide(Widget w) {
		w.addStyleName(CSS.HIDE);
	}
	
	public void show(Widget w) {
		w.removeStyleName(CSS.HIDE);
	}

}
