package edu.rpi.metpetdb.client.ui.input.attributes.specific.search;

import java.util.ArrayList;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.model.SearchSample;
import edu.rpi.metpetdb.client.model.interfaces.MObject;
import edu.rpi.metpetdb.client.ui.CSS;
import edu.rpi.metpetdb.client.ui.widgets.MHtmlList;
import edu.rpi.metpetdb.client.ui.widgets.MTabPanel;
import edu.rpi.metpetdb.client.ui.widgets.MTwoColPanel;

public class SearchInterface {
	private SearchTabAttribute[] tabAtts;
	private ArrayList<Widget[]> currentEditWidgets;
	private MTabPanel tabs = new MTabPanel();
	private static final String CRITERIA_STYLENAME = "criteria-summary";
	private static final String CRITERIA_CONTENT_ID = CRITERIA_STYLENAME + "-content";
	private final MHtmlList searchActions = new MHtmlList();
	
	private FlowPanel critContents = new FlowPanel();
	private final HTMLPanel criteriaSummaryPanel = new HTMLPanel(
			"<div class=\"header-wrap\"><div class=\"header-content\"><div>Criteria Summary</div></div></div>" +
			"<div class=\"content-wrap\"><span id=\""+CRITERIA_CONTENT_ID+"\"></span></div>" +
			"<div class=\"bottom-wrap\"><div class=\"bottom\"></div></div>");
	private final Label noCriteriaMsg = new Label("Set your search criteria by selecting from the categories on the left.");

	public ArrayList<SearchGenericAttribute> getAttributes(){
		ArrayList<SearchGenericAttribute> atts = new ArrayList();
		for (int i = 0; i < tabAtts.length; i++){
			atts.addAll(tabAtts[i].getAttributes());
		}
		return atts;
	}
	
	public ArrayList<Widget[]> getCurrentEditWidgets(){
		return currentEditWidgets;
	}
	
	public SearchInterface(final SearchTabAttribute[] tabAtts) {
		this.tabAtts = tabAtts;
		
	
		
	}

	public Widget[] createDisplayWidget(final MObject obj) {
		return new Widget[] {};
	}

	public Widget[] createEditWidget(final MObject obj, final String id) {
		currentEditWidgets = new ArrayList();
		final MTwoColPanel panel = new MTwoColPanel();
		
		for (int i = 0; i < tabAtts.length; i++) {
			final FlowPanel fp = new FlowPanel();
			fp.add(tabAtts[i].createEditWidget(obj, id));
			tabAtts[i].setSearchInterface(this);
			tabs.add(fp,tabAtts[i].getTitle());
			currentEditWidgets.addAll(tabAtts[i].getCurrentEditWidgets());
		}
		tabs.selectTab(0);

		panel.getLeftCol().add(tabs);
		panel.setLeftColWidth("70%");
		panel.getRightCol().add(SearchConstraintDisplay());
		panel.getRightCol().add(searchActions);
		searchActions.setStyleName("search-actions");
		panel.setRightColWidth("30%");
		return new Widget[] {
			panel
		};
	}

	protected void set(final MObject obj, final Object o) {
	}

	protected Object get(Widget editWidget) throws ValidationException {
		for (int i = 0; i < tabAtts.length; i++) {
		}

		return null;
	}


	public Widget SearchConstraintDisplay() {
		criteriaSummaryPanel.setStyleName(CRITERIA_STYLENAME);
		if (criteriaSummaryPanel.getElementById(CRITERIA_CONTENT_ID) != null)
			criteriaSummaryPanel.addAndReplaceElement(critContents, CRITERIA_CONTENT_ID);
		critContents.setStyleName(CRITERIA_CONTENT_ID);
		critContents.add(noCriteriaMsg);
		noCriteriaMsg.setStyleName(CSS.EMPTY);

		return criteriaSummaryPanel;
	}

	public void addConstraints(SearchSample ss) {

	}

	public void clearConstraints() {
		critContents.clear();
		critContents.add(noCriteriaMsg);
		for(SearchTabAttribute sta:  tabAtts){
			sta.onClear();
		}
	}
	
	private void removeCriteriaForTab(){
		for (int i = 0; i < critContents.getWidgetCount(); i++){
			if (critContents.getWidget(i) instanceof CritContainer) {
				if (((CritContainer)critContents.getWidget(i)).getTabAttribute() == getSelectedTabAtt()){
					critContents.remove(i);
					return;
				}
			}
		}
	}
	
	public void createCritera(){
		removeCriteriaForTab();
		if (!getSelectedTabAtt().getCriteria().isEmpty()) {
			if (noCriteriaMsg.isAttached()) noCriteriaMsg.removeFromParent();
			critContents.add(new CritContainer(getSelectedTabAtt()));
		} else if (critContents.getWidgetCount() == 0)
			critContents.add(noCriteriaMsg);
	}

	private class CritContainer extends FlowPanel {
		private SearchTabAttribute sta;

		public CritContainer(final SearchTabAttribute sta) {
			this.sta = sta;
			setStyleName("section");
			final Label tabTitle = new Label(sta.getTitle());
			tabTitle.setStyleName("section-title");
			tabTitle.addStyleName(CSS.TYPE_SMALL_CAPS);
			add(tabTitle);
			for (int i=0; i<sta.getCriteria().size(); i++)
				add(sta.getCriteria().get(i));
		}
	
		public ArrayList<Widget> getCriteria() {
			return sta.getCriteria();
		}

		public SearchTabAttribute getTabAttribute() {
			return sta;
		}
	};
	
	private SearchTabAttribute getSelectedTabAtt() {
		return tabAtts[tabs.getTabBar().getSelectedTab()];
	}
	
	public void passActionWidget(Widget w) {
		searchActions.add(w);
	}
}
