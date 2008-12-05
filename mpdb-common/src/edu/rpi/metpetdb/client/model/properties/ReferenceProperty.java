package edu.rpi.metpetdb.client.model.properties;

import edu.rpi.metpetdb.client.model.Reference;

public enum ReferenceProperty implements Property<Reference> {
	name {
		public String get(final Reference reference) {
			return ((Reference) reference).getName();
		}

		public void set(final Reference reference, final Object name) {
			((Reference) reference).setName((String) name);
		}
	},
}
