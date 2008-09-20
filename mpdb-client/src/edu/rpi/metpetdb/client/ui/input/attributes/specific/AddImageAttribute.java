package edu.rpi.metpetdb.client.ui.input.attributes.specific;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.model.Image;
import edu.rpi.metpetdb.client.model.interfaces.MObject;
import edu.rpi.metpetdb.client.model.XrayImage;
import edu.rpi.metpetdb.client.model.validation.ObjectConstraint;
import edu.rpi.metpetdb.client.ui.ServerOp;
import edu.rpi.metpetdb.client.ui.input.attributes.GenericAttribute;
import edu.rpi.metpetdb.client.ui.widgets.MHtmlList;

public class AddImageAttribute extends GenericAttribute implements
		ClickListener {

	private final Button addImage;
	private Set<Image> images;
	private MHtmlList list;
	private final VerticalPanel vp;

	public AddImageAttribute(final ObjectConstraint ic) {
		super(ic);
		addImage = new Button("Add Image", this);
		vp = new VerticalPanel();
		vp.add(addImage);
	}

	public Widget[] createEditWidget(final MObject obj, final String id,
			final GenericAttribute ga) {
		if (list != null)
			vp.remove(list);
		list = new MHtmlList();
		addImage.setEnabled(true);
		final Widget[] widgets = createDisplayWidget(obj, true);
		for (int i = 0; i < widgets.length; ++i)
			list.add(widgets[i]);
		vp.add(list);
		if (get(obj) != null)
			images = new HashSet(get(obj));
		else
			images = new HashSet();
		return new Widget[] {
			vp
		};
	}

	public Widget[] createDisplayWidget(final MObject obj) {
		return createDisplayWidget(obj, false);
	}

	public Widget[] createDisplayWidget(final MObject obj,
			final boolean editMode) {
		final VerticalPanel vp = new VerticalPanel();
		if (get(obj) != null) {
			final Collection images = (Collection) get(obj);
			Iterator itr = images.iterator();
			while (itr.hasNext()) {
				final edu.rpi.metpetdb.client.model.Image image = (edu.rpi.metpetdb.client.model.Image) itr
						.next();
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
		imageContainer.add(new com.google.gwt.user.client.ui.Image(image
				.get64x64ServerPath()));
		imageContainer.add(new Label("Image Type: " + image.getImageType()));
		imageContainer.add(new Label("Contrast: "
				+ (image.getContrast() != null ? image.getContrast().toString()
						: "")));
		imageContainer.add(new Label("Lut: "
				+ (image.getLut() != null ? image.getLut().toString() : "")));
		imageContainer.add(new Label("Brightness: "
				+ (image.getBrightness() != null ? image.getBrightness()
						.toString() : "")));
		imageContainer.add(new Label("Pixel Size: "
				+ (image.getPixelsize() != null ? image.getPixelsize()
						.toString() : "")));
		if (image.getImageType().equals("X-ray")) {
			final XrayImage xray = (XrayImage) image;
			imageContainer.add(new Label("Current: "
					+ (xray.getCurrent() != null ? xray.getCurrent().toString()
							: "")));
			imageContainer.add(new Label("Voltage: "
					+ (xray.getVoltage() != null ? xray.getVoltage().toString()
							: "")));
			imageContainer.add(new Label("Dwelltime: "
					+ (xray.getDwelltime() != null ? xray.getDwelltime()
							.toString() : "")));
			imageContainer.add(new Label("Lines: "
					+ (xray.getLines() != null ? xray.getLines().toString()
							: "")));
			imageContainer.add(new Label("Radiation: "
					+ (xray.getRadiation() != null ? xray.getRadiation()
							.toString() : "")));
			imageContainer.add(new Label("Element: "
					+ (xray.getElement() != null ? xray.getElement().toString()
							: "")));
		}
		if (editMode) {
			imageContainer.add(new Button("Remove", new ClickListener() {
				public void onClick(final Widget sender) {
					images.remove(image);
					imageContainer.removeFromParent();
				}
			}));
		}
		return imageContainer;
	}

	public void set(final MObject obj, final Object images) {
		mSet(obj, images);
	}

	protected Set get(final MObject obj) {
		return (Set) mGet(obj);
	}

	public void onClick(final Widget sender) {
		if (sender == addImage) {
			new ServerOp<Image>() {
				public void begin() {
					new AddImageWizard(this).show();
				}
				public void onSuccess(final Image result) {
					if (((Image) result).getImageType().equals("X-ray")) {
						AddImageAttribute.this.images.add(result);
					} else {
						AddImageAttribute.this.images.add(result);
					}
					AddImageAttribute.this.list
							.add(makeImageContainer(
									(edu.rpi.metpetdb.client.model.Image) result,
									true));
				}
				public void onFailure(final Throwable e) {
					// uploadImage.getMyPanel().forceFailure(AddImageAttribute.
					// this,
					// (ValidationException) e);
				}
			}.begin();
		}
	}
	protected Object get(final Widget editWidget) {
		return images;
	}

}
