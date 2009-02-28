package edu.rpi.metpetdb.client.ui.image.browser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.model.ChemicalAnalysis;
import edu.rpi.metpetdb.client.model.Subsample;
import edu.rpi.metpetdb.client.ui.commands.ServerOp;
import edu.rpi.metpetdb.client.ui.image.browser.dialogs.AddPointDialog;
import edu.rpi.metpetdb.client.ui.image.browser.dialogs.PointPopup;
import edu.rpi.metpetdb.client.ui.widgets.MAbsolutePanel;
import edu.rpi.metpetdb.client.ui.widgets.MAbsolutePanel.ZMode;

public class ImageBrowserMouseListener implements MouseListener {

	private final MAbsolutePanel grid;
	private Collection<ImageOnGridContainer> imagesOnGrid;
	private ImageOnGridContainer currentImage;
	private ChemicalAnalysis currentPoint;
	private boolean isBeingDragged = false;
	private int lastX;
	private int lastY;
	private MouseMode mode;
	private ResizeCorner resizeDirection = ResizeCorner.NONE;
	private final ZOrderManager zOrderManager;
	private final Subsample subsample;
	private Image pointer;
	private final ImageBrowserDetails imageBrowser;
	private final FlowPanel viewControls;

	public enum MouseMode {
		NONE, MOVE_IMAGE, // 0
		PAN_GRID, // 1
		RESIZE_IMAGE, // 2
		PLACE_POINT, // 3
		MOVING_POINT
		// 4
	}

	private enum ResizeCorner {
		NONE, NORTH_WEST, NORTH_EAST, SOUTH_WEST, SOUTH_EAST,
	}

	public void setPoint(final Image w) {
		pointer = w;
	}

	public void setMode(final MouseMode m) {
		mode = m;
	}

	public void setCurrentImage(final ImageOnGridContainer iog) {
		currentImage = iog;
	}

	public ImageBrowserMouseListener(final MAbsolutePanel ap,
			final Collection<ImageOnGridContainer> s, final ZOrderManager z,
			final Subsample ss, final ImageBrowserDetails ibd,
			final FlowPanel fp) {
		grid = ap;
		imagesOnGrid = s;
		zOrderManager = z;
		subsample = ss;
		imageBrowser = ibd;
		viewControls = fp;
	}

	public void onMouseEnter(final Widget sender) {

	}

	public void onMouseLeave(final Widget sender) {
		DOM.releaseCapture(sender.getElement());
	}

	/**
	 * Takes care of showing the place point dialog when the user clicks, and
	 * also adding the point once the dialog is submitted
	 * 
	 * @param x
	 * @param y
	 */
	private void handlePlacePoint(final int x, final int y) {
		DOM.setStyleAttribute(currentImage.getActualImage().getElement(),
				"cursor", "default");
		new ServerOp<ChemicalAnalysis>() {
			public void begin() {
				if (validateAddChemicalAnalysis(x, y)) {
					new AddPointDialog(subsample, currentImage, this, x, y)
							.show();
				}
			}
			public void onSuccess(final ChemicalAnalysis result) {
				addChemicalAnalysis(result, x, y);
				mode = MouseMode.NONE;
			}
		}.begin();
	}

	/**
	 * When performing a mouse down it checks for image actions like resizing,
	 * dragging, and specifying z order
	 * 
	 * @param sender
	 * @param x
	 * @param y
	 */
	private void handleImageOperations(final Widget sender, final int x,
			final int y) {
		// User wants to drag an image, resize, or specify z
		// order
		if (grid.getZMode() == ZMode.NO_ZMODE) {
			resizeDirection = getResizeCorner(sender.getAbsoluteLeft() + x,
					sender.getAbsoluteTop() + y);
			currentImage.getImageContainer().addStyleName("image-moving");
			if (resizeDirection != ResizeCorner.NONE) {
				mode = MouseMode.RESIZE_IMAGE;
				currentImage.setupForResize();
			} else {
				// if we are not resizing then we are moving
				mode = MouseMode.MOVE_IMAGE;
			}
		} else if (grid.getZMode() == ZMode.BRING_TO_FRONT) {
			zOrderManager.bringToFront(currentImage);
		} else if (grid.getZMode() == ZMode.SEND_TO_BACK) {
			zOrderManager.sendToBack(currentImage);
		}
	}

