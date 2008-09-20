package edu.rpi.metpetdb.client.model.properties;

import edu.rpi.metpetdb.client.model.interfaces.MObject;
import edu.rpi.metpetdb.client.model.User;

public enum UserProperty implements Property {
	emailAddress {
		public <T extends MObject> String get(final T user) {
			return ((User) user).getEmailAddress();
		}

		public <T extends MObject, K> void set(final T user, final K value) {
			((User) user).setEmailAddress((String) value);
		}
	},
	name {
		public <T extends MObject> String get(final T user) {
			return ((User) user).getName();
		}

		public <T extends MObject, K> void set(final T user, final K value) {
			((User) user).setName((String) value);
		}
	},
	address {
		public <T extends MObject> String get(final T user) {
			return ((User) user).getAddress();
		}

		public <T extends MObject, K> void set(final T user, final K value) {
			((User) user).setAddress((String) value);
		}
	},
	city {
		public <T extends MObject> String get(final T user) {
			return ((User) user).getCity();
		}

		public <T extends MObject, K> void set(final T user, final K value) {
			((User) user).setCity((String) value);
		}
	},
	province {
		public <T extends MObject> String get(final T user) {
			return ((User) user).getProvince();
		}

		public <T extends MObject, K> void set(final T user, final K value) {
			((User) user).setProvince((String) value);
		}
	},
	country {
		public <T extends MObject> String get(final T user) {
			return ((User) user).getCountry();
		}

		public <T extends MObject, K> void set(final T user, final K value) {
			((User) user).setCountry((String) value);
		}
	},
	postalCode {
		public <T extends MObject> String get(final T user) {
			return ((User) user).getPostalCode();
		}

		public <T extends MObject, K> void set(final T user, final K value) {
			((User) user).setPostalCode((String) value);
		}
	},
	institution {
		public <T extends MObject> String get(final T user) {
			return ((User) user).getInstitution();
		}

		public <T extends MObject, K> void set(final T user, final K value) {
			((User) user).setInstitution((String) value);
		}
	},
	referenceEmail {
		public <T extends MObject> String get(final T user) {
			return ((User) user).getReferenceEmail();
		}

		public <T extends MObject, K> void set(final T user, final K value) {
			((User) user).setReferenceEmail((String) value);
		}
	}
}
