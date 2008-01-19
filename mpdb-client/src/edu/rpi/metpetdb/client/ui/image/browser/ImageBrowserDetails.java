package edu.rpi.metpetdb.client.ui.image.browser;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.locale.LocaleHandler;
import edu.rpi.metpetdb.client.model.Grid;
import edu.rpi.metpetdb.client.model.ImageOnGrid;
import edu.rpi.metpetdb.client.model.MineralAnalysis;
import edu.rpi.metpetdb.client.model.Subsample;
import edu.rpi.metpetdb.client.ui.MetPetDBApplication;
import edu.rpi.metpetdb.client.ui.MpDb;
import edu.rpi.metpetdb.client.ui.ServerOp;
import edu.rpi.metpetdb.client.ui.TokenSpace;
import edu.rpi.metpetdb.client.ui.image.browser.click.listeners.AddPointListener;
import edu.rpi.metpetdb.client.ui.image.browser.click.listeners.HideMenuListener;
import edu.rpi.metpetdb.client.ui.image.browser.click.listeners.LockListener;
import edu.rpi.metpetdb.client.ui.image.browser.click.listeners.OpacityListener;
import edu.rpi.metpetdb.client.ui.image.browser.click.listeners.RemoveListener;
import edu.rpi.metpetdb.client.ui.image.browser.click.listeners.RotateListener;
import edu.rpi.metpetdb.client.ui.image.browser.dialogs.AddExistingImagePopup;
import edu.rpi.metpetdb.client.ui.image.browser.dialogs.PointPopup;
import edu.rpi.metpetdb.client.ui.image.browser.dialogs.PopupMenu;
import edu.rpi.metpetdb.client.ui.image.browser.widgets.DCFlowPanel;
import edu.rpi.metpetdb.client.ui.image.browser.widgets.ResizableWidget;
import edu.rpi.metpetdb.client.ui.widgets.ImageHyperlink;
import edu.rpi.metpetdb.client.ui.widgets.MAbsolutePanel;
import edu.rpi.metpetdb.client.ui.widgets.MLink;
import edu.rpi.metpetdb.client.ui.widgets.MUnorderedList;

//TODO stores the imagesongrid better for transient stuff
public class ImageBrowserDetails extends FlowPanel implements ClickListener {

	// Links
	private final MLink addExistingImage;
	private MLink addNewImageToSubsample;
	private final MLink bringToFront;
	private final MLink sendToBack;
	private final MLink panUp = new MLink("", this);
	private final MLink panRight = new MLink("", this);
	private final MLink panDown = new MLink("", this);
	private final MLink panLeft = new MLink("", this);
	private final MLink panHome = new MLink("", this);

	// Buttons/Labels/Widgets
	private final Button save;
	private final Label info;

	private Grid g;
	private final MAbsolutePanel grid;
	private LeftSideLayer layers;

	// Listeners
	private ImageBrowserMouseListener mouseListener;
	private LockListener lockListener;
	private HideMenuListener hideMenuListener;
	private final ZOrderManager zOrderManager = new ZOrderManager();

	// Scale
	private int totalXOffset = 0;
	private int totalYOffset = 0;
	private final Label scaleDisplay;
	private float scale = 30; /* in milli meters */
	private int unit = 1;
	private static String[] units = {
	"m", /* meter 10x10^0 */
	"mm", /* millimeter 10x10^-3 */
	"µm", /* micrometer 10x10^-6 */
	"nm", /* nanometer 10x10^-9 */
	};

	public ImageBrowserDetails() {
		info = new Label();
		scaleDisplay = new Label();
		scaleDisplay.setStyleName("mpdb-scale");
		scaleDisplay.setText(String.valueOf(scale));
		addExistingImage = new MLink("Add Existing Image", this);
		addExistingImage.setStyleName("addlink");
		bringToFront = new MLink("Bring To Front", this);
		sendToBack = new MLink("Send To Back", this);
		save = new Button(LocaleHandler.lc_text.buttonSave(), this);
		grid = new MAbsolutePanel();
		grid.add(scaleDisplay);
	}

