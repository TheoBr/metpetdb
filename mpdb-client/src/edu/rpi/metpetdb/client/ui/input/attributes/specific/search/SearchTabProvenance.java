package edu.rpi.metpetdb.client.ui.input.attributes.specific.search;

import java.util.ArrayList;

import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.model.interfaces.MObject;
import edu.rpi.metpetdb.client.ui.CSS;
import edu.rpi.metpetdb.client.ui.MpDb;
import edu.rpi.metpetdb.client.ui.input.attributes.DateRangeAttribute;
import edu.rpi.metpetdb.client.ui.input.attributes.specific.SearchOwnersAttribute;

public class SearchTabProvenance extends SearchTabAttribute{
	private static SearchGenericAttribute[] atts = {new SearchOwnersAttribute(MpDb.oc.SearchSample_owner),
		new SearchCollectorsAttribute(MpDb.oc.SearchSample_collector),
		new SearchAliasAttribute(MpDb.oc.SearchSample_alias),
		new SearchSesarAttribute(MpDb.oc.SearchSample_sesarNumber),
		new SearchReferenceAttribute(MpDb.oc.SearchSample_references),
		new DateRangeAttribute(MpDb.oc.SearchSample_collectionDateRange)};
	
	private ArrayList<Widget[]> currentEditWidgets;
	private FlexTable table = new FlexTable();
	
	public ArrayList<Widget[]> getCurrentEditWidgets(){
		return currentEditWidgets;
	}
	
	public SearchTabProvenance(){
		super(atts, "Provenance");
		table.setStyleName(CSS.SEARCH_PROVENANCE);
	}
	
	public Widget createEditWidget(final MObject obj, final String id){
		currentEditWidgets = new ArrayList();
		table.clear();
		for (int i = 0; i < atts.length; i++){
			Widget[] w = (atts[i].createEditWidget(obj, id, atts[i]));
			currentEditWidgets.add(w);
			
			Label labelWrap = new Label(atts[i].getLabel());
			labelWrap.setStyleName(CSS.SEARCH_LABEL);
			
			FlowPanel inputWrap = new FlowPanel();
			inputWrap.setStyleName(CSS.SEARCH_INPUT);
			for (int j = 0; j < w.length; j++){
				inputWrap.add(w[j]);
			}
			
			table.setWidget(i, 0, labelWrap);
			table.setWidget(i, 1, inputWrap);
		}
		return table;
	}
}
