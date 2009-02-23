package edu.rpi.metpetdb.client.ui.input.attributes.specific.search;

import java.util.ArrayList;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.model.interfaces.MObject;
import edu.rpi.metpetdb.client.ui.MpDb;
import edu.rpi.metpetdb.client.ui.input.attributes.specific.SearchChemistryAttribute;

public class SearchTabChemicalAnalysis extends SearchTabAttribute{
	private static SearchGenericAttribute[] atts = {new SearchChemistryAttribute(MpDb.doc.SearchSample_elements,MpDb.doc.SearchSample_oxides)};
	
	public SearchTabChemicalAnalysis(){
		super(atts, "Chemistry"); 
	}
	
	private ArrayList<Widget[]> currentEditWidgets;
	
	public ArrayList<Widget[]> getCurrentEditWidgets(){
		return currentEditWidgets;
	}
	
	public Widget createEditWidget(final MObject obj, final String id){
		currentEditWidgets = new ArrayList();
		final FlowPanel container = new FlowPanel();
		container.setStyleName("search-chem");
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