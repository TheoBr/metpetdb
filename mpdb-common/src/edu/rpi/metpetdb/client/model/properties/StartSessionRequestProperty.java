package edu.rpi.metpetdb.client.model.properties;

import edu.rpi.metpetdb.client.model.StartSessionRequest;

public enum StartSessionRequestProperty
		implements
		Property<StartSessionRequest> {
	emailAddress {
		public String get(final StartSessionRequest startSessionRequest) {
			return ((StartSessionRequest) startSessionRequest)
					.getEmailAddress();
		}

		public void set(final StartSessionRequest startSessionRequest,
				final Object userEmail) {
			((StartSessionRequest) startSessionRequest)
					.setEmailAddress((String) userEmail);
		}
	},
	password {
		public String get(final StartSessionRequest startSessionRequest) {
			return ((StartSessionRequest) startSessionRequest).getPassword();
		}

		public void set(final StartSessionRequest startSessionRequest,
				final Object password) {
			((StartSessionRequest) startSessionRequest)
					.setPassword((String) password);
		}
	},
}
