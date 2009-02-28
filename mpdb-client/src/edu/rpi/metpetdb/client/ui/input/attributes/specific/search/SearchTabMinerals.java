package edu.rpi.metpetdb.client.ui.input.attributes.specific.search;

import java.util.ArrayList;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.model.interfaces.MObject;
import edu.rpi.metpetdb.client.ui.MpDb;
import edu.rpi.metpetdb.client.ui.input.attributes.specific.SearchMineralsAttribute;

public class SearchTabMinerals extends SearchTabAttribute{
	private static SearchGenericAttribute[] atts = {new SearchMineralsAttribute(MpDb.doc.SearchSample_minerals)};
	
	public SearchTabMinerals(){
		super(atts, "Minerals");
	}
	
	private ArrayList<Widget[]> currentEditWidgets;
	
	public ArrayList<Widget[]> getCurrentEditWidgets(){
		return currentEditWidgets;
	}
	
	public Widget createEditWidget(final MObject obj, final String id){
		currentEditWidgets = new ArrayList();
		final FlowPanel container = new FlowPanel();
		for (int i = 0; i < atts.length; i++){		
			Widget[] w = (atts[i].createEditWidget(obj, id, atts[i]));
			currentEditWidgets.add(w);
			for (int j = 0; j < w.length; j++){
				container.add(w[j]);
			}
		}
		return container;
	}
}
