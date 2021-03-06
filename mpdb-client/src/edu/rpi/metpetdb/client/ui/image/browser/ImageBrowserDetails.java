package edu.rpi.metpetdb.client.ui.image.browser;

import java.util.ArrayList;
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
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.locale.LocaleHandler;
import edu.rpi.metpetdb.client.model.ChemicalAnalysis;
import edu.rpi.metpetdb.client.model.Grid;
import edu.rpi.metpetdb.client.model.Image;
import edu.rpi.metpetdb.client.model.ImageOnGrid;
import edu.rpi.metpetdb.client.model.Subsample;
import edu.rpi.metpetdb.client.ui.MetPetDBApplication;
import edu.rpi.metpetdb.client.ui.MpDb;
import edu.rpi.metpetdb.client.ui.PageChangeListener;
import edu.rpi.metpetdb.client.ui.TokenSpace;
import edu.rpi.metpetdb.client.ui.commands.ServerOp;
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
import edu.rpi.metpetdb.client.ui.widgets.ImageHyperlink;
import edu.rpi.metpetdb.client.ui.widgets.MHtmlList;
import edu.rpi.metpetdb.client.ui.widgets.MLink;
import edu.rpi.metpetdb.client.ui.widgets.MLinkandText;
import edu.rpi.metpetdb.client.ui.widgets.panels.MAbsolutePanel;
import edu.rpi.metpetdb.client.ui.widgets.panels.MPagePanel;
import edu.rpi.metpetdb.client.ui.widgets.panels.MAbsolutePanel.ZMode;

public class ImageBrowserDetails extends MPagePanel implements ClickListener, PageChangeListener {

	// Links
	private final MLink addExistingImage;
	private MLink addNewImageToSubsample;
	private final MLink bringToFront;
	private final MLink senBack;
	private final MLink groupTogether;
	private final MLink unGroup;
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

	private Grid g;
	private final MAbsolutePanel grid;
	public LayersSidebar layers;

	// Listeners
	private ImageBrowserMouseListener mouseListener;
	private LockListener lockListener;
	private HideMenuListener hideMenuListener;
	private final ZOrderManager zOrderManager = new ZOrderManager();
	private ZoomInListener zoomInListener;
	private ZoomOutListener zoomOutListener;
	private ZoomHandler zoomer;

	private final PanHandler panHandler;
	private FlowPanel boundary = new FlowPanel();

	// Scale
	public int totalXOffset = 100;
	public int totalYOffset = 10;
	private final Label scaleDisplayBottom;
	private final Label scaleDisplayTop;
	public float scale = 1f; /* in milli meters */
	private int unit = 1;
	private static String[] units = {
		"m", /* meter 10x10^0 */
		"mm", /* millimeter 10x10^-3 */
		"um", /* micrometer 10x10^-6 */
		"nm", /* nanometer 10x10^-9 */
	};

	/** which chemical analyses that need to be saved */
	private final Collection<ChemicalAnalysis> chemicalAnalyses;

	/** keeps track of selected and grouped images **/
	public final SelectionHandler selectionHandler;

	private final Map<Image, ImageOnGridContainer> imagesOnGrid;
	private Boolean leavingPage = false;

	public ImageBrowserDetails() {
		selectionHandler = new SelectionHandler(this);
		this.info = new Label();
		this.scaleDisplayTop = new Label();
		this.scaleDisplayTop.setStyleName("mpdb-scale-top");
		this.scaleDisplayTop.setText(String.valueOf(this.scale) + " mm");
		this.scaleDisplayBottom = new Label();
		this.scaleDisplayBottom.setStyleName("mpdb-scale-bottom");
		this.scaleDisplayBottom.setText(String.valueOf(this.scale) + " mm");
		this.addExistingImage = new MLink("Add Existing Image", this);
		this.addExistingImage.setStyleName("addlink");
		this.bringToFront = new MLink("Bring To Front", this);
		this.senBack = new MLink("Send To Back", this);
		this.groupTogether = new MLink("Group", this);
		this.unGroup = new MLink("Ungroup", this);
		this.setGroupEnabled(false);
		this.setUngroupEnabled(false);
		this.save = new Button(LocaleHandler.lc_text.buttonSave(), this);
		this.grid = new MAbsolutePanel();
		this.grid.add(this.scaleDisplayTop);
		this.grid.add(this.scaleDisplayBottom);
		updateScale(1);
		this.imagesOnGrid = new HashMap<Image, ImageOnGridContainer>();
		panHandler = new PanHandler(grid,this);
		chemicalAnalyses = new ArrayList<ChemicalAnalysis>();
		sinkEvents(Event.ONMOUSEDOWN | Event.ONMOUSEMOVE| Event.ONMOUSEUP);
		MetPetDBApplication.registerPageWatcher(this);
	}

