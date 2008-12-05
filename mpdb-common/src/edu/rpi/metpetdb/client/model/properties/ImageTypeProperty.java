package edu.rpi.metpetdb.client.model.properties;

import edu.rpi.metpetdb.client.model.ImageType;

public enum ImageTypeProperty implements Property<ImageType> {
	imageType {
		public  String get(final ImageType imageType) {
			return ((ImageType) imageType).getImageType();
		}

		public void set(final ImageType imageType, final Object name) {
			((ImageType) imageType).setImageType((String) name);
		}
	},
}
