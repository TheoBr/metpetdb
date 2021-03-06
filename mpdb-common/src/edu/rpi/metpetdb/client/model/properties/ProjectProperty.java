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
	description {
		public String get(final Project project) {
			return ((Project) project).getDescription();
		}
		public void set(final Project project, final Object description) {
			((Project) project).setDescription((String) description);
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
			return ((Project) project).getMemberCount();
		}

		public void set(final Project project, final Object memberCount) {
			project.setMemberCount((Integer) memberCount);
		}
	},

}
