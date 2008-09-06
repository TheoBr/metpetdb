package edu.rpi.metpetdb.client.ui.image.browser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.model.ImageDTO;
import edu.rpi.metpetdb.client.model.SubsampleDTO;
import edu.rpi.metpetdb.client.ui.MpDb;
import edu.rpi.metpetdb.client.ui.ServerOp;
import edu.rpi.metpetdb.client.ui.TokenSpace;
import edu.rpi.metpetdb.client.ui.image.browser.dialogs.ViewImage;
import edu.rpi.metpetdb.client.ui.widgets.ImageHyperlink;
import edu.rpi.metpetdb.client.ui.widgets.MLink;

public class ImageListViewer extends FlowPanel implements ClickListener {
	private ArrayList images;
	private final long id;
	private FlowPanel fp;
	private ListBox lb;
	private Label imagesLabel;
	private FlexTable noImagesContainer = new FlexTable();

	public ImageListViewer(final long subsampleId, final boolean isScreen) {
		this(subsampleId, isScreen, null);
	}

	public ImageListViewer(final long subsampleId, final String type) {
		this(subsampleId, true, type);
	}

	private ImageListViewer(final long subsampleId, final boolean isScreen,
			final String type) {
		id = subsampleId;
		fp = new FlowPanel();
		new ServerOp() {
			public void begin() {
				MpDb.image_svc.allImages(id, this);
			}
			public void onSuccess(final Object result) {
				if (result == null) {
					return;
				} else {
					images = (ArrayList) ((ArrayList) result).clone();
					if (images == null || images.size() <= 0) {
						add(new Label(
								"There are no images associated with this subsample"));
					} else {
						((ImageDTO) images.get(0)).getSubsample().setImages(
								new HashSet(images));
						buildInterface(isScreen, type);
					}
				}
			}
		}.begin();

	}

	private void buildInterface(final boolean isScreen, final String type) {
		if (isScreen) {
			final FlexTable header2 = new FlexTable();
			final Label title = new Label(((ImageDTO) images.get(0))
					.getSubsample().getName()
					+ " Images");
			final Label title2 = new Label("Images attached to");
			final MLink subsampleLink = new MLink(((ImageDTO) images.get(0))
					.getSubsample().getName(), TokenSpace
					.detailsOf(((ImageDTO) images.get(0)).getSubsample()));
			final Button addImage = new Button("Add Image",
					new ClickListener() {
						public void onClick(Widget sender) {
							History.newItem(TokenSpace.edit(((ImageDTO) images
									.get(0)).getSubsample()));
						}
					});
			title.addStyleName("big");
			header2.setWidget(0, 0, title);
			header2.setWidget(1, 0, title2);
			header2.setWidget(1, 1, subsampleLink);
			header2.setWidget(0, 1, addImage);
			header2.getFlexCellFormatter().setColSpan(0, 1, 2);
			header2.getFlexCellFormatter().setRowSpan(0, 1, 2);
			header2.getFlexCellFormatter().setAlignment(0, 1,
					HasHorizontalAlignment.ALIGN_RIGHT,
					HasVerticalAlignment.ALIGN_MIDDLE);
			header2.getFlexCellFormatter().setColSpan(0, 0, 2);
			header2.getFlexCellFormatter().setWidth(1, 0, "130px");
			header2.addStyleName("subsample-header");
			addImage.addStyleName("addlink");
			add(header2);
			/* FIXME image type is not taken from the database
			if (type == null)
				createListBox(MpDb.doc.Image_imageType.getValues(), null);
			else
				createListBox(MpDb.doc.Image_imageType.getValues(), type);
				*/
		}

		final FlexTable header = new FlexTable();
		imagesLabel = new Label();
		imagesLabel.addStyleName("bold");
		header.setWidget(1, 0, imagesLabel);
		header.getFlexCellFormatter().setHeight(1, 0, "38px");
		header.setWidth("100%");
		header.getRowFormatter().setStyleName(1, "mpdb-dataTableLightBlue");

		add(header);

		loadimages(isScreen, type);
	}

