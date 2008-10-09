package edu.rpi.metpetdb.client.ui.input.attributes.specific.search;

import java.util.ArrayList;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.model.validation.PropertyConstraint;
import edu.rpi.metpetdb.client.ui.input.attributes.GenericAttribute;

public abstract class SearchGenericAttribute extends GenericAttribute{
	private ArrayList<Object> objs;
	public SearchGenericAttribute(final PropertyConstraint[] pcs){
		super(pcs);
	}
	
	public SearchGenericAttribute(final PropertyConstraint pc){
		super(pc);
	}
	
	public abstract ArrayList<Pair> getCriteria();
	
	public abstract void onRemoveCriteria(final Object obj);
	
	public void setObjects(final ArrayList<Object>	objs){
		this.objs = objs;
	}
	
	public ArrayList<Object> getObjects(){
		return objs;
	}
	
	public Widget createCritRow(final String label, final String value) {
		final FlowPanel container = new FlowPanel();
		final Label critLabel = new Label(label);
		critLabel.addStyleName("critlabel");
		critLabel.addStyleName("inline");
		final Label critConstraint = new Label(value);
		critConstraint.addStyleName("inline");
		container.add(critLabel);
		container.add(critConstraint);
		return container;	
	}
	protected class Pair{
		public Widget criteria;
		public Object obj;
		public Pair(final Widget criteria, final Object obj){
			this.obj = obj;
			this.criteria = criteria;
		}
	}
}
