package edu.rpi.metpetdb.client.model;

import java.util.HashSet;
import java.util.Set;

public class Grid extends MObject {
	private static final long serialVersionUID = 1L;

	private int id;
	private int version;
	private int width;
	private int height;
	private Subsample Subsample;
	private Set<ImageOnGrid> imagesOnGrid;

	public int getId() {
		return id;
	}

	public void setId(final int i) {
		id = i;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(final int v) {
		version = v;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(final int s) {
		width = s;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(final int s) {
		height = s;
	}

	public Subsample getSubsample() {
		return Subsample;
	}

	public void setSubsample(final Subsample s) {
		Subsample = s;
	}

	public Set<ImageOnGrid> getImagesOnGrid() {
		if (imagesOnGrid == null)
			imagesOnGrid = new HashSet<ImageOnGrid>();
		return imagesOnGrid;
	}

	public void setImagesOnGrid(final Set<ImageOnGrid> s) {
		imagesOnGrid = s;
	}

	public void addImageOnGrid(final ImageOnGrid imageOnGrid) {
		imageOnGrid.setGrid(this);
		if (imagesOnGrid == null)
			imagesOnGrid = new HashSet<ImageOnGrid>();
		imagesOnGrid.add(imageOnGrid);
	}

	public boolean equals(final Object o) {
		return o instanceof Grid && id == ((Grid) o).id;
	}

	public int hashCode() {
		return id;
	}

	public boolean mIsNew() {
		return id == 0;
	}
}