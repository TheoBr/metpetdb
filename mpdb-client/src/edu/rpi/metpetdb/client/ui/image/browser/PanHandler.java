package edu.rpi.metpetdb.client.ui.image.browser;

import java.util.Collection;
import java.util.Iterator;

import com.google.gwt.user.client.DOM;

import edu.rpi.metpetdb.client.ui.widgets.MAbsolutePanel;

public class PanHandler {

	// Offset from the origin
	private int xOffset = 0;
	private int yOffset = 0;
	private int currentBackgroundX = 0;
	private int currentBackgroundY = 0;
	private final MAbsolutePanel grid;
	private Collection<ImageOnGridContainer> imagesOnGrid;

	public PanHandler(final MAbsolutePanel grid) {
		this.grid = grid;
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
		DOM.setStyleAttribute(this.grid.getElement(), "backgroundPosition",
				(currentBackgroundX + deltaX) + "px "
						+ (currentBackgroundY + deltaY) + "px");
		final Iterator<ImageOnGridContainer> itr = imagesOnGrid.iterator();
		while (itr.hasNext()) {
			final ImageOnGridContainer iog = itr.next();
			final int newX = iog.getCurrentContainerPosition().x + deltaX;
			final int newY = iog.getCurrentContainerPosition().y + deltaY;
			this.grid.setWidgetPosition(iog.getImageContainer(), newX, newY);
			iog.pan(deltaX, deltaY);
		}
		currentBackgroundX += deltaX;
		currentBackgroundY += deltaY;
	}

	/**
	 * Moves the grid back to the origin
	 */
	public void reset() {
		pan(-xOffset, -yOffset);
		xOffset = 0;
		yOffset = 0;
	}

}
