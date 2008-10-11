package edu.rpi.metpetdb.client.ui.input.attributes.specific.search;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.model.interfaces.MObject;
import edu.rpi.metpetdb.client.model.validation.ObjectConstraint;
import edu.rpi.metpetdb.client.model.validation.primitive.StringConstraint;
import edu.rpi.metpetdb.client.ui.MpDb;
import edu.rpi.metpetdb.client.ui.ServerOp;
import edu.rpi.metpetdb.client.ui.input.attributes.specific.MultipleSuggestTextAttribute;
import edu.rpi.metpetdb.client.ui.input.attributes.specific.search.SearchGenericAttribute.Pair;
import edu.rpi.metpetdb.client.ui.widgets.MSuggestText;

public class SearchCountriesAttribute extends SearchGenericAttribute {

	private MultipleSuggestTextAttribute sta;
	public SearchCountriesAttribute(final ObjectConstraint sc) {
		super(sc);
		sta = new MultipleSuggestTextAttribute(sc){
			public void setSuggest(){
				new ServerOp() {
					@Override
					public void begin() {
						MpDb.sample_svc.allCountries(this);
					}
					public void onSuccess(final Object result) {
						createSuggest((Set<String>) result);
					}
				}.begin();
			}
		};
	}
	
	public SearchCountriesAttribute(final StringConstraint sc) {
		super(sc);
	}
	
	public Widget[] createDisplayWidget(final MObject obj){
		return sta.createDisplayWidget(obj);
	}
	
	public Widget[] createEditWidget(final MObject obj, final String id){
		return sta.createEditWidget(obj,id);
	}
	protected void set(final MObject obj, final Object o) {
		mSet(obj, o);
	}

	public Set get(MObject obj) {
		return (Set<String>) obj.mGet(this.getConstraint().property);
	}

	@Override
	protected Object get(Widget editWidget) throws ValidationException {
		final HashSet countries = new HashSet();
		final Iterator itr = sta.getRealEditWidgets().iterator();
		while (itr.hasNext()) {
			final Object obj = itr.next();
			String name = ((MSuggestText) obj).getText();
			if (!name.equals("")) {
				countries.add(name);
			}
		}
		return countries;
	}
	public void onRemoveCriteria(final Object obj){
		if (sta.getRealEditWidgets().contains(obj)) {
			int index = sta.getRealEditWidgets().indexOf(obj);
			if (sta.getRealEditWidgets().size() < 2){
				((MSuggestText)((FlowPanel) sta.getEditList().getListItemAtIndex(0).getWidget()).getWidget(0)).setText("");
				
			} else {
				sta.getRealEditWidgets().remove(obj);
				sta.getEditList().remove(index);
			}
		}
		
	}
	
	public ArrayList<Pair> getCriteria(){
		final ArrayList<Pair> criteria = new ArrayList<Pair>();
		final Iterator<Widget> itr = sta.getRealEditWidgets().iterator();
		while (itr.hasNext()) {
			final MSuggestText st = (MSuggestText) itr.next();
			if (!st.getText().equals(""))
				criteria.add(new Pair(createCritRow("Country:", st.getText()), st));
		}
		return criteria;
	}
}