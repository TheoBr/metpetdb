package org.postgis;

public abstract class ComposedGeom extends Geometry {
	
	protected Geometry[] subgeoms;
	
	public ComposedGeom() {
	}
	
	public ComposedGeom(Geometry[] geoms) {
		this.subgeoms = geoms;
		if (geoms.length > 0) {
            dimension = geoms[0].dimension;
        } else {
            dimension = 0;
        }
	}
}
