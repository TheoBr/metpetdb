package edu.rpi.metpetdb.client.ui.input.attributes.specific;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.model.interfaces.MObject;
import edu.rpi.metpetdb.client.model.Reference;
import edu.rpi.metpetdb.client.model.validation.ObjectConstraint;

public class ReferenceAttribute extends MultipleTextAttribute<Reference> {

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
			String name = ((HasText) obj).getText();
			if (!name.equals("")) {
				m.setName(name);
				references.add(m);
			}
		}
		return references;
	}

}
