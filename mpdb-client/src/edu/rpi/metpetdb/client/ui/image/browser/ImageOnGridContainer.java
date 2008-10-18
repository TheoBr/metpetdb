package edu.rpi.metpetdb.client.ui.image.browser;

import java.util.Iterator;
import java.util.Set;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.FlowPanel;

import edu.rpi.metpetdb.client.model.ChemicalAnalysis;
import edu.rpi.metpetdb.client.model.ImageOnGrid;

public class ImageOnGridContainer {

	private ImageOnGrid iog;
	private FlowPanel imageContainer;
	private com.google.gwt.user.client.ui.Image actualImage;
	private AbsolutePanel imagePanel;
	// Always the size of the image
	private int currentWidth;
	private int currentHeight;
	// Original is used before doing a resize operation to make comparisons
	private int originalWidth;
	private int originalHeight;
	private float aspectRatio;
	private float aspectRatioHeight;
	private boolean isShown;
	private Set<ChemicalAnalysis> chemicalAnalyses;
	private boolean isLocked;
	private boolean isMenuHidden;
	private int zoomLevelsSkipped;
	// The point at which the container was located before a modify action was
	// performed on it (top left x/y)
	private Point originalContainerPosition = new Point();
	// The current position of the container (top left x/y)
	private Point currentContainerPosition = new Point();

	public ImageOnGridContainer(final ImageOnGrid iog) {
		this.iog = iog;
		currentContainerPosition.x = iog.getTopLeftX();
		currentContainerPosition.y = iog.getTopLeftY();
	}

	/**
	 * Moves where the current container is, and also updates where the image is
	 * with respect to the default zoom level and origin
	 * 
	 * @param deltaX
	 * @param deltaY
	 * @param scale
	 */
	public void move(final int deltaX, final int deltaY, final int scale) {
		iog.setTopLeftX(iog.getTopLeftX() + (scale * deltaX));
		iog.setTopLeftY(iog.getTopLeftY() + (scale * deltaY));
		currentContainerPosition.x += deltaX;
		currentContainerPosition.y += deltaY;
	}

	/**
	 * Since panning is not saved we only update the position of the container
	 * 
	 * @param deltaX
	 * @param deltaY
	 */
	public void pan(final int deltaX, final int deltaY) {
		currentContainerPosition.x += deltaX;
		currentContainerPosition.y += deltaY;
	}

	public Point getCurrentContainerPosition() {
		return currentContainerPosition;
	}

	public void setCurrentContainerPosition(Point currentContainerPosition) {
		this.currentContainerPosition = currentContainerPosition;
	}

	public Point getOriginalContainerPosition() {
		return originalContainerPosition;
	}

	public void setOriginalContainerPosition(Point originalContainerPosition) {
		this.originalContainerPosition = originalContainerPosition;
	}
	
	public void setupForResize() {
		originalWidth = currentWidth;
		originalHeight = currentHeight;
		aspectRatio = originalHeight / (float) originalWidth;
		aspectRatioHeight = originalWidth / (float) originalHeight;
	}

	public boolean skipZoom(int width, int height) {
		if (width < this.getCurrentWidth() || height < this.getCurrentHeight()) {
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
			if (!this.isMenuHidden()
					&& !this.getImageContainer().getStyleName().equals(
							"imageContainerHidden")) {
				this.getImageContainer().setStyleName("imageContainer");
			}
		}

		this.getImagePanel().setWidth(width + "px");
		this.getImagePanel().setHeight(height + "px");
		this.getActualImage().setWidth(width + "px");
		this.getActualImage().setHeight(height + "px");
		
		currentWidth  = width;
		currentHeight = height;

		final Iterator<ChemicalAnalysis> itr = this.getChemicalAnalyses()
				.iterator();
		while (itr.hasNext()) {

			// TODO proportionally move the chemical analyses
			final ChemicalAnalysis ma = (ChemicalAnalysis) itr.next();
			// ma.setPointX((int) (width * ma.getPercentX()));
			// ma.setPointY((int) (height * ma.getPercentY()));
			// this.getImagePanel().setWidgetPosition(ma.getActualImage(),
			// ma.getPointX(), ma.getPointY());
		}

		// this.setWidth(width);
		// this.setHeight(height);

		if (!this.actualImage.getUrl().equals(this.getGoodLookingPicture()))
			this.actualImage.setUrl(this.getGoodLookingPicture());
	}

