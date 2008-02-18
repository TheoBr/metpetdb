package edu.rpi.metpetdb.server.model;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.FlowPanel;

import edu.rpi.metpetdb.client.error.InvalidPropertyException;

public class ImageOnGrid extends MObject {

	private int id;
	private Grid grid;
	private Image image;
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
	private transient FlowPanel imageContainer;
	private transient com.google.gwt.user.client.ui.Image actualImage;
	private transient AbsolutePanel imagePanel;
	private transient int panTopLeftX;
	private transient int panTopLeftY;
	private transient int width;
	private transient int height;
	private transient int temporaryTopLeftX;
	private transient int temporaryTopLeftY;
	private transient boolean isShown;
	private transient Set<MineralAnalysis> mineralAnalyses;
	private transient boolean isLocked;
	private transient boolean isMenuHidden;
	private transient int zoomLevelsSkipped;

	public int getId() {
		return id;
	}
	public void setId(final int i) {
		id = i;
	}

	public Grid getGrid() {
		return grid;
	}
	public void setGrid(final Grid g) {
		grid = g;
	}

	public Image getImage() {
		return image;
	}
	public void setImage(final Image i) {
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
		final Iterator<MineralAnalysis> itr = mineralAnalyses.iterator();
		while (itr.hasNext()) {
			((MineralAnalysis) itr.next()).setImage(null);
		}
	}

	public FlowPanel getImageContainer() {
		return imageContainer;
	}
	public void setImageContainer(final FlowPanel fp) {
		imageContainer = fp;
	}

	public com.google.gwt.user.client.ui.Image getActualImage() {
		return actualImage;
	}
	public void setActualImage(final com.google.gwt.user.client.ui.Image w) {
		actualImage = w;
	}

	public AbsolutePanel getImagePanel() {
		return imagePanel;
	}
	public void setImagePanel(final AbsolutePanel ap) {
		imagePanel = ap;
	}

	public int getPanTopLeftX() {
		return panTopLeftX;
	}
	public void setPanTopLeftX(final int i) {
		panTopLeftX = i;
	}

	public int getPanTopLeftY() {
		return panTopLeftY;
	}
	public void setPanTopLeftY(final int i) {
		panTopLeftY = i;
	}

	public int getWidth() {
		return width;
	}
	public void setWidth(final int i) {
		width = i;
	}

	public int getHeight() {
		return height;
	}
	public void setHeight(final int i) {
		height = i;
	}

	public int getTemporaryTopLeftY() {
		return temporaryTopLeftY;
	}
	public void setTemporaryTopLeftY(final int i) {
		temporaryTopLeftY = i;
	}

	public int getTemporaryTopLeftX() {
		return temporaryTopLeftX;
	}
	public void setTemporaryTopLeftX(final int i) {
		temporaryTopLeftX = i;
	}

	public boolean getIsShown() {
		return isShown;
	}
	public void setIsShown(final boolean b) {
		isShown = b;
	}

	public Set<MineralAnalysis> getMineralAnalyses() {
		return mineralAnalyses;
	}
	public void setMineralAnalyses(final Set<MineralAnalysis> s) {
		mineralAnalyses = s;
	}
	public void addMineralAnalysis(final MineralAnalysis ma) {
		if (mineralAnalyses == null)
			mineralAnalyses = new HashSet<MineralAnalysis>();
		mineralAnalyses.add(ma);
	}

	public boolean getIsLocked() {
		return isLocked;
	}
	public void setIsLocked(final boolean b) {
		isLocked = b;
	}

	public boolean getIsMenuHidden() {
		return isMenuHidden;
	}
	public void setIsMenuHidden(final boolean b) {
		isMenuHidden = b;
	}

	public int getZoomLevelsSkipped() {
		return zoomLevelsSkipped;
	}
	public void setZoomLevelsSkipped(final int i) {
		zoomLevelsSkipped = i;
	}

	public String getGoodLookingPicture() {
		return this.getGoodLookingPicture(false);
	}

	public String getGoodLookingPicture(final boolean original) {
		if (this.width <= 100) {
			return this.get64x64ServerPath(original);
		}
		if (this.width >= 100 && this.width <= this.image.getWidth() * .5) {
			return this.getHalfServerPath(original);
		}
		if (this.width >= this.image.getWidth() * .5 + 100) {
			return this.getServerPath(original);
		}
		return this.getServerPath(original);
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

	public boolean skipZoom(int width, int height) {
		if (width < this.getWidth() || height < this.getHeight()) {
			if (width <= 5 || height <= 5) {
				if (this.getZoomLevelsSkipped() >= 1) {
					this.getImageContainer().setStyleName(
							"imageContainerHidden");
				}
				this.setZoomLevelsSkipped(this.getZoomLevelsSkipped() + 1);
				return true;
			}
		} else {
			if (this.getZoomLevelsSkipped() > 0) {
				this.setZoomLevelsSkipped(this.getZoomLevelsSkipped() - 1);
				if (this.getZoomLevelsSkipped() <= 1) {
					this.getImageContainer().setStyleName("imageContainer");
				}
				return true;
			}
		}
		return false;
	}

	public void resizeImage(int width, int height, boolean resize) {

		if (width < height && height < 32) {
			this.getImageContainer().setStyleName("imageContainerNoMenu");
		} else if (height < width && width < 32) {
			this.getActualImage().setStyleName("imageContainerNoMenu");
		} else {
			if (!this.getIsMenuHidden()
					&& !this.getImageContainer().getStyleName().equals(
							"imageContainerHidden")) {
				this.getImageContainer().setStyleName("imageContainer");
			}
		}

		// note does not work with resize
		int dwidth = width - this.getWidth();
		int dheight = height - this.getHeight();

		dwidth = Math.round(dwidth / (float) 2);
		dheight = Math.round(dheight / (float) 2);

		this.getImagePanel().setWidth(width + "px");
		this.getImagePanel().setHeight(height + "px");
		this.getActualImage().setWidth(width + "px");
		this.getActualImage().setHeight(height + "px");

		if (!resize) {
			this.setTemporaryTopLeftX(this.getTemporaryTopLeftX() - dwidth);
			this.setTemporaryTopLeftY(this.getTemporaryTopLeftY() - dheight);
		}

		final Iterator<MineralAnalysis> itr = this.getMineralAnalyses().iterator();
		while (itr.hasNext()) {
			final MineralAnalysis ma = (MineralAnalysis) itr.next();
			ma.setPointX((int) (width * ma.getPercentX()));
			ma.setPointY((int) (height * ma.getPercentY()));
			this.getImagePanel().setWidgetPosition(ma.getActualImage(),
					ma.getPointX(), ma.getPointY());
		}

		this.setWidth(width);
		this.setHeight(height);

		if (!this.actualImage.getUrl().equals(this.getGoodLookingPicture()))
			this.actualImage.setUrl(this.getGoodLookingPicture());
	}

	public boolean equals(final Object o) {
		return o instanceof ImageOnGrid && grid == ((ImageOnGrid) o).getGrid()
				&& image == ((ImageOnGrid) o).getImage();
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
