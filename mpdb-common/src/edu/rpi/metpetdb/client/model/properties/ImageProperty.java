package edu.rpi.metpetdb.client.model.properties;

import edu.rpi.metpetdb.client.model.ImageDTO;
import edu.rpi.metpetdb.client.model.MObjectDTO;
import edu.rpi.metpetdb.client.model.SubsampleDTO;

public enum ImageProperty implements Property {
	imageType {
		public <T extends MObjectDTO> String get(final T image) {
			return ((ImageDTO) image).getImageType();
		}

		public <T extends MObjectDTO, K> void set(final T image,
				final K imageType) {
			((ImageDTO) image).setImageType((String) imageType);
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
			((ImageDTO) image).setPixelsize((Integer) pixelsize);
		}
	},
	contrast {
		public <T extends MObjectDTO> Integer get(final T image) {
			return ((ImageDTO) image).getContrast();
		}

		public <T extends MObjectDTO, K> void set(final T image,
				final K contrast) {
			((ImageDTO) image).setContrast((Integer) contrast);
		}
	},
	brightness {
		public <T extends MObjectDTO> Integer get(final T image) {
			return ((ImageDTO) image).getBrightness();
		}

		public <T extends MObjectDTO, K> void set(final T image,
				final K brightness) {
			((ImageDTO) image).setBrightness((Integer) brightness);
		}
	},
	lut {
		public <T extends MObjectDTO> Integer get(final T image) {
			return ((ImageDTO) image).getLut();
		}

		public <T extends MObjectDTO, K> void set(final T image, final K lut) {
			((ImageDTO) image).setLut((Integer) lut);
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
}