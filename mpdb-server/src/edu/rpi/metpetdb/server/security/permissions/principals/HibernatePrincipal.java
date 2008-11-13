package edu.rpi.metpetdb.server.security.permissions.principals;

import java.io.Serializable;
import java.security.Principal;

final public class HibernatePrincipal implements Principal, Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;

	public HibernatePrincipal() {
		name = "";
	}

	public HibernatePrincipal(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public int hashCode() {
		return name.hashCode();
	}

	public boolean equals(Object o) {
		if (!(o instanceof HibernatePrincipal)) {
			return false;
		}
		return name.equals(((HibernatePrincipal) o).name);
	}

	public String toString() {
		return name;
	}
}
