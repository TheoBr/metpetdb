package edu.rpi.metpetdb.client.ui.input.attributes.specific;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.model.MetamorphicGrade;
import edu.rpi.metpetdb.client.model.interfaces.MObject;
import edu.rpi.metpetdb.client.model.validation.ObjectConstraint;
import edu.rpi.metpetdb.client.model.validation.primitive.StringConstraint;
import edu.rpi.metpetdb.client.ui.MpDb;
import edu.rpi.metpetdb.client.ui.ServerOp;

public class MetamorphicGradeAttribute extends MultipleSuggestTextAttribute {

	public MetamorphicGradeAttribute(final ObjectConstraint sc) {
		super(sc);
	}
	public MetamorphicGradeAttribute(final StringConstraint sc) {
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
			final MetamorphicGrade mg = new MetamorphicGrade();
			String name = ((SuggestBox) obj).getText();
			if (!name.equals("")) {
				mg.setName(name);
				metamorphicGrades.add(mg);
			}
		}
		return metamorphicGrades;
	}
	public void setSuggest(){
		new ServerOp() {
			@Override
			public void begin() {
				MpDb.metamorphicGrade_svc.allMetamorphicGrades(this);
			}
			public void onSuccess(final Object result) {
				createSuggest((Set<String>) result);
			}
		}.begin();
	}

}
