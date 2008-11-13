package edu.rpi.metpetdb.server.security.permissions.principals;

import java.io.Serializable;
import java.security.Principal;

import edu.rpi.metpetdb.client.model.User;

public final class OwnerPrincipal implements Principal,Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;
	
	public OwnerPrincipal(final User u) {
		id = u.getId();
	}
	
	public OwnerPrincipal(final int id)  {
		this.id = id;
	}

	public String getName() {
		return (String.valueOf(id));
	}
	
	public int hashCode() {
		return id;
	}
	
	public boolean equals(final Object o) {
		if (!(o.getClass().getName().equals(OwnerPrincipal.class.getName()))) {
			return false;
		} else {
			return ((OwnerPrincipal)o).getName().equals(getName());
		}
	}
}
