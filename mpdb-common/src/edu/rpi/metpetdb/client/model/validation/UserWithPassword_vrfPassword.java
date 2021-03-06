package edu.rpi.metpetdb.client.model.validation;

import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.error.validation.ValueNotEqualException;
import edu.rpi.metpetdb.client.model.interfaces.MObject;
import edu.rpi.metpetdb.client.model.properties.UserWithPasswordProperty;
import edu.rpi.metpetdb.client.model.validation.primitive.StringConstraint;

public class UserWithPassword_vrfPassword extends StringConstraint {
	PropertyConstraint newPassword;

	public void validateEntity(final MObject obj) throws ValidationException {
		super.validateEntity(obj);
		final String n = (String) obj
				.mGet(UserWithPasswordProperty.newPassword);
		final String v = (String) obj
				.mGet(UserWithPasswordProperty.vrfPassword);
		if (n == null || v == null)
			return;// Already should have reported an error.
		if (!n.equals(v))
			throw new ValueNotEqualException(this, newPassword);
	}
}
