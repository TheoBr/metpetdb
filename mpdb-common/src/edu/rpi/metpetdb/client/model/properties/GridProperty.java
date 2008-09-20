package edu.rpi.metpetdb.client.model.properties;

import edu.rpi.metpetdb.client.model.Grid;
import edu.rpi.metpetdb.client.model.interfaces.MObject;

public enum GridProperty implements Property {

	width {
		public <T extends MObject> Integer get(final T grid) {
			return ((Grid) grid).getWidth();
		}

		public <T extends MObject, K> void set(final T grid, final K width) {
			((Grid) grid).setWidth((Integer) width);
		}
	},
	height {
		public <T extends MObject> Integer get(final T grid) {
			return ((Grid) grid).getHeight();
		}

		public <T extends MObject, K> void set(final T grid, final K height) {
			((Grid) grid).setHeight((Integer) height);
		}
	};

}
