package edu.rpi.metpetdb.client.model.validation;

import java.util.ArrayList;
import java.util.Collection;

import edu.rpi.metpetdb.client.error.InvalidImageTypeException;
import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.model.validation.interfaces.HasValues;

public class ImageTypeConstraint extends PropertyConstraint implements
		HasValues {

	private ArrayList<String> imageTypes = new ArrayList<String>();

	// TODO: Perhaps these should be in the db rather than in the code?
	public ImageTypeConstraint() {
		imageTypes.add("Transmitted Unpolarized");
		imageTypes.add("Transmitted Plane polarized");
		imageTypes.add("Transmitted Crossed Polars");
		imageTypes.add("Reflected Unpolarized");
		imageTypes.add("Reflected Plane polarized");
		imageTypes.add("Reflected Crossed Polars");
		imageTypes.add("SE Secondary Electron");
		imageTypes.add("BSE Backscattered Electron");
		imageTypes.add("CL Cathodoluminescence");
		imageTypes.add("X-ray");
	}

	public Collection<?> getValues() {
		return imageTypes;
	}

	public void validateValue(final Object value) throws ValidationException {
		super.validateValue(value);
		if (value == null || !isValidImageType(value.toString())) {
			throw new InvalidImageTypeException(value.toString());
		}
	}

	private boolean isValidImageType(final String value) {
		for (int i = 0; i < imageTypes.size(); ++i) {
			if (imageTypes.get(i).equals(value))
				return true;
		}
		return false;
	}
}
