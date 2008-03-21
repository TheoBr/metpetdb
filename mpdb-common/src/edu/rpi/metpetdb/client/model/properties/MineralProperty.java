package edu.rpi.metpetdb.client.model.properties;

import edu.rpi.metpetdb.client.model.ElementDTO;
import edu.rpi.metpetdb.client.model.MObjectDTO;

public enum MineralProperty implements Property {
	name {
		public <T extends MObjectDTO> String get(final T element) {
			return ((ElementDTO) element).getName();
		}

		public <T extends MObjectDTO, K> void set(final T element, final K name) {
			((ElementDTO) element).setName((String) name);
		}
	},
}
