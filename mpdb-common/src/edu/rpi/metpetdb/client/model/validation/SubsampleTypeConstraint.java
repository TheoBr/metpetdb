package edu.rpi.metpetdb.client.model.validation;

import java.util.ArrayList;
import java.util.Collection;

import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.model.validation.interfaces.HasValues;

public class SubsampleTypeConstraint extends PropertyConstraint implements
		HasValues {

	private ArrayList<String> subsampleTypeNames = new ArrayList<String>();

	public SubsampleTypeConstraint() {
		subsampleTypeNames.add("Thin section");
		subsampleTypeNames.add("Polished thin section");
		subsampleTypeNames.add("Rock Chip");
		subsampleTypeNames.add("Mineral separate");
	}

	public void validateValue(final Object value) throws ValidationException {
		super.validateValue(value);
	}

	public Collection<?> getValues() {
		return subsampleTypeNames;
	}

}
