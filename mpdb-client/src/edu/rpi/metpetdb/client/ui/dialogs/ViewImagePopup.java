package edu.rpi.metpetdb.client.ui.dialogs;

import java.util.ArrayList;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.model.Image;
import edu.rpi.metpetdb.client.ui.widgets.MLink;

public class ViewImagePopup extends MDialogBox implements ClickListener,
		KeyboardListener {
	private final MLink close;
	private FlexTable ft;
	final FocusPanel f;
	private int index;
	private Label imageTitle;
	private Label page;
	private com.google.gwt.user.client.ui.Image displayImage;
	private ArrayList<Image> images;

	public ViewImagePopup(final ArrayList<Image> images,
			final com.google.gwt.user.client.ui.Image image, int indexStart) {
		close = new MLink("", this);
		this.images = images;
		// close.setStyleName(Styles.PRIMARY_BUTTON);
		close.addStyleName("lbCloseLink");
		ft = new FlexTable();
		displayImage = image;
		
		index = indexStart;
		Image currentImage = (Image) images.get(index);
		imageTitle = new Label(parseFilename(currentImage.getFilename()));
		page = new Label("Image " + (index + 1) + " of " + images.size());

		displayImage.setStyleName("image-title");
		imageTitle.setStyleName("gray");
		ft.setWidget(0, 0, displayImage);
		ft.setWidget(1, 0, imageTitle);
		ft.setWidget(2, 0, page);
		ft.setWidget(1, 1, close);
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

	private void nextImage(final boolean direction) {
		if (direction)
			index = ++index % images.size();
		else {
			--index;
			if (index < 0)
				index = images.size() - 1;
		}

		Image temp = (Image) images.get(index);
		com.google.gwt.user.client.ui.Image tempImage = new com.google.gwt.user.client.ui.Image();
		tempImage.setUrl(temp.getServerPath());
		ViewImagePopup.this.imageTitle = new Label(parseFilename(temp.getFilename()));
		ViewImagePopup.this.page = new Label("Image " + (index + 1) + " of "
				+ images.size());
		ViewImagePopup.this.displayImage.setUrl(tempImage.getUrl());
		ft.setWidget(0, 0, ViewImagePopup.this.displayImage);
		ft.setWidget(1, 0, ViewImagePopup.this.imageTitle);
		ft.setWidget(2, 0, ViewImagePopup.this.page);
		displayImage.setStyleName("image-title");
		imageTitle.setStyleName("gray");
	}

	private String parseFilename(final String filename) {
		String name = filename;
		while (name.indexOf('\\') >= 0)
			name = name.substring(name.indexOf('\\') + 1);
		return name;
	}

	public void onClick(Widget sender) {
		if (sender == close) {
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
	public void onKeyDown(final Widget sender, final char kc, final int mod) {

	}
	public void onKeyUp(final Widget sender, final char kc, final int mod) {

	}

	protected void onLoad() {
		final int left2 = (Window.getClientWidth() - getOffsetWidth()) / 2;
		final int top2 = (Window.getClientHeight() - getOffsetHeight()) / 2;
		setPopupPosition(left2, top2);
		
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
	}
}
