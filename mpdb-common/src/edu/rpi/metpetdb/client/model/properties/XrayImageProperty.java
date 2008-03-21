package edu.rpi.metpetdb.client.model.properties;

import edu.rpi.metpetdb.client.model.MObjectDTO;
import edu.rpi.metpetdb.client.model.SubsampleDTO;
import edu.rpi.metpetdb.client.model.XrayImageDTO;

public enum XrayImageProperty implements Property {
	imageType {
		public <T extends MObjectDTO> String get(final T xrayImage) {
			return ((XrayImageDTO) xrayImage).getImageType();
		}

		public <T extends MObjectDTO, K> void set(final T xrayImage,
				final K imageType) {
			((XrayImageDTO) xrayImage).setImageType((String) imageType);
		}
	},
	subsample {
		public <T extends MObjectDTO> SubsampleDTO get(final T xrayImage) {
			return ((XrayImageDTO) xrayImage).getSubsample();
		}

		public <T extends MObjectDTO, K> void set(final T xrayImage,
				final K subsample) {
			((XrayImageDTO) xrayImage).setSubsample((SubsampleDTO) subsample);
		}
	},
	pixelsize {
		public <T extends MObjectDTO> Integer get(final T xrayImage) {
			return ((XrayImageDTO) xrayImage).getPixelsize();
		}

		public <T extends MObjectDTO, K> void set(final T xrayImage,
				final K pixelsize) {
			((XrayImageDTO) xrayImage).setPixelsize((Integer) pixelsize);
		}
	},
	contrast {
		public <T extends MObjectDTO> Integer get(final T xrayImage) {
			return ((XrayImageDTO) xrayImage).getContrast();
		}

		public <T extends MObjectDTO, K> void set(final T xrayImage,
				final K contrast) {
			((XrayImageDTO) xrayImage).setContrast((Integer) contrast);
		}
	},
	brightness {
		public <T extends MObjectDTO> Integer get(final T xrayImage) {
			return ((XrayImageDTO) xrayImage).getBrightness();
		}

		public <T extends MObjectDTO, K> void set(final T xrayImage,
				final K brightness) {
			((XrayImageDTO) xrayImage).setBrightness((Integer) brightness);
		}
	},
	lut {
		public <T extends MObjectDTO> Integer get(final T xrayImage) {
			return ((XrayImageDTO) xrayImage).getLut();
		}

		public <T extends MObjectDTO, K> void set(final T xrayImage, final K lut) {
			((XrayImageDTO) xrayImage).setLut((Integer) lut);
		}
	},
	radiation {
		public <T extends MObjectDTO> Boolean get(final T xrayImage) {
			return ((XrayImageDTO) xrayImage).getRadiation();
		}

		public <T extends MObjectDTO, K> void set(final T xrayImage,
				final K radiation) {
			((XrayImageDTO) xrayImage).setRadiation((Boolean) radiation);
		}
	},
	lines {
		public <T extends MObjectDTO> String get(final T xrayImage) {
			return ((XrayImageDTO) xrayImage).getLines();
		}

		public <T extends MObjectDTO, K> void set(final T xrayImage,
				final K lines) {
			((XrayImageDTO) xrayImage).setLines((String) lines);
		}
	},
	dwelltime {
		public <T extends MObjectDTO> Integer get(final T xrayImage) {
			return ((XrayImageDTO) xrayImage).getDwelltime();
		}

		public <T extends MObjectDTO, K> void set(final T xrayImage,
				final K dwelltime) {
			((XrayImageDTO) xrayImage).setDwelltime((Integer) dwelltime);
		}
	},
	current {
		public <T extends MObjectDTO> Integer get(final T xrayImage) {
			return ((XrayImageDTO) xrayImage).getCurrent();
		}

		public <T extends MObjectDTO, K> void set(final T xrayImage,
				final K current) {
			((XrayImageDTO) xrayImage).setCurrent((Integer) current);
		}
	},
	voltage {
		public <T extends MObjectDTO> Integer get(final T xrayImage) {
			return ((XrayImageDTO) xrayImage).getVoltage();
		}

		public <T extends MObjectDTO, K> void set(final T xrayImage,
				final K voltage) {
			((XrayImageDTO) xrayImage).setVoltage((Integer) voltage);
		}
	},
	element {
		public <T extends MObjectDTO> Integer get(final T xrayImage) {
			return ((XrayImageDTO) xrayImage).getLut();
		}

		public <T extends MObjectDTO, K> void set(final T xrayImage, final K element) {
			((XrayImageDTO) xrayImage).setLut((Integer) element);
		}
	};
}
