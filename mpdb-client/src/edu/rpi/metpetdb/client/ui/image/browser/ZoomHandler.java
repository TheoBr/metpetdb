package edu.rpi.metpetdb.client.ui.image.browser;

import java.util.Collection;
import java.util.Iterator;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;

public class ZoomHandler {

	private Collection<ImageOnGridContainer> imagesOnGrid;
	public final Element zSlide;
	public final ImageBrowserDetails imageBrowser;
	private final static int zoomMultiplier = 2;
	public final static int MAXZOOM = 10;
	public final static int MINZOOM = 130;
	private int referenceX = 0;
	private int referenceY = 0;

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

	public int getCurrentScale() {
		return zoomMultiplier * (6 - (getCurrentZoomLevel() / 10) <= 0 ? 6 - (getCurrentZoomLevel() / 10)
				: 6 - (getCurrentZoomLevel() / 10));
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
		final Iterator<ImageOnGridContainer> itr = imagesOnGrid.iterator();
		/* by default put refence in the center */
		referenceY = imageBrowser.getGrid().getOffsetHeight() / 2;
		referenceX = imageBrowser.getGrid().getOffsetWidth() / 2;
		imageBrowser.updateScale(level == 1 ? zoomMultiplier
				: (1 / (float) zoomMultiplier));
		while (itr.hasNext()) {
			final ImageOnGridContainer iog = itr.next();
			final int newWidth = Math
					.round((iog.getCurrentWidth() * (level == 1 ? zoomMultiplier
							: 1 / (float) zoomMultiplier)));
			final int newHeight = Math
					.round((iog.getCurrentHeight() * (level == 1 ? zoomMultiplier
							: 1 / (float) zoomMultiplier)));
			if (!iog.skipZoom(newWidth, newHeight)) {
				iog.resizeImage(newWidth, newHeight, false);
				changePosition(iog, level);
				imageBrowser.getGrid().setWidgetPosition(
						iog.getImageContainer(), iog.getCurrentContainerPosition().x,
						iog.getCurrentContainerPosition().y);

			}
		}
	}

	private void changePosition(final ImageOnGridContainer iog, final int level) {
		int centerX = getCenterX(iog);
		int centerY = getCenterY(iog);

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

		// transform centers to top lefts
		iog.getCurrentContainerPosition().x = (centerX
				- Math.round((getCurrentWidth(iog) / (float) 2)));
		iog.getCurrentContainerPosition().y = (centerY
				- Math.round((getCurrentHeight(iog) / (float) 2)));
	}

	private int getCenterX(final ImageOnGridContainer iog) {
		return Math.round(iog.getCurrentContainerPosition().x
				+ (getCurrentWidth(iog) / (float) 2));
	}

	private int getCenterY(final ImageOnGridContainer iog) {
		return Math.round(iog.getCurrentContainerPosition().y
				+ (getCurrentHeight(iog) / (float) 2));
	}

	private int getCurrentWidth(final ImageOnGridContainer iog) {
		// return Math.round((iog.getImage().getWidth() *
		// (iog.getResizeRatio())));
		return iog.getCurrentWidth();
	}

	private int getCurrentHeight(final ImageOnGridContainer iog) {
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

}
