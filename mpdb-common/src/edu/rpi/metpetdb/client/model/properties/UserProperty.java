package edu.rpi.metpetdb.client.model.properties;

import edu.rpi.metpetdb.client.model.User;

public enum UserProperty implements Property<User> {
	emailAddress {
		public String get(final User user) {
			return ((User) user).getEmailAddress();
		}

		public void set(final User user, final Object value) {
			((User) user).setEmailAddress((String) value);
		}
	},
	name {
		public String get(final User user) {
			return ((User) user).getName();
		}

		public void set(final User user, final Object value) {
			((User) user).setName((String) value);
		}
	},
	address {
		public String get(final User user) {
			return ((User) user).getAddress();
		}

		public void set(final User user, final Object value) {
			((User) user).setAddress((String) value);
		}
	},
	city {
		public String get(final User user) {
			return ((User) user).getCity();
		}

		public void set(final User user, final Object value) {
			((User) user).setCity((String) value);
		}
	},
	province {
		public String get(final User user) {
			return ((User) user).getProvince();
		}

		public void set(final User user, final Object value) {
			((User) user).setProvince((String) value);
		}
	},
	country {
		public String get(final User user) {
			return ((User) user).getCountry();
		}

		public void set(final User user, final Object value) {
			((User) user).setCountry((String) value);
		}
	},
	postalCode {
		public String get(final User user) {
			return ((User) user).getPostalCode();
		}

		public void set(final User user, final Object value) {
			((User) user).setPostalCode((String) value);
		}
	},
	institution {
		public String get(final User user) {
			return ((User) user).getInstitution();
		}

		public void set(final User user, final Object value) {
			((User) user).setInstitution((String) value);
		}
	},

	
	professionalUrl {
		public String get(final User user) {
			return ((User) user).getProfessionalUrl();
		}

		public void set(final User user, final Object value) {
			((User) user).setProfessionalUrl((String) value);
		}
	},
	

	researchInterests {
		public String get(final User user) {
			return ((User) user).getResearchInterests();
		}

		public void set(final User user, final Object value) {
			((User) user).setResearchInterests((String) value);
		}
	},
	
	requestContributor {
		public Boolean get(final User user) {
			return ((User) user).getRequestContributor();
		}

		public void set(final User user, final Object value) {
			((User) user).setRequestContributor((Boolean) value);
		}
	}
}
