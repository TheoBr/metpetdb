package edu.rpi.metpetdb.client.ui.image.browser;

import java.util.Collection;
import java.util.Iterator;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Image;

public class ZoomHandler {

	private Collection<ImageOnGridContainer> imagesOnGrid;
	public final Element zSlide;
	public final ImageBrowserDetails imageBrowser;
	public final static double zoomMultiplier = 1.5f;
	public final static int MAXZOOM = 10;
	public final static int MINZOOM = 130;
	private int referenceX = 0;
	private int referenceY = 0;
	private int scale = 0;

	public ZoomHandler(final Collection<ImageOnGridContainer> imagesOnGrid,
			final Element e, final ImageBrowserDetails ibm) {
		this.imagesOnGrid = imagesOnGrid;
		zSlide = e;
		imageBrowser = ibm;
	}

	public Collection<ImageOnGridContainer> getImagesOnGrid() {
		return imagesOnGrid;
	}

	public void setImagesOnGrid(Collection<ImageOnGridContainer> imagesOnGrid) {
		this.imagesOnGrid = imagesOnGrid;
	}

	public int getCurrentZoomLevel() {
		int top = getCurrentZoomPixel();
		return top;
	}

	public float getCurrentScale() {
		return (float) Math.pow(zoomMultiplier, scale);
	}

	public int getCurrentZoomPixel() {
		String currentTop = DOM.getStyleAttribute(zSlide, "top");
		int top = 0;
		for (int i = 0; i < currentTop.length(); ++i) {
			if (Character.isLetter(currentTop.charAt(i))) {
				top = Integer.parseInt(currentTop.substring(0, i));
				break;
			}
		}
		return top;
	}

	public void zoom(final int level) {
		scale += (level*-1);
		final Iterator<ImageOnGridContainer> itr = imagesOnGrid.iterator();
		/* by default put refence in the center */
		referenceY = imageBrowser.getGrid().getOffsetHeight() / 2;
		referenceX = imageBrowser.getGrid().getOffsetWidth() / 2;
		imageBrowser.updateScale(level == 1 ?  (1 / zoomMultiplier) : zoomMultiplier);
		if (level == -1) {
			imageBrowser.totalYOffset = (int)Math.round(((double)imageBrowser.totalYOffset/2.0) + ((double)imageBrowser.getGrid().getOffsetHeight()/4.0));
			imageBrowser.totalXOffset = (int)Math.round(((double)imageBrowser.totalXOffset/2.0) + ((double)imageBrowser.getGrid().getOffsetWidth()/4.0));
		} else {
			imageBrowser.totalYOffset = (int)Math.round((2*imageBrowser.totalYOffset) - ((double)imageBrowser.getGrid().getOffsetHeight()/2.0));
			imageBrowser.totalXOffset = (int)Math.round((2*imageBrowser.totalXOffset) - ((double)imageBrowser.getGrid().getOffsetWidth()/2.0));
		}
		DOM.setStyleAttribute(imageBrowser.getGrid().getElement(), "backgroundPosition",
				(imageBrowser.totalXOffset) + "px "
						+ (imageBrowser.totalYOffset) + "px");
		imageBrowser.updateBoundary();
		while (itr.hasNext()) {
			final ImageOnGridContainer iog = itr.next();
			final double newWidth = (iog.getCurrentWidth() * (level == 1 ? zoomMultiplier
							: 1 / zoomMultiplier));
			final double newHeight = (iog.getCurrentHeight() * (level == 1 ? zoomMultiplier
							: 1 /  zoomMultiplier));
			if (!iog.skipZoom(newWidth, newHeight)) {
				double centerX = getCenterX(iog);
				double centerY = getCenterY(iog);
				iog.resizeImage(newWidth, newHeight, false);
				changePosition(iog, level,centerX,centerY);
				imageBrowser.getGrid().setWidgetPosition(
						iog.getImageContainer(), iog.getCurrentContainerPosition().x,
						iog.getCurrentContainerPosition().y);

			}
		}
	}
	
	private void changeBoundaryPosition(final int level, int centerX, int centerY){
	
	}

	private void changePosition(final ImageOnGridContainer iog, final int level, double centerX, double centerY) {


		// transform center based on refenceX/Y
		centerX -= referenceX;
		centerY -= referenceY;

		// calculate new centerX/Y
		centerX = (Math.round(centerX
				* (level == 1 ? zoomMultiplier : 1 / (float) zoomMultiplier)));
		centerY = (Math.round(centerY
				* (level == 1 ? zoomMultiplier : 1 / (float) zoomMultiplier)));

		// transform to 0,0
		centerX += referenceX;
		centerY += referenceY;

		iog.getCurrentContainerPosition().x = Math.round(ImageBrowserDetails.pps*(iog.getImageOnGrid().getTopLeftX()/imageBrowser.scale));
		iog.getCurrentContainerPosition().y = Math.round(ImageBrowserDetails.pps*(iog.getImageOnGrid().getTopLeftY()/imageBrowser.scale));
		
		final Iterator<Image> itr = iog.getChemicalAnalysisImages().iterator();
		while (itr.hasNext()) {
			final Image i = itr.next();
			imageBrowser.getGrid().remove(i);
		}
		iog.getChemicalAnalysisImages().clear();
		
		iog.pan(imageBrowser.totalXOffset, imageBrowser.totalYOffset);
		imageBrowser.addPoints(iog);
	}

	private double getCenterX(final ImageOnGridContainer iog) {
		return Math.round(iog.getCurrentContainerPosition().x
				+ (getCurrentWidth(iog) / 2));
	}

	private double getCenterY(final ImageOnGridContainer iog) {
		return Math.round(iog.getCurrentContainerPosition().y
				+ (getCurrentHeight(iog) / 2));
	}

	private double getCurrentWidth(final ImageOnGridContainer iog) {
		// return Math.round((iog.getImage().getWidth() *
		// (iog.getResizeRatio())));
		return iog.getCurrentWidth();
	}

	private double getCurrentHeight(final ImageOnGridContainer iog) {
		// return Math.round( (iog.getImage().getHeight() *
		// (iog.getResizeRatio())));
		return iog.getCurrentHeight();
	}

	public void setReferencePoint(final int x, final int y) {
		referenceX = x;
		referenceY = y;
	}

	public int getReferenceX() {
		return referenceX;
	}
	public int getReferenceY() {
		return referenceY;
	}
	
	public void updateSlider(final int offset) {
		DOM.setStyleAttribute(zSlide, "top", getCurrentZoomPixel() + offset + "px");
	}

}
