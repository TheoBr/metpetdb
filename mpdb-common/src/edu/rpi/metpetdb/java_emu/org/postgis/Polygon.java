package org.postgis;

public class Polygon extends ComposedGeom {

	public Polygon() {
		
	}
	
	public Polygon(LinearRing[] rings) {
		super(rings);
	}
	
	public LinearRing getRing(int idx) {
		return (LinearRing) subgeoms[idx];
	}
}
