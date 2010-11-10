package edu.rpi.metpetdb.client.ui.dialogs;

import java.util.ArrayList;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.model.Image;
import edu.rpi.metpetdb.client.ui.widgets.MLink;

/**
 * Displays an image in a pop up dialog. Supports browsing between 
 * multiple images.
 * 
 * @author ?, millib2
 */
public class ViewImagePopup extends MDialogBox implements ClickListener,
		KeyboardListener {
	
	private FocusPanel f;
	private FlexTable ft;
	private MLink closeBtn;
	private Label imageTitle;
	private Label pageLbl;
	private Label descriptionLbl;
	private Label scaleLbl;
	private com.google.gwt.user.client.ui.Image displayImage;
	private ArrayList<Image> images;
	private int index;

	/**
	 * Creates a pop-up to display the given collection of images.
	 * 
	 * @param images List of images to display.
	 * @param indexStart Index of image to display initially.
	 */
	public ViewImagePopup(final ArrayList<Image> images, 
			final com.google.gwt.user.client.ui.Image image, int indexStart) {		
		// We want the dialog to close when we click outside it
		super(true, false);
		
		closeBtn = new MLink("", this);
		this.images = images;
		closeBtn.addStyleName("lbCloseLink");
		ft = new FlexTable();
		displayImage = image;
		
		index = indexStart;
		Image currentImage = (Image) images.get(index);
		setupFields(currentImage);

		displayImage.setStyleName("image-title");
		//imageTitle.setStyleName("gray");
		ft.setWidget(0, 0, this.closeBtn);
		ft.setWidget(1, 0, this.imageTitle);
		ft.setWidget(2, 0, this.pageLbl);
		ft.setWidget(3, 0, this.descriptionLbl);
		ft.setWidget(4, 0, this.scaleLbl);
		ft.setWidget(5, 0, this.displayImage);
		
		ft.getFlexCellFormatter().setRowSpan(1, 1, 2);
		ft.getFlexCellFormatter().setColSpan(0, 0, 2);
		ft.getFlexCellFormatter().setAlignment(1, 1,
				HasHorizontalAlignment.ALIGN_RIGHT,
				HasVerticalAlignment.ALIGN_MIDDLE);
		ft.getFlexCellFormatter().setAlignment(1, 0,
				HasHorizontalAlignment.ALIGN_LEFT,
				HasVerticalAlignment.ALIGN_MIDDLE);
		ft.getFlexCellFormatter().setAlignment(2, 0,
				HasHorizontalAlignment.ALIGN_LEFT,
				HasVerticalAlignment.ALIGN_MIDDLE);
		ft.setCellSpacing(4);
		f = new FocusPanel();
		f.addKeyboardListener(this);
		f.setWidget(ft);
		setWidget(f);
	}

	/**
	 * Change the image being displayed in the pop-up.
	 * 
	 * @param right 
	 * 		If true increases the index by one (moves to the right in the list of images.)
	 */
	private void nextImage(final boolean right) {
		if (right)
			index = ++index % images.size();
		else {
			--index;
			if (index < 0)
				index = images.size() - 1;
		}
		
		setCurrentImage(index);		
		setDimensions();
	}
	
	/**
	 * Sets the values of the data fields to match the given image.
	 * 
	 * @param currentImage
	 */
	private void setupFields(Image currentImage) {
		imageTitle = new Label(parseFilename(currentImage.getFilename()));
		imageTitle.setStyleName("gray");	
		pageLbl = new Label("Image " + (index + 1) + " of " + images.size());
		descriptionLbl = new Label(currentImage.getDescription());
		scaleLbl = new Label(currentImage.getScale().toString() + " millimeters in width");
	}
	
	/**
	 * Rebuilds the pop-up to display the image at the given index.
	 * 
	 * @param index
	 */
	private void setCurrentImage(int index) {
		ft = new FlexTable();
		closeBtn = new MLink("", this);
		closeBtn.addStyleName("lbCloseLink");	
		
		Image currentImage = (Image)images.get(index);
		
		displayImage = new com.google.gwt.user.client.ui.Image();
		displayImage.setUrl(currentImage.getServerPath());
		displayImage.setStyleName("image-title");
		setupFields(currentImage);
		ft.setWidget(0, 0, this.closeBtn);
		ft.setWidget(1, 0, this.imageTitle);
		ft.setWidget(2, 0, this.pageLbl);
		ft.setWidget(3, 0, this.descriptionLbl);
		ft.setWidget(4, 0, this.scaleLbl);
		ft.setWidget(5, 0, this.displayImage);	
		
		ft.getFlexCellFormatter().setRowSpan(1, 1, 2);
		ft.getFlexCellFormatter().setColSpan(0, 0, 2);
		ft.getFlexCellFormatter().setAlignment(1, 1,
				HasHorizontalAlignment.ALIGN_RIGHT,
				HasVerticalAlignment.ALIGN_MIDDLE);
		ft.getFlexCellFormatter().setAlignment(1, 0,
				HasHorizontalAlignment.ALIGN_LEFT,
				HasVerticalAlignment.ALIGN_MIDDLE);
		ft.getFlexCellFormatter().setAlignment(2, 0,
				HasHorizontalAlignment.ALIGN_LEFT,
				HasVerticalAlignment.ALIGN_MIDDLE);
		ft.setCellSpacing(4);	
		
		f = new FocusPanel();
		f.addKeyboardListener(this);
		f.setWidget(ft);
		setWidget(f);
	}

	private String parseFilename(final String filename) {
		String name = filename;
		while (name.indexOf('\\') >= 0)
			name = name.substring(name.indexOf('\\') + 1);
		return name;
	}

	public void onClick(Widget sender) {
		if (sender == closeBtn) {
			this.hide();
		}
	}

	public void onKeyPress(final Widget sender, final char kc, final int mod) {
		if (kc == KEY_ESCAPE) {
			ViewImagePopup.this.hide();
		}
		if (kc == 'n')
			nextImage(true);
		else if (kc == 'p')
			nextImage(false);
	}
	
	public void onKeyDown(final Widget sender, final char kc, final int mod) { }
	
	public void onKeyUp(final Widget sender, final char kc, final int mod) { }

	@Override
	protected void onLoad() {
		setDimensions();
	}
	
	/**
	 * Positions the pop-up and scales the displayed image.
	 */
	private void setDimensions() {
		
		//resize the image if it exceeds the size of the browser window
		int imageHeight = displayImage.getHeight();
		int imageWidth = displayImage.getWidth();
		int screenHeight = Window.getClientHeight() - 100; //allow extra room for close button, etc.
		int screenWidth = Window.getClientWidth() - 50;
		
		if(imageHeight > screenHeight || imageWidth > screenWidth){
			double imageMultiplier;
			//compare ratios to figure out which one needs to be the multiplier used for scaling
			//e.g. if imageWidth is much bigger than screenWidth, imageWidth will be resized to fit the screen
			//     and imageHeight will be multiplied by the same ratio
			if(((double) imageHeight / (double) screenHeight) > ((double) imageWidth / (double) screenWidth)){
				imageMultiplier = 1.0 / ((double) imageHeight / (double) screenHeight);
			}
			else{ 
				imageMultiplier = 1.0 / ((double) imageWidth / (double) screenWidth);
			}
			displayImage.setSize(((int) (imageWidth * imageMultiplier)) + "px",
					((int) (imageHeight * imageMultiplier)) + "px");
		}

		super.onLoad();
	}
}
