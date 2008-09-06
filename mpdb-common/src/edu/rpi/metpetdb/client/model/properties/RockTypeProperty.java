package edu.rpi.metpetdb.client.model.properties;

import edu.rpi.metpetdb.client.model.MObjectDTO;
import edu.rpi.metpetdb.client.model.RockTypeDTO;

public enum RockTypeProperty implements Property {
	rockType {
		public <T extends MObjectDTO> String get(final T rockType) {
			return ((RockTypeDTO) rockType).getRockType();
		}

		public <T extends MObjectDTO, K> void set(final T rockType,
				final K name) {
			((RockTypeDTO) rockType).setRockType((String) name);
		}
	}
}
