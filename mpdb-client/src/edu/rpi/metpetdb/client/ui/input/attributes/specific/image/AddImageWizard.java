package edu.rpi.metpetdb.client.ui.input.attributes.specific.image;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Button;

import edu.rpi.metpetdb.client.model.Image;
import edu.rpi.metpetdb.client.model.ImageType;
import edu.rpi.metpetdb.client.model.XrayImage;
import edu.rpi.metpetdb.client.ui.MpDb;
import edu.rpi.metpetdb.client.ui.commands.MCommand;
import edu.rpi.metpetdb.client.ui.input.DetailsPanel;
import edu.rpi.metpetdb.client.ui.input.WizardDialog;
import edu.rpi.metpetdb.client.ui.input.attributes.GenericAttribute;
import edu.rpi.metpetdb.client.ui.input.attributes.ListboxAttribute;
import edu.rpi.metpetdb.client.ui.input.attributes.TextAttribute;

public class AddImageWizard extends WizardDialog {

	public AddImageWizard(final MCommand<Image> r) {
		final XrayImage xray = new XrayImage();
		final UploadImageAttribute uploadImage = new UploadImageAttribute(
				MpDb.doc.Subsample_images);
		final GenericAttribute xray_attributes[] = {
				new TextAttribute(MpDb.doc.XrayImage_current,true,false,false),
				new TextAttribute(MpDb.doc.XrayImage_voltage,true,false,false),
				new TextAttribute(MpDb.doc.XrayImage_dwelltime,true,false,false),
				new TextAttribute(MpDb.doc.XrayImage_element),
		};

		final DetailsPanel<XrayImage> p_xray = new DetailsPanel<XrayImage>(
				xray_attributes, new Button[] {});
		final MCommand<Object> notifier = new MCommand<Object>() {
			public void execute(final Object result) {
				if (result instanceof ImageType) {
					if (((ImageType) result).getImageType().contains("X-ray")) {
						p_xray.edit(xray);
						AddImageWizard.this.addStep(p_xray, 1,
								" X-ray Attributes");
						AddImageWizard.this.enableNextButton(true);
						AddImageWizard.this.enableFinishButton(false);
					} else {
						AddImageWizard.this.removeStep(1);
						AddImageWizard.this.enableNextButton(false);
						AddImageWizard.this.enableFinishButton(true);
					}
				}
			}
		};
		final ListboxAttribute imageType = new ListboxAttribute(
				MpDb.doc.Image_imageType, notifier);
		final GenericAttribute image[] = {
				uploadImage, imageType,
				new TextAttribute(MpDb.doc.Image_collector),
				new TextAttribute(MpDb.doc.Image_scale,true,true,false)
		};
		final DetailsPanel<Image> p_image = new DetailsPanel<Image>(image,
				new Button[] {});
		p_image.edit(xray);

		final Command dialog_finish = new Command() {
			public void execute() {
				r.execute(uploadImage.getImage());
			}
		};

		this.addDialogFinishListener(dialog_finish);
		this.addStep(p_image, 0, "Upload Image");
	}

}
