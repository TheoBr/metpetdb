package edu.rpi.metpetdb.client.ui.image.browser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.ui.MpDb;
import edu.rpi.metpetdb.client.ui.commands.ServerOp;
import edu.rpi.metpetdb.client.ui.widgets.ImageHyperlink;
import edu.rpi.metpetdb.client.ui.widgets.MLink;

public class ImageList extends HorizontalPanel implements ClickListener {
	private List<edu.rpi.metpetdb.client.model.Image> images;
	private int currentIndex = 0;
	private Image currentImage;
	// Set<Image>
	private final ArrayList<edu.rpi.metpetdb.client.model.Image> selectedImages;
	private Widget lastImage;
	boolean selected = false;
	private boolean onlySelectOne;

	public ImageList(final long subsampleId, final boolean showAll) {
		this(subsampleId, showAll, false, null);
	}

	public ImageList(final long subsampleId, final boolean showAll,
			final Collection<ImageOnGridContainer> imagesOnGrid) {
		this(subsampleId, showAll, false, imagesOnGrid);
	}

	public ImageList(final long subsampleId, final boolean showAll,
			final boolean onlySelectOne) {
		this(subsampleId, showAll, onlySelectOne, null);
	}

	public ImageList(final long subsampleId, final boolean showAll,
			final boolean onlySelectOne,
			final Collection<ImageOnGridContainer> imagesOnGrid) {
		new ServerOp<List<edu.rpi.metpetdb.client.model.Image>>() {
			public void begin() {
				MpDb.image_svc.allImages(subsampleId, this);
			}
			public void onSuccess(
					final List<edu.rpi.metpetdb.client.model.Image> result) {
				if (result == null) {
					return;
				} else {
					if (imagesOnGrid != null) {
						Iterator<ImageOnGridContainer> itr = imagesOnGrid
								.iterator();
						while (itr.hasNext()) {
							final edu.rpi.metpetdb.client.model.Image image = itr
									.next().getIog().getImage();
							if (result.contains(image)) {
								result.remove(image);
							}
						}
					}
					images = result;
					if (images == null || images.size() <= 0) {
						add(new Label("No Image"));
					} else {
						buildInterface(showAll);
					}
				}
			}
		}.begin();
		this.onlySelectOne = onlySelectOne;
		selectedImages = new ArrayList<edu.rpi.metpetdb.client.model.Image>();
	}

	public ArrayList<edu.rpi.metpetdb.client.model.Image> getSelectedImages() {
		return selectedImages;
	}

	public Collection<edu.rpi.metpetdb.client.model.Image> getImages() {
		return images;
	}

	public int getNumberOfImages() {
		if (images != null) {
			return images.size();
		} else {
			return 0;
		}
	}

	public void buildInterface(final boolean showAll) {
		if (showAll) {
			final Iterator<edu.rpi.metpetdb.client.model.Image> itr = images
					.iterator();
			final Grid g = new Grid((int) (Math.ceil(images.size() / 3.0)), 3);
			int row = 0;
			while (itr.hasNext()) {
				for (int i = 0; i < 3; ++i) {
					if (itr.hasNext()) {
						final edu.rpi.metpetdb.client.model.Image currentImage = (edu.rpi.metpetdb.client.model.Image) itr
								.next();
						final Image image = new Image();
						image.setUrl(currentImage.get64x64ServerPath());
						final ImageHyperlink imageLink = new ImageHyperlink(
								image, this, currentImage, currentImage
										.getFilename());
						imageLink.setStyleName("ssimg");
						g.setWidget(row, i, imageLink);
					}
				}
				++row;
			}
			add(g);
		} else {
			if (images.size() > 1)
				add(new MLink("prev", new ClickListener() {
					public void onClick(final Widget sender) {
						showPrev();
					}
				}));

			currentImage = new Image();
			currentImage.setUrl(((edu.rpi.metpetdb.client.model.Image) images
					.get(0)).get64x64ServerPath());
			final ImageHyperlink imageLink = new ImageHyperlink(currentImage,
					images.get(0).getFilename(), this, true);
			imageLink.setStyleName("ssimg");
			add(imageLink);
			if (images.size() > 1)
				add(new MLink("next", new ClickListener() {
					public void onClick(final Widget sender) {
						showNext();
					}
				}));
		}
	}

	public void showNext() {
		++currentIndex;
		if (currentIndex >= images.size())
			currentIndex = 0;
		currentImage.setUrl(((edu.rpi.metpetdb.client.model.Image) images
				.get(currentIndex)).get64x64ServerPath());
	}

	public void showPrev() {
		--currentIndex;
		if (currentIndex < 0)
			currentIndex = images.size() - 1;
		currentImage.setUrl(((edu.rpi.metpetdb.client.model.Image) images
				.get(currentIndex)).get64x64ServerPath());
	}

	public void onClick(final Widget sender) {
		if (sender.getStyleName().equals("ssimg-checked")) {
			sender.setStyleName("ssimg");
			if (sender instanceof ImageHyperlink) {
				if (((ImageHyperlink) sender).getObject() != null) {
					selectedImages
							.remove(((ImageHyperlink) sender).getObject());
				}
			}
		} else {
			sender.setStyleName("ssimg-checked");
			if (sender instanceof ImageHyperlink) {
				if (((ImageHyperlink) sender).getObject() != null) {
					if (lastImage != null && onlySelectOne) {
						selectedImages.clear();
						lastImage.setStyleName("ssimg");
					}
					selectedImages
							.add((edu.rpi.metpetdb.client.model.Image) ((ImageHyperlink) sender)
									.getObject());

				}
			}
			lastImage = sender;
		}
		selected = !selected;
	}
}
