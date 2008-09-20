package edu.rpi.metpetdb.client.model.properties;

import edu.rpi.metpetdb.client.model.interfaces.MObject;
import edu.rpi.metpetdb.client.model.StartSessionRequest;

public enum StartSessionRequestProperty implements Property {
	username {
		public <T extends MObject> String get(final T startSessionRequest) {
			return ((StartSessionRequest) startSessionRequest)
					.getUsername();
		}

		public <T extends MObject, K> void set(final T startSessionRequest,
				final K username) {
			((StartSessionRequest) startSessionRequest)
					.setUsername((String) username);
		}
	},
	password {
		public <T extends MObject> String get(final T startSessionRequest) {
			return ((StartSessionRequest) startSessionRequest).getPassword();
		}

		public <T extends MObject, K> void set(final T startSessionRequest,
				final K password) {
			((StartSessionRequest) startSessionRequest)
					.setPassword((String) password);
		}
	},
}
