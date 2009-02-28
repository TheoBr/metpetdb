package edu.rpi.metpetdb.client.model.properties;

import edu.rpi.metpetdb.client.model.User;
import edu.rpi.metpetdb.client.model.UserWithPassword;

public enum UserWithPasswordProperty implements Property<UserWithPassword> {
	oldPassword {
		public String get(final UserWithPassword userWithPassword) {
			return ((UserWithPassword) userWithPassword).getOldPassword();
		}

		public void set(final UserWithPassword userWithPassword,
				final Object oldPassword) {
			((UserWithPassword) userWithPassword)
					.setOldPassword((String) oldPassword);
		}
	},
	newPassword {
		public String get(final UserWithPassword userWithPassword) {
			return ((UserWithPassword) userWithPassword).getNewPassword();
		}

		public void set(final UserWithPassword userWithPassword,
				final Object newPassword) {
			((UserWithPassword) userWithPassword)
					.setNewPassword((String) newPassword);
		}
	},
	vrfPassword {
		public String get(final UserWithPassword userWithPassword) {
			return ((UserWithPassword) userWithPassword).getVrfPassword();
		}

		public void set(final UserWithPassword userWithPassword,
				final Object vrfPassword) {
			((UserWithPassword) userWithPassword)
					.setVrfPassword((String) vrfPassword);
		}
	},
	user {
		public User get(final UserWithPassword userWithPassword) {
			return ((UserWithPassword) userWithPassword).getUser();
		}

		public void set(final UserWithPassword userWithPassword,
				final Object user) {
			((UserWithPassword) userWithPassword).setUser((User) user);
		}
	},
}
