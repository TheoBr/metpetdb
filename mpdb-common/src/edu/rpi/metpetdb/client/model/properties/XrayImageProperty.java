package edu.rpi.metpetdb.client.model.properties;

import edu.rpi.metpetdb.client.model.XrayImage;

public enum XrayImageProperty implements Property<XrayImage> {
	dwelltime {
		public Integer get(final XrayImage xrayImage) {
			return ((XrayImage) xrayImage).getDwelltime();
		}

		public void set(final XrayImage xrayImage, final Object dwelltime) {
			((XrayImage) xrayImage).setDwelltime(PropertyUtils
					.convertToInteger(dwelltime));
		}
	},
	current {
		public Integer get(final XrayImage xrayImage) {
			return ((XrayImage) xrayImage).getCurrent();
		}

		public void set(final XrayImage xrayImage, final Object current) {
			((XrayImage) xrayImage).setCurrent(PropertyUtils
					.convertToInteger(current));
		}
	},
	voltage {
		public Integer get(final XrayImage xrayImage) {
			return ((XrayImage) xrayImage).getVoltage();
		}

		public void set(final XrayImage xrayImage, final Object voltage) {
			((XrayImage) xrayImage).setVoltage(PropertyUtils
					.convertToInteger(voltage));
		}
	},
	element {
		public Object get(final XrayImage xrayImage) {
			return ((XrayImage) xrayImage).getElement();
		}

		public void set(final XrayImage xrayImage, final Object element) {
			((XrayImage) xrayImage).setElement((String) element);
		}
	};
}
