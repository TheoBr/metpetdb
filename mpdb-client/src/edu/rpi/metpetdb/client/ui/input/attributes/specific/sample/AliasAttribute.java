package edu.rpi.metpetdb.client.ui.input.attributes.specific.sample;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.model.SampleAlias;
import edu.rpi.metpetdb.client.model.interfaces.MObject;
import edu.rpi.metpetdb.client.model.validation.ObjectConstraint;
import edu.rpi.metpetdb.client.ui.input.attributes.specific.MultipleTextAttribute;

public class AliasAttribute extends MultipleTextAttribute<SampleAlias> {

	public AliasAttribute(final ObjectConstraint sc) {
		super(sc);
	}

	@Override
	public Set<SampleAlias> get(MObject obj) {
		return (Set<SampleAlias>) obj.mGet(this.getConstraint().property);
	}

	@Override
	protected Object get(Widget editWidget) throws ValidationException {
		final HashSet aliases = new HashSet();
		final Iterator itr = realEditWidgets.iterator();
		while (itr.hasNext()) {
			final Object obj = itr.next();
			final SampleAlias sa = new SampleAlias();
			String name = ((HasText) obj).getText();
			if (!name.equals("")) {
				sa.setAlias(name);
				aliases.add(sa);
			}
		}
		return aliases;
	}

}
