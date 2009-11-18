package edu.rpi.metpetdb.client.ui.image.browser;

import java.util.Collection;
import java.util.Iterator;

import com.google.gwt.user.client.DOM;

import edu.rpi.metpetdb.client.ui.widgets.panels.MAbsolutePanel;

public class PanHandler {

	// Offset from the origin
	private int xOffset = 0;
	private int yOffset = 0;
	private final MAbsolutePanel grid;
	private Collection<ImageOnGridContainer> imagesOnGrid;
	public final ImageBrowserDetails imageBrowser;

	public PanHandler(final MAbsolutePanel grid, final ImageBrowserDetails imageBrowser) {
		this.grid = grid;
		this.imageBrowser = imageBrowser;
		
	}

	public Collection<ImageOnGridContainer> getImagesOnGrid() {
		return imagesOnGrid;
	}

	public void setImagesOnGrid(Collection<ImageOnGridContainer> imagesOnGrid) {
		this.imagesOnGrid = imagesOnGrid;
	}


	/**
	 * Pans the grid by deltaX and deltaY
	 * 
	 * @param deltaX
	 * @param deltaY
	 */
	public void pan(final int deltaX, final int deltaY) {
		this.xOffset += deltaX;
		this.yOffset += deltaY;
		imageBrowser.totalXOffset += deltaX;
		imageBrowser.totalYOffset += deltaY;
		DOM.setStyleAttribute(this.grid.getElement(), "backgroundPosition",
				(imageBrowser.totalXOffset + deltaX) + "px "
						+ (imageBrowser.totalYOffset + deltaY) + "px");
		final Iterator<ImageOnGridContainer> itr = imagesOnGrid.iterator();
		imageBrowser.updateBoundary();
		while (itr.hasNext()) {
			final ImageOnGridContainer iog = itr.next();
			final double newX = iog.getCurrentContainerPosition().x + deltaX;
			final double newY = iog.getCurrentContainerPosition().y + deltaY;
			this.grid.setWidgetPosition(iog.getImageContainer(), newX, newY);
			iog.pan(deltaX, deltaY);
		}
	}

	/**
	 * Moves the grid back to the origin
	 */
	public void reset() {
		pan(-xOffset, -yOffset);
		xOffset = 0;
		yOffset = 0;
	}
	
	public int getXOffset(){
		return xOffset;
	}
	
	public int getYOffset(){
		return yOffset;
	}

}