	public ImageBrowserDetails createNew(final long subsampleId) {
		new ServerOp() {
			public void begin() {
				MpDb.subsample_svc.details(subsampleId, this);
			}
			public void onSuccess(final Object result) {
				final Subsample s = (Subsample) result;
				g = new Grid();
				g.setSubsample(s);
				s.setGrid(g);
				buildInterface();
				doSave();
			}
		}.begin();
		return this;
	}

	public ImageBrowserDetails showById(final long id) {
		new ServerOp() {
			public void begin() {
				MpDb.imageBrowser_svc.details(id, this);
			}
			public void onSuccess(final Object result) {
				g = (Grid) result;
				buildInterface();
				addExistingImages(true);
			}
		}.begin();
		return this;
	}

	private void buildInterface() {
		final Element header = DOM.createElement("h2");
		final Element sampleHeader = DOM.createElement("h1");
		DOM.setInnerText(header, g.getSubsample().getName() + "'s Map");
		DOM.setInnerText(sampleHeader, g.getSubsample().getSample().getAlias());
		DOM.appendChild(this.getElement(), sampleHeader);
		DOM.appendChild(this.getElement(), header);
		DOM.setElementAttribute(grid.getElement(), "id", "canvas");
		final FlowPanel fp = createViewControls();
		grid.add(fp);
		final MUnorderedList ul = new MUnorderedList();
		addNewImageToSubsample = new MLink("Add New Image to Subsample",
				TokenSpace.edit(g.getSubsample()));
		addNewImageToSubsample.setStyleName("addlink");
		ul.setStyleName("options");
		ul.add(addExistingImage);
		ul.add(addNewImageToSubsample);
		ul.add(bringToFront);
		ul.add(sendToBack);
		add(ul);
		add(grid);
		add(save);
		add(info);
		layers = new LeftSideLayer(g.getSubsample().getName());
		MetPetDBApplication.appendToLeft(layers);
		mouseListener = new ImageBrowserMouseListener(grid,
				g.getImagesOnGrid(), zOrderManager, g.getSubsample(), this, fp);
		grid.addMouseListener(mouseListener);
	}

	public void updateScale(final float multiplier) {
		scale *= multiplier;

		while (String.valueOf((int) scale).length() > 3) {
			++unit;
			if (unit == units.length) {
				--unit;
			} else {
				scale *= 1000;
			}
		}
		if ((int) scale == 0) {
			--unit;
			scale /= 1000;
		}

		if (unit >= units.length || unit < 0)
			return;
		scaleDisplay.setText(String.valueOf(scale) + " " + units[unit]);
	}

	public void onLoad() {
		super.onLoad();
	}

	private void addExistingImages(boolean firstTime) {
		final Iterator itr = g.getImagesOnGrid().iterator();
		while (itr.hasNext()) {
			final ImageOnGrid iog = (ImageOnGrid) itr.next();
			if (firstTime) {
				iog.setWidth(Math.round((iog.getImage().getWidth() * iog
						.getResizeRatio())));
				iog.setHeight(Math.round((iog.getImage().getHeight() * iog
						.getResizeRatio())));
			}

			addImage(iog);
		}
	}

	private void addImage(final edu.rpi.metpetdb.client.model.Image i,
			int[] cascade) {
		ImageOnGrid iog = new ImageOnGrid();
		iog.setGrid(g);
		iog.setImage(i);
		iog.setOpacity(100);
		iog.setResizeRatio(1);
		iog.setZorder(1);
		iog.setTopLeftX(cascade[0]);
		iog.setTopLeftY(cascade[0]);
		iog.setWidth(Math.round((iog.getImage().getWidth() * (iog
				.getResizeRatio()))));
		iog.setHeight(Math.round((iog.getImage().getHeight() * (iog
				.getResizeRatio()))));
		iog.setZorder(zOrderManager.getHighestZOrder() + 1);
		iog.setGwidth(i.getWidth());
		iog.setGheight(i.getHeight());
		iog.setGchecksum(i.getChecksum());
		iog.setGchecksum64x64(i.getChecksum64x64());
		iog.setGchecksumHalf(i.getChecksumHalf());
		addImage(iog);
		cascade[0] += 10;
		g.addImageOnGrid(iog);
	}

