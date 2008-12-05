package edu.rpi.metpetdb.client.model.properties;

import edu.rpi.metpetdb.client.model.SubsampleType;

public enum SubsampleTypeProperty implements Property<SubsampleType> {
	subsampleType {
		public  String get(final SubsampleType subsampleType) {
			return ((SubsampleType) subsampleType).getSubsampleType();
		}

		public void set(final SubsampleType subsampleType,
				final Object name) {
			((SubsampleType) subsampleType).setSubsampleType((String) name);
		}
	},
}
