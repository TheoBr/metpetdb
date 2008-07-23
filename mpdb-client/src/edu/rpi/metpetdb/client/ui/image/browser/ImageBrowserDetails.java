package edu.rpi.metpetdb.client.ui.image.browser;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
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
import edu.rpi.metpetdb.client.model.ChemicalAnalysisDTO;
import edu.rpi.metpetdb.client.model.GridDTO;
import edu.rpi.metpetdb.client.model.ImageDTO;
import edu.rpi.metpetdb.client.model.ImageOnGridDTO;
import edu.rpi.metpetdb.client.model.SubsampleDTO;
import edu.rpi.metpetdb.client.ui.MetPetDBApplication;
import edu.rpi.metpetdb.client.ui.MpDb;
import edu.rpi.metpetdb.client.ui.ServerOp;
import edu.rpi.metpetdb.client.ui.TokenSpace;
import edu.rpi.metpetdb.client.ui.dialogs.ConfirmationDialogBox;
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
import edu.rpi.metpetdb.client.ui.left.side.MySubsamples;
import edu.rpi.metpetdb.client.ui.widgets.ImageHyperlink;
import edu.rpi.metpetdb.client.ui.widgets.MAbsolutePanel;
import edu.rpi.metpetdb.client.ui.widgets.MLink;
import edu.rpi.metpetdb.client.ui.widgets.MLinkandText;
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
	private MLink zIn;
	private MLink zOut;
	private MLink zSlide;

	// Buttons/Labels/Widgets
	private final Button save;
	private final Label info;

	private GridDTO g;
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

	private final Map<ImageDTO, ImageOnGrid> imagesOnGrid;

	public ImageBrowserDetails() {
		this.info = new Label();
		this.scaleDisplay = new Label();
		this.scaleDisplay.setStyleName("mpdb-scale");
		this.scaleDisplay.setText(String.valueOf(this.scale) + " mm");
		this.addExistingImage = new MLink("Add Existing Image", this);
		this.addExistingImage.setStyleName("addlink");
		this.bringToFront = new MLink("Bring To Front", this);
		this.sendToBack = new MLink("Send To Back", this);
		this.save = new Button(LocaleHandler.lc_text.buttonSave(), this);
		this.grid = new MAbsolutePanel();
		this.grid.add(this.scaleDisplay);
		this.imagesOnGrid = new HashMap<ImageDTO, ImageOnGrid>();
	}

	public ImageBrowserDetails createNew(final long subsampleId) {
		new ServerOp() {
			@Override
			public void begin() {
				MpDb.subsample_svc.details(subsampleId, this);
			}

			public void onSuccess(final Object result) {
				final SubsampleDTO s = (SubsampleDTO) result;
				ImageBrowserDetails.this.g = new GridDTO();
				ImageBrowserDetails.this.g.setSubsample(s);
				new ServerOp() {
					@Override
					public void begin() {
						MpDb.chemicalAnalysis_svc.all(subsampleId, this);
					}

					public void onSuccess(final Object result) {
						final List<ChemicalAnalysisDTO> ss = (List<ChemicalAnalysisDTO>) result;
						ImageBrowserDetails.this.g.getSubsample()
								.setChemicalAnalyses(
										new HashSet<ChemicalAnalysisDTO>(ss));
						s.setGrid(ImageBrowserDetails.this.g);
						ImageBrowserDetails.this.buildInterface();
						ImageBrowserDetails.this.doSave();
					}
				}.begin();
			}
		}.begin();

		return this;
	}

	public ImageBrowserDetails showById(final long id) {
		new ServerOp() {
			@Override
			public void begin() {
				MpDb.imageBrowser_svc.details(id, this);
			}

			public void onSuccess(final Object result) {
				ImageBrowserDetails.this.g = (GridDTO) result;
				new ServerOp() {
					@Override
					public void begin() {
						MpDb.chemicalAnalysis_svc.all(((GridDTO) result)
								.getSubsample().getId(), this);
					}

					public void onSuccess(final Object result2) {
						final List<ChemicalAnalysisDTO> s = (List<ChemicalAnalysisDTO>) result2;
						ImageBrowserDetails.this.g.getSubsample()
								.setChemicalAnalyses(
										new HashSet<ChemicalAnalysisDTO>(s));
						ImageBrowserDetails.this.buildInterface();
						ImageBrowserDetails.this.addImagesOnGrid(true);
					}
				}.begin();
			}
		}.begin();
		return this;
	}

	private void buildInterface() {
		final MLinkandText header = new MLinkandText("Subsample ", this.g
				.getSubsample().getName(), "'s Map", TokenSpace
				.detailsOf(this.g.getSubsample()));
		final MLinkandText sampleHeader = new MLinkandText("Sample ", this.g
				.getSubsample().getSample().getName(), "", TokenSpace
				.detailsOf(this.g.getSubsample().getSample()));
		header.addStyleName("h3");
		sampleHeader.addStyleName("h2");
		this.add(sampleHeader);
		this.add(header);
		DOM.setElementAttribute(this.grid.getElement(), "id", "canvas");
		final FlowPanel fp = this.createViewControls();
		this.grid.add(fp);
		final MUnorderedList ul = new MUnorderedList();
		this.addNewImageToSubsample = new MLink("Add New Image to Subsample",
				TokenSpace.edit(this.g.getSubsample()));
		this.addNewImageToSubsample.setStyleName("addlink");
		ul.setStyleName("options");
		ul.add(this.addExistingImage);
		ul.add(this.addNewImageToSubsample);
		ul.add(this.bringToFront);
		ul.add(this.sendToBack);
		this.add(ul);
		this.add(this.grid);
		this.add(this.save);
		this.add(this.info);
		this.layers = new LeftSideLayer(this.g.getSubsample().getName());
		// MetPetDBApplication.appendToLeft(this.layers);

		((MySubsamples) MetPetDBApplication.getFromLeft(0)).insertLayers(
				this.layers, this.g.getSubsample());
		this.mouseListener = new ImageBrowserMouseListener(this.grid,
				this.imagesOnGrid.values(), this.zOrderManager, this.g
						.getSubsample(), this, fp);
		this.grid.addMouseListener(this.mouseListener);
	}

	public void updateScale(final float multiplier) {
		this.scale *= multiplier;

		while (String.valueOf((int) this.scale).length() > 3) {
			++this.unit;
			if (this.unit == ImageBrowserDetails.units.length)
				--this.unit;
			else
				this.scale *= 1000;
		}
		if ((int) this.scale == 0) {
			--this.unit;
			this.scale /= 1000;
		}

		if ((this.unit >= ImageBrowserDetails.units.length) || (this.unit < 0))
			return;
		this.scaleDisplay.setText(String.valueOf(this.scale) + " "
				+ ImageBrowserDetails.units[this.unit]);
	}

	@Override
	public void onLoad() {
		super.onLoad();
	}

	private void addImagesOnGrid(final boolean firstTime) {
		final Iterator<ImageOnGridDTO> itr = this.g.getImagesOnGrid()
				.iterator();
		while (itr.hasNext()) {
			final ImageOnGridDTO iog = itr.next();
			final ImageOnGrid imageOnGrid = new ImageOnGrid();
			imageOnGrid.setIog(iog);
			if (firstTime) {
				imageOnGrid.setWidth(Math
						.round((iog.getImage().getWidth() * iog
								.getResizeRatio())));
				imageOnGrid.setHeight(Math
						.round((iog.getImage().getHeight() * iog
								.getResizeRatio())));
			}
			this.addImage(imageOnGrid);
		}
	}

	private void addImage(final ImageDTO i, final int[] cascade) {
		final ImageOnGridDTO iog = new ImageOnGridDTO();
		iog.setGrid(this.g);
		iog.setImage(i);
		iog.setOpacity(100);
		iog.setResizeRatio(1);
		iog.setZorder(1);
		iog.setTopLeftX(cascade[0]);
		iog.setTopLeftY(cascade[0]);
		iog.setZorder(this.zOrderManager.getHighestZOrder() + 1);
		iog.setGwidth(i.getWidth());
		iog.setGheight(i.getHeight());
		iog.setGchecksum(i.getChecksum());
		iog.setGchecksum64x64(i.getChecksum64x64());
		iog.setGchecksumHalf(i.getChecksumHalf());
		final ImageOnGrid imageOnGrid = new ImageOnGrid();
		imageOnGrid.setIog(iog);
		imageOnGrid.setWidth(Math.round((iog.getImage().getWidth() * (iog
				.getResizeRatio()))));
		imageOnGrid.setHeight(Math.round((iog.getImage().getHeight() * (iog
				.getResizeRatio()))));
		this.addImage(imageOnGrid);
		cascade[0] += 10;
		this.g.addImageOnGrid(iog);
	}

	public void addImage(final ImageOnGrid iog) {
		final DCFlowPanel imageContainer = new DCFlowPanel();
		imageContainer.setStyleName("imageContainer");
		iog.setImageContainer(imageContainer);
		this.scale(iog);

		iog.setTemporaryTopLeftX(iog.getIog().getTopLeftX());
		iog.setTemporaryTopLeftY(iog.getIog().getTopLeftY());

		final SimplePanel imagePanel = this.createImagePanel(iog);

		this.lockListener = new LockListener(iog);
		this.hideMenuListener = new HideMenuListener(iog);

		imageContainer.add(this.makeTopMenu(iog));
		imageContainer.add(imagePanel);
		imageContainer.add(this.makeBottomMenu(iog));

		final PopupMenu popupMenu = this.createPopupMenu(iog);
		imageContainer.setPopupMenu(popupMenu);

		DOM.setStyleAttribute(imageContainer.getElement(), "zIndex", String
				.valueOf(iog.getIog().getZorder()));
		this.setOpacity(iog.getActualImage().getElement(), iog.getIog()
				.getOpacity());
		this.setOpacity(imageContainer.getElement(), iog.getIog().getOpacity());

		this.layers.registerImage(iog);
		this.zOrderManager.register(iog);

		this.grid.add(imageContainer, iog.getTemporaryTopLeftX(), iog
				.getTemporaryTopLeftY());
		this.imagesOnGrid.put(iog.getIog().getImage(), iog);
	}

	private PopupMenu createPopupMenu(final ImageOnGrid iog) {
		final PopupMenu popupMenu = new PopupMenu();
		popupMenu.addItem(new ImageHyperlink(new Image(GWT.getModuleBaseURL()
				+ "/images/icon-details.gif"), "Details", TokenSpace
				.detailsOf(this.g.getSubsample()), false));
		popupMenu.addItem(new ImageHyperlink(new Image(GWT.getModuleBaseURL()
				+ "/images/icon-addpoint.gif"), "Add Point",
				new AddPointListener(this.mouseListener, iog), false));
		popupMenu.addItem(new ImageHyperlink(new Image(GWT.getModuleBaseURL()
				+ "/images/icon-remove.gif"), "Remove", new RemoveListener(iog,
				this.layers, imagesOnGrid), false));
		popupMenu.addItem(new ImageHyperlink(new Image(GWT.getModuleBaseURL()
				+ "/images/icon-rotate.gif"), "Rotate",
				new RotateListener(iog), false));
		popupMenu.addItem(new ImageHyperlink(new Image(GWT.getModuleBaseURL()
				+ "/images/icon-opacity.gif"), "Opacity", new OpacityListener(
				iog, this.grid), false));
		final MLink lock = new MLink("Lock", this.lockListener);
		this.lockListener.addNotifier(lock);
		popupMenu.addItem(lock);
		final MLink hideMenu = new MLink("Hide Menu", this.hideMenuListener);
		this.hideMenuListener.addNotifier(hideMenu);
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
		final HashSet<ChemicalAnalysisDTO> chemicalAnalyses = this
				.getChemicalAnalyses(iog.getIog().getImage(), this.g
						.getSubsample().getChemicalAnalyses());
		iog.setChemicalAnalyses(chemicalAnalyses);

		image.setWidth(iog.getWidth() + "px");
		image.setHeight(iog.getHeight() + "px");
		final ResizableWidget rw = new ResizableWidget(ap, this.grid, iog
				.getImageContainer(), iog.getIog());
		imagePanel.setWidget(rw);
		iog.setActualImage(image);
		iog.setImagePanel(ap);
		this.addPoints(iog);
		return imagePanel;
	}

	public void addToTotalXOffset(final int amount) {
		this.totalXOffset += amount;
	}

	public void addToTotalYOffset(final int amount) {
		this.totalYOffset += amount;
	}

	private void addPoints(final ImageOnGrid iog) {
		final Iterator<ChemicalAnalysisDTO> itr = iog.getChemicalAnalyses()
				.iterator();
		while (itr.hasNext()) {
			final ChemicalAnalysisDTO ma = itr.next();
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

	private HashSet<ChemicalAnalysisDTO> getChemicalAnalyses(
			final edu.rpi.metpetdb.client.model.ImageDTO i,
			final Set<ChemicalAnalysisDTO> chemicalAnalyses) {
		final Iterator<ChemicalAnalysisDTO> itr = chemicalAnalyses.iterator();
		final HashSet<ChemicalAnalysisDTO> mas = new HashSet<ChemicalAnalysisDTO>();
		while (itr.hasNext()) {
			final ChemicalAnalysisDTO ma = itr.next();
			if (ma.getImage() != null)
				if (ma.getImage().equals(i))
					mas.add(ma);
		}
		return mas;
	}

	private void scale(final ImageOnGrid iog) {
		final int multiplier = 1;
		iog.setWidth(Math.round((iog.getIog().getImage().getWidth()
				* multiplier * iog.getIog().getResizeRatio())));
		iog.setHeight(Math.round((iog.getIog().getImage().getHeight()
				* multiplier * iog.getIog().getResizeRatio())));
	}

	private MUnorderedList makeBottomMenu(final ImageOnGrid iog) {
		final MUnorderedList ulBottom = new MUnorderedList();
		final MLink details = new ImageHyperlink(new Image(GWT
				.getModuleBaseURL()
				+ "/images/icon-details.gif"), "Details", TokenSpace
				.detailsOf(this.g.getSubsample()), false);
		final MLink addPoint = new ImageHyperlink(new Image(GWT
				.getModuleBaseURL()
				+ "/images/icon-addpoint.gif"), "Add Point",
				new AddPointListener(this.mouseListener, iog), false);
		final MLink remove = new ImageHyperlink(new Image(GWT
				.getModuleBaseURL()
				+ "/images/icon-remove.gif"), "Remove", new RemoveListener(iog,
				this.layers, imagesOnGrid), false);
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
				iog, this.grid), false);
		final MLink hideMenu = new MLink("Hide Menu", this.hideMenuListener);
		this.hideMenuListener.addNotifier(hideMenu);
		final MLink lock = new MLink("Lock", this.lockListener);
		this.lockListener.addNotifier(lock);
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
		zSlide = new MLink("", this);
		DOM.setStyleAttribute(zSlide.getElement(), "top", "60px");
		zIn = new MLink("", new ZoomInListener(this.imagesOnGrid.values(),
				zSlide.getElement(), this));
		zOut = new MLink("", new ZoomOutListener(this.imagesOnGrid.values(),
				zSlide.getElement(), this));
		this.panUp.setStyleName("imageBrowser-hyperlink");
		this.panRight.setStyleName("imageBrowser-hyperlink");
		this.panDown.setStyleName("imageBrowser-hyperlink");
		this.panLeft.setStyleName("imageBrowser-hyperlink");
		this.panHome.setStyleName("imageBrowser-hyperlink");
		zIn.setStyleName("imageBrowser-hyperlink");
		zSlide.setStyleName("imageBrowser-hyperlink");
		zOut.setStyleName("imageBrowser-hyperlink");
		DOM.setElementAttribute(this.panUp.getElement(), "id", "panUp");
		DOM.setElementAttribute(this.panRight.getElement(), "id", "panRight");
		DOM.setElementAttribute(this.panDown.getElement(), "id", "panDown");
		DOM.setElementAttribute(this.panLeft.getElement(), "id", "panLeft");
		DOM.setElementAttribute(this.panHome.getElement(), "id", "panHome");
		DOM.setElementAttribute(zIn.getElement(), "id", "zIn");
		DOM.setElementAttribute(zSlide.getElement(), "id", "zSlide");
		DOM.setElementAttribute(zOut.getElement(), "id", "zOut");
		panning.add(this.panUp);
		panning.add(this.panRight);
		panning.add(this.panDown);
		panning.add(this.panLeft);
		panning.add(this.panLeft);
		panning.add(this.panHome);
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
		if (sender == this.save) {
			Iterator<ChemicalAnalysisDTO> itr = ImageBrowserDetails.this.g
					.getSubsample().getChemicalAnalyses().iterator();
			while (itr.hasNext()) {
				this.doSaveChemicalAnalysis(itr.next());
			}
			this.doSave();
		} else if (sender == this.panUp)
			this.doPanUp();
		else if (sender == this.panLeft)
			this.doPanLeft();
		else if (sender == this.panRight)
			this.doPanRight();
		else if (sender == this.panDown)
			this.doPanDown();
		else if (sender == this.panHome)
			this.doPanHome();
		else if (sender == this.bringToFront)
			this.doBringToFront();
		else if (sender == this.sendToBack)
			this.doSendToBack();
		else if (sender == this.addExistingImage)
			this.doAddExistingImage();
		else if (sender == this.addNewImageToSubsample)
			this.doAddNewImageToSubsample();
	}

	private void doAddNewImageToSubsample() {

	}

	private void doBringToFront() {
		this.grid.setZMode(1);
	}

	private void doSendToBack() {
		this.grid.setZMode(2);
	}

	private void doAddExistingImage() {
		final ImageBrowserDetails imageBrowser = this;
		new ServerOp() {
			@Override
			public void begin() {
				new AddExistingImagePopup(
						ImageBrowserDetails.this.addExistingImage, this,
						ImageBrowserDetails.this.g.getSubsample(),
						ImageBrowserDetails.this.imagesOnGrid).show();
			}

			public void onSuccess(final Object result) {
				final Iterator itr = ((Collection) result).iterator();
				final int[] cascade = {
					100
				};
				while (itr.hasNext()) {
					ImageBrowserDetails.this
							.addImage(
									(edu.rpi.metpetdb.client.model.ImageDTO) itr
											.next(), cascade);
					cascade[0] += 20;
				}
				MetPetDBApplication.show(imageBrowser);
			}
		}.begin();
	}

	private void doSave() {
		new ServerOp() {
			@Override
			public void begin() {
				MpDb.imageBrowser_svc
						.saveGrid(ImageBrowserDetails.this.g, this);
				ImageBrowserDetails.this.info.setText("");
			}

			public void onSuccess(final Object result) {
				new ConfirmationDialogBox(LocaleHandler.lc_text
						.notice_GridSaved(ImageBrowserDetails.this.g
								.getSubsample().getName()), false);
			}
		}.begin();

	}

	private void doSaveChemicalAnalysis(final ChemicalAnalysisDTO ma) {
		new ServerOp() {
			public void begin() {
				MpDb.chemicalAnalysis_svc.save(ma, this);
			}
			public void onSuccess(final Object result) {

			}
		}.begin();
	}

	private void doPanUp() {
		this.pan(0, 201);
	}

	private void doPanLeft() {
		this.pan(201, 0);
	}

	private void doPanRight() {
		this.pan(-201, 0);
	}

	private void doPanDown() {
		this.pan(0, -201);
	}

	private void doPanHome() {
		this.pan(-this.totalXOffset, -this.totalYOffset);
		this.totalXOffset = 0;
		this.totalYOffset = 0;
	}

	public void pan(final int xoffset, final int yoffset) {
		final String currentBackgroundPosition = DOM.getStyleAttribute(
				this.grid.getElement(), "backgroundPosition");
		this.totalXOffset += xoffset;
		this.totalYOffset += yoffset;
		int x = 0;
		int y = 0;
		if (!currentBackgroundPosition.equals("")) {
			final String currentX = currentBackgroundPosition.split(" ")[0]
					.toString();
			final String currentY = currentBackgroundPosition.split(" ")[1]
					.toString();

			for (int i = 0; i < currentX.length(); ++i)
				if (Character.isLetter(currentX.charAt(i))) {
					x = Integer.parseInt(currentX.substring(0, i));
					break;
				}
			for (int i = 0; i < currentY.length(); ++i)
				if (Character.isLetter(currentY.charAt(i))) {
					y = Integer.parseInt(currentY.substring(0, i));
					break;
				}
		}
		DOM.setStyleAttribute(this.grid.getElement(), "backgroundPosition",
				(x + xoffset) + "px " + (y + yoffset) + "px");
		if (this.g.getImagesOnGrid() != null) {
			final Iterator<ImageOnGrid> itr = this.imagesOnGrid.values()
					.iterator();
			while (itr.hasNext()) {
				final ImageOnGrid iog = itr.next();
				final int newX = iog.getTemporaryTopLeftX() + xoffset;
				final int newY = iog.getTemporaryTopLeftY() + yoffset;
				this.grid
						.setWidgetPosition(iog.getImageContainer(), newX, newY);
				iog.setTemporaryTopLeftX(newX);
				iog.setTemporaryTopLeftY(newY);
			}
		}
	}

	public MAbsolutePanel getGrid() {
		return this.grid;
	}

}
