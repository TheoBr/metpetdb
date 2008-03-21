package edu.rpi.metpetdb.client.model.properties;

import edu.rpi.metpetdb.client.model.MObjectDTO;
import edu.rpi.metpetdb.client.model.ReferenceDTO;

public enum ReferenceProperty implements Property {
	name {
		public <T extends MObjectDTO> String get(final T reference) {
			return ((ReferenceDTO) reference).getName();
		}

		public <T extends MObjectDTO, K> void set(final T reference,
				final K name) {
			((ReferenceDTO) reference).setName((String) name);
		}
	},
}
