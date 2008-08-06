package edu.rpi.metpetdb.client.model.validation;

import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.error.validation.InvalidDateStringException;
import edu.rpi.metpetdb.client.model.validation.primitive.StringConstraint;

public class DateStringConstraint extends StringConstraint {
	public void validateValue(final Object value) throws ValidationException {
		if (value == null)
			return;
		final String s = (String) value;

		if (s.matches("^((\\d{2})([-/]))?((\\d{2})([-/]))?(\\d{4})$")) {
			// MM-DD-YYYY
			int month = 0, day = 0;

			if (s.length() == 10) { // MM-DD-YYYY
				month = Integer.valueOf(s.substring(0, 2));
				day = Integer.valueOf(s.substring(3, 5));
			} else if (s.length() == 7) { // MM-YYYY
				month = Integer.valueOf(s.substring(0, 2));
				day = 1;
			} else { // YYYY
				month = 1;
				day = 1;
			}

			if (validDate(month, day)) {
				return;
			}

		} else if (s.matches("^(\\d{4})(([-/])(\\d{2}))?(([-/])(\\d{2}))?$")) {
			// YYYY-MM-DD
			int month = 0, day = 0;

			if (s.length() == 10) { // YYYY-MM-DD
				month = Integer.valueOf(s.substring(5, 7));
				day = Integer.valueOf(s.substring(8, 10));
			} else if (s.length() == 7) { // YYYY-MM
				month = Integer.valueOf(s.substring(5, 7));
				day = 1;
			} else { // YYYY
				month = 1;
				day = 1;
			}

			if (validDate(month, day)) {
				return;
			}
		}
		throw new InvalidDateStringException(this);
	}

	// This is quite naive, but still better than nothing
	private boolean validDate(int month, int day) {
		if (month < 1 || month > 12)
			return false;
		else if (day < 1 || day > 31)
			return false;
		return true;
	}
}
