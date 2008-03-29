package edu.rpi.metpetdb.client.model.validation;

import edu.rpi.metpetdb.client.error.InvalidRockTypeException;
import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.model.interfaces.IHasListItems;

public class RockTypeConstraint extends StringConstraint implements
		IHasListItems {

	private String[] rockTypes = {
			"Amphibolite", "Blueschist", "Calc-silicate", "Eclogite", "Gneiss",
			"Granofels", "Greenschist", "Hornfels", "Marble", "Metabasite",
			"Metagreywacke", "Metapelite", "Meta-arkose", "Migmatite",
			"Mylonite", "Phyllite", "Quartzite", "Schist", "Serpentinite",
			"Skarn", "Slate",
	};

	@Override
	public void validateValue(final Object value) throws ValidationException {
		if (!isValidRockName(value.toString())) {
			throw new InvalidRockTypeException(value.toString(), rockTypes);
		}
	}

	public String[] getListItems() {
		return rockTypes;
	}

	private boolean isValidRockName(final String rockName) {
		for (int i = 0; i < rockTypes.length; ++i) {
			if (rockName.equals(rockTypes[i]))
				return true;
		}
		return false;
	}

}
