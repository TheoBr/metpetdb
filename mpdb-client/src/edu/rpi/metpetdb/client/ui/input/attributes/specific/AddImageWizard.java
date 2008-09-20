package edu.rpi.metpetdb.client.ui.input.attributes.specific;

import com.google.gwt.user.client.ui.Button;

import edu.rpi.metpetdb.client.model.Image;
import edu.rpi.metpetdb.client.model.XrayImage;
import edu.rpi.metpetdb.client.ui.MpDb;
import edu.rpi.metpetdb.client.ui.ServerOp;
import edu.rpi.metpetdb.client.ui.input.DetailsPanel;
import edu.rpi.metpetdb.client.ui.input.WizardDialog;
import edu.rpi.metpetdb.client.ui.input.attributes.GenericAttribute;
import edu.rpi.metpetdb.client.ui.input.attributes.ListboxAttribute;
import edu.rpi.metpetdb.client.ui.input.attributes.RadioButtonAttribute;
import edu.rpi.metpetdb.client.ui.input.attributes.TextAttribute;

public class AddImageWizard extends WizardDialog {

	public AddImageWizard(final ServerOp r) {
		final XrayImage xray = new XrayImage();
		final UploadImageAttribute uploadImage = new UploadImageAttribute(
				MpDb.doc.Subsample_images);
		final GenericAttribute xray_attributes[] = {
				new TextAttribute(MpDb.doc.XrayImage_current),
				new TextAttribute(MpDb.doc.XrayImage_voltage),
				new TextAttribute(MpDb.doc.XrayImage_dwelltime),
				new TextAttribute(MpDb.doc.XrayImage_lines),
				new RadioButtonAttribute(MpDb.doc.XrayImage_radiation),
				new ListboxAttribute(MpDb.doc.XrayImage_element),
		};

		final DetailsPanel p_xray = new DetailsPanel(xray_attributes,
				new Button[] {});
		final ServerOp notifier = new ServerOp() {
			public void begin() {
			}
			public void onSuccess(final Object result) {
				if (result instanceof String) {
					if (result.equals("X-ray")) {
						p_xray.edit(xray);
						AddImageWizard.this.addStep(p_xray, 1,
								"X-ray Attributes");
					} else {
						AddImageWizard.this.removeStep(1);
					}
				}
			}
		};
		final ListboxAttribute imageType = new ListboxAttribute(
				MpDb.doc.Image_imageType, notifier);
		final GenericAttribute image[] = {
				uploadImage, imageType, new TextAttribute(MpDb.doc.Image_lut),
				new TextAttribute(MpDb.doc.Image_contrast),
				new TextAttribute(MpDb.doc.Image_brightness),
				new TextAttribute(MpDb.doc.Image_pixelsize),
		};
		final DetailsPanel p_image = new DetailsPanel(image, new Button[] {});
		p_image.edit(xray);

		final ServerOp dialog_finish = new ServerOp() {
			public void begin() {
				uploadImage.getStatus(this);
			}
			public void onSuccess(final Object result) {
				if (((Image) result).getImageType().equals("X-ray")) {
					r.onSuccess(result);
				} else {
					r.onSuccess(((XrayImage) result).getImage());
				}
			}
			public void onFailure(final Throwable e) {
				r.onFailure(e);
			}
		};
		this.addDialogFinishListener(dialog_finish);
		this.addStep(p_image, 0, "Upload Image");
	}

}
