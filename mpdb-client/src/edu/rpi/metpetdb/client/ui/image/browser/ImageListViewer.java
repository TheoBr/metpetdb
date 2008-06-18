package edu.rpi.metpetdb.client.ui.image.browser;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.widgetideas.table.client.FixedWidthFlexTable;

import edu.rpi.metpetdb.client.model.ImageDTO;
import edu.rpi.metpetdb.client.ui.MpDb;
import edu.rpi.metpetdb.client.ui.ServerOp;
import edu.rpi.metpetdb.client.ui.TokenSpace;
import edu.rpi.metpetdb.client.ui.image.browser.dialogs.ViewImage;
import edu.rpi.metpetdb.client.ui.widgets.ImageHyperlink;
import edu.rpi.metpetdb.client.ui.widgets.MLink;

public class ImageListViewer extends FlowPanel implements ClickListener {
	private ArrayList images;

	public ImageListViewer(final long subsampleId) {
		new ServerOp() {
			public void begin() {
				MpDb.image_svc.allImages(subsampleId, this);
			}
			public void onSuccess(final Object result) {
				if (result == null) {
					return;
				} else {
					images = (ArrayList) ((ArrayList) result).clone();
					if (images == null || images.size() <= 0) {
						add(new Label("No Image"));
					} else {
						((edu.rpi.metpetdb.client.model.ImageDTO) images.get(0))
								.getSubsample().setImages(new HashSet(images));
						buildInterface();
					}
				}
			}
		}.begin();

	}

	private void buildInterface() {

		final FlexTable header2 = new FlexTable();
		final Label title = new Label(((ImageDTO) images.get(0)).getSubsample()
				.getName()
				+ " Images");
		final Label title2 = new Label("Images attached to");
		final MLink subsampleLink = new MLink(((ImageDTO) images.get(0))
				.getSubsample().getName(), TokenSpace
				.detailsOf(((ImageDTO) images.get(0)).getSubsample()));
		final Button addImage = new Button("Add Image", new ClickListener() {
			public void onClick(Widget sender) {
				History.newItem(TokenSpace.edit(((ImageDTO) images.get(0))
						.getSubsample()));
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
		final FlexTable header = new FlexTable();

		final FixedWidthFlexTable ft = new FixedWidthFlexTable();
		final Label imagesLabel = new Label("Images");
		imagesLabel.addStyleName("bold");
		header.setWidget(1, 0, imagesLabel);
		header.getFlexCellFormatter().setHeight(1, 0, "38px");
		header.setWidth("100%");
		ft.setWidth("100%");
		ft.addStyleName("subsample-imagetypes");
		ft.setCellSpacing(10);
		header.getRowFormatter().setStyleName(1, "mpdb-dataTableLightBlue");
		int row = 0;
		ft.setHeight("500px");
		for (int i = 0; i < 5; i++) {
			ft.insertRow(i);
			ft.getFlexCellFormatter().setHeight(i, 0, "90px");
		}
		for (int i = 0; i < 3; i++)
			ft.addCell(0);
		final Iterator itr = images.iterator();
		while (itr.hasNext()) {
			for (int i = 0; i < 4; i++) {
				if (itr.hasNext()) {
					final FlexTable cell = new FlexTable();
					final ImageDTO currentImage = (ImageDTO) itr.next();
					final Image image = new Image();
					image.setUrl(currentImage.get64x64ServerPath());
					final Image bigImage = new Image();
					bigImage.setUrl(currentImage.getServerPath());
					final ImageHyperlink imageLink = new ImageHyperlink(image,
							new ClickListener() {
								public void onClick(final Widget sender) {
									new ViewImage(images, bigImage).show();
								}
							});
					// imageLink.setStyleName("ssimg");
					cell.setWidget(0, 0, imageLink);
					cell.getFlexCellFormatter().setRowSpan(0, 0, 2);
					final Label imageTitle = new Label(
							parseFilename(currentImage.getFilename()));
					final Label imageType = new Label(currentImage
							.getImageType());
					imageTitle.addStyleName("bold");
					imageTitle.addStyleName("white");
					imageType.addStyleName("white");
					cell.setWidget(0, 1, imageTitle);
					cell.setWidget(1, 0, imageType);
					cell.setCellSpacing(5);
					cell.getFlexCellFormatter().setAlignment(0, 0,
							HasHorizontalAlignment.ALIGN_CENTER,
							HasVerticalAlignment.ALIGN_MIDDLE);
					ft.setWidget(row, i, cell);
				}
			}
			++row;
		}
		add(header2);
		add(header);
		add(ft);
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
