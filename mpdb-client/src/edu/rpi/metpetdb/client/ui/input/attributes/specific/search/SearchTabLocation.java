package edu.rpi.metpetdb.client.ui.input.attributes.specific.search;

import java.util.ArrayList;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.model.interfaces.MObject;
import edu.rpi.metpetdb.client.ui.MpDb;
import edu.rpi.metpetdb.client.ui.input.attributes.specific.SearchLocationAttribute;

public class SearchTabLocation extends SearchTabAttribute{
	private static SearchGenericAttribute[] atts = {new SearchLocationAttribute(MpDb.oc.SearchSample_boundingBox),
		new SearchRegionAttribute(MpDb.oc.SearchSample_region),
		new SearchCountriesAttribute(MpDb.oc.SearchSample_country)};
	
	public SearchTabLocation(){
		super(atts, "Location");
	}
	
	private ArrayList<Widget[]> currentEditWidgets;
	
	public ArrayList<Widget[]> getCurrentEditWidgets(){
		return currentEditWidgets;
	}
	
	public Widget createEditWidget(final MObject obj, final String id){
		currentEditWidgets = new ArrayList();
		final FlowPanel container = new FlowPanel();
		for (int i = 0; i < atts.length; i++){
			Widget[] w = (atts[i].createEditWidget(obj, id));
			currentEditWidgets.add(w);
			if (i > 0){
				container.add(new Label(atts[i].getLabel()));
			}
			for (int j = 0; j < w.length; j++){
				container.add(w[j]);
			}
		}
		return container;
	}
}
