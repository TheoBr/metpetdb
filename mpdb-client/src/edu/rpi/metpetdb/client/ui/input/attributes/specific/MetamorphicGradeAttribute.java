package edu.rpi.metpetdb.client.ui.input.attributes.specific;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.model.interfaces.MObject;
import edu.rpi.metpetdb.client.model.MetamorphicGrade;
import edu.rpi.metpetdb.client.model.validation.ObjectConstraint;

public class MetamorphicGradeAttribute extends
		MultipleTextAttribute<MetamorphicGrade> {

	public MetamorphicGradeAttribute(ObjectConstraint sc) {
		super(sc);
	}

	@Override
	public Set<MetamorphicGrade> get(MObject obj) {
		return (Set<MetamorphicGrade>) obj.mGet(this.getConstraint().property);
	}

	@Override
	protected Object get(Widget editWidget) throws ValidationException {
		final HashSet metamorphicGrades = new HashSet();
		final Iterator itr = realEditWidgets.iterator();
		while (itr.hasNext()) {
			final Object obj = itr.next();
			final MetamorphicGrade m = new MetamorphicGrade();
			String name = ((HasText) obj).getText();
			if (!name.equals("")) {
				m.setName(name);
				metamorphicGrades.add(m);
			}
		}
		return metamorphicGrades;
	}

}
