package edu.rpi.metpetdb.client.model.properties;

import edu.rpi.metpetdb.client.model.ImageType;
import edu.rpi.metpetdb.client.model.interfaces.MObject;

public enum ImageTypeProperty implements Property {
	imageType {
		public <T extends MObject> String get(final T imageType) {
			return ((ImageType) imageType).getImageType();
		}

		public <T extends MObject, K> void set(final T imageType, final K name) {
			((ImageType) imageType).setImageType((String) name);
		}
	},
}
