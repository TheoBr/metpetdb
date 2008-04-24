package edu.rpi.metpetdb.client.model.properties;

import edu.rpi.metpetdb.client.model.ElementDTO;
import edu.rpi.metpetdb.client.model.MObjectDTO;
import edu.rpi.metpetdb.client.model.XrayImageDTO;

public enum XrayImageProperty implements Property {
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
			((XrayImageDTO) xrayImage).setDwelltime(PropertyUtils
					.convertToInteger(dwelltime));
		}
	},
	current {
		public <T extends MObjectDTO> Integer get(final T xrayImage) {
			return ((XrayImageDTO) xrayImage).getCurrent();
		}

		public <T extends MObjectDTO, K> void set(final T xrayImage,
				final K current) {
			((XrayImageDTO) xrayImage).setCurrent(PropertyUtils
					.convertToInteger(current));
		}
	},
	voltage {
		public <T extends MObjectDTO> Integer get(final T xrayImage) {
			return ((XrayImageDTO) xrayImage).getVoltage();
		}

		public <T extends MObjectDTO, K> void set(final T xrayImage,
				final K voltage) {
			((XrayImageDTO) xrayImage).setVoltage(PropertyUtils
					.convertToInteger(voltage));
		}
	},
	element {
		public <T extends MObjectDTO> Object get(final T xrayImage) {
			return ((XrayImageDTO) xrayImage).getElement();
		}

		public <T extends MObjectDTO, K> void set(final T xrayImage,
				final K element) {
			((XrayImageDTO) xrayImage).setElement((ElementDTO) element);
		}
	};
}
