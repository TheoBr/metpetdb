package edu.rpi.metpetdb.client.ui.input.attributes.specific.search;

import java.util.ArrayList;
import java.util.Map;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.model.validation.PropertyConstraint;
import edu.rpi.metpetdb.client.ui.input.attributes.GenericAttribute;

public abstract class SearchGenericAttribute extends GenericAttribute{
	private ArrayList<Object> objs;
	private SearchInterface si;
	public SearchGenericAttribute(final PropertyConstraint[] pcs){
		super(pcs);
	}
	
	public SearchGenericAttribute(final PropertyConstraint pc){
		super(pc);
	}
	
	public abstract ArrayList<Widget> getCriteria();
	
	public abstract void onClear();
	
	public void setObjects(final ArrayList<Object>	objs){
		this.objs = objs;
	}
	
	public ArrayList<Object> getObjects(){
		return objs;
	}
	
	public Widget createCritRow(final String label) {
		final FlowPanel container = new FlowPanel();
		final Label critLabel = new Label(label);
		container.add(critLabel);
		return container;	
	}
	
	public Widget createCritRow(final Widget label) {
		final FlowPanel container = new FlowPanel();
		container.add(label);
		return container;	
	}
	
	public void setSearchInterface(final SearchInterface si){
		this.si = si;
	}
	
	public SearchInterface getSearchInterface(){
		return si;
	}
}
