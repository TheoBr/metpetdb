package edu.rpi.metpetdb.client.ui.input.attributes.specific.image;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.model.Image;
import edu.rpi.metpetdb.client.model.XrayImage;
import edu.rpi.metpetdb.client.model.interfaces.HasImages;
import edu.rpi.metpetdb.client.model.validation.ObjectConstraint;
import edu.rpi.metpetdb.client.ui.commands.MCommand;
import edu.rpi.metpetdb.client.ui.dialogs.ViewImagePopup;
import edu.rpi.metpetdb.client.ui.input.attributes.GenericAttribute;
import edu.rpi.metpetdb.client.ui.widgets.MHtmlList;

public class AddImageAttribute<DataType extends HasImages> extends
		GenericAttribute<DataType> implements ClickListener {

	private final Button addImage;
	private final Button addXrayImage;
	private Set<Image> images;
	private MHtmlList list;
	private final VerticalPanel vp;

	public AddImageAttribute(final ObjectConstraint<Image> ic) {
		super(ic);
		addImage = new Button("Add Image", this);
		addXrayImage = new Button("Add X-Ray Image", this);
		FlowPanel fp = new FlowPanel();
		fp.add(addImage);
		fp.add(addXrayImage);
		vp = new VerticalPanel();
		vp.add(fp);
	}

	@Override
	public Widget[] createEditWidget(final DataType obj, final String id,
			final GenericAttribute<DataType> ga) {
		if (list != null)
			vp.remove(list);
		list = new MHtmlList();
		addImage.setEnabled(true);
		final Widget[] widgets = createDisplayWidget(obj, true);
		for (int i = 0; i < widgets.length; ++i)
			list.add(widgets[i]);
		vp.add(list);
		if (get(obj) != null)
			images = new HashSet<Image>(get(obj));
		else
			images = new HashSet<Image>();
		return new Widget[] {
			vp
		};
	}

	public Widget[] createDisplayWidget(final HasImages obj) {
		//On the samples page, we want to see the images here
		if(obj.getClass().toString().equals("class edu.rpi.metpetdb.client.model.Sample")){
			return createDisplayWidget(obj, false);
		}
		//FIXME: hack to not show the images when viewing, because they are shown in a list on the right
		return new Widget[] {
			new Label("")
		};
	}
	public Widget[] createDisplayWidget(final HasImages obj,
			final boolean editMode) {
		final VerticalPanel vp = new VerticalPanel();
		if (get(obj) != null) {
			final Collection<Image> images = (Collection<Image>) get(obj);
			Iterator<Image> itr = images.iterator();
			while (itr.hasNext()) {
				final Image image = itr.next();
				vp.add(makeImageContainer(image, editMode));
			}
		}
		return new Widget[] {
			vp
		};
	}

	private VerticalPanel makeImageContainer(
			final edu.rpi.metpetdb.client.model.Image image,
			final boolean editMode) {
		final VerticalPanel imageContainer = new VerticalPanel();
		final com.google.gwt.user.client.ui.Image img = new com.google.gwt.user.client.ui.Image(image
				.get64x64ServerPath());
		img.addClickListener(new ClickListener() {
			public void onClick(final Widget sender) {
				final ArrayList<Image> lol = new ArrayList<Image>();
				lol.add(image);
				new ViewImagePopup(
						lol,
						new com.google.gwt.user.client.ui.Image(
								image.getHalfServerPath()),
						0).show();
			}
		});
		imageContainer.add(img);
		imageContainer.add(new Label(image.getImageType().toString()));
		if (image.getCollector() != null && !image.getCollector().equals("") && !image.getCollector().equals("null")) {
			imageContainer.add(new Label("Collected by: " + image.getCollector()));
		}
		if (image.getImageType().getImageType().contains("X-ray")) {
			final XrayImage xray = (XrayImage) image;
			if (xray.getCurrent() != null)
				imageContainer.add(new Label("Current: " + xray.getCurrent()));
			if (xray.getVoltage() != null)
				imageContainer.add(new Label("Voltage: " + xray.getVoltage()));
			if (xray.getDwelltime() != null)
				imageContainer.add(new Label("Dwelltime: "
						+ xray.getDwelltime()));
			if (xray.getElement() != null && xray.getElement().length() > 0)
				imageContainer.add(new Label("Element: " + xray.getElement()));
		}
		if (editMode) {
			FlowPanel hr = new FlowPanel();
			hr.add(new Button("Edit", new ClickListener() {
				public void onClick(final Widget sender) {
					//Open a panel to edit the image
					new EditImageWizard(image).show();
				}
			}));
			hr.add(new Button("Remove", new ClickListener() {
				public void onClick(final Widget sender) {
					images.remove(image);
					imageContainer.removeFromParent();
				}
			}));
			imageContainer.add(hr);
		}
		return imageContainer;
	}

	public void set(final HasImages obj, final Object images) {
		mSet(obj, images);
	}

	protected Set<Image> get(final HasImages obj) {
		return obj.getImages();
	}

	public void onClick(final Widget sender) {
		// X-ray images not implemented
		if (sender == addImage || sender == addXrayImage) {
			new AddImageWizard((sender == addXrayImage), new MCommand<Image>() {
		//if (sender == addImage) {
		//	new AddImageWizard(false, new MCommand<Image>() {
				@Override
				public void execute(Image result) {
					images.add(result);
					list.add(makeImageContainer(result, true));
				}
			}).show();
		}
	}
	protected Set<Image> get(final Widget editWidget) {
		return images;
	}

}