	public void onMouseDown(final Widget sender, final int x, final int y) {
		if (mode == MouseMode.PLACE_POINT) {
			handlePlacePoint(x, y);
		} else {
			mode = MouseMode.NONE;
			if (!grid.getCanDrag() || isInViewControl(x, y)) {
				// If we are inside the view control or we can't drag then just
				// ignore the mouse down request
			} else {
				grid.setCanDrag(false);
				DOM.setCapture(sender.getElement());
				currentImage = findImageOnGrid(x, y);
				isBeingDragged = true;
				lastX = x;
				lastY = y;
				// If we found an image and we are not locked
				if (currentImage != null && !currentImage.isLocked()) {
					currentPoint = findPointOnGrid(
							x
									- (currentImage.getImagePanel()
											.getAbsoluteLeft() - grid
											.getAbsoluteLeft()), y
									- (currentImage.getImagePanel()
											.getAbsoluteTop()
											- grid.getAbsoluteTop() + 13));
					if (currentPoint != null && !currentPoint.isLocked()) {
						// user is wanting to drag a chemical analysis point
						mode = MouseMode.MOVING_POINT;
					} else {
						handleImageOperations(sender, x, y);
					}
				} else {
					// User wants to Pan the grid
					mode = MouseMode.PAN_GRID;
					grid.addStyleName("image-moving");
				}
			}
		}
		// Reset z mode
		grid.setZMode(ZMode.NO_ZMODE);
	}

	/**
	 * Adds a chemical analysis to the current image
	 * 
	 * @param ca
	 * @param x
	 * @param y
	 */
	private void addChemicalAnalysis(final ChemicalAnalysis ca, final int x,
			final int y) {
		ca.setImage(currentImage.getIog().getImage());
		int pointX = x;
		int pointY = y;
		pointX -= currentImage.getImagePanel().getAbsoluteLeft()
				- grid.getAbsoluteLeft() + 4;
		pointY -= currentImage.getImagePanel().getAbsoluteTop()
				- grid.getAbsoluteTop() + 13;
		ca.setReferenceX(pointX);
		ca.setReferenceY(pointY);
		ca.setActualImage(pointer);
		ca.setLocked(true);
		((Image) pointer).addClickListener(new ClickListener() {
			public void onClick(final Widget sender) {
				new PointPopup(ca, currentImage, ((Image) pointer)
						.getAbsoluteLeft(), ((Image) pointer).getAbsoluteTop())
						.show();
			}
		});
		ca.setPercentX(pointX / (float) currentImage.getCurrentWidth());
		ca.setPercentY(pointY / (float) currentImage.getCurrentHeight());
		currentImage.getChemicalAnalyses().add(ca);
		imageBrowser.getChemicalAnalysesToSave().add(ca);
	}

	/**
	 * Validates whether where the user is placing the chemical analysis is
	 * within the image dimension
	 * 
	 * @param x
	 * @param y
	 * @return returns true if it is a valid point, false otherwise
	 */
	private boolean validateAddChemicalAnalysis(final int x, final int y) {
		int pointX = x;
		int pointY = y;
		pointX -= currentImage.getImagePanel().getAbsoluteLeft()
				- grid.getAbsoluteLeft() + 4;
		pointY -= currentImage.getImagePanel().getAbsoluteTop()
				- grid.getAbsoluteTop() + 13;
		if (pointX < 0 || pointX > currentImage.getCurrentWidth()) {
			return false;
		}
		if (pointY < 0 || pointY > currentImage.getCurrentHeight()) {
			return false;
		}
		return true;
	}

