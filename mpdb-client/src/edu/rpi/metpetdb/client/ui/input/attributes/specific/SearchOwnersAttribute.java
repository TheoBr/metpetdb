package edu.rpi.metpetdb.client.ui.input.attributes.specific;

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
import edu.rpi.metpetdb.client.ui.widgets.MSuggestText;

public class SearchOwnersAttribute extends MultipleSuggestTextAttribute {

	public SearchOwnersAttribute(final ObjectConstraint sc) {
		super(sc);
	}

	public SearchOwnersAttribute(final StringConstraint sc) {
		super(sc);
	}
	@Override
	public Set get(MObject obj) {
		return (Set<String>) obj.mGet(this.getConstraint().property);
	}

	@Override
	protected Object get(Widget editWidget) throws ValidationException {
		final HashSet owners = new HashSet();
		final Iterator itr = realEditWidgets.iterator();
		while (itr.hasNext()) {
			final Object obj = itr.next();
			String name = ((MSuggestText) obj).getText();
			if (!name.equals("")) {
				owners.add(name);
			}
		}
		return owners;
	}
	public void setSuggest(){
		new ServerOp() {
			@Override
			public void begin() {
				MpDb.user_svc.allNames(this);
			}
			public void onSuccess(final Object result) {
				createSuggest((Set<String>) result);
			}
		}.begin();
	}
}
