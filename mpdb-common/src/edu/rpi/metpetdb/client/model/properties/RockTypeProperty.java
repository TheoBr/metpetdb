package edu.rpi.metpetdb.client.model.properties;

import edu.rpi.metpetdb.client.model.interfaces.MObject;
import edu.rpi.metpetdb.client.model.RockType;

public enum RockTypeProperty implements Property {
	rockType {
		public <T extends MObject> String get(final T rockType) {
			return ((RockType) rockType).getRockType();
		}

		public <T extends MObject, K> void set(final T rockType, final K name) {
			((RockType) rockType).setRockType((String) name);
		}
	}
}
