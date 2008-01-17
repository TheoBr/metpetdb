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

import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.model.Image;
import edu.rpi.metpetdb.client.model.MObject;
import edu.rpi.metpetdb.client.model.XrayImage;
import edu.rpi.metpetdb.client.model.validation.ImageConstraint;
import edu.rpi.metpetdb.client.ui.MpDb;
import edu.rpi.metpetdb.client.ui.ServerOp;
import edu.rpi.metpetdb.client.ui.input.DetailsPanel;
import edu.rpi.metpetdb.client.ui.input.WizardDialog;
import edu.rpi.metpetdb.client.ui.input.attributes.GenericAttribute;
import edu.rpi.metpetdb.client.ui.input.attributes.ListboxAttribute;
import edu.rpi.metpetdb.client.ui.input.attributes.RadioButtonAttribute;
import edu.rpi.metpetdb.client.ui.input.attributes.TextAttribute;
import edu.rpi.metpetdb.client.ui.widgets.MUnorderedList;

public class AddImageAttribute extends GenericAttribute
		implements
			ClickListener {

	private final Button addImage;
	private Set images;
	private MUnorderedList list;
	private final VerticalPanel vp;

	public AddImageAttribute(final ImageConstraint ic) {
		super(ic);
		addImage = new Button("Add Image", this);
		vp = new VerticalPanel();
		vp.add(addImage);
	}

	public Widget[] createEditWidget(final MObject obj, final String id,
			final GenericAttribute ga) {
		if (list != null)
			vp.remove(list);
		list = new MUnorderedList();
		addImage.setEnabled(true);
		final Widget[] widgets = createDisplayWidget(obj,true);
		for (int i = 0; i < widgets.length; ++i)
			list.add(widgets[i]);
		vp.add(list);
		if (get(obj)!=null)
			images = new HashSet(get(obj));
		else
			images = new HashSet();
		return new Widget[]{vp};
	}
	
	public Widget[] createDisplayWidget(final MObject obj) {
		return createDisplayWidget(obj,false);
	}

	public Widget[] createDisplayWidget(final MObject obj, final boolean editMode) {
		final VerticalPanel vp = new VerticalPanel();
		if (get(obj) != null) {
			final Collection images = (Collection) get(obj);
			Iterator itr = images.iterator();
			while (itr.hasNext()) {
				final edu.rpi.metpetdb.client.model.Image image = (edu.rpi.metpetdb.client.model.Image) itr
						.next();
				vp.add(makeImageContainer(image,editMode));
			}
		}
		return new Widget[]{vp};
	}

	private VerticalPanel makeImageContainer(
			final edu.rpi.metpetdb.client.model.Image image, final boolean editMode) {
		final VerticalPanel imageContainer = new VerticalPanel();
		imageContainer.add(new com.google.gwt.user.client.ui.Image(image
				.get64x64ServerPath()));
		imageContainer.add(new Label("Image Type: " + image.getImageType()));
		imageContainer.add(new Label("Contrast: " + (image.getContrast() != null ? image.getContrast().toString() : "")));
		imageContainer.add(new Label("Lut: " + (image.getLut() != null ? image.getLut().toString() : "")));
		imageContainer.add(new Label("Brightness: " + (image.getBrightness() != null ? image.getBrightness().toString() : "")));
		imageContainer.add(new Label("Pixel Size: " +( image.getPixelsize() != null ? image.getPixelsize().toString() : "")));
		if (image.getImageType().equals("X-ray")) {
			final XrayImage xray = (XrayImage) image;
			imageContainer.add(new Label("Current: " +( xray.getCurrent() != null ? xray.getCurrent().toString() : "")));
			imageContainer.add(new Label("Voltage: " +( xray.getVoltage() != null ? xray.getVoltage().toString() : "")));
			imageContainer.add(new Label("Dwelltime: " +( xray.getDwelltime() != null ? xray.getDwelltime().toString() : "")));
			imageContainer.add(new Label("Lines: " +( xray.getLines() != null ? xray.getLines().toString() : "")));
			imageContainer.add(new Label("Radiation: " +( xray.getRadiation() != null ? xray.getRadiation().toString() : "")));
			imageContainer.add(new Label("Element: " +( xray.getElement() != null ? xray.getElement().toString() : "")));
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

	private WizardDialog makeWizardDialog() {
		final XrayImage xray = new XrayImage();
		final WizardDialog wd = new WizardDialog();
		final UploadImageAttribute uploadImage = new UploadImageAttribute(
				MpDb.doc.Subsample_images);
		final GenericAttribute xray_attributes[] = {
				new TextAttribute(MpDb.doc.XrayImage_current),
				new TextAttribute(MpDb.doc.XrayImage_voltage),
				new TextAttribute(MpDb.doc.XrayImage_dwelltime),
				new TextAttribute(MpDb.doc.XrayImage_lines),
				new RadioButtonAttribute(MpDb.doc.XrayImage_radiation),
				new TextAttribute(MpDb.doc.XrayImage_element),
				};

		final DetailsPanel p_xray = new DetailsPanel(xray_attributes,
				new Button[]{});
		final ServerOp notifier = new ServerOp() {
			public void begin() {
			}
			public void onSuccess(final Object result) {
				if (result instanceof String) {
					if (result.equals("X-ray")) {
						p_xray.edit(xray);
						wd.addStep(p_xray, 1, "X-ray Attributes");
					} else {
						wd.removeStep(1);
					}
				}
			}
		};
		final ListboxAttribute imageType = new ListboxAttribute(
				MpDb.doc.Image_imageType, notifier);
		final GenericAttribute image[] = {uploadImage, imageType,
				new TextAttribute(MpDb.doc.Image_lut),
				new TextAttribute(MpDb.doc.Image_contrast),
				new TextAttribute(MpDb.doc.Image_brightness),
				new TextAttribute(MpDb.doc.Image_pixelsize),};
		final DetailsPanel p_image = new DetailsPanel(image, new Button[]{});
		p_image.edit(xray);

		final ServerOp dialog_finish = new ServerOp() {
			public void begin() {
				uploadImage.getStatus(this);
			}
			public void onSuccess(final Object result) {
				if (((Image) result).getImageType().equals("X-ray")) {
					AddImageAttribute.this.images.add(result);
				} else {
					AddImageAttribute.this.images.add(((XrayImage)result).getImage());
				}
				AddImageAttribute.this.list
						.add(makeImageContainer((edu.rpi.metpetdb.client.model.Image) result,true));
			}
			public void onFailure(final Throwable e) {
				uploadImage.getMyPanel().forceFailure(
						AddImageAttribute.this, (ValidationException) e);
			}
		};
		wd.addDialogFinishListener(dialog_finish);
		wd.addStep(p_image, 0, "Upload Image");
		return wd;
	}

	public void set(final MObject obj, final Object images) {
		mSet(obj, images);
	}

	protected Set get(final MObject obj) {
		return (Set) mGet(obj);
	}

	public void onClick(final Widget sender) {
		if (sender == addImage) {
			makeWizardDialog().show();
		}
	}

	protected Object get(final Widget editWidget) {
		return images;
	}

}
