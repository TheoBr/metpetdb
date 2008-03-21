package edu.rpi.metpetdb.client.model.properties;

import edu.rpi.metpetdb.client.model.MObjectDTO;
import edu.rpi.metpetdb.client.model.UserDTO;

public enum UserProperty implements Property {
	username {
		public <T extends MObjectDTO> String get(final T user) {
			return ((UserDTO) user).getUsername();
		}

		public <T extends MObjectDTO, K> void set(final T user, final K username) {
			((UserDTO) user).setUsername((String) username);
		}
	},
	emailAddress {
		public <T extends MObjectDTO> String get(final T user) {
			return ((UserDTO) user).getEmailAddress();
		}

		public <T extends MObjectDTO, K> void set(final T user, final K password) {
			((UserDTO) user).setEmailAddress((String) password);
		}
	},
}
