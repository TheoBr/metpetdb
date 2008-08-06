package edu.rpi.metpetdb.client.model.validation;

import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.error.validation.InvalidCharacterException;
import edu.rpi.metpetdb.client.model.validation.primitive.StringConstraint;

public class RestrictedCharacterStringConstraint extends StringConstraint {
	/** Regular expression pattern that the string must match. */
	public String pattern;

	public void validateValue(final Object value) throws ValidationException {
		super.validateValue(value);
		if (value == null)
			return;
		final String s = (String) value;
		final String re = "^[" + pattern + "]+$";
		if (!s.matches(re)) {
			for (int k = 0; k < s.length(); k++) {
				final String b = s.substring(k, k + 1);
				if (!b.matches(re))
					throw new InvalidCharacterException(this, b.charAt(0));
			}
			throw new InvalidCharacterException(this, ' ');
		}
	}
}
