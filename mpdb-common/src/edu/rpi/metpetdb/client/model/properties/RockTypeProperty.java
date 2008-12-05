package edu.rpi.metpetdb.client.model.properties;

import edu.rpi.metpetdb.client.model.RockType;

public enum RockTypeProperty implements Property<RockType> {
	rockType {
		public  String get(final RockType rockType) {
			return ((RockType) rockType).getRockType();
		}

		public void set(final RockType rockType, final Object name) {
			((RockType) rockType).setRockType((String) name);
		}
	}
}
