package edu.rpi.metpetdb.client.ui.input.attributes.specific;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.model.MObjectDTO;
import edu.rpi.metpetdb.client.model.ReferenceDTO;
import edu.rpi.metpetdb.client.model.validation.ObjectConstraint;

public class ReferenceAttribute extends MultipleTextAttribute<ReferenceDTO> {

	public ReferenceAttribute(final ObjectConstraint sc) {
		super(sc);
	}

	@Override
	public Set<ReferenceDTO> get(MObjectDTO obj) {
		return (Set<ReferenceDTO>) obj.mGet(this.getConstraint().property);
	}

	@Override
	protected Object get(Widget editWidget) throws ValidationException {
		final HashSet references = new HashSet();
		final Iterator itr = realEditWidgets.iterator();
		while (itr.hasNext()) {
			final Object obj = itr.next();
			final ReferenceDTO m = new ReferenceDTO();
			String name = ((HasText) obj).getText();
			if (!name.equals("")) {
				m.setName(name);
				references.add(m);
			}
		}
		return references;
	}

}
