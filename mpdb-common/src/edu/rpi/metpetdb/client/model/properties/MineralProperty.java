package edu.rpi.metpetdb.client.model.properties;

import edu.rpi.metpetdb.client.model.Mineral;

public enum MineralProperty implements Property<Mineral> {
	name {
		public  String get(final Mineral mineral) {
			return mineral.getName();
		}

		public void set(final Mineral mineral, final Object name) {
			mineral.setName((String) name);
		}
	},
}
