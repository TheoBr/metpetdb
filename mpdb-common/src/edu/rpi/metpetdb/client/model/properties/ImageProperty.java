package edu.rpi.metpetdb.client.model.properties;

import java.util.Set;

import edu.rpi.metpetdb.client.model.Image;
import edu.rpi.metpetdb.client.model.ImageComment;
import edu.rpi.metpetdb.client.model.ImageType;
import edu.rpi.metpetdb.client.model.interfaces.MObject;
import edu.rpi.metpetdb.client.model.Subsample;

public enum ImageProperty implements Property {
	imageType {
		public <T extends MObject> Object get(final T image) {
			return ((Image) image).getImageType();
		}

		public <T extends MObject, K> void set(final T image, final K imageType) {
			((Image) image).setImageType((ImageType) imageType);
		}
	},
	subsample {
		public <T extends MObject> Subsample get(final T image) {
			return ((Image) image).getSubsample();
		}

		public <T extends MObject, K> void set(final T image, final K subsample) {
			((Image) image).setSubsample((Subsample) subsample);
		}
	},
	pixelsize {
		public <T extends MObject> Integer get(final T image) {
			return ((Image) image).getPixelsize();
		}

		public <T extends MObject, K> void set(final T image, final K pixelsize) {
			((Image) image).setPixelsize(PropertyUtils
					.convertToInteger(pixelsize));
		}
	},
	contrast {
		public <T extends MObject> Integer get(final T image) {
			return ((Image) image).getContrast();
		}

		public <T extends MObject, K> void set(final T image, final K contrast) {
			((Image) image).setContrast(PropertyUtils
					.convertToInteger(contrast));
		}
	},
	brightness {
		public <T extends MObject> Integer get(final T image) {
			return ((Image) image).getBrightness();
		}

		public <T extends MObject, K> void set(final T image, final K brightness) {
			((Image) image).setBrightness(PropertyUtils
					.convertToInteger(brightness));
		}
	},
	lut {
		public <T extends MObject> Integer get(final T image) {
			return ((Image) image).getLut();
		}

		public <T extends MObject, K> void set(final T image, final K lut) {
			((Image) image).setLut(PropertyUtils.convertToInteger(lut));
		}
	},
	checksum {
		public <T extends MObject> String get(final T image) {
			return ((Image) image).getChecksum();
		}

		public <T extends MObject, K> void set(final T image, final K checksum) {
			((Image) image).setChecksum((String) checksum);
		}
	},
	scale {
		public <T extends MObject> Integer get(final T image) {
			return ((Image) image).getScale();
		}

		public <T extends MObject, K> void set(final T image, final K scale) {
			((Image) image).setScale(PropertyUtils.convertToInteger(scale));
		}
	},
	comments {
		public <T extends MObject> Object get(final T sample) {
			return ((Image) sample).getComments();
		}

		public <T extends MObject, K> void set(final T sample, final K comments) {
			((Image) sample).setComments((Set<ImageComment>) comments);
		}
	},
}