	public ImageBrowserDetails createNew(final long subsampleId) {
		new ServerOp<Subsample>() {
			@Override
			public void begin() {
				MpDb.subsample_svc.detailsWithAnalyses(subsampleId, this);
			}

			public void onSuccess(final Subsample s) {
				ImageBrowserDetails.this.g = new Grid();
				ImageBrowserDetails.this.g.setSubsample(s);
				ImageBrowserDetails.this.buildInterface();
			}
		}.begin();

		return this;
	}

	public ImageBrowserDetails showById(final long id) {
		new ServerOp<Grid>() {
			@Override
			public void begin() {
				MpDb.imageBrowser_svc.details(id, this);
			}

			public void onSuccess(final Grid result) {
				ImageBrowserDetails.this.g = result;
				ImageBrowserDetails.this.buildInterface();
				ImageBrowserDetails.this.addImagesOnGrid(true);
			}
		}.begin();
		return this;
	}

	public Collection<ChemicalAnalysis> getChemicalAnalysesToSave() {
		return chemicalAnalyses;
	}
	private void buildInterface() {
		final MLinkandText header = new MLinkandText("Subsample ", this.g
				.getSubsample().getName(), "'s Map", TokenSpace
				.detailsOf(this.g.getSubsample()));
		final MLinkandText sampleHeader = new MLinkandText("Sample ", this.g
				.getSubsample().getSampleName(), "", TokenSpace
				.detailsOfSample(this.g.getSubsample().getSampleId()));
		header.addStyleName("h3");
		sampleHeader.addStyleName("h2");
		this.add(sampleHeader);
		this.add(header);
		DOM.setElementAttribute(this.grid.getElement(), "id", "canvas");
		DOM.setStyleAttribute(grid.getElement(), "backgroundPosition",
				(totalXOffset) + "px "
				+ (totalYOffset) + "px");
		final FlowPanel fp = this.createViewControls();
		this.grid.add(fp);
		this.grid.add(this.createWorkspaceBoundary());
		final MHtmlList ul = new MHtmlList();
		this.addNewImageToSubsample = new MLink("Add New Image to Subsample",
				TokenSpace.edit(this.g.getSubsample()));
		this.addNewImageToSubsample.setStyleName("addlink");
		ul.setStyleName("options");
		ul.add(this.addExistingImage);
		ul.add(this.addNewImageToSubsample);
		ul.add(this.bringToFront);
		ul.add(this.senBack);
		ul.add(this.groupTogether);
		ul.add(this.unGroup);
		this.add(ul);
		this.add(this.grid);
		this.add(this.save);
		this.add(this.info);
		this.layers = new LayersSidebar(this.g.getSubsample().getName(), this);
		setSidebar(this.layers);
		this.mouseListener = new ImageBrowserMouseListener(this.grid,
				this.imagesOnGrid.values(), this.zOrderManager, this.g
				.getSubsample(), this, fp);
		panHandler.setImagesOnGrid(this.imagesOnGrid.values());
		this.grid.addMouseListener(this.mouseListener);

	}

	public void updateScale(final double multiplier) {
		this.scale *= multiplier;

		double scaleString = 4*scale;

		if (4*scale >= 1) {
			scaleString = Math.round(4*scale);
		} else {
			scaleString = (double)Math.round(4*scale*10)/10;
		}

		this.scaleDisplayTop.setText(String.valueOf(scaleString) + " "
				+ ImageBrowserDetails.units[this.unit]);
		this.scaleDisplayBottom.setText(String.valueOf(scaleString) + " "
				+ ImageBrowserDetails.units[this.unit]);
		int pixels = ImageBrowserUtil.MMToPixels(scaleString, scale, 1);
		DOM.setStyleAttribute(scaleDisplayTop.getElement(), "width", pixels+"px");
		DOM.setStyleAttribute(scaleDisplayBottom.getElement(), "width", pixels+"px");
	}

