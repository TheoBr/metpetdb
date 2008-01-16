package edu.rpi.metpetdb.client.ui.image.browser;

import java.util.Iterator;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;

import edu.rpi.metpetdb.client.model.Grid;
import edu.rpi.metpetdb.client.model.ImageOnGrid;

public class ZoomHandler {

	public final Grid grid;
	public final Element zSlide;
	public final ImageBrowserDetails imageBrowser;
	private final static int zoomMultiplier = 2;
	public final static int MAXZOOM = 10;
	public final static int MINZOOM = 130;
	private int referenceX = 0;
	private int referenceY = 0;

	public ZoomHandler(final Grid g, final Element e,
			final ImageBrowserDetails ibm) {
		grid = g;
		zSlide = e;
		imageBrowser = ibm;
	}

	public int getCurrentZoomLevel() {
		int top = getCurrentZoomPixel();
		return top;
	}

	public int getCurrentScale() {
		return 6 - (getCurrentZoomLevel() / 10) <= 0
				? 5 - (getCurrentZoomLevel() / 10)
				: 6 - (getCurrentZoomLevel() / 10);
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
		final Iterator itr = grid.getImagesOnGrid().iterator();
		ImageOnGrid iog;
		/* by default put refence in the center */
		referenceY = imageBrowser.getGrid().getOffsetHeight() / 2;
		referenceX = imageBrowser.getGrid().getOffsetWidth() / 2;
		imageBrowser.updateScale(level == 1
				? zoomMultiplier
				: (1 / (float) zoomMultiplier));
		while (itr.hasNext()) {
			iog = (ImageOnGrid) itr.next();
			final int newWidth = Math.round((iog.getWidth() * (level == 1
					? zoomMultiplier
					: 1 / (float) zoomMultiplier)));
			final int newHeight = Math.round((iog.getHeight() * (level == 1
					? zoomMultiplier
					: 1 / (float) zoomMultiplier)));
			if (!iog.skipZoom(newWidth, newHeight)) {
				iog.resizeImage(newWidth, newHeight, false);
				changePosition(iog, level);
				imageBrowser.getGrid().setWidgetPosition(
						iog.getImageContainer(), iog.getTemporaryTopLeftX(),
						iog.getTemporaryTopLeftY());

			}
		}
	}

	private void changePosition(final ImageOnGrid iog, final int level) {
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
		iog.setTemporaryTopLeftX(centerX
				- Math.round((getCurrentWidth(iog) / (float) 2)));
		iog.setTemporaryTopLeftY(centerY
				- Math.round((getCurrentHeight(iog) / (float) 2)));
	}

	private int getCenterX(final ImageOnGrid iog) {
		return Math.round(iog.getTemporaryTopLeftX()
				+ (getCurrentWidth(iog) / (float) 2));
	}

	private int getCenterY(final ImageOnGrid iog) {
		return Math.round(iog.getTemporaryTopLeftY()
				+ (getCurrentHeight(iog) / (float) 2));
	}

	private int getCurrentWidth(final ImageOnGrid iog) {
		// return Math.round((iog.getImage().getWidth() *
		// (iog.getResizeRatio())));
		return iog.getWidth();
	}

	private int getCurrentHeight(final ImageOnGrid iog) {
		// return Math.round( (iog.getImage().getHeight() *
		// (iog.getResizeRatio())));
		return iog.getHeight();
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
