package edu.rpi.metpetdb.client.model.properties;


import edu.rpi.metpetdb.client.model.MetamorphicRegion;

public enum MetamorphicRegionProperty implements Property<MetamorphicRegion> {
	name {
		public String get(final MetamorphicRegion metamorphicRegion) {
			return ((MetamorphicRegion) metamorphicRegion).getName();
		}

		public void set(final MetamorphicRegion metamorphicRegion,
				final Object name) {
			((MetamorphicRegion) metamorphicRegion).setName((String) name);
		}
	},
	description {
		public String get(final MetamorphicRegion metamorphicRegion){
			return ((MetamorphicRegion) metamorphicRegion).getDescription();
		}
		public void set(final MetamorphicRegion metamorphicRegion, final Object description){
			((MetamorphicRegion) metamorphicRegion).setDescription((String) description);
		}
	},
	metamorphicRegion {
		public MetamorphicRegion get(final MetamorphicRegion metamorphicRegion) {
			return ((MetamorphicRegion) metamorphicRegion);
		}

		public void set(final MetamorphicRegion metamorphicRegion,
				final Object name) {
		}
	},
}