	@Override
	public void onLoad() {
		super.onLoad();
	}

	@Override
	public void onBrowserEvent(Event event) {
		switch (DOM.eventGetType(event)) {
			case Event.ONMOUSEDOWN:
				if (DOM.eventGetCtrlKey(event) || DOM.eventGetMetaKey(event)) {
					mouseListener.setClickMode(ImageBrowserMouseListener.ClickMode.CTRL);
				} else if (DOM.eventGetShiftKey(event)) {
					mouseListener.setClickMode(ImageBrowserMouseListener.ClickMode.SHIFT);
				} else 
					mouseListener.setClickMode(ImageBrowserMouseListener.ClickMode.NORMAL);
				break;
			case Event.ONMOUSEMOVE:
				// before we prevent default make sure the action is within
				// our widget
				if (DOM.eventGetShiftKey(event)) {
					mouseListener.setClickMode(ImageBrowserMouseListener.ClickMode.SHIFT);
				} else {
					mouseListener.setClickMode(ImageBrowserMouseListener.ClickMode.NORMAL);
				}
				final int eventX = DOM.eventGetClientX(event);
				final int eventY = DOM.eventGetClientY(event);
				final int x = grid.getAbsoluteLeft();
				final int y = grid.getAbsoluteTop();
				final int width = grid.getOffsetWidth();
				final int height = grid.getOffsetHeight();
				if (eventX > x && eventX < x + width) {
					if (eventY > y && eventY < y + height) {
						DOM.eventPreventDefault(event);
					}
				}
				break;
			case Event.ONMOUSEUP:
				if (DOM.eventGetShiftKey(event)) {
					mouseListener.setClickMode(ImageBrowserMouseListener.ClickMode.NORMAL);
				} 
				break;
		}
	}

	private void addImagesOnGrid(final boolean firstTime) {
		final Iterator<ImageOnGrid> itr = this.g.getImagesOnGrid().iterator();
		while (itr.hasNext()) {
			final ImageOnGrid iog = itr.next();
			final ImageOnGridContainer imageOnGrid = new ImageOnGridContainer(
					iog, this.scale);
			if (firstTime) {
				for (ChemicalAnalysis ca : g.getSubsample().getChemicalAnalyses()) {
					if (ca.getImage() != null && ca.getImage().getId() == iog.getImage().getId())
						imageOnGrid.getChemicalAnalyses().add(ca);
				}
			}
			imageOnGrid.pan(totalXOffset, totalYOffset);
			this.addImage(imageOnGrid);
		}
	}

	private void addImage(final Image i, final int[] cascade) {
		final ImageOnGrid iog = new ImageOnGrid();
		iog.setGrid(this.g);
		iog.setImage(i);
		iog.setOpacity(100);
		iog.setActualCurrentResizeRatio(ImageBrowserUtil.calculateCurrentResizeRatio(i.getScale(), i.getWidth(), scale));
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
		final ImageOnGridContainer imageOnGrid = new ImageOnGridContainer(iog,scale);
		imageOnGrid.setCurrentWidth((int)Math
				.round((iog.getGwidth() * (iog.getActualCurrentResizeRatio()))));
		imageOnGrid.setCurrentHeight((int)Math
				.round((iog.getGheight() * (iog.getActualCurrentResizeRatio()))));
		imageOnGrid.pan(totalXOffset, totalYOffset);
		this.addImage(imageOnGrid);
		this.g.addImageOnGrid(iog);
	}

	public void addImage(final ImageOnGridContainer iog) {
		final DCFlowPanel imageContainer = new DCFlowPanel();
		imageContainer.setStyleName("imageContainer");
		iog.setImageContainer(imageContainer);
		this.scale(iog);

		final SimplePanel imagePanel = this.createImagePanel(iog);

		this.lockListener = new LockListener(iog, this);
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

		this.grid.add(imageContainer, (int)iog.getCurrentContainerPosition().x, (int)iog
				.getCurrentContainerPosition().y);

		this.imagesOnGrid.put(iog.getIog().getImage(), iog);
		selectionHandler.addNewImage(iog);
	}

