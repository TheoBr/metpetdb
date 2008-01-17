package edu.rpi.metpetdb.client.model.validation;

import edu.rpi.metpetdb.client.error.ValidationException;


//TODO make this extend CollectionConstraint
public class ImageTypeConstraint extends StringConstraint
		implements
			IHasListItems {

	private static String[] IMAGE_TYPES = {"Transmitted Unpolarized",
			"Transmitted Plane polarized", "Transmitted Crossed Polars",
			"Reflected Unpolarized", "Reflected Plane polarized",
			"Reflected Crossed Polars", "SE Secondary Electron",
			"BSE Backscattered Electron", "CL Cathodoluminescence", "X-ray",};

	public ImageTypeConstraint() {

	}

	public String[] getListItems() {
		return IMAGE_TYPES;
	}

	public void validateValue(final Object value) throws ValidationException {
		super.validateValue(value);
	}
}
