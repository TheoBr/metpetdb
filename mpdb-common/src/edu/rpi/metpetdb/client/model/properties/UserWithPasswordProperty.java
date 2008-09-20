package edu.rpi.metpetdb.client.model.properties;

import edu.rpi.metpetdb.client.model.interfaces.MObject;
import edu.rpi.metpetdb.client.model.User;
import edu.rpi.metpetdb.client.model.UserWithPassword;

public enum UserWithPasswordProperty implements Property {
	oldPassword {
		public <T extends MObject> String get(final T userWithPassword) {
			return ((UserWithPassword) userWithPassword).getOldPassword();
		}

		public <T extends MObject, K> void set(final T userWithPassword,
				final K oldPassword) {
			((UserWithPassword) userWithPassword)
					.setOldPassword((String) oldPassword);
		}
	},
	newPassword {
		public <T extends MObject> String get(final T userWithPassword) {
			return ((UserWithPassword) userWithPassword).getNewPassword();
		}

		public <T extends MObject, K> void set(final T userWithPassword,
				final K newPassword) {
			((UserWithPassword) userWithPassword)
					.setNewPassword((String) newPassword);
		}
	},
	vrfPassword {
		public <T extends MObject> String get(final T userWithPassword) {
			return ((UserWithPassword) userWithPassword).getVrfPassword();
		}

		public <T extends MObject, K> void set(final T userWithPassword,
				final K vrfPassword) {
			((UserWithPassword) userWithPassword)
					.setVrfPassword((String) vrfPassword);
		}
	},
	user {
		public <T extends MObject> User get(final T userWithPassword) {
			return ((UserWithPassword) userWithPassword).getUser();
		}

		public <T extends MObject, K> void set(final T userWithPassword,
				final K user) {
			((UserWithPassword) userWithPassword).setUser((User) user);
		}
	},
}
