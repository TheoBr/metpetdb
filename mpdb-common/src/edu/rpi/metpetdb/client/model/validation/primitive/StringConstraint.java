package edu.rpi.metpetdb.client.model.validation.primitive;

import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.error.validation.ValueTooLongException;
import edu.rpi.metpetdb.client.error.validation.ValueTooShortException;
import edu.rpi.metpetdb.client.model.validation.PropertyConstraint;
import edu.rpi.metpetdb.client.model.validation.interfaces.MaxLengthConstraint;

/** Applies to any string value. */
public class StringConstraint extends PropertyConstraint implements
		MaxLengthConstraint {
	/**
	 * Minimum length of this field.
	 * <p>
	 * If this is a string typed property, it is the minimum number of
	 * characters that must appear in this property for the value to be
	 * considered valid by the database. This is usually 0 for non-required
	 * values; 1 for required values; and {@link #maxLength} for values that
	 * must be completely populated.
	 * </p>
	 */
	public int minLength;

	/**
	 * Maximum length of this field.
	 * <p>
	 * If this is a string typed property, it is the maximum number of
	 * characters the database will allow. This is always at least 1.
	 * </p>
	 */
	public int maxLength;

	@Override
	public void validateValue(final Object value) throws ValidationException {
		super.validateValue(value);
		if (value == null)
			return;
		final String s = (String) value;
		if (s.length() < minLength)
			throw new ValueTooShortException(this);
		if (s.length() > maxLength)
			throw new ValueTooLongException(this);
	}

	public int getMaxLength() {
		return maxLength;
	}
}
