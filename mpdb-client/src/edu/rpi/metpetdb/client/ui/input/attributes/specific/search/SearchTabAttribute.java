package edu.rpi.metpetdb.client.ui.input.attributes.specific.search;

import java.util.ArrayList;
import java.util.Arrays;

import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.model.interfaces.MObject;
import edu.rpi.metpetdb.client.ui.search.SearchInterface;

public abstract class SearchTabAttribute {
	private final ArrayList<SearchGenericAttribute> atts;
	private final String title;
	
	public SearchTabAttribute(final SearchGenericAttribute[] atts, final String title){
		this.atts = new ArrayList<SearchGenericAttribute>(Arrays.asList(atts));
		this.title = title;
	}
	
	public abstract Widget createEditWidget(final MObject obj, final String id);
	
	public abstract ArrayList<Widget[]> getCurrentEditWidgets();
	
	public ArrayList<Widget> getCriteria(){
		ArrayList<Widget> criteria = new ArrayList<Widget>();
		for (int i = 0; i < atts.size(); i++){
			criteria.addAll(atts.get(i).getCriteria());
		}
		return criteria;
	}
	
	public void onClear(){
		for (int i = 0; i < atts.size(); i++)
			atts.get(i).onClear();
	}
	
	public ArrayList<SearchGenericAttribute> getAttributes(){
		return atts;
	}
	
	public String getTitle(){
		return title;
	}
	
	public void setSearchInterface(final SearchInterface si){
		for (int i = 0; i < atts.size(); i++){
			atts.get(i).setSearchInterface(si);
		}
	}
	
}
