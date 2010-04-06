package edu.rpi.metpetdb.client.ui.input.attributes.specific.search;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.SuggestionEvent;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.model.interfaces.MObject;
import edu.rpi.metpetdb.client.model.validation.ObjectConstraint;
import edu.rpi.metpetdb.client.model.validation.primitive.StringConstraint;
import edu.rpi.metpetdb.client.ui.MpDb;
import edu.rpi.metpetdb.client.ui.commands.ServerOp;
import edu.rpi.metpetdb.client.ui.input.attributes.specific.MetamorphicRegionSuggestText;
import edu.rpi.metpetdb.client.ui.widgets.MGoogleEarthPopUp;
import edu.rpi.metpetdb.client.ui.input.attributes.specific.MultipleSuggestTextAttribute;
import edu.rpi.metpetdb.client.ui.widgets.MSuggestText;
import edu.rpi.metpetdb.client.ui.widgets.panels.MultipleInputPanel;

public class SearchMetamorphicRegionsAttribute extends SearchGenericAttribute {

	//private MultipleSuggestTextAttribute sta;
	private MetamorphicRegionSuggestText mr;
	public SearchMetamorphicRegionsAttribute(final ObjectConstraint sc) {
		super(sc);
		//sta = new MultipleSuggestTextAttribute(sc){
		mr= new MetamorphicRegionSuggestText(sc, true){
			public void setSuggest(){
				new ServerOp() {
					@Override
					public void begin() {
						int userId = 0;
						if (MpDb.isLoggedIn())
							userId = MpDb.currentUser().getId();
						MpDb.metamorphicRegion_svc.viewableNamesForUser(userId, this);
					}
					public void onSuccess(final Object result) {
						createSuggest((Set<String>) result);
					}
				}.begin();
			}
			public void onChange(final Widget sender){
				SearchMetamorphicRegionsAttribute.this.getSearchInterface().createCritera();
			}
			public void onSuggestionSelected(final SuggestionEvent event){
				SearchMetamorphicRegionsAttribute.this.getSearchInterface().createCritera();
			}
		};
	}
	
	public SearchMetamorphicRegionsAttribute(final StringConstraint sc) {
		super(sc);
	}
	
	public Widget[] createDisplayWidget(final MObject obj){
		return mr.createDisplayWidget(obj);
	}
	
	public Widget[] createEditWidget(final MObject obj, final String id){
		return mr.createEditWidget(obj,id);
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
		final Iterator itr = mr.getRealEditWidgets().iterator();
		while (itr.hasNext()) {
			final Object obj = itr.next();
			String name = ((MSuggestText)((FlowPanel) obj).getWidget(0)).getText();
			if (!name.equals("")) {
				countries.add(name);
			}
		}
		return countries;
	}

	public void onClear(){
		while (mr.getRealEditWidgets().size()>1){
				mr.getRealEditWidgets().remove(0);
				mr.getEditList().remove(0);
		}
		((MSuggestText)((FlowPanel)((MultipleInputPanel) mr.getEditList().getListItemAtIndex(0).getWidget()).getInputWidget()).getWidget(0)).setText("");
	}
	
	public ArrayList<Widget> getCriteria(){
		final ArrayList<Widget> criteria = new ArrayList<Widget>();
		final Iterator<Widget> itr = mr.getRealEditWidgets().iterator();
		String crit = "";
		if (itr.hasNext()){
			final MSuggestText st = (MSuggestText)((FlowPanel) itr.next()).getWidget(0);
			if (!st.getText().equals(""))
			crit = "Metamorphic Region: " + st.getText() + ", ";;
		}
		while (itr.hasNext()) {
			final MSuggestText st = (MSuggestText)((FlowPanel) itr.next()).getWidget(0);
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