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
	private final MTwoColPanel mainPanel = new MTwoColPanel();
	
	private static final String HEADER_ID = "page-header";
	private static final String CATEGORY_ID = "page-category";
	private static final String TITLE_ID = "page-title";
	private static final String ACTIONS_ID = "page-actions";
	private static final String ACTION_ITEM = "action-item";
	private static final String DESCRIPTION_ID = "page-desc";
	private static final String NO_CATEGORY = "nocat";
	
	public FlowPanel sidebar;

	public MPagePanel(boolean hasSidebar) {
		super();
		header = new HTMLPanel("<span id=\""+CATEGORY_ID+"\" class=\""+CSS.HIDE+"\"></span>" +
				"<span id=\""+TITLE_ID+"\" class=\""+CSS.HIDE+"\">" +
				"</span><span id=\""+DESCRIPTION_ID+"\" class=\""+CSS.HIDE+"\">" +
				"</span><span id=\""+ACTIONS_ID+"\" class=\""+CSS.HIDE+"\"></span>");
		header.setStylePrimaryName(HEADER_ID);
		super.add(header);
		hide(header);
		super.add(mainPanel);
		mainPanel.setStylePrimaryName("page-panel");
		if (hasSidebar) mainPanel.addStyleDependentName("twocol");
		mainPanel.setRightColStyle("page-sidebar");
		mainPanel.setLeftColStyle("page-content");
		sidebar = mainPanel.getRightCol();
	}
	
	public MPagePanel() {
		this(false);
	}
	
	public void setPageTitle(String text) {
		setPageTitle(text, "");
	}

	public void setPageTitle(String text, String cat) {
		if (header.getElementById(TITLE_ID) != null) header.addAndReplaceElement(title, TITLE_ID);
		title.getElement().setInnerHTML(text);
		
		if (header.getElementById(CATEGORY_ID) != null) header.addAndReplaceElement(category, CATEGORY_ID);
		category.getElement().setInnerHTML(cat);
		
		if (cat == "" || cat == null) header.addStyleDependentName(NO_CATEGORY);
		else removeStyleDependentName(NO_CATEGORY);
		show(header);
	}
	
	public void setPageDescription(String text) {
		setPageDescription(new Label(text));
	}

	public void setPageDescription(Widget w) {
		w.setStyleName(DESCRIPTION_ID);
		if (header.getElementById(DESCRIPTION_ID) != null) header.addAndReplaceElement(w, DESCRIPTION_ID);
	}
	
	public void setPageActionList() {
		actionList.setStylePrimaryName(ACTIONS_ID);
		if (header.getElementById(ACTIONS_ID) != null) header.addAndReplaceElement(actionList, ACTIONS_ID);
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
	
	// FlowPanel Overrides
	
	public void add(Widget w) {
		insert(w, mainPanel.getLeftCol().getWidgetCount());
	}
	
	public void insert(Widget w, int pos) {
		mainPanel.getLeftCol().insert(w, pos);
	}
	
	public boolean remove(int i) {
		return mainPanel.getLeftCol().remove(i);
	}

	public boolean remove(Widget w) {
		return mainPanel.getLeftCol().remove(w);
	}
}
