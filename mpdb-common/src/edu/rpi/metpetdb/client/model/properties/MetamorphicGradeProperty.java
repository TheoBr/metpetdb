package edu.rpi.metpetdb.client.model.properties;

import edu.rpi.metpetdb.client.model.interfaces.MObject;
import edu.rpi.metpetdb.client.model.MetamorphicGrade;

public enum MetamorphicGradeProperty implements Property {
	name {
		public <T extends MObject> String get(final T metamorphicGrade) {
			return ((MetamorphicGrade) metamorphicGrade).getName();
		}

		public <T extends MObject, K> void set(final T metamorphicGrade,
				final K name) {
			((MetamorphicGrade) metamorphicGrade).setName((String) name);
		}
	},
}
