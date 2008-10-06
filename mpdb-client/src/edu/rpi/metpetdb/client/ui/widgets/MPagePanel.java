package edu.rpi.metpetdb.client.ui.widgets;

import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.ui.CSS;

public class MPagePanel extends FlowPanel {
	private final MText title = new MText("","h1");
	private final MText category = new MText("","h4");
	private final HTMLPanel header;
	private final FlowPanel actionList = new FlowPanel();
	
	private static final String HEADER_ID = "page-header";
	private static final String CATEGORY_ID = "page-category";
	private static final String TITLE_ID = "page-title";
	private static final String ACTIONS_ID = "page-actions";
	private static final String ACTION_ITEM = "action-item";
	private static final String DESCRIPTION_ID = "page-desc";
	private static final String NO_CATEGORY = "nocat";

	public MPagePanel() {
		super();
		header = new HTMLPanel("<span id=\""+CATEGORY_ID+"\" class=\""+CSS.HIDE+"\"></span>" +
				"<span id=\""+TITLE_ID+"\" class=\""+CSS.HIDE+"\">" +
				"</span><span id=\""+DESCRIPTION_ID+"\" class=\""+CSS.HIDE+"\">" +
				"</span><span id=\""+ACTIONS_ID+"\" class=\""+CSS.HIDE+"\"></span>");
		header.setStylePrimaryName(HEADER_ID);
		add(header);
		hide(header);
	}
	
	public void setPageTitle(String text) {
		setPageTitle(text, "");
	}

	public void setPageTitle(String text, String cat) {
		header.addAndReplaceElement(title, TITLE_ID);
		title.setText(text);
		header.addAndReplaceElement(category, CATEGORY_ID);
		category.setText(cat);
		if (cat == "" || cat != null) header.addStyleDependentName(NO_CATEGORY);
		else removeStyleDependentName(NO_CATEGORY);
		show(header);
	}
	
	public void setPageDescription(String text) {
		setPageDescription(new Label(text));
	}

	public void setPageDescription(Widget w) {
		w.setStyleName(DESCRIPTION_ID);
		header.addAndReplaceElement(w, DESCRIPTION_ID);
	}
	
	public void setPageActionList() {
		actionList.setStylePrimaryName(ACTIONS_ID);
		header.addAndReplaceElement(actionList, ACTIONS_ID);
	}

	public void addPageActionItem(final MLink lnk) {
		insertActionListItem(lnk, DOM.getChildCount(actionList.getElement()));
	}

	public void insertActionListItem(final MLink lnk, int pos) {
		if (!DOM.isOrHasChild(header.getElement(), actionList
				.getElement()))
			setPageActionList();

		final Element item = DOM.createDiv();
		CSS.setStyleName(item, ACTION_ITEM);
		DOM.appendChild(item, lnk.getElement());
		DOM.insertChild(actionList.getElement(), item, pos);
	}
	
	public void hide(Widget w) {
		CSS.hide(w);
	}
	
	public void show(Widget w) {
		CSS.show(w);
	}

}
