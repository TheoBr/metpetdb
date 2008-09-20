package edu.rpi.metpetdb.client.ui.image.browser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.model.ChemicalAnalysis;
import edu.rpi.metpetdb.client.model.Subsample;
import edu.rpi.metpetdb.client.ui.ServerOp;
import edu.rpi.metpetdb.client.ui.image.browser.dialogs.AddPointDialog;
import edu.rpi.metpetdb.client.ui.image.browser.dialogs.PointPopup;
import edu.rpi.metpetdb.client.ui.widgets.MAbsolutePanel;

public class ImageBrowserMouseListener implements MouseListener {

	private final MAbsolutePanel grid;
	private Collection<ImageOnGridContainer> imagesOnGrid;
	private ImageOnGridContainer currentImage;
	private ChemicalAnalysis currentPoint;
	private boolean isBeingDragged = false;
	private int startX;
	private int startY;
	private int positionOnGridX;
	private int positionOnGridY;
	private int mode = -1; // 0 = move image, 1= pan grid, 2 = resizing, 3
	// placing point, 4 moving point
	private String resizeDirection = "";
	private int startWidth;
	private int startHeight;
	private float aspectRatio;
	private float aspectRatioHeight;
	private final ZOrderManager zOrderManager;
	private final Subsample subsample;
	private Widget pointer;
	private final ImageBrowserDetails imageBrowser;
	private final FlowPanel viewControls;

	public void setPoint(final Widget w) {
		pointer = w;
	}

