package edu.rpi.metpetdb.client.model.properties;

import edu.rpi.metpetdb.client.model.interfaces.MObject;
import edu.rpi.metpetdb.client.model.Region;

public enum RegionProperty implements Property {
	name {
		public <T extends MObject> String get(final T region) {
			return ((Region) region).getName();
		}

		public <T extends MObject, K> void set(final T region, final K name) {
			((Region) region).setName((String) name);
		}
	},
}