	public String get64x64ServerPath(final boolean original) {
		final String checksum;
		if (original)
			checksum = this.iog.getImage().getChecksum64x64();
		else
			checksum = this.iog.getGchecksum64x64();
		return GWT.getModuleBaseURL() + "/image/?checksum=" + checksum;
	}

	public String getHalfServerPath(final boolean original) {
		final String checksum;
		if (original)
			checksum = this.iog.getImage().getChecksumHalf();
		else
			checksum = this.iog.getGchecksumHalf();
		return GWT.getModuleBaseURL() + "/image/?checksum=" + checksum;
	}

	public String getServerPath(final boolean original) {
		final String checksum;
		if (original)
			checksum = this.iog.getImage().getChecksum();
		else
			checksum = this.iog.getGchecksum();
		return GWT.getModuleBaseURL() + "/image/?checksum=" + checksum;
	}

	public String getGoodLookingPicture() {
		return this.getGoodLookingPicture(false);
	}

	public String getGoodLookingPicture(final boolean original) {
		if (this.currentWidth <= 100) {
			return get64x64ServerPath(original);
		}
		if (this.currentWidth >= 100
				&& this.currentWidth <= iog.getImage().getWidth() * .5) {
			return getHalfServerPath(original);
		}
		if (this.currentWidth >= iog.getImage().getWidth() * .5 + 100) {
			return getServerPath(original);
		}
		return getServerPath(original);
	}

	public ImageOnGrid getIog() {
		return iog;
	}

	public void setIog(ImageOnGrid iog) {
		this.iog = iog;
	}

	public FlowPanel getImageContainer() {
		return imageContainer;
	}

	public void setImageContainer(FlowPanel imageContainer) {
		this.imageContainer = imageContainer;
	}

	public com.google.gwt.user.client.ui.Image getActualImage() {
		return actualImage;
	}

	public void setActualImage(com.google.gwt.user.client.ui.Image actualImage) {
		this.actualImage = actualImage;
	}

	public AbsolutePanel getImagePanel() {
		return imagePanel;
	}

	public void setImagePanel(AbsolutePanel imagePanel) {
		this.imagePanel = imagePanel;
	}

	public int getCurrentWidth() {
		return currentWidth;
	}

	public void setCurrentWidth(int width) {
		this.currentWidth = width;
	}

	public int getCurrentHeight() {
		return currentHeight;
	}

	public void setCurrentHeight(int height) {
		this.currentHeight = height;
	}

	public int getOriginalWidth() {
		return originalWidth;
	}

	public void setOriginalWidth(int originalWidth) {
		this.originalWidth = originalWidth;
	}

	public int getOriginalHeight() {
		return originalHeight;
	}

	public void setOriginalHeight(int originalHeight) {
		this.originalHeight = originalHeight;
	}

	public float getAspectRatio() {
		return aspectRatio;
	}

	public void setAspectRatio(float aspectRatio) {
		this.aspectRatio = aspectRatio;
	}

	public float getAspectRatioHeight() {
		return aspectRatioHeight;
	}

	public void setAspectRatioHeight(float aspectRatioHeight) {
		this.aspectRatioHeight = aspectRatioHeight;
	}

	public boolean isShown() {
		return isShown;
	}

	public void setShown(boolean isShown) {
		this.isShown = isShown;
	}

	public Set<ChemicalAnalysis> getChemicalAnalyses() {
		return chemicalAnalyses;
	}

	public void setChemicalAnalyses(Set<ChemicalAnalysis> chemicalAnalyses) {
		this.chemicalAnalyses = chemicalAnalyses;
	}

	public boolean isLocked() {
		return isLocked;
	}

	public void setLocked(boolean isLocked) {
		this.isLocked = isLocked;
	}

	public boolean isMenuHidden() {
		return isMenuHidden;
	}

	public void setMenuHidden(boolean isMenuHidden) {
		this.isMenuHidden = isMenuHidden;
	}

	public int getZoomLevelsSkipped() {
		return zoomLevelsSkipped;
	}

	public void setZoomLevelsSkipped(int zoomLevelsSkipped) {
		this.zoomLevelsSkipped = zoomLevelsSkipped;
	}
}
