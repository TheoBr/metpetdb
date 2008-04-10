package edu.rpi.metpetdb.client.ui.input.attributes.specific;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.model.MObjectDTO;
import edu.rpi.metpetdb.client.model.MetamorphicGradeDTO;
import edu.rpi.metpetdb.client.model.validation.StringConstraint;

public class MetamorphicGradeAttribute extends
		MultipleTextAttribute<MetamorphicGradeDTO> {

	public MetamorphicGradeAttribute(StringConstraint sc) {
		super(sc);
	}

	@Override
	public Set<MetamorphicGradeDTO> get(MObjectDTO obj) {
		return (Set<MetamorphicGradeDTO>) obj
				.mGet(this.getConstraint().property);
	}

	@Override
	protected Object get(Widget editWidget) throws ValidationException {
		final HashSet metamorphicGrades = new HashSet();
		final Iterator itr = realEditWidgets.iterator();
		while (itr.hasNext()) {
			final Object obj = itr.next();
			final MetamorphicGradeDTO m = new MetamorphicGradeDTO();
			String name = ((HasText) obj).getText();
			if (!name.equals("")) {
				m.setName(name);
				metamorphicGrades.add(m);
			}
		}
		return metamorphicGrades;
	}

}
