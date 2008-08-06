package edu.rpi.metpetdb.client.model.validation;

import java.util.ArrayList;
import java.util.Collection;

import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.error.validation.InvalidSubsampleTypeException;
import edu.rpi.metpetdb.client.model.validation.interfaces.HasValues;

public class SubsampleTypeConstraint extends PropertyConstraint implements
		HasValues {

	private ArrayList<String> subsampleTypeNames = new ArrayList<String>();

	// TODO: Perhaps these should be in the db rather than in the code?
	public SubsampleTypeConstraint() {
		subsampleTypeNames.add("Thin section");
		subsampleTypeNames.add("Polished thin section");
		subsampleTypeNames.add("Rock Chip");
		subsampleTypeNames.add("Mineral separate");
	}

	public void validateValue(final Object value) throws ValidationException {
		super.validateValue(value);
		if (value == null || !isValidSubsampleType(value.toString()))
			throw new InvalidSubsampleTypeException(value.toString());
	}

	public Collection<?> getValues() {
		return subsampleTypeNames;
	}

	public boolean isValidSubsampleType(final String type) {
		for (String validType : subsampleTypeNames) {
			if (validType.equals(type)) {
				return true;
			}
		}
		return false;
	}
}
