package edu.rpi.metpetdb.client.model.properties;

import edu.rpi.metpetdb.client.model.MetamorphicGrade;

public enum MetamorphicGradeProperty implements Property<MetamorphicGrade> {
	name {
		public String get(final MetamorphicGrade metamorphicGrade) {
			return ((MetamorphicGrade) metamorphicGrade).getName();
		}

		public void set(final MetamorphicGrade metamorphicGrade,
				final Object name) {
			((MetamorphicGrade) metamorphicGrade).setName((String) name);
		}
	},
	metamorphicGrade {
		public MetamorphicGrade get(final MetamorphicGrade metamorphicGrade) {
			return ((MetamorphicGrade) metamorphicGrade);
		}

		public void set(final MetamorphicGrade metamorphicGrade,
				final Object name) {
		}
	},
}
