package edu.rpi.metpetdb.client.model.validation;

import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.error.ValueNotEqualException;
import edu.rpi.metpetdb.client.model.MObject;
import edu.rpi.metpetdb.client.model.UserWithPassword;

public class UserWithPassword_vrfPassword extends StringConstraint {
	PropertyConstraint newPassword;

	public void validateEntity(final MObject obj) throws ValidationException {
		super.validateEntity(obj);
		final String n = (String) obj.mGet(UserWithPassword.P_newPassword);
		final String v = (String) obj.mGet(UserWithPassword.P_vrfPassword);
		if (n == null || v == null)
			return;// Already should have reported an error.
		if (!n.equals(v))
			throw new ValueNotEqualException(this, newPassword);
	}
}
