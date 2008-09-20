package edu.rpi.metpetdb.client.model.properties;

import edu.rpi.metpetdb.client.model.Element;
import edu.rpi.metpetdb.client.model.interfaces.MObject;

public enum MineralProperty implements Property {
	name {
		public <T extends MObject> String get(final T element) {
			return ((Element) element).getName();
		}

		public <T extends MObject, K> void set(final T element, final K name) {
			((Element) element).setName((String) name);
		}
	},
}
