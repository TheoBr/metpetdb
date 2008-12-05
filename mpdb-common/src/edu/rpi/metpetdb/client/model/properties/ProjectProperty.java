package edu.rpi.metpetdb.client.model.properties;

import edu.rpi.metpetdb.client.model.Project;
import edu.rpi.metpetdb.client.model.User;

public enum ProjectProperty implements Property<Project> {
	name {
		public String get(final Project project) {
			return ((Project) project).getName();
		}

		public void set(final Project project, final Object name) {
			((Project) project).setName((String) name);
		}
	},
	owner {
		public User get(final Project project) {
			return ((Project) project).getOwner();
		}

		public void set(final Project project, final Object owner) {
			((Project) project).setOwner((User) owner);
		}
	},
	memberCount {
		public Integer get(final Project project) {
			return ((Project) project).getMembers().size();
		}

		public void set(final Project project, final Object subsampleCount) {
		}
	},

}
