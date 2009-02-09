package edu.rpi.metpetdb.server.security;

import edu.rpi.metpetdb.client.error.LoginRequiredException;
import edu.rpi.metpetdb.client.error.MpDbException;
import edu.rpi.metpetdb.client.error.security.AccountNotEnabledException;
import edu.rpi.metpetdb.client.error.security.NoPermissionsException;
import edu.rpi.metpetdb.client.error.security.NotOwnerException;
import edu.rpi.metpetdb.client.error.security.UnableToModifyPublicDataException;

public class ConvertSecurityException {

	public static MpDbException convertToException(final String name) {
		// get the enum
		SecurityExceptionConsts exp = SecurityExceptionConsts.valueOf(name);
		switch (exp) {
		case INVALID_SUBJECT:
			return new LoginRequiredException("Your session expired.");
		case DISABLED_ACCOUNT:
			return new AccountNotEnabledException();
		case NOT_OWNER:
			return new  NotOwnerException();
		case PUBLIC_DATA_MODIFICATION:
			return new UnableToModifyPublicDataException();
		default:
			return new NoPermissionsException();
		}
	}

}
