package edu.rpi.metpetdb.client.model.properties;

import edu.rpi.metpetdb.client.model.GridDTO;
import edu.rpi.metpetdb.client.model.MObjectDTO;

public enum GridProperty implements Property {

	width {
		public <T extends MObjectDTO> Integer get(final T grid) {
			return ((GridDTO) grid).getWidth();
		}

		public <T extends MObjectDTO, K> void set(final T grid, final K width) {
			((GridDTO) grid).setWidth((Integer) width);
		}
	},
	height {
		public <T extends MObjectDTO> Integer get(final T grid) {
			return ((GridDTO) grid).getHeight();
		}

		public <T extends MObjectDTO, K> void set(final T grid, final K height) {
			((GridDTO) grid).setHeight((Integer) height);
		}
	};

}
