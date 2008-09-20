package edu.rpi.metpetdb.client.model.properties;

import edu.rpi.metpetdb.client.model.Element;
import edu.rpi.metpetdb.client.model.interfaces.MObject;
import edu.rpi.metpetdb.client.model.XrayImage;

public enum XrayImageProperty implements Property {
	radiation {
		public <T extends MObject> Boolean get(final T xrayImage) {
			return ((XrayImage) xrayImage).getRadiation();
		}

		public <T extends MObject, K> void set(final T xrayImage,
				final K radiation) {
			((XrayImage) xrayImage).setRadiation((Boolean) radiation);
		}
	},
	lines {
		public <T extends MObject> String get(final T xrayImage) {
			return ((XrayImage) xrayImage).getLines();
		}

		public <T extends MObject, K> void set(final T xrayImage, final K lines) {
			((XrayImage) xrayImage).setLines((String) lines);
		}
	},
	dwelltime {
		public <T extends MObject> Integer get(final T xrayImage) {
			return ((XrayImage) xrayImage).getDwelltime();
		}

		public <T extends MObject, K> void set(final T xrayImage,
				final K dwelltime) {
			((XrayImage) xrayImage).setDwelltime(PropertyUtils
					.convertToInteger(dwelltime));
		}
	},
	current {
		public <T extends MObject> Integer get(final T xrayImage) {
			return ((XrayImage) xrayImage).getCurrent();
		}

		public <T extends MObject, K> void set(final T xrayImage,
				final K current) {
			((XrayImage) xrayImage).setCurrent(PropertyUtils
					.convertToInteger(current));
		}
	},
	voltage {
		public <T extends MObject> Integer get(final T xrayImage) {
			return ((XrayImage) xrayImage).getVoltage();
		}

		public <T extends MObject, K> void set(final T xrayImage,
				final K voltage) {
			((XrayImage) xrayImage).setVoltage(PropertyUtils
					.convertToInteger(voltage));
		}
	},
	element {
		public <T extends MObject> Object get(final T xrayImage) {
			return ((XrayImage) xrayImage).getElement();
		}

		public <T extends MObject, K> void set(final T xrayImage,
				final K element) {
			((XrayImage) xrayImage).setElement((Element) element);
		}
	};
}
