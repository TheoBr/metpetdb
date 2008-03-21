package edu.rpi.metpetdb.client.model.properties;

import edu.rpi.metpetdb.client.model.ImageTypeDTO;
import edu.rpi.metpetdb.client.model.MObjectDTO;

public enum ImageTypeProperty implements Property {
	name {
		public <T extends MObjectDTO> String get(final T imageType) {
			return ((ImageTypeDTO) imageType).getName();
		}

		public <T extends MObjectDTO, K> void set(final T imageType,
				final K name) {
			((ImageTypeDTO) imageType).setName((String) name);
		}
	},
}
