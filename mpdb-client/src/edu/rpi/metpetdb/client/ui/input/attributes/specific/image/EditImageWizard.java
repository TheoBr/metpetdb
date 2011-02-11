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

/**
 * Provides an interface for editing an image's meta data.
 * 
 * @author millib2
 */
public class EditImageWizard extends WizardDialog {

	private XrayImage image;
	
	/**
	 * When this constructor is used the wizard will be used to edit an existing image.
	 * 
	 * @param image Image to be edited.
	 */
	public EditImageWizard(Image image) {
		
		this.image = (XrayImage) image;
		
		final UploadImageAttribute uploadImage = new UploadImageAttribute(
				MpDb.doc.Subsample_images);
		
		final GenericAttribute xrayAttributes[] = {
				new TextAttribute(MpDb.doc.XrayImage_current,true,false,false),
				new TextAttribute(MpDb.doc.XrayImage_voltage,true,false,false),
				new TextAttribute(MpDb.doc.XrayImage_dwelltime,true,false,false),
				new TextAttribute(MpDb.doc.XrayImage_element),
		};

		final DetailsPanel<XrayImage> xrayPanel = new DetailsPanel<XrayImage>(
				xrayAttributes, new Button[] {});
		
		final MCommand<Object> notifier = new MCommand<Object>() {
			public void execute(final Object result) {
				if (result instanceof ImageType) {
					if (((ImageType) result).getImageType().contains("X-ray")) {
						EditImageWizard.this.removeStep(1);
						xrayPanel.edit(EditImageWizard.this.image);
						EditImageWizard.this.addStep(xrayPanel, 
								EditImageWizard.this.getPanelCount(),
								" X-ray Attributes");
						EditImageWizard.this.enableNextButton(true);
						EditImageWizard.this.enableFinishButton(false);
					} else {
						EditImageWizard.this.removeStep(1);
						EditImageWizard.this.enableNextButton(false);
						EditImageWizard.this.enableFinishButton(true);
					}
				}
			}
		};
		
		final ListboxAttribute imageType = new ListboxAttribute(
				MpDb.doc.Image_imageType, notifier);
		
		final GenericAttribute imageAttributes[] = {
			imageType,
			new TextAttribute(MpDb.doc.Image_description),
			new TextAttribute(MpDb.doc.Image_collector),
			new TextAttribute(MpDb.doc.Image_scale, true, true, false)
		};
		
		final DetailsPanel<Image> imagePanel = new DetailsPanel<Image>(imageAttributes,
				new Button[] {});
		imagePanel.edit(image);

		final Command dialog_finish = new Command() {
			public void execute() {
				//imagePanel.validateEdit();
				//xrayPanel.validateEdit();
			}
		};

		this.addDialogFinishListener(dialog_finish);
		this.addStep(imagePanel, 0, "Edit Image");
	}

}

