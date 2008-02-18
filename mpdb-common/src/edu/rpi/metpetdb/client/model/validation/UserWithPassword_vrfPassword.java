package edu.rpi.metpetdb.client.model.validation;

import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.error.ValueNotEqualException;
import edu.rpi.metpetdb.client.model.MObjectDTO;
import edu.rpi.metpetdb.client.model.UserWithPasswordDTO;

public class UserWithPassword_vrfPassword extends StringConstraint {
	PropertyConstraint newPassword;

	public void validateEntity(final MObjectDTO obj) throws ValidationException {
		super.validateEntity(obj);
		final String n = (String) obj.mGet(UserWithPasswordDTO.P_newPassword);
		final String v = (String) obj.mGet(UserWithPasswordDTO.P_vrfPassword);
		if (n == null || v == null)
			return;// Already should have reported an error.
		if (!n.equals(v))
			throw new ValueNotEqualException(this, newPassword);
	}
}
