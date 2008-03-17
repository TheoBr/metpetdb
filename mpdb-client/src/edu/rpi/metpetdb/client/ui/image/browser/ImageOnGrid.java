package edu.rpi.metpetdb.client.ui.image.browser;

import java.util.Iterator;
import java.util.Set;

import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.FlowPanel;

import edu.rpi.metpetdb.client.model.ImageOnGridDTO;
import edu.rpi.metpetdb.client.model.MineralAnalysisDTO;

public class ImageOnGrid {

	private ImageOnGridDTO iog;
	private FlowPanel imageContainer;
	private com.google.gwt.user.client.ui.Image actualImage;
	private AbsolutePanel imagePanel;
	private int panTopLeftX;
	private int panTopLeftY;
	private int width;
	private int height;
	private int temporaryTopLeftX;
	private int temporaryTopLeftY;
	private boolean isShown;
	private Set<MineralAnalysisDTO> mineralAnalyses;
	private boolean isLocked;
	private boolean isMenuHidden;
	private int zoomLevelsSkipped;

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
			if (!this.isMenuHidden()
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

		final Iterator<MineralAnalysisDTO> itr = this.getMineralAnalyses()
				.iterator();
		while (itr.hasNext()) {
			final MineralAnalysisDTO ma = (MineralAnalysisDTO) itr.next();
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

	public String getGoodLookingPicture() {
		return this.getGoodLookingPicture(false);
	}

	public String getGoodLookingPicture(final boolean original) {
		if (this.width <= 100) {
			return iog.get64x64ServerPath(original);
		}
		if (this.width >= 100 && this.width <= iog.getImage().getWidth() * .5) {
			return iog.getHalfServerPath(original);
		}
		if (this.width >= iog.getImage().getWidth() * .5 + 100) {
			return iog.getServerPath(original);
		}
		return iog.getServerPath(original);
	}

	public ImageOnGridDTO getIog() {
		return iog;
	}

	public void setIog(ImageOnGridDTO iog) {
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

	public int getPanTopLeftX() {
		return panTopLeftX;
	}

	public void setPanTopLeftX(int panTopLeftX) {
		this.panTopLeftX = panTopLeftX;
	}

	public int getPanTopLeftY() {
		return panTopLeftY;
	}

	public void setPanTopLeftY(int panTopLeftY) {
		this.panTopLeftY = panTopLeftY;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getTemporaryTopLeftX() {
		return temporaryTopLeftX;
	}

	public void setTemporaryTopLeftX(int temporaryTopLeftX) {
		this.temporaryTopLeftX = temporaryTopLeftX;
	}

	public int getTemporaryTopLeftY() {
		return temporaryTopLeftY;
	}

	public void setTemporaryTopLeftY(int temporaryTopLeftY) {
		this.temporaryTopLeftY = temporaryTopLeftY;
	}

	public boolean isShown() {
		return isShown;
	}

	public void setShown(boolean isShown) {
		this.isShown = isShown;
	}

	public Set<MineralAnalysisDTO> getMineralAnalyses() {
		return mineralAnalyses;
	}

	public void setMineralAnalyses(Set<MineralAnalysisDTO> mineralAnalyses) {
		this.mineralAnalyses = mineralAnalyses;
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
