package edu.rpi.metpetdb.client.model.properties;

import edu.rpi.metpetdb.client.model.MObjectDTO;
import edu.rpi.metpetdb.client.model.UserDTO;
import edu.rpi.metpetdb.client.model.UserWithPasswordDTO;

public enum UserWithPasswordProperty implements Property {
	oldPassword {
		public <T extends MObjectDTO> String get(final T userWithPassword) {
			return ((UserWithPasswordDTO) userWithPassword).getOldPassword();
		}

		public <T extends MObjectDTO, K> void set(final T userWithPassword,
				final K oldPassword) {
			((UserWithPasswordDTO) userWithPassword)
					.setOldPassword((String) oldPassword);
		}
	},
	newPassword {
		public <T extends MObjectDTO> String get(final T userWithPassword) {
			return ((UserWithPasswordDTO) userWithPassword).getNewPassword();
		}

		public <T extends MObjectDTO, K> void set(final T userWithPassword,
				final K newPassword) {
			((UserWithPasswordDTO) userWithPassword)
					.setNewPassword((String) newPassword);
		}
	},
	vrfPassword {
		public <T extends MObjectDTO> String get(final T userWithPassword) {
			return ((UserWithPasswordDTO) userWithPassword).getVrfPassword();
		}

		public <T extends MObjectDTO, K> void set(final T userWithPassword,
				final K vrfPassword) {
			((UserWithPasswordDTO) userWithPassword)
					.setVrfPassword((String) vrfPassword);
		}
	},
	user {
		public <T extends MObjectDTO> UserDTO get(final T userWithPassword) {
			return ((UserWithPasswordDTO) userWithPassword).getUser();
		}

		public <T extends MObjectDTO, K> void set(final T userWithPassword,
				final K user) {
			((UserWithPasswordDTO) userWithPassword)
					.setUser((UserDTO) user);
		}
	},
}
