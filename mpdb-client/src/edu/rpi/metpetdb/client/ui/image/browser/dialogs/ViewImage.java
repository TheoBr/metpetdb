package edu.rpi.metpetdb.client.ui.image.browser.dialogs;

import java.util.ArrayList;
import java.util.Iterator;

import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.model.ImageDTO;
import edu.rpi.metpetdb.client.ui.dialogs.MDialogBox;
import edu.rpi.metpetdb.client.ui.widgets.MLink;

public class ViewImage extends MDialogBox implements ClickListener {
	private final MLink close;
	private FlexTable ft;
	private int index;
	private Label imageTitle;
	private Label page;

	public ViewImage(final ArrayList images, final Image image) {
		close = new MLink("", this);
		// close.setStyleName(Styles.PRIMARY_BUTTON);
		close.addStyleName("lbCloseLink");
		ft = new FlexTable();
		Iterator<?> itr = images.iterator();
		// while (itr.hasNext()) {
		// ImageDTO currentImage = (ImageDTO) itr.next();
		// if (currentImage.getServerPath() == image.g)
		// }
		image.addClickListener(new ClickListener() {
			public void onClick(final Widget sender) {
				Iterator<?> itr = images.iterator();
				index = 0;
				while (itr.hasNext()) {
					ImageDTO temp = (ImageDTO) itr.next();
					Image tempImage = new Image();
					tempImage.setUrl(temp.getServerPath());
					imageTitle = new Label(parseFilename(temp.getFilename()));
					page = new Label("Image " + index + " of " + images.size());
					if (tempImage.getUrl() == ((Image) ft.getWidget(0, 0))
							.getUrl()) {
						if (itr.hasNext())
							ft.setWidget(0, 0, tempImage);
						break;
					}
					++index;
				}
			}
		});
		ft.setWidget(0, 0, image);
		ft.setWidget(1, 0, imageTitle);
		ft.setWidget(2, 0, page);
		ft.setWidget(1, 1, close);
		ft.getFlexCellFormatter().setRowSpan(1, 1, 2);
		ft.getFlexCellFormatter().setColSpan(0, 0, 2);
		ft.getFlexCellFormatter().setAlignment(1, 1,
				HasHorizontalAlignment.ALIGN_RIGHT,
				HasVerticalAlignment.ALIGN_MIDDLE);
		this.setWidget(ft);

	}

	private String parseFilename(final String filename) {
		String name = filename;
		while (name.indexOf('\\') >= 0)
			name = name.substring(name.indexOf('\\') + 1);
		return name;
	}

	public void onClick(Widget sender) {
		if (sender == close)
			this.hide();
	}
}