	public void addImage(final ImageOnGrid iog) {
		final DCFlowPanel imageContainer = new DCFlowPanel();
		imageContainer.setStyleName("imageContainer");
		iog.setImageContainer(imageContainer);
		scale(iog);

		iog.setTemporaryTopLeftX(iog.getTopLeftX());
		iog.setTemporaryTopLeftY(iog.getTopLeftY());

		final SimplePanel imagePanel = createImagePanel(iog);

		lockListener = new LockListener(iog);
		hideMenuListener = new HideMenuListener(iog);

		imageContainer.add(makeTopMenu(iog));
		imageContainer.add(imagePanel);
		imageContainer.add(makeBottomMenu(iog));

		final PopupMenu popupMenu = createPopupMenu(iog);
		imageContainer.setPopupMenu(popupMenu);

		DOM.setStyleAttribute(imageContainer.getElement(), "zIndex", String
				.valueOf(iog.getZorder()));
		setOpacity(iog.getActualImage().getElement(), iog.getOpacity());
		setOpacity(imageContainer.getElement(), iog.getOpacity());

		layers.registerImage(iog);
		zOrderManager.register(iog);

		grid.add(imageContainer, iog.getTemporaryTopLeftX(), iog
				.getTemporaryTopLeftY());
	}

	private PopupMenu createPopupMenu(final ImageOnGrid iog) {
		final PopupMenu popupMenu = new PopupMenu();
		popupMenu.addItem(new ImageHyperlink(new Image(GWT.getModuleBaseURL()
				+ "/images/icon-details.gif"), "Details", TokenSpace
				.detailsOf(g.getSubsample()), false));
		popupMenu.addItem(new ImageHyperlink(new Image(GWT.getModuleBaseURL()
				+ "/images/icon-addpoint.gif"), "Add Point",
				new AddPointListener(mouseListener, iog), false));
		popupMenu.addItem(new ImageHyperlink(new Image(GWT.getModuleBaseURL()
				+ "/images/icon-remove.gif"), "Remove", new RemoveListener(iog,
				layers), false));
		popupMenu.addItem(new ImageHyperlink(new Image(GWT.getModuleBaseURL()
				+ "/images/icon-rotate.gif"), "Rotate",
				new RotateListener(iog), false));
		popupMenu.addItem(new ImageHyperlink(new Image(GWT.getModuleBaseURL()
				+ "/images/icon-opacity.gif"), "Opacity", new OpacityListener(
				iog, grid), false));
		final MLink lock = new MLink("Lock", lockListener);
		lockListener.addNotifier(lock);
		popupMenu.addItem(lock);
		final MLink hideMenu = new MLink("Hide Menu", hideMenuListener);
		hideMenuListener.addNotifier(hideMenu);
		popupMenu.addItem(hideMenu);
		return popupMenu;
	}

	private SimplePanel createImagePanel(final ImageOnGrid iog) {
		final SimplePanel imagePanel = new SimplePanel();
		final AbsolutePanel ap = new AbsolutePanel();
		ap.setHeight(iog.getHeight() + "px");
		ap.setWidth(iog.getWidth() + "px");
		final Image image = new Image(iog.getGoodLookingPicture());
		ap.add(image, 0, 0);
		final HashSet mineralAnalyses = getMineralAnalyses(iog.getImage(), g
				.getSubsample().getMineralAnalyses());
		iog.setMineralAnalyses(mineralAnalyses);

		image.setWidth(iog.getWidth() + "px");
		image.setHeight(iog.getHeight() + "px");
		final ResizableWidget rw = new ResizableWidget(ap, grid, iog
				.getImageContainer(), iog);
		imagePanel.setWidget(rw);
		iog.setActualImage(image);
		iog.setImagePanel(ap);
		addPoints(iog);
		return imagePanel;
	}

	public void addToTotalXOffset(final int amount) {
		totalXOffset += amount;
	}

	public void addToTotalYOffset(final int amount) {
		totalYOffset += amount;
	}

