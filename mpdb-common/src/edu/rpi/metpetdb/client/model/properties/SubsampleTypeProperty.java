package edu.rpi.metpetdb.client.model.properties;

import edu.rpi.metpetdb.client.model.MObjectDTO;
import edu.rpi.metpetdb.client.model.SubsampleTypeDTO;

public enum SubsampleTypeProperty implements Property {
	subsampleType {
		public <T extends MObjectDTO> String get(final T subsampleType) {
			return ((SubsampleTypeDTO) subsampleType).getSubsampleType();
		}

		public <T extends MObjectDTO, K> void set(final T subsampleType,
				final K name) {
			((SubsampleTypeDTO) subsampleType).setSubsampleType((String) name);
		}
	},
}
