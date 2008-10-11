package edu.rpi.metpetdb.client.ui.input.attributes.specific.search;

import java.util.ArrayList;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.model.interfaces.MObject;
import edu.rpi.metpetdb.client.ui.MpDb;
import edu.rpi.metpetdb.client.ui.input.attributes.DateRangeAttribute;
import edu.rpi.metpetdb.client.ui.input.attributes.specific.SearchOwnersAttribute;

public class SearchTabProvenance extends SearchTabAttribute{
	private static SearchGenericAttribute[] atts = {new SearchOwnersAttribute(MpDb.oc.SearchSample_owner),
		new SearchCollectorsAttribute(MpDb.oc.SearchSample_collector),
		new SearchAliasAttribute(MpDb.oc.SearchSample_alias),
		new SearchSesarAttribute(MpDb.oc.SearchSample_sesarNumber),
		new SearchMetamorphicGradeAttribute(MpDb.oc.SearchSample_metamorphicGrades),
		new SearchReferenceAttribute(MpDb.oc.SearchSample_references),
		new DateRangeAttribute(MpDb.oc.SearchSample_collectionDateRange)};
	
	private ArrayList<Widget[]> currentEditWidgets;
	
	public ArrayList<Widget[]> getCurrentEditWidgets(){
		return currentEditWidgets;
	}
	
	public SearchTabProvenance(){
		super(atts, "Provenance");
	}
	
	
	
	public Widget createEditWidget(final MObject obj, final String id){
		currentEditWidgets = new ArrayList();
		final FlowPanel container = new FlowPanel();
		for (int i = 0; i < atts.length; i++){
			Widget[] w = (atts[i].createEditWidget(obj, id, atts[i]));
			currentEditWidgets.add(w);
			container.add(new Label(atts[i].getLabel()));
			for (int j = 0; j < w.length; j++){
				container.add(w[j]);
			}
		}
		return container;
	}
}
