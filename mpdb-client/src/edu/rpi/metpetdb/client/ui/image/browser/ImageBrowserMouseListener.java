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
import edu.rpi.metpetdb.client.ui.widgets.panels.MAbsolutePanel;
import edu.rpi.metpetdb.client.ui.widgets.panels.MAbsolutePanel.ZMode;

public class ImageBrowserMouseListener implements MouseListener {

	private final MAbsolutePanel grid;
	private Collection<ImageOnGridContainer> imagesOnGrid;
	private ImageOnGridContainer currentImage;
	private ChemicalAnalysis currentPoint;
	private boolean isBeingDragged = false;
	private int lastX;
	private int lastY;
	private MouseMode mode;
	private ClickMode clickMode = ClickMode.NORMAL;
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
	
	public enum ClickMode {
		NORMAL, CTRL, SHIFT
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
			public void cancel() {
				mode = MouseMode.NONE;
				// remove the arrow for the spot
				pointer.removeFromParent();
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
			for (ImageOnGridContainer iog : imageBrowser.getSelectedImages()) {
				iog.getImageContainer().setStylePrimaryName("image-moving");
			}
			currentImage.getImageContainer().setStylePrimaryName("image-moving");		
			if (resizeDirection != ResizeCorner.NONE) {
				mode = MouseMode.RESIZE_IMAGE;
				currentImage.setupForResize();
			} else {
				// if we are not resizing then we are moving
				mode = MouseMode.MOVE_IMAGE;
			}
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
					if (findImageAndMenuOnGrid(x,y) == null) {
						// User wants to Pan the grid
						mode = MouseMode.PAN_GRID;
						grid.addStyleName("image-moving");
					} else {
						mode = MouseMode.NONE;
					}
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
		double pointX = x;
		double pointY = y;
		pointX -= currentImage.getImagePanel().getAbsoluteLeft()
				- grid.getAbsoluteLeft() ;
		pointY -= currentImage.getImagePanel().getAbsoluteTop()
				- grid.getAbsoluteTop() ;

		ca.setReferenceX(ImageBrowserUtil.pixelsToMM(pointX,imageBrowser.scale,currentImage.getIog().getResizeRatio()));
		ca.setReferenceY(ImageBrowserUtil.pixelsToMM(pointY,imageBrowser.scale,currentImage.getIog().getResizeRatio()));
		ca.setActualImage(pointer);
		ca.setLocked(true);
		((Image) pointer).addClickListener(new ClickListener() {
			public void onClick(final Widget sender) {
				new PointPopup(ca, currentImage, ((Image) pointer)
						.getAbsoluteLeft(), ((Image) pointer).getAbsoluteTop(), imageBrowser)
						.show();
			}
		});
		ca.setPercentX((float) pointX / (float) currentImage.getCurrentWidth());
		ca.setPercentY((float) pointY / (float) currentImage.getCurrentHeight());
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
				- grid.getAbsoluteLeft() ;
		pointY -= currentImage.getImagePanel().getAbsoluteTop()
				- grid.getAbsoluteTop() ;
		pointX -= 4;
		pointY -= 13;
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
	
	public ImageOnGridContainer findImageAndMenuOnGrid(final int x, final int y) {
		final Iterator<ImageOnGridContainer> itr = imagesOnGrid.iterator();
	final ArrayList<ImageOnGridContainer> candidates = new ArrayList<ImageOnGridContainer>();
	while (itr.hasNext()) {
		final ImageOnGridContainer iog = itr.next();
		if (x >= iog.getCurrentContainerPosition().x
				&& x <= iog.getCurrentContainerPosition().x
						+ iog.getImageContainer().getOffsetWidth()) {
			if (y >= iog.getCurrentContainerPosition().y - iog.getImageContainer().getWidget(0).getOffsetHeight()
					&& y <= iog.getCurrentContainerPosition().y
							+ iog.getImageContainer().getWidget(1).getOffsetHeight() + iog.getImageContainer().getWidget(2).getOffsetHeight()) {
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
								+ iog.getImageContainer().getWidget(1).getOffsetHeight()) {
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
		
		if (!imageBrowser.getSelectedImages().contains(currentImage)) {
			if (clickMode == ClickMode.NORMAL) {
				for (ImageOnGridContainer iog : imageBrowser.getSelectedImages()) {
					iog.getImageContainer().removeStyleDependentName("selected");
				}
				imageBrowser.getSelectedImages().clear();
			}
			handleMoveImageHelper(currentImage,deltaX,deltaY);
		}
		
		for (ImageOnGridContainer iog : imageBrowser.getSelectedImages()) {
			handleMoveImageHelper(iog, deltaX, deltaY);
		}
	}
	
	private void handleMoveImageHelper(final ImageOnGridContainer iog, final int deltaX, final int deltaY) {
		// Just move where the image is shown to the user
		
		final double scale = imageBrowser.scale;
		double newX = iog.getCurrentContainerPosition().x + deltaX;
		double newY = iog.getCurrentContainerPosition().y + deltaY;
		grid.setWidgetPosition(iog.getImageContainer(), newX, newY);
		// we multiple by -1, because if we are zoomed out the images
		// are
		// smaller, but we have to multiply by the scale when moving

		GWT.log("Scale:" + scale + " deltaX:" + deltaX + " deltaY:" + deltaY
				+ " currentX:" + iog.getCurrentContainerPosition().x
				+ " currentY:" + iog.getCurrentContainerPosition().y
				+ " iogX:" + iog.getIog().getTopLeftX() + " iogY:"
				+ iog.getIog().getTopLeftY(), null);
		iog.move(deltaX, deltaY, scale, imageBrowser);
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
	
	private void handleResizeHelper(ImageOnGridContainer iog, final int x, final int y) {
		double width = 0;
		double height = 0;
		if (resizeDirection == ResizeCorner.NORTH_WEST) {
			double newX = iog.getCurrentContainerPosition().x
					+ (x - lastX);
			double newY = iog.getCurrentContainerPosition().y
					+  ((x - lastX) * iog.getAspectRatio());
			grid
					.setWidgetPosition(iog.getImageContainer(), newX,
							newY);
			width = iog.getCurrentWidth()
					+ (iog.getCurrentContainerPosition().x - newX);
			height = (width * iog.getAspectRatio());
		}
		if (resizeDirection == ResizeCorner.NORTH_EAST) {
			double newY = iog.getCurrentContainerPosition().y
					+ ((y - lastY) * iog.getAspectRatio());
			grid.setWidgetPosition(iog.getImageContainer(),
					iog.getCurrentContainerPosition().x, newY);
			height = iog.getCurrentHeight()
					+ (iog.getCurrentContainerPosition().y - newY);
			width = (height * iog.getAspectRatioHeight());
		}
		if (resizeDirection == ResizeCorner.SOUTH_WEST) {
			double newX = iog.getCurrentContainerPosition().x
					+ (x - lastX);
			grid.setWidgetPosition(iog.getImageContainer(), newX,
					iog.getCurrentContainerPosition().y);
			width = iog.getCurrentWidth()
					+ (iog.getCurrentContainerPosition().x - newX);
			height = (width * iog.getAspectRatio());
		}
		if (resizeDirection == ResizeCorner.SOUTH_EAST) {
			double newX = iog.getCurrentContainerPosition().x
					+ (x - lastX);
			width = iog.getCurrentWidth()
					+ (newX - iog.getCurrentContainerPosition().x);
			height = (width * iog.getAspectRatio());
		}
		if (!iog.getActualImage().getUrl().equals(
				iog.getGoodLookingPicture()))
			iog.getActualImage().setUrl(
					iog.getGoodLookingPicture());
		moveResizedImage(x, y, iog);
		iog.resizeImage(width, height, true);
		imageBrowser.updatePoints(iog);
		imageBrowser.layers.updateImageScale(iog);
	}

	/**
	 * Handles resizing an image
	 * 
	 * @param x
	 * @param y
	 */
	private void handleResize(final int x, final int y) {
		if (!imageBrowser.getSelectedImages().contains(currentImage)) {
			if (clickMode == ClickMode.NORMAL) {
				for (ImageOnGridContainer iog : imageBrowser.getSelectedImages()) {
					iog.getImageContainer().removeStyleDependentName("selected");
				}
				imageBrowser.getSelectedImages().clear();
			}
			handleResizeHelper(currentImage,x,y);
		}
		
		for (ImageOnGridContainer iog : imageBrowser.getSelectedImages()) {
			handleResizeHelper(iog,x,y);
		}
	}

	private void moveResizedImage(final int x, final int y, final ImageOnGridContainer iog) {
		int deltaX = 0;
		int deltaY = 0;
		if (resizeDirection == ResizeCorner.NORTH_EAST) {
			deltaY = (int) ((y - lastY) * iog.getAspectRatio());
		} else if (resizeDirection == ResizeCorner.NORTH_WEST) {
			deltaX = x - lastX;
			deltaY = (int) ((x - lastX) * iog.getAspectRatio());
		} else if (resizeDirection == ResizeCorner.SOUTH_WEST) {
			deltaX = x - lastX;
		}
	
		final double scale = imageBrowser.scale;
		iog.move(deltaX, deltaY, scale,imageBrowser);
		edu.rpi.metpetdb.client.model.Image i = iog.getIog().getImage();
		iog.getIog()
				.setResizeRatio(
						iog.getCurrentWidth()
								/ (float) (iog.getIog().getGwidth()*(((float)i.getScale()/(float)i.getWidth())/imageBrowser.scale)*(float)ImageBrowserUtil.pps));
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
		double newX = x
				- (currentImage.getImagePanel().getAbsoluteLeft()
						- grid.getAbsoluteLeft() + 4);
		double newY = y
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
				currentPoint.getActualImage(), (int)Math.round(newX), (int)Math.round(newY));
	}

	public void onMouseUp(final Widget sender, final int x, final int y) {
		DOM.releaseCapture(sender.getElement());
		if (isBeingDragged) {
			isBeingDragged = false;

			grid.setCanDrag(true);
			switch (mode) {
				case MOVE_IMAGE:
					handleMoveImage(x, y);
					currentImage.getImageContainer().removeStyleName(
							"image-moving");
					currentImage.getImageContainer().setStylePrimaryName("imageContainer");
					currentImage.getImageContainer().addStyleDependentName("selected");
					for (ImageOnGridContainer iog : imageBrowser.getSelectedImages()) {
						iog.getImageContainer().removeStyleName("image-moving");
						iog.getImageContainer().setStylePrimaryName("imageContainer");
						iog.getImageContainer().addStyleDependentName("selected");
					}
					switch (clickMode) {
						case NORMAL:
							if (!imageBrowser.getSelectedImages().contains(currentImage)) { 
								for (ImageOnGridContainer iog : imageBrowser.getSelectedImages()) {
									iog.getImageContainer().removeStyleDependentName("selected");
								}
								imageBrowser.getSelectedImages().clear();
								imageBrowser.getSelectedImages().add(currentImage);
								currentImage.getImageContainer().addStyleDependentName("selected");
							}
							break;
						case SHIFT:
							if (imageBrowser.getSelectedImages().contains(currentImage)) {
								currentImage.getImageContainer().removeStyleDependentName("selected");
								imageBrowser.getSelectedImages().remove(currentImage);
							} else {
								imageBrowser.getSelectedImages().add(currentImage);
								currentImage.getImageContainer().addStyleDependentName("selected");
							}
							break;
							case CTRL:
								if (imageBrowser.getSelectedImages().contains(currentImage)) {
								currentImage.getImageContainer().removeStyleDependentName("selected");
								imageBrowser.getSelectedImages().remove(currentImage);
							} else {
								imageBrowser.getSelectedImages().add(currentImage);
								currentImage.getImageContainer().addStyleDependentName("selected");
							}
								break;
					}

					break;
				case PAN_GRID:
					handleEndPan(x, y);
					for (ImageOnGridContainer iog : imageBrowser.getSelectedImages()) {
						iog.getImageContainer().removeStyleDependentName("selected");
					}
					imageBrowser.getSelectedImages().clear();
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
	
	public void setClickMode(final ClickMode mode) {
		clickMode = mode;
	}
}
