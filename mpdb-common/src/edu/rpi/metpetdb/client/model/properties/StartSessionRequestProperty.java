package edu.rpi.metpetdb.client.model.properties;

import edu.rpi.metpetdb.client.model.MObjectDTO;
import edu.rpi.metpetdb.client.model.StartSessionRequestDTO;

public enum StartSessionRequestProperty implements Property {
	emailAddress {
		public <T extends MObjectDTO> String get(final T startSessionRequest) {
			return ((StartSessionRequestDTO) startSessionRequest).getEmailAddress();
		}

		public <T extends MObjectDTO, K> void set(final T startSessionRequest,
				final K username) {
			((StartSessionRequestDTO) startSessionRequest)
					.setEmailAddress((String) username);
		}
	},
	password {
		public <T extends MObjectDTO> String get(final T startSessionRequest) {
			return ((StartSessionRequestDTO) startSessionRequest).getPassword();
		}

		public <T extends MObjectDTO, K> void set(final T startSessionRequest,
				final K password) {
			((StartSessionRequestDTO) startSessionRequest)
					.setPassword((String) password);
		}
	},
}