	private PopupMenu createPopupMenu(final ImageOnGridContainer iog) {
		final PopupMenu popupMenu = new PopupMenu();
		popupMenu.addItem(new ImageHyperlink(
				new com.google.gwt.user.client.ui.Image("images/icon-details.gif"), "Details", TokenSpace
						.detailsOf(this.g.getSubsample()), false));
		popupMenu.addItem(new ImageHyperlink(
				new com.google.gwt.user.client.ui.Image("images/icon-addpoint.gif"), "Add Point",
						new AddPointListener(this.mouseListener, iog), false));
		popupMenu.addItem(new ImageHyperlink(
				new com.google.gwt.user.client.ui.Image("images/icon-remove.gif"), "Remove",
						new RemoveListener(iog, this.layers, imagesOnGrid,selectionHandler), false));
		popupMenu.addItem(new ImageHyperlink(
				new com.google.gwt.user.client.ui.Image("images/icon-rotate.gif"), "Rotate",
						new RotateListener(iog,this), false));
		popupMenu.addItem(new ImageHyperlink(
				new com.google.gwt.user.client.ui.Image("images/icon-opacity.gif"), "Opacity",
						new OpacityListener(iog, this.grid, this), false));
		final MLink lock = new MLink("Lock", this.lockListener);
		this.lockListener.addNotifier(lock);
		popupMenu.addItem(lock);
		final MLink hideMenu = new MLink("Hide Menu", this.hideMenuListener);
		this.hideMenuListener.addNotifier(hideMenu);
		popupMenu.addItem(hideMenu);
		return popupMenu;
	}

	private SimplePanel createImagePanel(final ImageOnGridContainer iog) {
		final SimplePanel imagePanel = new SimplePanel();
		final AbsolutePanel ap = new AbsolutePanel();
		ap.setHeight(iog.getCurrentHeight() + "px");
		ap.setWidth(iog.getCurrentWidth() + "px");
		final com.google.gwt.user.client.ui.Image image = new com.google.gwt.user.client.ui.Image(
				iog.getGoodLookingPicture());
		ap.add(image, 0, 0);
		final HashSet<ChemicalAnalysis> chemicalAnalyses = this
		.getChemicalAnalyses(iog.getIog().getImage(), this.g
				.getSubsample().getChemicalAnalyses());
		iog.setChemicalAnalyses(chemicalAnalyses);

		image.setWidth(iog.getCurrentWidth() + "px");
		image.setHeight(iog.getCurrentHeight() + "px");
		final ResizableWidget rw = new ResizableWidget(ap, this.grid, iog
				.getImageContainer(), iog.getIog());
		imagePanel.setWidget(rw);
		iog.setActualImage(image);
		iog.setImagePanel(ap);
		this.addPoints(iog);
		return imagePanel;
	}

	public void adTotalXOffset(final int amount) {
		this.totalXOffset += amount;
	}

	public void adTotalYOffset(final int amount) {
		this.totalYOffset += amount;
	}

	public void updatePoints(ImageOnGridContainer iog) {
		final Iterator<ChemicalAnalysis> itr2 = iog.getChemicalAnalyses().iterator();
		while (itr2.hasNext()) {
			final ChemicalAnalysis ma = itr2.next();
			final com.google.gwt.user.client.ui.Image i = (com.google.gwt.user.client.ui.Image) ma.getActualImage();		
			// convert MM to pixels
			int x = ImageBrowserUtil.MMToPixelsChemWidth(ma.getReferenceX(), scale, iog.getIog().getResizeRatio());
			int y = ImageBrowserUtil.MMToPixelsChemHeight(ma.getReferenceY(), scale, iog.getIog().getResizeRatio());
			// calculate original origin
			int originX = (int)Math.round((iog.getImageOnGrid().getImage().getWidth() * (iog.getImageOnGrid().getActualCurrentResizeRatio())))/2;
			int originY = (int)Math.round((iog.getImageOnGrid().getImage().getHeight() * (iog.getImageOnGrid().getActualCurrentResizeRatio())))/2;
			// translate point relative to original origin
			x -= originX;
			y = (int)Math.round((iog.getImageOnGrid().getImage().getHeight() * (iog.getImageOnGrid().getActualCurrentResizeRatio()))) - y;
			y -= originY; 
			// rotate around original origin
			double theta = -1*Math.toRadians(iog.getIog().getAngle());
			int newx = (int)Math.round(((Math.cos(theta) * x) - (Math.sin(theta)*y)));
			int newy = (int)Math.round(((Math.sin(theta) * x) + (Math.cos(theta)*y)));
			x = newx;
			y = newy;
			// calculate new origin
			int newOriginX = (int)Math.round((iog.getIog().getGwidth() * (iog.getImageOnGrid().getActualCurrentResizeRatio())))/2;
			int newOriginY = (int)Math.round((iog.getIog().getGheight() * (iog.getImageOnGrid().getActualCurrentResizeRatio())))/2;
			// translate rotated point back from the new origin
			x += newOriginX;
			y = newOriginY - y;
			this.grid.add(i,(int)iog.getCurrentContainerPosition().x + x, (int)iog.getCurrentContainerPosition().y + y);
		}
	}

