package edu.rpi.metpetdb.client.error;

import edu.rpi.metpetdb.client.locale.LocaleHandler;

public class InvalidRockTypeException extends ValidationException {

	private static final long serialVersionUID = 1L;
	private String rockType;
	private String[] rockTypes;

	public InvalidRockTypeException() {

	}

	public InvalidRockTypeException(final String rockType,
			final String[] rockTypes) {
		this.rockType = rockType;
		this.rockTypes = rockTypes;
	}

	@Override
	public String format() {
		return LocaleHandler.lc_text.errorDesc_InvalidRockType(rockType,
				format(this.rockTypes));
	}

	private String format(final String[] rockTypes) {
		final StringBuffer sb = new StringBuffer();
		for (int i = 0; i < rockTypes.length; ++i) {

			sb.append(rockTypes[i]);
			if (i != 0 && i != rockTypes.length - 1)
				sb.append(", ");
		}
		return sb.toString();
	}

}
