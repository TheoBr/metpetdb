package edu.rpi.metpetdb.client.model.validation;

import edu.rpi.metpetdb.client.error.ValidationException;


//TODO make this extend CollectionConstraint
public class RockTypeConstraint extends StringConstraint
		implements
			IHasListItems {

	private String[] rockNames = {"Amphibolite", "Blueschist", "Calc-silicate",
			"Eclogite", "Gneiss", "Granofels", "Greenschist", "Hornfels",
			"Marble", "Metabasite", "Metagreywacke", "Metapelite",
			"Meta-arkose", "Migmatite", "Mylonite", "Phyllite", "Quartzite",
			"Schist", "Serpentinite", "Skarn", "Slate",};

	public void validateValue(final Object value) throws ValidationException {
		super.validateValue(value);
	}

	public String[] getListItems() {
		return rockNames;
	}

}