	public void addPoints(final ImageOnGridContainer iog) {
		final Iterator<ChemicalAnalysis> itr = iog.getChemicalAnalyses()
		.iterator();
		while (itr.hasNext()) {
			final ChemicalAnalysis ma = itr.next();
			final com.google.gwt.user.client.ui.Image i = new com.google.gwt.user.client.ui.Image("images/point0.gif");

			// convert MM to pixels
			int x = ImageBrowserUtil.MMToPixelsChemWidth(ma.getReferenceX(), scale, iog.getIog().getResizeRatio());
			int y = ImageBrowserUtil.MMToPixelsChemHeight(ma.getReferenceY(), scale, iog.getIog().getResizeRatio());
			// calculate original origin
			int originX = (int)Math.round((iog.getImageOnGrid().getImage().getWidth() * (iog.getImageOnGrid().getActualCurrentResizeRatio())))/2;
			int originY = (int)Math.round((iog.getImageOnGrid().getImage().getHeight() * (iog.getImageOnGrid().getActualCurrentResizeRatio())))/2;
			// translate point relative to original origin
			x -= originX;
			y = (int)Math.round((iog.getImageOnGrid().getImage().getHeight() * (iog.getImageOnGrid().getActualCurrentResizeRatio()))) - y;
			y -= originY; 
			// rotate around original origin
			double theta = -1*Math.toRadians(iog.getIog().getAngle());
			int newx = (int)Math.round(((Math.cos(theta) * x) - (Math.sin(theta)*y)));
			int newy = (int)Math.round(((Math.sin(theta) * x) + (Math.cos(theta)*y)));
			x = newx;
			y = newy;
			// calculate new origin
			int newOriginX = (int)Math.round((iog.getIog().getGwidth() * (iog.getImageOnGrid().getActualCurrentResizeRatio())))/2;
			int newOriginY = (int)Math.round((iog.getIog().getGheight() * (iog.getImageOnGrid().getActualCurrentResizeRatio())))/2;
			// translate rotated point back from the new origin
			x += newOriginX;
			y = newOriginY - y;
			i.setStyleName("chem-point");
			this.grid.add(i,(int)iog.getCurrentContainerPosition().x + x, (int)iog.getCurrentContainerPosition().y + y);
			iog.getChemicalAnalysisImages().add(i);

			ma.setActualImage(i);
			ma.setPercentX((float) (ImageBrowserUtil.MMToPixels(ma.getReferenceX(), scale, 1)/ iog.getCurrentWidth()));
			ma.setPercentY((float) (ImageBrowserUtil.MMToPixels(ma.getReferenceY(), scale, 1)/ iog.getCurrentHeight()));
			ma.setLocked(true);
			i.addClickListener(new ClickListener() {
				public void onClick(final Widget sender) {
					new PointPopup(ma, iog, i.getAbsoluteLeft(), i
							.getAbsoluteTop(), ImageBrowserDetails.this).show();
				}
			});
		}
	}

	private HashSet<ChemicalAnalysis> getChemicalAnalyses(
			final edu.rpi.metpetdb.client.model.Image i,
			final Set<ChemicalAnalysis> chemicalAnalyses) {
		final Iterator<ChemicalAnalysis> itr = chemicalAnalyses.iterator();
		final HashSet<ChemicalAnalysis> mas = new HashSet<ChemicalAnalysis>();
		while (itr.hasNext()) {
			final ChemicalAnalysis ma = itr.next();
			if (ma.getImage() != null)
				if (ma.getImage().equals(i))
					mas.add(ma);
		}
		return mas;
	}

