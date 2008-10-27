package edu.rpi.metpetdb.client.ui.input.attributes.specific.search;

import java.util.ArrayList;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.model.SearchSample;
import edu.rpi.metpetdb.client.model.interfaces.MObject;
import edu.rpi.metpetdb.client.ui.CSS;
import edu.rpi.metpetdb.client.ui.widgets.MTabPanel;
import edu.rpi.metpetdb.client.ui.widgets.MTwoColPanel;

public class SearchInterface {
	private SearchTabAttribute[] tabAtts;
	private ArrayList<CritContainer> crits;
	private ArrayList<Widget[]> currentEditWidgets;
	private MTabPanel tabs = new MTabPanel();

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
		crits = new ArrayList<CritContainer>();
		final MTwoColPanel panel = new MTwoColPanel();
		
		for (int i = 0; i < tabAtts.length; i++) {
			final FlowPanel fp = new FlowPanel();
			fp.add(tabAtts[i].createEditWidget(obj, id));
			tabAtts[i].setSearchInterface(this);
			tabs.add(fp,tabAtts[i].getTitle());
			currentEditWidgets.addAll(tabAtts[i].getCurrentEditWidgets());
		}
		tabs.selectTab(0);
		final Widget display = SearchConstraintDisplay();

		panel.getLeftCol().add(tabs);
		panel.setLeftColWidth("70%");
		panel.getRightCol().add(display);
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

	private VerticalPanel vp;
	private final static Label noConstraints = new Label(
			"Set your search criteria by selecting from the categories on the left.");

	public Widget SearchConstraintDisplay() {
		vp = new VerticalPanel();
		vp.setStyleName("criteria");
		final FlexTable ft = new FlexTable();
		final Label header = new Label("Search Criteria");
		final Hyperlink save = new Hyperlink();
		save.setText("save");
		save.addStyleName(CSS.BETA);
		final Hyperlink clear = new Hyperlink();
		clear.setText("clear");
		clear.addClickListener(new ClickListener() {
			public void onClick(Widget sender) {
				clearConstraints();
			}
		});
		final HorizontalPanel actionHolder = new HorizontalPanel();
		actionHolder.add(save);
		actionHolder.add(new Label("|"));
		actionHolder.add(clear);
		ft.setWidget(0, 0, header);
		ft.setWidget(0, 1, actionHolder);
		ft.getFlexCellFormatter().setAlignment(0, 1,
				HasHorizontalAlignment.ALIGN_RIGHT,
				HasVerticalAlignment.ALIGN_MIDDLE);
		ft.setStyleName("titlebar");
		vp.add(ft);
		vp.add(noConstraints);
		return (vp);
	}

	public Widget addConstraint(final Widget constraint) {
		if (noConstraints.getParent() == vp) {
			noConstraints.removeFromParent();
		}
		final FlexTable row = new FlexTable();
		row.setWidget(0, 0, constraint);
		row.setWidth("100%");
		vp.add(row);
		return row;
	}

	public void addConstraints(SearchSample ss) {

	}

	public void clearConstraints() {
		while (vp.getWidgetCount() > 1)
			vp.remove(vp.getWidgetCount() - 1);
		vp.add(noConstraints);
		for(SearchTabAttribute sta:  tabAtts){
			sta.onClear();
		}
	}
	
	private void removeCriteriaForTab(final SearchTabAttribute sta){
		for (int i = 0; i < crits.size(); i++){
			if (crits.get(i).getTabAttribute() == sta){
				for (int j = 0; j < crits.get(i).getCriteria().size(); j++){
					 vp.remove(crits.get(i).getCriteria().get(j).getParent());
				}
				crits.remove(i);
				return;
			}
		}
	}
	
	public void createCritera(){
		removeCriteriaForTab(tabAtts[tabs.getTabBar().getSelectedTab()]);
		ArrayList<Widget> displayedCriteria = new ArrayList<Widget>();
		ArrayList<Widget> criteria = tabAtts[tabs.getTabBar().getSelectedTab()].getCriteria();
		for (int j = 0; j < criteria.size(); j++){
			displayedCriteria.add(addConstraint(criteria.get(j)));
		}
		crits.add(new CritContainer(criteria,tabAtts[tabs.getTabBar().getSelectedTab()]));
	}

	private class CritContainer extends SimplePanel {
		private ArrayList<Widget> displayedCriteria;
		private SearchTabAttribute sta;

		public CritContainer(final ArrayList<Widget> displayedCriteria, final SearchTabAttribute sta) {
			this.sta = sta;
			this.displayedCriteria = displayedCriteria;
		}
	
		public ArrayList<Widget> getCriteria() {
			return displayedCriteria;
		}

		public SearchTabAttribute getTabAttribute() {
			return sta;
		}
	};
}
