package edu.rpi.metpetdb.server.security.permissions.principals;

import java.io.Serializable;
import java.security.Principal;

import edu.rpi.metpetdb.client.model.User;

public class EnabledPrincipal implements Principal, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Boolean enabled;

	public EnabledPrincipal(final User u) {
		enabled = u.getEnabled();
	}

	public EnabledPrincipal(final boolean enabled) {
		this.enabled = enabled;
	}

	public String getName() {
		return enabled.toString();
	}

	public int hashCode() {
		return enabled.hashCode();
	}

	public boolean equals(final Object o) {
		if (!(o.getClass().getName().equals(EnabledPrincipal.class.getName()))) {
			return false;
		} else {
			return ((EnabledPrincipal) o).getName().equals(getName());
		}
	}
}
