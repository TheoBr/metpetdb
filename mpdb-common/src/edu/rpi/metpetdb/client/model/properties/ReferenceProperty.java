package edu.rpi.metpetdb.client.model.properties;

import edu.rpi.metpetdb.client.model.interfaces.MObject;
import edu.rpi.metpetdb.client.model.Reference;

public enum ReferenceProperty implements Property {
	name {
		public <T extends MObject> String get(final T reference) {
			return ((Reference) reference).getName();
		}

		public <T extends MObject, K> void set(final T reference, final K name) {
			((Reference) reference).setName((String) name);
		}
	},
}
