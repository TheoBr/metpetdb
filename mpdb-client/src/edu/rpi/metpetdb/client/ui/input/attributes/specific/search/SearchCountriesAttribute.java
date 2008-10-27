package edu.rpi.metpetdb.client.ui.input.attributes.specific.search;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.model.interfaces.MObject;
import edu.rpi.metpetdb.client.model.validation.ObjectConstraint;
import edu.rpi.metpetdb.client.model.validation.primitive.StringConstraint;
import edu.rpi.metpetdb.client.ui.MpDb;
import edu.rpi.metpetdb.client.ui.ServerOp;
import edu.rpi.metpetdb.client.ui.input.attributes.specific.MultipleSuggestTextAttribute;
import edu.rpi.metpetdb.client.ui.input.attributes.specific.SearchOwnersAttribute;
import edu.rpi.metpetdb.client.ui.widgets.MSuggestText;
import edu.rpi.metpetdb.client.ui.widgets.MultipleInputPanel;

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
			public void onChange(final Widget sender){
				SearchCountriesAttribute.this.getSearchInterface().createCritera();
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

	public void onClear(){
		while (sta.getRealEditWidgets().size()>1){
				sta.getRealEditWidgets().remove(0);
				sta.getEditList().remove(0);
		}
		((MSuggestText)((MultipleInputPanel) sta.getEditList().getListItemAtIndex(0).getWidget()).getInputWidget()).setText("");
	}
	
	public ArrayList<Widget> getCriteria(){
		final ArrayList<Widget> criteria = new ArrayList<Widget>();
		final Iterator<Widget> itr = sta.getRealEditWidgets().iterator();
		String crit = "";
		if (itr.hasNext()){
			final MSuggestText st = (MSuggestText) itr.next();
			if (!st.getText().equals(""))
			crit = "Country: " + st.getText() + ", ";;
		}
		while (itr.hasNext()) {
			final MSuggestText st = (MSuggestText) itr.next();
			if (!st.getText().equals(""))
				crit += st.getText() + ", ";
		}
		if (!crit.equals("")){
			crit = crit.substring(0,crit.length()-2);
			criteria.add(createCritRow(crit));
		}
		return criteria;
	}
}