package edu.rpi.metpetdb.client.ui.input.attributes.specific;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.model.interfaces.MObject;
import edu.rpi.metpetdb.client.model.SampleComment;
import edu.rpi.metpetdb.client.model.validation.ObjectConstraint;

public class CommentAttribute extends MultipleTextAttribute<SampleComment> {

	public CommentAttribute(final ObjectConstraint sc) {
		super(sc);
	}

	@Override
	public Set<SampleComment> get(MObject obj) {
		return (Set<SampleComment>) obj.mGet(this.getConstraint().property);
	}

	@Override
	protected Object get(Widget editWidget) throws ValidationException {
		final HashSet metamorphicGrades = new HashSet();
		final Iterator itr = realEditWidgets.iterator();
		while (itr.hasNext()) {
			final Object obj = itr.next();
			final SampleComment m = new SampleComment();
			String name = ((HasText) obj).getText();
			if (!name.equals("")) {
				m.setText(name);
				metamorphicGrades.add(m);
			}
		}
		return metamorphicGrades;
	}

}