	private void scale(final ImageOnGridContainer iog) {
		Image i = iog.getIog().getImage();
		iog.getImageOnGrid().setActualCurrentResizeRatio((float)iog.getIog().getResizeRatio()*(((float)i.getScale()/(float)i.getWidth())/this.scale)*ImageBrowserUtil.pps);

		iog.setCurrentWidth((int)Math.round((iog.getIog().getGwidth()
				* iog.getIog().getActualCurrentResizeRatio())));
		iog.setCurrentHeight((int)Math.round((iog.getIog().getGheight()
				* iog.getIog().getActualCurrentResizeRatio())));
	}

	private MHtmlList makeBottomMenu(final ImageOnGridContainer iog) {
		final MHtmlList ulBottom = new MHtmlList();
		final MLink details = new ImageHyperlink(
				new com.google.gwt.user.client.ui.Image("images/icon-details.gif"), "Details", TokenSpace
						.detailsOf(this.g.getSubsample()), false);
		final MLink addPoint = new ImageHyperlink(
				new com.google.gwt.user.client.ui.Image("images/icon-addpoint.gif"), "Add Point",
						new AddPointListener(this.mouseListener, iog), false);
		final MLink remove = new ImageHyperlink(
				new com.google.gwt.user.client.ui.Image("images/icon-remove.gif"), "Remove",
						new RemoveListener(iog, this.layers, imagesOnGrid,selectionHandler), false);
		ulBottom.add(details);
		ulBottom.add(addPoint);
		ulBottom.add(remove);
		ulBottom.setLiStyle("last", remove);
		ulBottom.setStyleName("imageMenu-bottom");
		return ulBottom;
	}

