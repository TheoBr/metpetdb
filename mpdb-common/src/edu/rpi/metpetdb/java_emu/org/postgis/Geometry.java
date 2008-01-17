package org.postgis;

/* Emulation of org.postgis.Geometry for browser. */
public abstract class Geometry {
	public int dimension;

	public int srid;

	protected Geometry() {
	}

	protected Geometry(int dim, int rid) {
		dimension = dim;
		srid = rid;
	}

}