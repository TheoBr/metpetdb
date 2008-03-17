package edu.rpi.metpetdb.client.model;

import com.google.gwt.core.client.GWT;

import edu.rpi.metpetdb.client.error.InvalidPropertyException;

public class ImageOnGridDTO extends MObjectDTO {

	private int id;
	private GridDTO grid;
	private ImageDTO image;
	private int topLeftX;
	private int topLeftY;
	private int zOrder;
	private int opacity;
	private float resizeRatio;
	private int gwidth;
	private int gheight;
	private String gchecksum;
	private String gchecksum64x64;
	private String gchecksumHalf;

	public int getId() {
		return id;
	}

	public void setId(final int i) {
		id = i;
	}

	public GridDTO getGrid() {
		return grid;
	}

	public void setGrid(final GridDTO g) {
		grid = g;
	}

	public ImageDTO getImage() {
		return image;
	}

	public void setImage(final ImageDTO i) {
		image = i;
	}

	public int getGwidth() {
		return gwidth;
	}

	public void setGwidth(final int l) {
		gwidth = l;
	}

	public int getGheight() {
		return gheight;
	}

	public void setGheight(final int l) {
		gheight = l;
	}

	public String getGchecksum() {
		return gchecksum;
	}

	public void setGchecksum(final String i) {
		gchecksum = i;
	}

	public String getGchecksum64x64() {
		return gchecksum64x64;
	}

	public void setGchecksum64x64(final String s) {
		gchecksum64x64 = s;
	}

	public String getGchecksumHalf() {
		return gchecksumHalf;
	}

	public void setGchecksumHalf(final String s) {
		gchecksumHalf = s;
	}

	public int getTopLeftX() {
		return topLeftX;
	}

	public void setTopLeftX(final int s) {
		topLeftX = s;
		// originalTopLeftX = s;
	}

	public int getTopLeftY() {
		return topLeftY;
	}

	public void setTopLeftY(final int s) {
		topLeftY = s;
		// originalTopLeftY = s;
	}

	public int getZorder() {
		return zOrder;
	}

	public void setZorder(final int s) {
		zOrder = s;
	}

	public int getOpacity() {
		return opacity;
	}

	public void setOpacity(final int s) {
		opacity = s;
	}

	public float getResizeRatio() {
		return resizeRatio;
	}

	public void setResizeRatio(final float s) {
		resizeRatio = s;
	}

	public void delete() {
		this.getGrid().getImagesOnGrid().remove(this);
		this.setGrid(null);
	}

	public String get64x64ServerPath(final boolean original) {
		if (original)
			return GWT.getModuleBaseURL() + "/image/?checksum="
					+ this.getImage().getChecksum64x64();
		else
			return GWT.getModuleBaseURL() + "/image/?checksum="
					+ this.getGchecksum64x64();
	}

	public String getHalfServerPath(final boolean original) {
		if (original)
			return GWT.getModuleBaseURL() + "/image/?checksum="
					+ this.getImage().getChecksumHalf();
		else
			return GWT.getModuleBaseURL() + "/image/?checksum="
					+ this.getGchecksumHalf();
	}

	public String getServerPath(final boolean original) {
		if (original)
			return GWT.getModuleBaseURL() + "/image/?checksum="
					+ this.getImage().getChecksum();
		else
			return GWT.getModuleBaseURL() + "/image/?checksum="
					+ this.getGchecksum();
	}

	public boolean equals(final Object o) {
		return o instanceof ImageOnGridDTO
				&& grid == ((ImageOnGridDTO) o).getGrid()
				&& image == ((ImageOnGridDTO) o).getImage();
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
