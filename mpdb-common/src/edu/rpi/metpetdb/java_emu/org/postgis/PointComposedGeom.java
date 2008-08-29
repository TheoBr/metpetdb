package org.postgis;

public abstract class PointComposedGeom extends ComposedGeom {
	public PointComposedGeom() {
		
	}
	
	public PointComposedGeom(Point[] points) {
		super(points);
	}
}
