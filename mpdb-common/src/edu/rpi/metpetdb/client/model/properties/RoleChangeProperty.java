package edu.rpi.metpetdb.client.model.properties;

import edu.rpi.metpetdb.client.model.Role;
import edu.rpi.metpetdb.client.model.RoleChange;
import edu.rpi.metpetdb.client.model.User;

public enum RoleChangeProperty implements Property<RoleChange> {

	requestReason {
		public Object get(final RoleChange roleChange) {
			return roleChange.getRequestReason();
		}

		public void set(final RoleChange roleChange, final Object text) {
			roleChange.setRequestReason((String) text);
		}
	},
	

	user {
		public User get(final RoleChange roleChange) {
			return roleChange.getUser();
		}

		public void set(final RoleChange roleChange, final Object owner) {
			roleChange.setUser((User) owner);
		}
	},
	
	sponsor {
		public User get(final RoleChange roleChange) {
			return roleChange.getSponsor();
		}

		public void set(final RoleChange roleChange, final Object owner) {
			roleChange.setSponsor((User) owner);
		}
	},
	
	role {
		public Object get(final RoleChange roleChange) {
			return roleChange.getRole();
		}

		public void set(final RoleChange roleChange, final Object role) {
			roleChange.setRole((Role) role);
		}
	},

}
