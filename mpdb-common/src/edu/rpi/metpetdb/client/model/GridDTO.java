package edu.rpi.metpetdb.client.model;

import java.util.HashSet;
import java.util.Set;

import edu.rpi.metpetdb.client.error.InvalidPropertyException;

public class GridDTO extends MObjectDTO {
	public static final int P_width = 0;
	public static final int P_height = 1;

	private int id;
	private int version;
	private int width;
	private int height;
	private SubsampleDTO subsample;
	private Set<ImageOnGridDTO> imagesOnGrid;

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

	public SubsampleDTO getSubsample() {
		return subsample;
	}

	public void setSubsample(final SubsampleDTO s) {
		subsample = s;
	}

	public Set<ImageOnGridDTO> getImagesOnGrid() {
		if (imagesOnGrid == null)
			imagesOnGrid = new HashSet<ImageOnGridDTO>();
		return imagesOnGrid;
	}
	public void setImagesOnGrid(final Set<ImageOnGridDTO> s) {
		imagesOnGrid = s;
	}
	public void addImageOnGrid(final ImageOnGridDTO imageOnGrid) {
		imageOnGrid.setGrid(this);
		if (imagesOnGrid == null)
			imagesOnGrid = new HashSet<ImageOnGridDTO>();
		imagesOnGrid.add(imageOnGrid);
	}

	public boolean equals(final Object o) {
		return o instanceof GridDTO && id == ((GridDTO) o).id;
	}

	public int hashCode() {
		return id;
	}

	public boolean mIsNew() {
		return id == 0;
	}
	protected Object mSetGet(final int propertyId, final Object newValue) {
		switch (propertyId) {
		}
		throw new InvalidPropertyException(propertyId);
	}
}