	private MHtmlList makeTopMenu(final ImageOnGridContainer iog) {
		// Top Menu
		final MHtmlList ulTop = new MHtmlList();
		final MLink rotate = new ImageHyperlink(
				new com.google.gwt.user.client.ui.Image("images/icon-rotate.gif"), "Rotate",
						new RotateListener(iog, this), false);
		final MLink opacity = new ImageHyperlink(
				new com.google.gwt.user.client.ui.Image("images/icon-opacity.gif"), "Opacity",
						new OpacityListener(iog, this.grid, this), false);
		final MLink hideMenu = new MLink("Hide Menu", this.hideMenuListener);
		this.hideMenuListener.addNotifier(hideMenu);
		final MLink lock = new MLink("Lock", this.lockListener);
		if (iog.getImageOnGrid().isLocked()) {
			lock.setText("Unlock");
		}
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

	private FlowPanel createWorkspaceBoundary() {
		DOM.setElementAttribute(boundary.getElement(), "id", "workspaceBoundary");
		updateBoundary();
		return boundary;		
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
		zoomer = new ZoomHandler(imagesOnGrid.values(), zSlide.getElement(), this);
		zoomInListener = new ZoomInListener(zoomer);
		zoomOutListener = new ZoomOutListener(zoomer);
		zIn = new MLink("", zoomInListener);
		zOut = new MLink("", zoomOutListener);
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
		else if (sender == this.senBack)
			this.doSendToBack();
		else if (sender == this.addExistingImage)
			this.doAddExistingImage();
		else if (sender == this.addNewImageToSubsample)
			this.doAddNewImageToSubsample();
		else if (sender == this.groupTogether)
			selectionHandler.groupSelectedImages();
		else if (sender == this.unGroup)
			selectionHandler.ungroupSelectedImages();
	}

	private void doAddNewImageToSubsample() {

	}

	private void doBringToFront() {
		this.zOrderManager.bringToFront(selectionHandler.getSelectedImages());
	}

	private void doSendToBack() {
		this.zOrderManager.sendToBack(selectionHandler.getSelectedImages());
	}

	private void doAddExistingImage() {
		final ImageBrowserDetails imageBrowser = this;
		new ServerOp<Collection<Image>>() {
			@Override
			public void begin() {
				new AddExistingImagePopup(
						ImageBrowserDetails.this.addExistingImage, this,
						ImageBrowserDetails.this.g.getSubsample(),
						ImageBrowserDetails.this.imagesOnGrid).show();
			}

			public void onSuccess(final Collection<Image> result) {
				final Iterator<Image> itr = result.iterator();
				final int[] cascade = {
						0
				};
				while (itr.hasNext()) {
					ImageBrowserDetails.this.addImage(
							(edu.rpi.metpetdb.client.model.Image) itr.next(),
							cascade);
					cascade[0] += 1;
				}
				MetPetDBApplication.show(imageBrowser);
			}
		}.begin();
	}

	private void doSave() {
		// Save the grid
		// update chemical analyses that have been removed
		for (ImageOnGridContainer iog : this.imagesOnGrid.values()){
			for (ChemicalAnalysis ca : iog.getChemicalAnalyses()){
				for (ChemicalAnalysis ca2 : this.g.getSubsample().getChemicalAnalyses()){
					if (ca.getId() == ca2.getId()){
						ca2.setImage(ca.getImage());
						ca2.setReferenceX(ca.getReferenceX());
						ca2.setReferenceY(ca.getReferenceY());
					}
				}
			}
		}
		new ServerOp<Grid>() {
			@Override
			public void begin() {
				MpDb.imageBrowser_svc
				.saveGrid(ImageBrowserDetails.this.g, this);
				ImageBrowserDetails.this.info.setText("Saving, please wait...");
				save.setEnabled(false);
			}

			public void onSuccess(final Grid result) {
				save.setEnabled(true);
				info.setText("Image map has been saved");
				// After saving we have to update our copies
				g = result;
				final Iterator<ImageOnGrid> itr = g.getImagesOnGrid()
				.iterator();
				while (itr.hasNext()) {
					final ImageOnGrid iog = itr.next();
					final ImageOnGridContainer container = imagesOnGrid.get(iog
							.getImage());
					container.setIog(iog);
				}
				mouseListener.setImagesOnGrid(imagesOnGrid.values());
				if (leavingPage) {
					leavingPage();
				}
			}
		}.begin();
	}

	private void doPanUp() {
		panHandler.pan(0, 201);
	}

	private void doPanLeft() {
		panHandler.pan(201, 0);
	}

	private void doPanRight() {
		panHandler.pan(-201, 0);
	}

	private void doPanDown() {
		panHandler.pan(0, -201);
	}

	private void doPanHome() {
		panHandler.reset();
	}

	public MAbsolutePanel getGrid() {
		return this.grid;
	}

	public List<ImageOnGridContainer> getSelectedImages(){
		return selectionHandler.getSelectedImages();
	}

	public float getZoomScale() {
		return zoomer.getCurrentScale();
	}

	public PanHandler getPanHandler() {
		return panHandler;
	}

	public FlowPanel getBoundary() {
		return boundary;
	}

	public Grid getImageGrid() {
		return g;
	}

	public void setGroupEnabled(final boolean enabled) {
		groupTogether.setVisible(enabled);
	}

	public void setUngroupEnabled(final boolean enabled) {
		unGroup.setVisible(enabled);
	}

	public Map<Image, ImageOnGridContainer> getImagesOnGrid(){
		return imagesOnGrid;
	}

	public void updateBoundary() {
		float height = (g.getHeight()/this.scale)*ImageBrowserUtil.pps;
		float width = (g.getWidth()/this.scale)*ImageBrowserUtil.pps;
		DOM.setStyleAttribute(boundary.getElement(), "width",width+"px");
		DOM.setStyleAttribute(boundary.getElement(), "height",height+"px");

		DOM.setStyleAttribute(boundary.getElement(), "top", totalYOffset-1+"px");
		DOM.setStyleAttribute(boundary.getElement(), "left",totalXOffset-1+"px");
	}

	public void onPageChanged() {

	}

	public void onBeforePageChanged() {
		leavingPage = true;
		if (g != null) {
			new ConfirmationDialogBox(LocaleHandler.lc_text.confirmation_SaveImageMap(), true, true) {
				public void onSubmit() {
					doSave();
				}
				public void onCancel() {
					leavingPage = false;
				}

				public void onNo() {
					leavingPage();
				}
			}.show();
		}
	}

	private void leavingPage(){
		leavingPage = false;
		MetPetDBApplication.finishDispatchingBeforeCurrentPageChanged();
		MetPetDBApplication.removePageWatcher(this);
	}

}
