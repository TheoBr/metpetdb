package edu.rpi.metpetdb.client.model.properties;

import edu.rpi.metpetdb.client.model.MObjectDTO;
import edu.rpi.metpetdb.client.model.ProjectDTO;
import edu.rpi.metpetdb.client.model.UserDTO;

public enum ProjectProperty implements Property {
	name {
		public <T extends MObjectDTO> String get(final T project) {
			return ((ProjectDTO) project).getName();
		}

		public <T extends MObjectDTO, K> void set(final T project, final K name) {
			((ProjectDTO) project).setName((String) name);
		}
	},
	owner {
		public <T extends MObjectDTO> UserDTO get(final T project) {
			return ((ProjectDTO) project).getOwner();
		}

		public <T extends MObjectDTO, K> void set(final T project,
				final K owner) {
			((ProjectDTO) project).setOwner((UserDTO) owner);
		}
	};

}
