package edu.rpi.metpetdb.client.model.properties;

import edu.rpi.metpetdb.client.model.interfaces.MObject;
import edu.rpi.metpetdb.client.model.Project;
import edu.rpi.metpetdb.client.model.User;

public enum ProjectProperty implements Property {
	name {
		public <T extends MObject> String get(final T project) {
			return ((Project) project).getName();
		}

		public <T extends MObject, K> void set(final T project, final K name) {
			((Project) project).setName((String) name);
		}
	},
	owner {
		public <T extends MObject> User get(final T project) {
			return ((Project) project).getOwner();
		}

		public <T extends MObject, K> void set(final T project, final K owner) {
			((Project) project).setOwner((User) owner);
		}
	},
	memberCount {
		public <T extends MObject> Integer get(final T project) {
			return ((Project) project).getMembers().size();
		}

		public <T extends MObject, K> void set(final T project,
				final K subsampleCount) {
		}
	},

}
