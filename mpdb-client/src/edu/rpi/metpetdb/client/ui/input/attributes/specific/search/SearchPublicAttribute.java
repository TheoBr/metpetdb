package edu.rpi.metpetdb.client.ui.input.attributes.specific.search;

import java.util.ArrayList;

import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.model.interfaces.MObject;
import edu.rpi.metpetdb.client.model.validation.PropertyConstraint;
import edu.rpi.metpetdb.client.model.validation.primitive.StringConstraint;

public class SearchPublicAttribute extends SearchGenericAttribute implements ClickListener{
	private static final String groupString = "public_attribute";
	private RadioButton getPrivate;
	private RadioButton getPublic;
	private RadioButton getBoth;
	
	public SearchPublicAttribute(final PropertyConstraint sc) {
		super(sc);
	}
	public SearchPublicAttribute(final StringConstraint sc) {
		super(sc);
	}
	public Widget[] createDisplayWidget(final MObject obj){
		return new Widget[]{};
	}
	
	public Widget[] createEditWidget(final MObject obj, final String id){
		final FlowPanel container = new FlowPanel();
		getPrivate = new RadioButton(groupString, "Private");
		getPublic = new RadioButton(groupString, "Public");
		getBoth = new RadioButton(groupString, "No Preference");
		getPrivate.addClickListener(this);
		getPublic.addClickListener(this);
		getBoth.addClickListener(this);
		
		if (get(obj) == 1){
			getPublic.setChecked(true);
		} else if (get(obj) == 2){
			getPrivate.setChecked(true);
		} else {
			getBoth.setChecked(true);
		}
		
		container.add(getBoth);
		container.add(getPublic);
		container.add(getPrivate);

		return new Widget[]{container};
	}
	
	public void onClick(final Widget sender){
		SearchPublicAttribute.this.getSearchInterface().createCritera();
	}
	
	protected void set(final MObject obj, final Object o) {
		mSet(obj, o);
	}
	protected int get(final MObject obj) {
		final Object value = mGet(obj);
		if (value != null)
			return (Integer) value;
		else 
			return 0;
	}

	@Override
	protected Object get(Widget editWidget) throws ValidationException {
		if (getPublic.isChecked())
			return 1;
		else if (getPrivate.isChecked())
			return 2;
		else
			return 0;
	}
	
	public void onClear(){
		getBoth.setChecked(true);
	}
	
	public ArrayList<Widget> getCriteria(){
		final ArrayList<Widget> criteria = new ArrayList<Widget>();
		if (getPublic.isChecked())
			criteria.add(createCritRow("Public Samples Only"));
		else if (getPrivate.isChecked())
			criteria.add(createCritRow("Private Samples Only"));
		return criteria;
	}
}

