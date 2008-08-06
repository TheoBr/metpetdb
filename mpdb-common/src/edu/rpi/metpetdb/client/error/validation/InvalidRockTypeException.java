package edu.rpi.metpetdb.client.error.validation;

import java.util.ArrayList;

import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.locale.LocaleHandler;

public class InvalidRockTypeException extends ValidationException {

	private static final long serialVersionUID = 1L;
	private String rockType;
	private ArrayList<String> rockTypes;

	public InvalidRockTypeException() {

	}

	public InvalidRockTypeException(final String rockType,
			final ArrayList<String> rockTypes) {
		this.rockType = rockType;
		this.rockTypes = rockTypes;
	}

	@Override
	public String format() {
		return LocaleHandler.lc_text.errorDesc_InvalidRockType(rockType,
				format(this.rockTypes));
	}

	private String format(final ArrayList<String> rockTypes) {
		final StringBuffer sb = new StringBuffer();
		for (int i = 0; i < rockTypes.size(); ++i) {

			sb.append(rockTypes.get(i));
			if (i != rockTypes.size() - 1)
				sb.append(", ");
		}
		return sb.toString();
	}

}
