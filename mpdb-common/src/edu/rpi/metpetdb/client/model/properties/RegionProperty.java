package edu.rpi.metpetdb.client.model.properties;

import edu.rpi.metpetdb.client.model.Region;

public enum RegionProperty implements Property<Region> {
	name {
		public  String get(final Region region) {
			return ((Region) region).getName();
		}

		public void set(final Region region, final Object name) {
			((Region) region).setName((String) name);
		}
	},
}
