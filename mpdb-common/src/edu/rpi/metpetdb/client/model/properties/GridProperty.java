package edu.rpi.metpetdb.client.model.properties;

import edu.rpi.metpetdb.client.model.Grid;

public enum GridProperty implements Property<Grid> {

	width {
		public Integer get(final Grid grid) {
			return ((Grid) grid).getWidth();
		}

		public void set(final Grid grid, final Object width) {
			((Grid) grid).setWidth((Integer) width);
		}
	},
	height {
		public Integer get(final Grid grid) {
			return ((Grid) grid).getHeight();
		}

		public void set(final Grid grid, final Object height) {
			((Grid) grid).setHeight((Integer) height);
		}
	};

}