	/**
	 * Returns whether the current coordinate is within the view controls (zoom
	 * in/out and pan)
	 * 
	 * @param x
	 * @param y
	 * @return true if within the zoom control, false otherwise
	 */
	private boolean isInViewControl(final int x, final int y) {
		final int absoluteX = x + grid.getAbsoluteLeft();
		final int absoluteY = y + grid.getAbsoluteTop();
		final int viewControlX = viewControls.getAbsoluteLeft();
		final int viewControlY = viewControls.getAbsoluteTop();
		final int viewControlWidth = viewControls.getOffsetWidth();
		final int viewControlHeight = viewControls.getOffsetHeight();
		if (absoluteX > viewControlX
				&& absoluteX < viewControlX + viewControlWidth) {
			if (absoluteY > viewControlY
					&& absoluteY < viewControlY + viewControlHeight) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns the correct resize corner for the current image, i.e. if the user
	 * was resizing the top left, it would return north west
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public ResizeCorner getResizeCorner(final int x, final int y) {
		if (currentImage != null) {
			if (x <= currentImage.getActualImage().getAbsoluteLeft()
					&& x >= currentImage.getActualImage().getAbsoluteLeft() - 9) {
				if (y >= currentImage.getActualImage().getAbsoluteTop() - 9
						&& y <= currentImage.getActualImage().getAbsoluteTop()) {
					return ResizeCorner.NORTH_WEST;
				} else if (y >= currentImage.getActualImage().getAbsoluteTop()
						+ currentImage.getActualImage().getOffsetHeight()
						&& y <= currentImage.getActualImage().getAbsoluteTop()
								+ currentImage.getActualImage()
										.getOffsetHeight() + 9) {
					return ResizeCorner.SOUTH_WEST;
				}
			} else if (x >= currentImage.getActualImage().getAbsoluteLeft()
					+ currentImage.getActualImage().getOffsetWidth()
					&& x <= currentImage.getActualImage().getAbsoluteLeft()
							+ currentImage.getActualImage().getOffsetWidth()
							+ 9) {
				if (y >= currentImage.getActualImage().getAbsoluteTop() - 9
						&& y <= currentImage.getActualImage().getAbsoluteTop()) {
					return ResizeCorner.NORTH_EAST;
				} else if (y >= currentImage.getActualImage().getAbsoluteTop()
						+ currentImage.getActualImage().getOffsetHeight()
						&& y <= currentImage.getActualImage().getAbsoluteTop()
								+ currentImage.getActualImage()
										.getOffsetHeight() + 9) {
					return ResizeCorner.SOUTH_EAST;
				}
			}
		}
		return ResizeCorner.NONE;
	}

	/**
	 * Returns a chemical analysis that is at point x,y on the current image,
	 * this method requires currentImage to be set to something
	 * 
	 * @param x
	 * @param y
	 * @return the ChemicalAnalysis if one is found, else null
	 */
	public ChemicalAnalysis findPointOnGrid(final int x, final int y) {
		// x,y should be with respect to image
		final Iterator<ChemicalAnalysis> itr = currentImage
				.getChemicalAnalyses().iterator();
		while (itr.hasNext()) {
			final ChemicalAnalysis ma = (ChemicalAnalysis) itr.next();
			if (x >= ma.getReferenceX() - 5 && x <= ma.getReferenceX() + 5) {
				if (y >= ma.getReferenceY() - 15 && y <= ma.getReferenceY() + 5) {
					return ma;
				}
			}
		}
		return null;
	}

	/**
	 * Returns the current image on grid that is within the x,y coordinates and
	 * on top of everything else
	 * 
	 * @param x
	 * @param y
	 * @return the ImageOnGridContainer if one is found, else returns null
	 */
	public ImageOnGridContainer findImageOnGrid(final int x, final int y) {
		final Iterator<ImageOnGridContainer> itr = imagesOnGrid.iterator();
		final ArrayList<ImageOnGridContainer> candidates = new ArrayList<ImageOnGridContainer>();
		while (itr.hasNext()) {
			final ImageOnGridContainer iog = itr.next();
			if (x >= iog.getCurrentContainerPosition().x
					&& x <= iog.getCurrentContainerPosition().x
							+ iog.getImageContainer().getOffsetWidth()) {
				if (y >= iog.getCurrentContainerPosition().y
						&& y <= iog.getCurrentContainerPosition().y
								+ iog.getImageContainer().getOffsetHeight()) {
					candidates.add(iog);
				}
			}
		}
		if (candidates.size() > 0) {
			ImageOnGridContainer topmost = candidates.get(0);
			final Iterator<ImageOnGridContainer> candidatesItr = candidates
					.iterator();
			while (candidatesItr.hasNext()) {
				final ImageOnGridContainer iog = candidatesItr.next();
				if (iog.getIog().getZorder() > topmost.getIog().getZorder())
					topmost = iog;
			}
			return topmost;
		}
		return null;
	}

	/**
	 * Handles the panning operation when moving the mouse
	 * 
	 * @param x
	 * @param y
	 */
	private void handlePan(final int x, final int y) {
		final int deltaX = x - lastX;
		final int deltaY = y - lastY;
		imageBrowser.getPanHandler().pan(deltaX, deltaY);
	}

	private void handleMovePoint(final int x, final int y) {
		currentImage.getImagePanel().setWidgetPosition(
				currentPoint.getActualImage(),
				x
						- (currentImage.getImagePanel().getAbsoluteLeft()
								- grid.getAbsoluteLeft() + 4),
				y
						- (currentImage.getImagePanel().getAbsoluteTop()
								- grid.getAbsoluteTop() + 13));
	}

	private void handleMoveImage(final int x, final int y) {
		final int deltaX = x - lastX;
		final int deltaY = y - lastY;
		// Just move where the image is shown to the user
		int newX = currentImage.getCurrentContainerPosition().x + deltaX;
		int newY = currentImage.getCurrentContainerPosition().y + deltaY;
		grid.setWidgetPosition(currentImage.getImageContainer(), newX, newY);
		// we multiple by -1, because if we are zoomed out the images
		// are
		// smaller, but we have to multiply by the scale when moving
		final float scale = imageBrowser.getZoomScale();
		GWT.log("Scale:" + scale + " deltaX:" + deltaX + " deltaY:" + deltaY
				+ " currentX:" + currentImage.getCurrentContainerPosition().x
				+ " currentY:" + currentImage.getCurrentContainerPosition().y
				+ " iogX:" + currentImage.getIog().getTopLeftX() + " iogY:"
				+ currentImage.getIog().getTopLeftY(), null);
		currentImage.move(deltaX, deltaY, scale);
	}

	private void handleMovingPoint(final int x, final int y) {
		currentImage.getImagePanel().setWidgetPosition(
				pointer,
				x
						- (currentImage.getImagePanel().getAbsoluteLeft()
								- grid.getAbsoluteLeft() + 4),
				y
						- (currentImage.getImagePanel().getAbsoluteTop()
								- grid.getAbsoluteTop() + 13));
	}

	public void onMouseMove(final Widget sender, final int x, final int y) {
		if (isBeingDragged) {
			// if we are being dragged we can either pan grid, resize image, or
			// move a point
			switch (mode) {
			case MOVE_IMAGE:
				handleMoveImage(x, y);
				break;
			case PAN_GRID:
				handlePan(x, y);
				break;
			case RESIZE_IMAGE:
				handleResize(x, y);
				break;
			case MOVING_POINT:
				handleMovePoint(x, y);
				break;
			};
		}
		// or we can be moving a point we want to place
		if (mode == MouseMode.PLACE_POINT) {
			handleMovingPoint(x, y);
		}
		lastX = x;
		lastY = y;
	}

	/**
	 * Handles resizing an image
	 * 
	 * @param x
	 * @param y
	 */
	private void handleResize(final int x, final int y) {
		int width = 0;
		int height = 0;
		if (resizeDirection == ResizeCorner.NORTH_WEST) {
			int newX = currentImage.getCurrentContainerPosition().x
					+ (x - lastX);
			int newY = currentImage.getCurrentContainerPosition().y
					+ (int) ((x - lastX) * currentImage.getAspectRatio());
			grid
					.setWidgetPosition(currentImage.getImageContainer(), newX,
							newY);
			width = currentImage.getCurrentWidth()
					+ (currentImage.getCurrentContainerPosition().x - newX);
			height = (int) (width * currentImage.getAspectRatio());
		}
		if (resizeDirection == ResizeCorner.NORTH_EAST) {
			int newY = currentImage.getCurrentContainerPosition().y
					+ (int) ((y - lastY) * currentImage.getAspectRatio());
			grid.setWidgetPosition(currentImage.getImageContainer(),
					currentImage.getCurrentContainerPosition().x, newY);
			height = currentImage.getCurrentHeight()
					+ (currentImage.getCurrentContainerPosition().y - newY);
			width = (int) (height * currentImage.getAspectRatioHeight());
		}
		if (resizeDirection == ResizeCorner.SOUTH_WEST) {
			int newX = currentImage.getCurrentContainerPosition().x
					+ (x - lastX);
			grid.setWidgetPosition(currentImage.getImageContainer(), newX,
					currentImage.getCurrentContainerPosition().y);
			width = currentImage.getCurrentWidth()
					+ (currentImage.getCurrentContainerPosition().x - newX);
			height = (int) (width * currentImage.getAspectRatio());
		}
		if (resizeDirection == ResizeCorner.SOUTH_EAST) {
			int newX = currentImage.getCurrentContainerPosition().x
					+ (x - lastX);
			width = currentImage.getCurrentWidth()
					+ (newX - currentImage.getCurrentContainerPosition().x);
			height = (int) (width * currentImage.getAspectRatio());
		}
		if (!currentImage.getActualImage().getUrl().equals(
				currentImage.getGoodLookingPicture()))
			currentImage.getActualImage().setUrl(
					currentImage.getGoodLookingPicture());
		moveResizedImage(x, y);
		currentImage.resizeImage(width, height, true);
	}

	private void moveResizedImage(final int x, final int y) {
		int deltaX = 0;
		int deltaY = 0;
		if (resizeDirection == ResizeCorner.NORTH_EAST) {
			deltaY = (int) ((y - lastY) * currentImage.getAspectRatio());
		} else if (resizeDirection == ResizeCorner.NORTH_WEST) {
			deltaX = x - lastX;
			deltaY = (int) ((x - lastX) * currentImage.getAspectRatio());
		} else if (resizeDirection == ResizeCorner.SOUTH_WEST) {
			deltaX = x - lastX;
		}

		final float scale = imageBrowser.getZoomScale() * -1 == 0 ? 1
				: imageBrowser.getZoomScale() * -1;
		currentImage.move(deltaX, deltaY, scale);
		currentImage.getIog()
				.setResizeRatio(
						currentImage.getCurrentWidth()
								/ (float) (currentImage.getIog().getImage()
										.getWidth()));
	}

	private void handleEndPan(final int x, final int y) {
		handlePan(x, y);
		grid.removeStyleName("image-moving");
	}

	private void handleEndResize(final int x, final int y) {
		// do the final resize
		handleResize(x, y);
	}

	private void handleEndMovePoint(final int x, final int y) {
		int newX = x
				- (currentImage.getImagePanel().getAbsoluteLeft()
						- grid.getAbsoluteLeft() + 4);
		int newY = y
				- (currentImage.getImagePanel().getAbsoluteTop()
						- grid.getAbsoluteTop() + 13);
		if (newX < 0 || newY < 0
				|| newX > currentImage.getImagePanel().getOffsetWidth()
				|| newY > currentImage.getImagePanel().getOffsetHeight()) {
			newX = currentPoint.getReferenceX();
			newY = currentPoint.getReferenceY();
		}
		currentPoint.setReferenceX(newX);
		currentPoint.setReferenceY(newY);
		currentImage.getImagePanel().setWidgetPosition(
				currentPoint.getActualImage(), newX, newY);
	}

	public void onMouseUp(final Widget sender, final int x, final int y) {
		// DOM.removeEventPreview(eventPreview);
		DOM.releaseCapture(sender.getElement());
		if (isBeingDragged) {
			isBeingDragged = false;

			grid.setCanDrag(true);
			switch (mode) {
			case MOVE_IMAGE:
				handleMoveImage(x, y);
				currentImage.getImageContainer()
						.removeStyleName("image-moving");
				break;
			case PAN_GRID:
				handleEndPan(x, y);
				break;
			case RESIZE_IMAGE:
				handleEndResize(x, y);
				break;
			case MOVING_POINT:
				handleEndMovePoint(x, y);
				break;
			}
		}
	}

	public Collection<ImageOnGridContainer> getImagesOnGrid() {
		return imagesOnGrid;
	}

	public void setImagesOnGrid(Collection<ImageOnGridContainer> imagesOnGrid) {
		this.imagesOnGrid = imagesOnGrid;
	}
}
