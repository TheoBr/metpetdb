package edu.rpi.metpetdb.client.model.validation;

import java.util.ArrayList;
import java.util.Collection;

import edu.rpi.metpetdb.client.error.InvalidRockTypeException;
import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.model.validation.interfaces.HasValues;

public class RockTypeConstraint extends PropertyConstraint implements HasValues {

	private ArrayList<String> rockTypes = new ArrayList<String>();

	// TODO: Perhaps these should be in the db rather than in the code?
	public RockTypeConstraint() {
		rockTypes.add("Amphibolite");
		rockTypes.add("Blueschist");
		rockTypes.add("Calc-silicate");
		rockTypes.add("Eclogite");
		rockTypes.add("Gneiss");
		rockTypes.add("Granofels");
		rockTypes.add("Greenschist");
		rockTypes.add("Hornfels");
		rockTypes.add("Marble");
		rockTypes.add("Metabasite");
		rockTypes.add("Metagreywacke");
		rockTypes.add("Metapelite");
		rockTypes.add("Meta-arkose");
		rockTypes.add("Migmatite");
		rockTypes.add("Mylonite");
		rockTypes.add("Phyllite");
		rockTypes.add("Quartzite");
		rockTypes.add("Schist");
		rockTypes.add("Serpentinite");
		rockTypes.add("Skarn");
		rockTypes.add("Slate");
	}

	@Override
	public void validateValue(final Object value) throws ValidationException {
		if (value == null || !isValidRockName(value.toString())) {
			throw new InvalidRockTypeException(value == null ? "" : value
					.toString(), rockTypes);
		}
	}

	public Collection<?> getValues() {
		return rockTypes;
	}

	private boolean isValidRockName(final String rockName) {
		for (int i = 0; i < rockTypes.size(); ++i) {
			if (rockName.equals(rockTypes.get(i)))
				return true;
		}
		return false;
	}

}
