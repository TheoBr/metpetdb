package edu.rpi.metpetdb.client.ui.input.attributes.specific.sample;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SuggestionEvent;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.model.Reference;
import edu.rpi.metpetdb.client.model.interfaces.MObject;
import edu.rpi.metpetdb.client.model.validation.ObjectConstraint;
import edu.rpi.metpetdb.client.ui.MpDb;
import edu.rpi.metpetdb.client.ui.commands.ServerOp;
import edu.rpi.metpetdb.client.ui.input.attributes.specific.MultipleSuggestTextAttribute;
import edu.rpi.metpetdb.client.ui.widgets.MSuggestText;

/**
 * ReferenceAttribute
 * 
 * @modified millib2
 */
public class ReferenceAttribute extends MultipleSuggestTextAttribute {

	public ReferenceAttribute(final ObjectConstraint sc) {
		super(sc);
	}

	@Override
	public Set<Reference> get(MObject obj) {
		return (Set<Reference>) obj.mGet(this.getConstraint().property);
	}

	@Override
	protected Object get(Widget editWidget) throws ValidationException {
		final HashSet references = new HashSet();
		final Iterator itr = realEditWidgets.iterator();
		while (itr.hasNext()) {
			final Object obj = itr.next();
			final Reference m = new Reference();
			String name = ((MSuggestText) obj).getText();
			if (!name.equals("")) {
				m.setName(name);
				references.add(m);
			}
		}
		return references;
	}
	
	/**
	 * Modifies the behavior of the suggest text widget such that when only
	 * one reference is present it is a label, not link that does nothing.
	 * 
	 * @param obj
	 */
	@Override
	public Widget[] createDisplayWidget(final MObject obj){
		final Set<Reference> s = get(obj);		
		if (s != null && s.size() == 1) {
			return new Widget[] {
				new Label(s.iterator().next().toString())
			};	
		}
		else {
			return super.createDisplayWidget(obj);
		}
	}
	
	public void setSuggest(){
		new ServerOp() {
			@Override
			public void begin() {
				MpDb.reference_svc.allReferences(this);
			}
			public void onSuccess(final Object result) {
				createSuggest((Set<String>) result);
			}
		}.begin();
	}
	
	public void onChange(final Widget sender){

	}
	public void onSuggestionSelected(final SuggestionEvent event){

	}
}
