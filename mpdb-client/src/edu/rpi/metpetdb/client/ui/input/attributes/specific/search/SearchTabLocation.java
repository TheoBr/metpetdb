package edu.rpi.metpetdb.client.ui.input.attributes.specific.search;

import java.util.ArrayList;

import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.model.interfaces.MObject;
import edu.rpi.metpetdb.client.ui.CSS;
import edu.rpi.metpetdb.client.ui.MpDb;

public class SearchTabLocation extends SearchTabAttribute{
	private static SearchGenericAttribute[] atts = {new SearchLocationAttribute(MpDb.oc.SearchSample_boundingBox),
		new SearchRegionAttribute(MpDb.oc.SearchSample_region),
		new SearchCountriesAttribute(MpDb.oc.SearchSample_country)};
	
	private final RadioButton coordsRadio = new RadioButton("loctype","Map Coordinates");
	private final RadioButton regionRadio = new RadioButton("loctype","Region");
	private static final String COORDS_ID = "coords";
	private static final String REGION_ID = "region";
	private final HTMLPanel tabOptionsPanel = new HTMLPanel("<ul>\n" +
			"<li>Specify Location by:</li>\n" +
			"<li><span id=\"" + REGION_ID + "\"></span></li>\n" +
			"<li><span id=\"" + COORDS_ID + "\"></span></li>\n" +
			"</ul>");
	private final FlowPanel container = new FlowPanel();
	private final FlowPanel coordsPanel = new FlowPanel();
	private static final String STYLENAME = "search-loc";
	private FlexTable regionTable = new FlexTable();
	private MapWidget map;
	
	public SearchTabLocation(){
		super(atts, "Location");
		container.setStyleName(STYLENAME);
		coordsPanel.setStyleName(STYLENAME + "-coords");
		regionTable.setStyleName(STYLENAME + "-region");
		tabOptionsPanel.addStyleName(CSS.SEARCH_TAB_OPTIONS);
		if (tabOptionsPanel.getElementById(REGION_ID)!=null) 
			tabOptionsPanel.addAndReplaceElement(regionRadio, REGION_ID);
		regionRadio.setChecked(true);
		regionRadio.addClickListener(new ClickListener() {
			public void onClick(Widget sender) {
				CSS.hide(coordsPanel);
				CSS.show(regionTable);
				regionRadio.setFocus(false);
			}
		});
		if (tabOptionsPanel.getElementById(COORDS_ID)!=null) 
			tabOptionsPanel.addAndReplaceElement(coordsRadio, COORDS_ID);
		coordsRadio.addClickListener(new ClickListener() {
			public void onClick(Widget sender) {
				CSS.hide(regionTable);
				CSS.show(coordsPanel);	
				coordsRadio.setFocus(false);
				map.checkResize();
			}
		});
	}
	
	private ArrayList<Widget[]> currentEditWidgets;
	
	public ArrayList<Widget[]> getCurrentEditWidgets(){
		return currentEditWidgets;
	}
	
	public Widget createEditWidget(final MObject obj, final String id){
		container.clear();
		container.add(tabOptionsPanel);
		currentEditWidgets = new ArrayList();
		regionTable.clear();
		for (int i = 0, j=0; i < atts.length; i++){
			Widget[] w = (atts[i].createEditWidget(obj, id));
			currentEditWidgets.add(w);
			
			if (atts[i] instanceof SearchRegionAttribute || 
					atts[i] instanceof SearchCountriesAttribute) {
				
				Label labelWrap = new Label(atts[i].getLabel());
				labelWrap.setStyleName(CSS.SEARCH_LABEL);
				
				FlowPanel inputWrap = new FlowPanel();
				inputWrap.setStyleName(CSS.SEARCH_INPUT);
				for (int k = 0; k < w.length; k++)
					inputWrap.add(w[k]);
				
				regionTable.setWidget(j, 0, labelWrap);
				regionTable.setWidget(j, 1, inputWrap);
				j++;
			} else if (atts[i] instanceof SearchLocationAttribute) {
				map = ((SearchLocationAttribute) atts[i]).getMap();
				for (int k = 0; k < w.length; k++)
					coordsPanel.add(w[k]);
			}
		}
		container.add(coordsPanel);
		CSS.hide(coordsPanel);
		container.add(regionTable);
		return container;
	}
}
