package edu.rpi.metpetdb.client.ui.input.attributes.specific.search;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.model.interfaces.MObject;
import edu.rpi.metpetdb.client.model.validation.PropertyConstraint;
import edu.rpi.metpetdb.client.model.validation.primitive.StringConstraint;
import edu.rpi.metpetdb.client.ui.widgets.MSuggestText;

public class SearchSesarAttribute extends SearchGenericAttribute{
	private TextBox tb;
	public SearchSesarAttribute(final PropertyConstraint sc) {
		super(sc);
	}
	public SearchSesarAttribute(final StringConstraint sc) {
		super(sc);
	}
	public Widget[] createDisplayWidget(final MObject obj){
		return new Widget[]{};
	}
	
	public Widget[] createEditWidget(final MObject obj, final String id){
		tb = new TextBox();
		return new Widget[]{tb};
	}
	protected void set(final MObject obj, final Object o) {
		mSet(obj, o);
	}
	public Set get(MObject obj) {
		return (Set<String>) obj.mGet(this.getConstraint().property);
	}

	@Override
	protected Object get(Widget editWidget) throws ValidationException {
		if (!tb.getText().equals("")) return tb.getText();
		return null;
	}
	public void onRemoveCriteria(final Object obj){
		if (tb == obj) tb.setText("");	
	}
	
	public ArrayList<Pair> getCriteria(){
		final ArrayList<Pair> criteria = new ArrayList<Pair>();
		if (!tb.getText().equals(""))
			criteria.add(new Pair(createCritRow("Sesar Number:", tb.getText()), tb));
		return criteria;
	}
}
