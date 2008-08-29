package org.postgis;

public class LinearRing extends PointComposedGeom {
	public LinearRing() {
		
	}
	public LinearRing(Point[] points) {
		super(points);
	}
	
	public int numPoints() {
		return subgeoms.length;
	}
	
	public Point getPoint(int idx) {
		return (Point) subgeoms[idx];
	}
}
