package edu.rpi.metpetdb.server.security.permissions.principals;

import java.io.Serializable;
import java.security.Principal;

public class AdminPrincipal implements Principal, Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AdminPrincipal() {
	}

	public String getName() {
		return "";
	}

	public int hashCode() {
		return "".hashCode();
	}

	public boolean equals(Object o) {
		if (!(o instanceof AdminPrincipal)) {
			return false;
		} else 
			return true;
	}
}
