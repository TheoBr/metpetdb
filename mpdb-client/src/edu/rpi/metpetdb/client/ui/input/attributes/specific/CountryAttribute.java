package edu.rpi.metpetdb.client.ui.input.attributes.specific;

import java.util.Set;

import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.model.validation.ObjectConstraint;
import edu.rpi.metpetdb.client.model.validation.primitive.StringConstraint;
import edu.rpi.metpetdb.client.ui.MpDb;
import edu.rpi.metpetdb.client.ui.ServerOp;
import edu.rpi.metpetdb.client.ui.input.attributes.SuggestTextAttribute;
import edu.rpi.metpetdb.client.ui.widgets.MSuggestText;

public class CountryAttribute  extends SuggestTextAttribute {

	public CountryAttribute(final ObjectConstraint sc) {
		super(sc);
	}

	public CountryAttribute(final StringConstraint sc) {
		super(sc);
	}
	
	protected Object get(Widget editWidget) throws ValidationException {
		if (editWidget instanceof MSuggestText){
			String name = ((MSuggestText) editWidget).suggestBox.getText();
			if (!name.equals("")) {
				return name;
			}
		}		
		return null;
	}

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
}
