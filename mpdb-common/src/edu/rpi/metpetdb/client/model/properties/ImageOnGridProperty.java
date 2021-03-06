package edu.rpi.metpetdb.client.model.properties;

import edu.rpi.metpetdb.client.model.ImageOnGrid;

public enum ImageOnGridProperty implements Property<ImageOnGrid> {
	topLeftX {
		public Object get(final ImageOnGrid image) {
			return image.getTopLeftX();
		}

		public void set(final ImageOnGrid image, final Object imageType) {
			image.setTopLeftX(PropertyUtils.convertToDouble(imageType));
		}
	},
	topLeftY {
		public Object get(final ImageOnGrid image) {
			return image.getTopLeftY();
		}

		public void set(final ImageOnGrid image, final Object imageType) {
			image.setTopLeftY(PropertyUtils.convertToDouble(imageType));
		}
	},
	locked {
		public Object get(final ImageOnGrid image) {
			return image.isLocked();
		}
		
		public void set(final ImageOnGrid image, final Object locked){
			image.setLocked((Boolean)locked);
		}
	}
}
