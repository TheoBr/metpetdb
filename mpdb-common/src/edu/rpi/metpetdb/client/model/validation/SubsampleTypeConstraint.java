package edu.rpi.metpetdb.client.model.validation;

import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.model.interfaces.IHasListItems;


//TODO make this extend CollectionConstraint
public class SubsampleTypeConstraint extends StringConstraint
		implements
			IHasListItems {

	private String[] subsampleTypeNames = {"Thin section", "Polished thin section",
										   "Rock Chip", "Mineral separate",};

	public void validateValue(final Object value) throws ValidationException {
		super.validateValue(value);
	}

	public String[] getListItems() {
		return subsampleTypeNames;
	}

}