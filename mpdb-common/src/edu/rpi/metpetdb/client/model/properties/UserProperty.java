package edu.rpi.metpetdb.client.model.properties;

import edu.rpi.metpetdb.client.model.MObjectDTO;
import edu.rpi.metpetdb.client.model.UserDTO;

public enum UserProperty implements Property {
	emailAddress {
		public <T extends MObjectDTO> String get(final T user) {
			return ((UserDTO) user).getEmailAddress();
		}

		public <T extends MObjectDTO, K> void set(final T user, final K value) {
			((UserDTO) user).setEmailAddress((String) value);
		}
	},
	name {
		public <T extends MObjectDTO> String get(final T user) {
			return ((UserDTO) user).getName();
		}

		public <T extends MObjectDTO, K> void set(final T user, final K value) {
			((UserDTO) user).setName((String) value);
		}
	},
	address {
		public <T extends MObjectDTO> String get(final T user) {
			return ((UserDTO) user).getAddress();
		}

		public <T extends MObjectDTO, K> void set(final T user, final K value) {
			((UserDTO) user).setAddress((String) value);
		}
	},
	city {
		public <T extends MObjectDTO> String get(final T user) {
			return ((UserDTO) user).getCity();
		}

		public <T extends MObjectDTO, K> void set(final T user, final K value) {
			((UserDTO) user).setCity((String) value);
		}
	},
	province {
		public <T extends MObjectDTO> String get(final T user) {
			return ((UserDTO) user).getProvince();
		}

		public <T extends MObjectDTO, K> void set(final T user, final K value) {
			((UserDTO) user).setProvince((String) value);
		}
	},
	country {
		public <T extends MObjectDTO> String get(final T user) {
			return ((UserDTO) user).getCountry();
		}

		public <T extends MObjectDTO, K> void set(final T user, final K value) {
			((UserDTO) user).setCountry((String) value);
		}
	},
	postalCode {
		public <T extends MObjectDTO> String get(final T user) {
			return ((UserDTO) user).getPostalCode();
		}

		public <T extends MObjectDTO, K> void set(final T user, final K value) {
			((UserDTO) user).setPostalCode((String) value);
		}
	},
	institution {
		public <T extends MObjectDTO> String get(final T user) {
			return ((UserDTO) user).getInstitution();
		}

		public <T extends MObjectDTO, K> void set(final T user, final K value) {
			((UserDTO) user).setInstitution((String) value);
		}
	},
	referenceEmail {
		public <T extends MObjectDTO> String get(final T user) {
			return ((UserDTO) user).getReferenceEmail();
		}

		public <T extends MObjectDTO, K> void set(final T user, final K value) {
			((UserDTO) user).setReferenceEmail((String) value);
		}
	}
}
