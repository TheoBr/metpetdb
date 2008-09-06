package edu.rpi.metpetdb.client.model.properties;

import edu.rpi.metpetdb.client.model.ImageTypeDTO;
import edu.rpi.metpetdb.client.model.MObjectDTO;

public enum ImageTypeProperty implements Property {
	imageType {
		public <T extends MObjectDTO> String get(final T imageType) {
			return ((ImageTypeDTO) imageType).getImageType();
		}

		public <T extends MObjectDTO, K> void set(final T imageType,
				final K name) {
			((ImageTypeDTO) imageType).setImageType((String) name);
		}
	},
}