	private void addPoints(final ImageOnGrid iog) {
		final Iterator itr = iog.getMineralAnalyses().iterator();
		while (itr.hasNext()) {
			final MineralAnalysis ma = (MineralAnalysis) itr.next();
			final Image i = new Image(GWT.getModuleBaseURL()
					+ "/images/point0.gif");
			iog.getImagePanel().add(i, ma.getPointX(), ma.getPointY());
			ma.setActualImage(i);
			ma.setPercentX(ma.getPointX() / (float) iog.getWidth());
			ma.setPercentY(ma.getPointY() / (float) iog.getHeight());
			ma.setIsLocked(true);
			i.addClickListener(new ClickListener() {
				public void onClick(final Widget sender) {
					new PointPopup(ma, iog, i.getAbsoluteLeft(), i
							.getAbsoluteTop()).show();
				}
			});
		}
	}

	private HashSet getMineralAnalyses(
			final edu.rpi.metpetdb.client.model.Image i,
			final Set mineralAnalyses) {
		final Iterator itr = mineralAnalyses.iterator();
		final HashSet mas = new HashSet();
		while (itr.hasNext()) {
			final MineralAnalysis ma = (MineralAnalysis) itr.next();
			if (ma.getImage() != null) {
				if (ma.getImage().equals(i))
					mas.add(ma);
			}
		}
		return mas;
	}

	private void scale(final ImageOnGrid iog) {
		// final int multiplier = zoomHandler.getCurrentScale();
		final int multiplier = 1;
		iog.setWidth(Math.round((iog.getImage().getWidth() * multiplier * iog
				.getResizeRatio())));
		iog.setHeight(Math.round((iog.getImage().getHeight() * multiplier * iog
				.getResizeRatio())));
	}

	private MUnorderedList makeBottomMenu(final ImageOnGrid iog) {
		final MUnorderedList ulBottom = new MUnorderedList();
		final MLink details = new ImageHyperlink(new Image(GWT
				.getModuleBaseURL()
				+ "/images/icon-details.gif"), "Details", TokenSpace
				.detailsOf(g.getSubsample()), false);
		final MLink addPoint = new ImageHyperlink(new Image(GWT
				.getModuleBaseURL()
				+ "/images/icon-addpoint.gif"), "Add Point",
				new AddPointListener(mouseListener, iog), false);
		final MLink remove = new ImageHyperlink(new Image(GWT
				.getModuleBaseURL()
				+ "/images/icon-remove.gif"), "Remove", new RemoveListener(iog,
				layers), false);
		ulBottom.add(details);
		ulBottom.add(addPoint);
		ulBottom.add(remove);
		ulBottom.setLiStyle("last", remove);
		ulBottom.setStyleName("imageMenu-bottom");
		return ulBottom;
	}

	private MUnorderedList makeTopMenu(final ImageOnGrid iog) {
		// Top Menu
		final MUnorderedList ulTop = new MUnorderedList();
		final MLink rotate = new ImageHyperlink(new Image(GWT
				.getModuleBaseURL()
				+ "/images/icon-rotate.gif"), "Rotate",
				new RotateListener(iog), false);
		final MLink opacity = new ImageHyperlink(new Image(GWT
				.getModuleBaseURL()
				+ "/images/icon-opacity.gif"), "Opacity", new OpacityListener(
				iog, grid), false);
		final MLink hideMenu = new MLink("Hide Menu", hideMenuListener);
		hideMenuListener.addNotifier(hideMenu);
		final MLink lock = new MLink("Lock", lockListener);
		lockListener.addNotifier(lock);
		ulTop.add(rotate);
		ulTop.add(opacity);
		ulTop.add(hideMenu);
		ulTop.add(lock);
		ulTop.setLiStyle("last", lock);
		ulTop.setStyleName("imageMenu-top");
		return ulTop;
	}

	private void setOpacity(final Element element, final int amount) {
		DOM.setStyleAttribute(element, "opacity", String
				.valueOf(amount / 100.0));
		DOM.setStyleAttribute(element, "filter", "alpha(opacity=" + amount
				+ ")");
	}

