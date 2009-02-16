package edu.rpi.metpetdb.server.security;

import org.hibernate.CallbackException;

import edu.rpi.metpetdb.client.error.MpDbException;
import edu.rpi.metpetdb.client.error.security.NoPermissionsException;

public class ConvertSecurityException {

	public static MpDbException convertToException(final CallbackException ce) {
		// get the enum
		if (ce.getCause() instanceof MpDbException) 
			return (MpDbException) ce.getCause();
		else 
			return new NoPermissionsException(ce.getMessage());
	}

}