	private void createListBox(final Collection<?> items, final String selected) {
		lb = new ListBox();

		lb.setVisibleItemCount(1);

		final Iterator<?> itr = items.iterator();
		lb.addItem("All");
		int index = 1;
		while (itr.hasNext()) {
			final Object o = itr.next();
			lb.addItem(o.toString());
			if (selected != null && o.equals(selected)) {
				lb.setItemSelected(index, true);
			}
			++index;
		}

		lb.setItemSelected(lb.getSelectedIndex(), true);

		lb.addChangeListener(new ChangeListener() {
			public void onChange(final Widget w) {
				loadimages(true, lb.getValue(lb.getSelectedIndex()));
			}
		});

		add(lb);
	}

	private void loadimages(final boolean isScreen, final String type) {
		if (noImagesContainer.isAttached())
			remove(noImagesContainer);
		fp.clear();
		fp.addStyleName("subsample-imagetypes");
		final ArrayList imagesToDisplay = new ArrayList();
		imagesLabel.setText("Images");
		if (type != null && !type.equals("All")) {
			imagesLabel.setText(type);
			for (int i = 0; i < images.size(); i++) {
				if (((ImageDTO) images.get(i)).getImageType().equals(type)) {
					imagesToDisplay.add(images.get(i));
				}
			}
		} else {
			imagesToDisplay.addAll(images);
		}

		final Iterator itr = imagesToDisplay.iterator();
		int i = 0;
		while (itr.hasNext()) {
			final FlexTable cell = new FlexTable();
			cell.setStyleName("inline");
			final ImageDTO currentImage = (ImageDTO) itr.next();
			final Image image = new Image();
			image.setUrl(currentImage.get64x64ServerPath());
			final Image bigImage = new Image();
			bigImage.setUrl(currentImage.getServerPath());
			final int index = i;
			final ImageHyperlink imageLink;
			if (isScreen) {
				imageLink = new ImageHyperlink(image, new ClickListener() {
					public void onClick(final Widget sender) {
						new ViewImage(imagesToDisplay, bigImage, index).show();
					}
				});
			} else {
				imageLink = new ImageHyperlink(image, new ClickListener() {
					public void onClick(final Widget sender) {
						new ServerOp<SubsampleDTO>() {
							public void begin() {
								MpDb.subsample_svc.details(id, this);
							}
							public void onSuccess(final SubsampleDTO s) {
								History.newItem(TokenSpace.ViewOf(s));
							}
						}.begin();
					}
				});
			}
			cell.setWidget(0, 0, imageLink);
			cell.getFlexCellFormatter().setRowSpan(0, 0, 2);
			final Label imageTitle = new Label(parseFilename(currentImage
					.getFilename()));
			final Label imageType = new Label(currentImage.getImageType().getImageType());
			imageTitle.addStyleName("bold");
			imageTitle.addStyleName("white");
			imageType.addStyleName("white");
			cell.setWidget(0, 1, imageTitle);
			cell.setWidget(1, 0, imageType);
			cell.setCellSpacing(5);
			cell.getFlexCellFormatter().setAlignment(0, 0,
					HasHorizontalAlignment.ALIGN_CENTER,
					HasVerticalAlignment.ALIGN_MIDDLE);
			fp.add(cell);
			i++;
		}
		if (imagesToDisplay.size() == 0) {
			noImagesContainer.setWidth("100%");
			noImagesContainer
					.setWidget(
							0,
							0,
							new Label(
									"There are no images of this type associated with this subsample"));
			noImagesContainer.getFlexCellFormatter().setAlignment(0, 0,
					HasHorizontalAlignment.ALIGN_CENTER,
					HasVerticalAlignment.ALIGN_MIDDLE);
			add(noImagesContainer);
		}
		add(fp);
	}

	private String parseFilename(final String filename) {
		String name = filename;
		while (name.indexOf('\\') >= 0)
			name = name.substring(name.indexOf('\\') + 1);
		return name;
	}

	public void onClick(Widget sender) {

	}
}