	private FlowPanel createViewControls() {
		final FlowPanel viewControls = new FlowPanel();
		final FlowPanel panning = new FlowPanel();
		final FlowPanel zoomSlider = new FlowPanel();
		DOM.setElementAttribute(zoomSlider.getElement(), "id", "zoomSlider");
		DOM.setElementAttribute(panning.getElement(), "id", "panning");
		DOM.setElementAttribute(viewControls.getElement(), "id", "viewControl");
		final MLink zSlide = new MLink("", this);
		DOM.setStyleAttribute(zSlide.getElement(), "top", "60px");
		final MLink zIn = new MLink("", new ZoomInListener(g, zSlide
				.getElement(), this));
		final MLink zOut = new MLink("", new ZoomOutListener(g, zSlide
				.getElement(), this));
		panUp.setStyleName("imageBrowser-hyperlink");
		panRight.setStyleName("imageBrowser-hyperlink");
		panDown.setStyleName("imageBrowser-hyperlink");
		panLeft.setStyleName("imageBrowser-hyperlink");
		panHome.setStyleName("imageBrowser-hyperlink");
		zIn.setStyleName("imageBrowser-hyperlink");
		zSlide.setStyleName("imageBrowser-hyperlink");
		zOut.setStyleName("imageBrowser-hyperlink");
		DOM.setElementAttribute(panUp.getElement(), "id", "panUp");
		DOM.setElementAttribute(panRight.getElement(), "id", "panRight");
		DOM.setElementAttribute(panDown.getElement(), "id", "panDown");
		DOM.setElementAttribute(panLeft.getElement(), "id", "panLeft");
		DOM.setElementAttribute(panHome.getElement(), "id", "panHome");
		DOM.setElementAttribute(zIn.getElement(), "id", "zIn");
		DOM.setElementAttribute(zSlide.getElement(), "id", "zSlide");
		DOM.setElementAttribute(zOut.getElement(), "id", "zOut");
		panning.add(panUp);
		panning.add(panRight);
		panning.add(panDown);
		panning.add(panLeft);
		panning.add(panLeft);
		panning.add(panHome);
		zoomSlider.add(zIn);
		zoomSlider.add(zSlide);
		zoomSlider.add(zOut);

		final MLink cExpand = new MLink("", "#");
		cExpand.setStyleName("imageBrowser-hyperlink");
		DOM.setElementAttribute(cExpand.getElement(), "id", "cExpand");

		// viewControls.add(cExpand);
		viewControls.add(panning);
		viewControls.add(zoomSlider);
		return viewControls;
	}

	public void onClick(final Widget sender) {
		if (sender == save) {
			doSave();
		} else if (sender == panUp) {
			doPanUp();
		} else if (sender == panLeft) {
			doPanLeft();
		} else if (sender == panRight) {
			doPanRight();
		} else if (sender == panDown) {
			doPanDown();
		} else if (sender == panHome) {
			doPanHome();
		} else if (sender == bringToFront) {
			doBringToFront();
		} else if (sender == sendToBack) {
			doSendToBack();
		} else if (sender == addExistingImage) {
			doAddExistingImage();
		} else if (sender == addNewImageToSubsample) {
			doAddNewImageToSubsample();
		}
	}

	private void doAddNewImageToSubsample() {

	}

	private void doBringToFront() {
		grid.setZMode(1);
	}

	private void doSendToBack() {
		grid.setZMode(2);
	}

	private void doAddExistingImage() {
		final ImageBrowserDetails imageBrowser = this;
		new ServerOp() {
			public void begin() {
				new AddExistingImagePopup(addExistingImage, this, g
						.getSubsample()).show();
			}
			public void onSuccess(final Object result) {
				Iterator itr = ((Collection) result).iterator();
				int[] cascade = {100};
				while (itr.hasNext()) {
					addImage((edu.rpi.metpetdb.client.model.Image) itr.next(),
							cascade);
					cascade[0] += 20;
				}
				MetPetDBApplication.show(imageBrowser);
			}
		}.begin();
	}

