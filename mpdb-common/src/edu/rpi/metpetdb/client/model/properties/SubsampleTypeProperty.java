package edu.rpi.metpetdb.client.model.properties;

import edu.rpi.metpetdb.client.model.interfaces.MObject;
import edu.rpi.metpetdb.client.model.SubsampleType;

public enum SubsampleTypeProperty implements Property {
	subsampleType {
		public <T extends MObject> String get(final T subsampleType) {
			return ((SubsampleType) subsampleType).getSubsampleType();
		}

		public <T extends MObject, K> void set(final T subsampleType,
				final K name) {
			((SubsampleType) subsampleType).setSubsampleType((String) name);
		}
	},
}
