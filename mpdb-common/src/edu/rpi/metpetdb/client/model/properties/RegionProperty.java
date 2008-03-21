package edu.rpi.metpetdb.client.model.properties;

import edu.rpi.metpetdb.client.model.MObjectDTO;
import edu.rpi.metpetdb.client.model.RegionDTO;

public enum RegionProperty implements Property {
	name {
		public <T extends MObjectDTO> String get(final T region) {
			return ((RegionDTO) region).getName();
		}

		public <T extends MObjectDTO, K> void set(final T region, final K name) {
			((RegionDTO) region).setName((String) name);
		}
	},
}