	public void setMode(final int i) {
		mode = i;
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

	public void onMouseDown(final Widget sender, final int x, final int y) {
		// DOM.addEventPreview(eventPreview);
		if (mode == 3) {
			DOM.setStyleAttribute(currentImage.getActualImage().getElement(),
					"cursor", "default");
			new ServerOp() {
				public void begin() {
					if (validateAddChemicalAnalysis(x, y)) {
						new AddPointDialog(subsample, currentImage, this, x, y)
								.show();
					}
				}

				public void onSuccess(final Object result) {
					if (result == null) {
						currentImage.getImagePanel().remove(pointer);
					} else {
						addChemicalAnalysis((ChemicalAnalysis) result, x, y);
					}
					mode = -1;
				}
			}.begin();
			return;
		}
		mode = -1;
		if (!grid.getCanDrag())
			return;
		else {
			if (isInViewControl(x, y))
				return;
			grid.setCanDrag(false);
			DOM.setCapture(sender.getElement());
			currentImage = findImageOnGrid(x, y);
			isBeingDragged = true;
			startX = x;
			startY = y;
			if (currentImage != null && !currentImage.isLocked()) {
				if (!currentImage.isLocked()) {
					currentPoint = findPointOnGrid(
							x
									- (currentImage.getImagePanel()
											.getAbsoluteLeft() - grid
											.getAbsoluteLeft()), y
									- (currentImage.getImagePanel()
											.getAbsoluteTop()
											- grid.getAbsoluteTop() + 13));
					if (currentPoint != null && currentPoint.isLocked())
						currentPoint = null;
					if (currentPoint != null) {
						mode = 4;
					} else
					// User wants to drag an image, resize, or specify z order
					if (grid.getZMode() == 0) {
						resizeDirection = getResizeCorner(sender
								.getAbsoluteLeft()
								+ x, sender.getAbsoluteTop() + y);
						mode = 0;
						positionOnGridX = currentImage.getTemporaryTopLeftX();
						positionOnGridY = currentImage.getTemporaryTopLeftY();
						currentImage.getImageContainer().addStyleName(
								"image-moving");
						if (!resizeDirection.equals("")) {
							mode = 2;
							startWidth = currentImage.getWidth();
							startHeight = currentImage.getHeight();
							aspectRatio = startHeight / (float) startWidth;
							aspectRatioHeight = startWidth
									/ (float) startHeight;
						}
					} else if (grid.getZMode() == 1) {
						zOrderManager.bringToFront(currentImage);
						grid.setZMode(0);
					} else if (grid.getZMode() == 2) {
						zOrderManager.senBack(currentImage);
						grid.setZMode(0);
					}
				}
			} else {
				if (grid.getZMode() != 0) {
					grid.setZMode(0);
				}
				// User wants to Pan the grid
				mode = 1;
				grid.addStyleName("image-moving");
				if (imagesOnGrid != null) {
					final Iterator<ImageOnGridContainer> itr = imagesOnGrid
							.iterator();
					while (itr.hasNext()) {
						final ImageOnGridContainer iog = itr.next();
						iog.setPanTopLeftX(iog.getTemporaryTopLeftX());
						iog.setPanTopLeftY(iog.getTemporaryTopLeftY());
					}
				}
			}
		}
	}

	private void addChemicalAnalysis(final ChemicalAnalysis ma, final int x,
			final int y) {
		ma.setImage(currentImage.getIog().getImage());
		int pointX = x;
		int pointY = y;
		pointX -= currentImage.getImagePanel().getAbsoluteLeft()
				- grid.getAbsoluteLeft() + 4;
		pointY -= currentImage.getImagePanel().getAbsoluteTop()
				- grid.getAbsoluteTop() + 13;
		ma.setPointX(pointX);
		ma.setPointY(pointY);
		ma.setActualImage(pointer);
		ma.setLocked(true);
		((Image) pointer).addClickListener(new ClickListener() {
			public void onClick(final Widget sender) {
				new PointPopup(ma, currentImage, ((Image) pointer)
						.getAbsoluteLeft(), ((Image) pointer).getAbsoluteTop())
						.show();
			}
		});
		ma.setPercentX(pointX / (float) currentImage.getWidth());
		ma.setPercentY(pointY / (float) currentImage.getHeight());
		currentImage.getChemicalAnalyses().add(ma);
	}

	private boolean validateAddChemicalAnalysis(final int x, final int y) {
		int pointX = x;
		int pointY = y;
		pointX -= currentImage.getImagePanel().getAbsoluteLeft()
				- grid.getAbsoluteLeft() + 4;
		pointY -= currentImage.getImagePanel().getAbsoluteTop()
				- grid.getAbsoluteTop() + 13;
		if (pointX < 0 || pointX > currentImage.getWidth()) {
			return false;
		}
		if (pointY < 0 || pointY > currentImage.getHeight()) {
			return false;
		}
		return true;
	}

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

	public String getResizeCorner(final int x, final int y) {
		if (currentImage != null) {
			if (x <= currentImage.getActualImage().getAbsoluteLeft()
					&& x >= currentImage.getActualImage().getAbsoluteLeft() - 9)
				if (y >= currentImage.getActualImage().getAbsoluteTop() - 9
						&& y <= currentImage.getActualImage().getAbsoluteTop()) {
					return "nw";
				} else if (y >= currentImage.getActualImage().getAbsoluteTop()
						+ currentImage.getActualImage().getOffsetHeight()
						&& y <= currentImage.getActualImage().getAbsoluteTop()
								+ currentImage.getActualImage()
										.getOffsetHeight() + 9) {
					return "sw";
				} else
					return "";
			else if (x >= currentImage.getActualImage().getAbsoluteLeft()
					+ currentImage.getActualImage().getOffsetWidth()
					&& x <= currentImage.getActualImage().getAbsoluteLeft()
							+ currentImage.getActualImage().getOffsetWidth()
							+ 9) {
				if (y >= currentImage.getActualImage().getAbsoluteTop() - 9
						&& y <= currentImage.getActualImage().getAbsoluteTop()) {
					return "ne";
				} else if (y >= currentImage.getActualImage().getAbsoluteTop()
						+ currentImage.getActualImage().getOffsetHeight()
						&& y <= currentImage.getActualImage().getAbsoluteTop()
								+ currentImage.getActualImage()
										.getOffsetHeight() + 9) {
					return "se";
				} else
					return "";
			}

		}
		return "";
	}

	public ChemicalAnalysis findPointOnGrid(final int x, final int y) {
		// x,y should be with respect to image
		final Iterator<ChemicalAnalysis> itr = currentImage
				.getChemicalAnalyses().iterator();
		while (itr.hasNext()) {
			final ChemicalAnalysis ma = (ChemicalAnalysis) itr.next();
			if (x >= ma.getPointX() - 5 && x <= ma.getPointX() + 5) {
				if (y >= ma.getPointY() - 15 && y <= ma.getPointY() + 5) {
					return ma;
				}
			}
		}
		return null;
	}

	public ImageOnGridContainer findImageOnGrid(final int x, final int y) {
		final Iterator<ImageOnGridContainer> itr = imagesOnGrid.iterator();
		final ArrayList<ImageOnGridContainer> candidates = new ArrayList<ImageOnGridContainer>();
		while (itr.hasNext()) {
			final ImageOnGridContainer iog = itr.next();
			if (x >= iog.getTemporaryTopLeftX()
					&& x <= iog.getTemporaryTopLeftX()
							+ iog.getImageContainer().getOffsetWidth()) {
				if (y >= iog.getTemporaryTopLeftY()
						&& y <= iog.getTemporaryTopLeftY()
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

	private void handlePan(final int x, final int y) {
		int newX = positionOnGridX + (x - startX);
		int newY = positionOnGridY + (y - startY);
		DOM.setStyleAttribute(grid.getElement(), "backgroundPosition", x
				+ "px " + y + "px");

		if (imagesOnGrid != null) {
			final Iterator<ImageOnGridContainer> itr = imagesOnGrid.iterator();
			while (itr.hasNext()) {
				final ImageOnGridContainer iog = itr.next();
				newX = iog.getTemporaryTopLeftX() + (x - startX);
				newY = iog.getTemporaryTopLeftY() + (y - startY);
				grid.setWidgetPosition(iog.getImageContainer(), newX, newY);
				iog.setPanTopLeftX(newX);
				iog.setPanTopLeftY(newY);
			}
		}
	}

	public void onMouseMove(final Widget sender, final int x, final int y) {
		if (isBeingDragged) {
			int newX = positionOnGridX + (x - startX);
			int newY = positionOnGridY + (y - startY);
			switch (mode) {
			case 0:
				grid.setWidgetPosition(currentImage.getImageContainer(), newX,
						newY);
				currentImage.setTemporaryTopLeftX(newX);
				currentImage.setTemporaryTopLeftY(newY);
				break;
			case 1:
				handlePan(x, y);
				break;
			case 2:
				resize(x, y);
				break;
			case 4:
				currentImage.getImagePanel().setWidgetPosition(
						currentPoint.getActualImage(),
						x
								- (currentImage.getImagePanel()
										.getAbsoluteLeft()
										- grid.getAbsoluteLeft() + 4),
						y
								- (currentImage.getImagePanel()
										.getAbsoluteTop()
										- grid.getAbsoluteTop() + 13));
				break;
			};
		}
		if (mode == 3) {
			currentImage.getImagePanel().setWidgetPosition(
					pointer,
					x
							- (currentImage.getImagePanel().getAbsoluteLeft()
									- grid.getAbsoluteLeft() + 4),
					y
							- (currentImage.getImagePanel().getAbsoluteTop()
									- grid.getAbsoluteTop() + 13));
		}
	}

	private void resize(final int x, final int y) {
		int width = 0;
		int height = 0;
		if ("nw".equals(resizeDirection)) {
			int newX = positionOnGridX + (x - startX);
			int newY = positionOnGridY + (int) ((x - startX) * aspectRatio);
			grid
					.setWidgetPosition(currentImage.getImageContainer(), newX,
							newY);

			width = startWidth + (positionOnGridX - newX);
			height = (int) (width * aspectRatio);
		}
		if ("ne".equals(resizeDirection)) {
			int newY = positionOnGridY + (int) ((y - startY) * aspectRatio);
			grid.setWidgetPosition(currentImage.getImageContainer(),
					positionOnGridX, newY);
			height = startHeight + (positionOnGridY - newY);
			width = (int) (height * aspectRatioHeight);
		}
		if ("sw".equals(resizeDirection)) {
			int newX = positionOnGridX + (x - startX);
			grid.setWidgetPosition(currentImage.getImageContainer(), newX,
					positionOnGridY);
			width = startWidth + (positionOnGridX - newX);
			height = (int) (width * aspectRatio);
		}
		if ("se".equals(resizeDirection)) {
			int newX = positionOnGridX + (x - startX);
			width = startWidth + (newX - positionOnGridX);
			height = (int) (width * aspectRatio);
		}
		if (!currentImage.getActualImage().getUrl().equals(
				currentImage.getGoodLookingPicture()))
			currentImage.getActualImage().setUrl(
					currentImage.getGoodLookingPicture());
		currentImage.resizeImage(width, height, true);
	}

	private void handleEndPan(final int x, final int y) {
		grid.removeStyleName("image-moving");
		imageBrowser.adTotalXOffset((x - startX));
		imageBrowser.adTotalYOffset((y - startY));
		if (imagesOnGrid != null) {
			final Iterator<ImageOnGridContainer> itr = imagesOnGrid.iterator();
			while (itr.hasNext()) {
				final ImageOnGridContainer iog = itr.next();
				iog.setTemporaryTopLeftX(iog.getPanTopLeftX());
				iog.setTemporaryTopLeftY(iog.getPanTopLeftY());
			}
		}
	}

	private void handleEndResize(final int x, final int y) {
		currentImage.setTemporaryTopLeftX(grid.getWidgetLeft(currentImage
				.getImageContainer()));
		currentImage.setTemporaryTopLeftY(grid.getWidgetTop(currentImage
				.getImageContainer()));
		currentImage.getIog().setTopLeftX(currentImage.getTemporaryTopLeftX());
		currentImage.getIog().setTopLeftY(currentImage.getTemporaryTopLeftY());
		currentImage.setWidth(currentImage.getActualImage().getOffsetWidth());
		currentImage.setHeight(currentImage.getActualImage().getOffsetHeight());
		currentImage.getIog()
				.setResizeRatio(
						currentImage.getWidth()
								/ (float) (currentImage.getIog().getImage()
										.getWidth()));
		if (!((Image) currentImage.getActualImage()).getUrl().equals(
				currentImage.getGoodLookingPicture()))
			((Image) currentImage.getActualImage()).setUrl(currentImage
					.getGoodLookingPicture());
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
			newX = currentPoint.getPointX();
			newY = currentPoint.getPointY();
		}
		currentPoint.setPointX(newX);
		currentPoint.setPointY(newY);
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
			case 0:
				currentImage.getIog().setTopLeftX(
						currentImage.getTemporaryTopLeftX());
				currentImage.getIog().setTopLeftY(
						currentImage.getTemporaryTopLeftY());
				currentImage.getImageContainer()
						.removeStyleName("image-moving");
				break;
			case 1:
				handleEndPan(x, y);
				break;
			case 2:
				handleEndResize(x, y);
				break;
			case 4:
				handleEndMovePoint(x, y);
				break;
			}
		}
	}

	public Collection<ImageOnGridContainer> getImagesOnGrid() {
		return imagesOnGrid;
	}

	public void setImagesOnGrid(Set<ImageOnGridContainer> imagesOnGrid) {
		this.imagesOnGrid = imagesOnGrid;
	}
}
