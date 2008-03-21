package edu.rpi.metpetdb.client.model.properties;

import edu.rpi.metpetdb.client.model.MObjectDTO;
import edu.rpi.metpetdb.client.model.MetamorphicGradeDTO;

public enum MetamorphicGradeProperty implements Property {
	name {
		public <T extends MObjectDTO> String get(final T metamorphicGrade) {
			return ((MetamorphicGradeDTO) metamorphicGrade).getName();
		}

		public <T extends MObjectDTO, K> void set(final T metamorphicGrade,
				final K name) {
			((MetamorphicGradeDTO) metamorphicGrade).setName((String) name);
		}
	},
}
