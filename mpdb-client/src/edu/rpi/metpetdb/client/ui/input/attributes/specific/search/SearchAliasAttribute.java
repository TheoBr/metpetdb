package edu.rpi.metpetdb.client.ui.input.attributes.specific.search;

import java.util.ArrayList;
import java.util.Set;

import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.model.interfaces.MObject;
import edu.rpi.metpetdb.client.model.validation.PropertyConstraint;
import edu.rpi.metpetdb.client.model.validation.primitive.StringConstraint;

public class SearchAliasAttribute extends SearchGenericAttribute{
	private TextBox tb;
	public SearchAliasAttribute(final PropertyConstraint sc) {
		super(sc);
	}
	public SearchAliasAttribute(final StringConstraint sc) {
		super(sc);
	}
	public Widget[] createDisplayWidget(final MObject obj){
		return new Widget[]{};
	}
	
	public Widget[] createEditWidget(final MObject obj, final String id){
		tb = new TextBox();
		tb.addChangeListener(new ChangeListener(){
			public void onChange(final Widget sender){
				SearchAliasAttribute.this.getSearchInterface().createCritera();
			}
		});
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
	
	public void onClear(){
		tb.setText("");	
	}
	
	public ArrayList<Widget> getCriteria(){
		final ArrayList<Widget> criteria = new ArrayList<Widget>();
		if (!tb.getText().equals(""))
			criteria.add(createCritRow("Alias: " + tb.getText()));
		return criteria;
	}
}
