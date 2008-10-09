package edu.rpi.metpetdb.client.ui.image.browser.dialogs;

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
import edu.rpi.metpetdb.client.ui.dialogs.MDialogBox;
import edu.rpi.metpetdb.client.ui.widgets.MLink;

public class ViewImage extends MDialogBox implements ClickListener,
		KeyboardListener {
	private final MLink close;
	private FlexTable ft;
	final FocusPanel f;
	private int index;
	private Label imageTitle;
	private Label page;
	private com.google.gwt.user.client.ui.Image displayImage;
	private ArrayList<Image> images;
	private PopupPanel left;
	private PopupPanel right;

	public ViewImage(final ArrayList<Image> images,
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
		image.addMouseListener(new MouseListener() {
			public void onMouseDown(final Widget sender, final int x,
					final int y) {

			}
			public void onMouseUp(final Widget sender, final int x, final int y) {
				if (x < displayImage.getOffsetWidth() / 2)
					nextImage(false);
				else
					nextImage(true);
			}
			public void onMouseMove(final Widget sender, final int x,
					final int y) {
				if (x < displayImage.getOffsetWidth() / 2) {
					left.show();
					right.hide();
				} else {
					right.show();
					left.hide();
				}
			}
			public void onMouseLeave(final Widget sender) {
				left.hide();
				right.hide();
			}
			public void onMouseEnter(final Widget sender) {
			}
		});

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
		ViewImage.this.imageTitle = new Label(parseFilename(temp.getFilename()));
		ViewImage.this.page = new Label("Image " + (index + 1) + " of "
				+ images.size());
		ViewImage.this.displayImage.setUrl(tempImage.getUrl());
		ft.setWidget(0, 0, ViewImage.this.displayImage);
		ft.setWidget(1, 0, ViewImage.this.imageTitle);
		ft.setWidget(2, 0, ViewImage.this.page);
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
			left.hide();
			right.hide();
		}
	}

	public void onKeyPress(final Widget sender, final char kc, final int mod) {
		if (kc == KEY_ESCAPE) {
			ViewImage.this.hide();
			left.hide();
			right.hide();
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

		right = new PopupPanel() {
			protected void onLoad() {
				final int topPos = (ViewImage.this.getPopupTop() + ViewImage.this
						.getOffsetHeight() / 4);
				final int rightPos = ViewImage.this.getPopupLeft()
						+ ViewImage.this.getOffsetWidth()
						- this.getOffsetWidth();
				setPopupPosition(rightPos, topPos);
				DOM.setStyleAttribute(right.getElement(), "zIndex", String
						.valueOf(10000));
				f.setFocus(true);
			}
		};
		left = new PopupPanel() {
			protected void onLoad() {
				final int topPos = (ViewImage.this.getPopupTop() + ViewImage.this
						.getOffsetHeight() / 4);
				final int leftPos = ViewImage.this.getPopupLeft();
				setPopupPosition(leftPos, topPos);
				DOM.setStyleAttribute(left.getElement(), "zIndex", String
						.valueOf(10000));
				f.setFocus(true);
			}
		};

		MLink prev = new MLink("", this);
		MLink next = new MLink("", this);
		prev.addStyleName("prevLink");
		next.addStyleName("nextLink");
		right.add(next);
		left.add(prev);
	}
}