	private void doSave() {
		new ServerOp() {
			public void begin() {
				MpDb.imageBrowser_svc.saveGrid(g, this);
				info.setText("");
			}
			public void onSuccess(final Object result) {
				// TODO double check this works (itr might return wrong object)
				info.setText("Grid Saved Successfully");
				final Iterator itr = g.getImagesOnGrid().iterator();
				final Iterator itrNew = ((Grid) result).getImagesOnGrid()
						.iterator();
				while (itr.hasNext()) {
					final ImageOnGrid iog = (ImageOnGrid) itr.next();
					final ImageOnGrid iogNew = (ImageOnGrid) itrNew.next();
					iogNew.setActualImage(iog.getActualImage());
					iogNew.setImageContainer(iog.getImageContainer());
					iogNew.setTemporaryTopLeftX(iog.getTemporaryTopLeftX());
					iogNew.setTemporaryTopLeftY(iog.getTemporaryTopLeftY());
					iogNew.setPanTopLeftX(iog.getPanTopLeftX());
					iogNew.setPanTopLeftY(iog.getPanTopLeftY());
					iogNew.setWidth(iog.getWidth());
					iogNew.setHeight(iog.getHeight());
					iogNew.setIsShown(iog.getIsShown());
					iogNew.setImagePanel(iog.getImagePanel());
					iogNew.setMineralAnalyses(iog.getMineralAnalyses());
					iogNew.setIsMenuHidden(iog.getIsMenuHidden());
					iogNew.setIsLocked(iog.getIsLocked());
					iogNew.setZoomLevelsSkipped(iog.getZoomLevelsSkipped());
					final Iterator itrMa = iog.getMineralAnalyses().iterator();
					final Iterator itrMaNew = iogNew.getMineralAnalyses()
							.iterator();
					while (itrMa.hasNext()) {
						final MineralAnalysis ma = (MineralAnalysis) itrMa
								.next();
						final MineralAnalysis maNew = (MineralAnalysis) itrMaNew
								.next();
						maNew.setActualImage(ma.getActualImage());
						maNew.setPercentX(ma.getPercentX());
						maNew.setPercentY(ma.getPercentY());
						maNew.setIsLocked(ma.getIsLocked());
					}
				}
				g = (Grid) result;
				ImageBrowserDetails.this.mouseListener.setImagesOnGrid(g.getImagesOnGrid());
			}
		}.begin();

	}

	private void doPanUp() {
		pan(0, 201);
	}

	private void doPanLeft() {
		pan(201, 0);
	}

	private void doPanRight() {
		pan(-201, 0);
	}

	private void doPanDown() {
		pan(0, -201);
	}

	private void doPanHome() {
		pan(-this.totalXOffset, -this.totalYOffset);
		this.totalXOffset = 0;
		this.totalYOffset = 0;
	}

	public void pan(final int xoffset, final int yoffset) {
		String currentBackgroundPosition = DOM.getStyleAttribute(grid
				.getElement(), "backgroundPosition");
		this.totalXOffset += xoffset;
		this.totalYOffset += yoffset;
		int x = 0;
		int y = 0;
		if (!currentBackgroundPosition.equals("")) {
			String currentX = currentBackgroundPosition.split(" ")[0]
					.toString();
			String currentY = currentBackgroundPosition.split(" ")[1]
					.toString();

			for (int i = 0; i < currentX.length(); ++i) {
				if (Character.isLetter(currentX.charAt(i))) {
					x = Integer.parseInt(currentX.substring(0, i));
					break;
				}
			}
			for (int i = 0; i < currentY.length(); ++i) {
				if (Character.isLetter(currentY.charAt(i))) {
					y = Integer.parseInt(currentY.substring(0, i));
					break;
				}
			}
		}
		DOM.setStyleAttribute(grid.getElement(), "backgroundPosition",
				(x + xoffset) + "px " + (y + yoffset) + "px");
		if (g.getImagesOnGrid() != null) {
			final Iterator itr = g.getImagesOnGrid().iterator();
			ImageOnGrid iog;
			while (itr.hasNext()) {
				iog = (ImageOnGrid) itr.next();
				int newX = iog.getTemporaryTopLeftX() + xoffset;
				int newY = iog.getTemporaryTopLeftY() + yoffset;
				grid.setWidgetPosition(iog.getImageContainer(), newX, newY);
				iog.setTemporaryTopLeftX(newX);
				iog.setTemporaryTopLeftY(newY);
			}
		}
	}

	public MAbsolutePanel getGrid() {
		return grid;
	}

}
