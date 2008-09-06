package edu.rpi.metpetdb.client.model.properties;

import java.util.Set;

import edu.rpi.metpetdb.client.model.ImageCommentDTO;
import edu.rpi.metpetdb.client.model.ImageDTO;
import edu.rpi.metpetdb.client.model.ImageTypeDTO;
import edu.rpi.metpetdb.client.model.MObjectDTO;
import edu.rpi.metpetdb.client.model.SubsampleDTO;

public enum ImageProperty implements Property {
	imageType {
		public <T extends MObjectDTO> Object get(final T image) {
			return ((ImageDTO) image).getImageType();
		}

		public <T extends MObjectDTO, K> void set(final T image,
				final K imageType) {
			((ImageDTO) image).setImageType((ImageTypeDTO) imageType);
		}
	},
	subsample {
		public <T extends MObjectDTO> SubsampleDTO get(final T image) {
			return ((ImageDTO) image).getSubsample();
		}

		public <T extends MObjectDTO, K> void set(final T image,
				final K subsample) {
			((ImageDTO) image).setSubsample((SubsampleDTO) subsample);
		}
	},
	pixelsize {
		public <T extends MObjectDTO> Integer get(final T image) {
			return ((ImageDTO) image).getPixelsize();
		}

		public <T extends MObjectDTO, K> void set(final T image,
				final K pixelsize) {
			((ImageDTO) image).setPixelsize(PropertyUtils
					.convertToInteger(pixelsize));
		}
	},
	contrast {
		public <T extends MObjectDTO> Integer get(final T image) {
			return ((ImageDTO) image).getContrast();
		}

		public <T extends MObjectDTO, K> void set(final T image,
				final K contrast) {
			((ImageDTO) image).setContrast(PropertyUtils
					.convertToInteger(contrast));
		}
	},
	brightness {
		public <T extends MObjectDTO> Integer get(final T image) {
			return ((ImageDTO) image).getBrightness();
		}

		public <T extends MObjectDTO, K> void set(final T image,
				final K brightness) {
			((ImageDTO) image).setBrightness(PropertyUtils
					.convertToInteger(brightness));
		}
	},
	lut {
		public <T extends MObjectDTO> Integer get(final T image) {
			return ((ImageDTO) image).getLut();
		}

		public <T extends MObjectDTO, K> void set(final T image, final K lut) {
			((ImageDTO) image).setLut(PropertyUtils.convertToInteger(lut));
		}
	},
	checksum {
		public <T extends MObjectDTO> String get(final T image) {
			return ((ImageDTO) image).getChecksum();
		}

		public <T extends MObjectDTO, K> void set(final T image,
				final K checksum) {
			((ImageDTO) image).setChecksum((String) checksum);
		}
	},
	scale {
		public <T extends MObjectDTO> Integer get(final T image) {
			return ((ImageDTO) image).getScale();
		}

		public <T extends MObjectDTO, K> void set(final T image,
				final K scale) {
			((ImageDTO) image).setScale(PropertyUtils.convertToInteger(scale));
		}
	},
	comments {
		public <T extends MObjectDTO> Object get(final T sample) {
			return ((ImageDTO) sample).getComments();
		}

		public <T extends MObjectDTO, K> void set(final T sample,
				final K comments) {
			((ImageDTO) sample).setComments((Set<ImageCommentDTO>) comments);
		}
	},
}
