package edu.rpi.metpetdb.client.ui.input.attributes.specific.image;

import java.util.ArrayList;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.model.Image;
import edu.rpi.metpetdb.client.model.XrayImage;
import edu.rpi.metpetdb.client.model.interfaces.MObject;
import edu.rpi.metpetdb.client.model.validation.interfaces.HasValues;
import edu.rpi.metpetdb.client.ui.MpDb;
import edu.rpi.metpetdb.client.ui.commands.MCommand;
import edu.rpi.metpetdb.client.ui.input.DetailsPanel;
import edu.rpi.metpetdb.client.ui.input.WizardDialog;
import edu.rpi.metpetdb.client.ui.input.attributes.GenericAttribute;
import edu.rpi.metpetdb.client.ui.input.attributes.ListboxAttribute;
import edu.rpi.metpetdb.client.ui.input.attributes.TextAttribute;
import edu.rpi.metpetdb.client.ui.input.attributes.specific.sample.UnitTextAttribute;

/**
 * Provides an interface for uploading images and editing their meta data.
 * 
 * @author millib2, ?
 */
public class AddImageWizard extends WizardDialog {

	private XrayImage image;
	
	/**
	 * When this constructor is used the wizard will be used to edit an existing image.
	 * 
	 * @param image Image to be edited.
	 */
	public AddImageWizard(Image image) {
		this.image = (XrayImage) image;
		setupWizard(image.getImageType().getImageType().contains("X-Ray"), false, null);
	}
	
	/**
	 * When this constructor is used the wizard will be used to upload a new image.
	 * 
	 * @param r
	 */
	public AddImageWizard(boolean xray, final MCommand<Image> r) {
		image = new XrayImage();
		setupWizard(xray, true, r);
	}
	
	/**
	 * Adds the necessary steps and fields to the wizard.
	 * 
	 * @param xray
	 * @param newImage
	 * @param r
	 */
	public void setupWizard(boolean xray, boolean newImage, final MCommand<Image> r) {
		
		final XrayImage image = new XrayImage();
		final UploadImageAttribute uploadImage = new UploadImageAttribute(
				MpDb.doc.Subsample_images);
		
		final GenericAttribute imageAttributes[];
		final ListboxAttribute imageType;
		
		if (xray) {			
			imageType = new ListboxAttribute(MpDb.doc.Image_imageType, null) {			
				@Override
				public Widget[] createEditWidget(final MObject obj, final String id,
						final GenericAttribute attr) {
					if (attr.getConstraint() instanceof HasValues) {
						String selectedValue = get(obj);					

						// Filter all non X-ray options
						ArrayList newValues = new ArrayList();
						for (Object o : ((HasValues)attr.getConstraint()).getValues()) {
							if (o.toString().contains("X-ray"))
								newValues.add(o);
						}
						
						return new Widget[] {
							createListBox(newValues, id, selectedValue)
						};
					} else {
						throw new RuntimeException("Wrong Attribute for a list box");
					}
				}			
			};

			if (newImage) {
				imageAttributes = new GenericAttribute[] {
					uploadImage, imageType,
					new TextAttribute(MpDb.doc.Image_description),
					new TextAttribute(MpDb.doc.Image_collector),
					new UnitTextAttribute(MpDb.doc.Image_scale, "(millimeters in width)"),
					new TextAttribute(MpDb.doc.XrayImage_current,true,false,false),
					new TextAttribute(MpDb.doc.XrayImage_voltage,true,false,false),
					new TextAttribute(MpDb.doc.XrayImage_dwelltime,true,false,false),
					new TextAttribute(MpDb.doc.XrayImage_element)
				};
			}
			else {
				imageAttributes = new GenericAttribute[] {
					imageType,
					new TextAttribute(MpDb.doc.Image_description),
					new TextAttribute(MpDb.doc.Image_collector),
					new TextAttribute(MpDb.doc.Image_scale, true, true, false),
					new TextAttribute(MpDb.doc.XrayImage_current,true,false,false),
					new TextAttribute(MpDb.doc.XrayImage_voltage,true,false,false),
					new TextAttribute(MpDb.doc.XrayImage_dwelltime,true,false,false),
					new TextAttribute(MpDb.doc.XrayImage_element)
				};
			}
		}
		else {
			imageType = new ListboxAttribute(MpDb.doc.Image_imageType, null) {			
				@Override
				public Widget[] createEditWidget(final MObject obj, final String id,
						final GenericAttribute attr) {
					if (attr.getConstraint() instanceof HasValues) {
						String selectedValue = get(obj);					

						// Filter all non X-ray options
						ArrayList newValues = new ArrayList();
						for (Object o : ((HasValues)attr.getConstraint()).getValues()) {
							if (!o.toString().contains("X-ray"))
								newValues.add(o);
						}
						
						return new Widget[] {
							createListBox(newValues, id, selectedValue)
						};
					} else {
						throw new RuntimeException("Wrong Attribute for a list box");
					}
				}			
			};
			
			if (newImage) {
				imageAttributes = new GenericAttribute[] {
					uploadImage, imageType,
					new TextAttribute(MpDb.doc.Image_description),
					new TextAttribute(MpDb.doc.Image_collector),
					new TextAttribute(MpDb.doc.Image_scale, true, true, false)
				};
			}
			else {
				imageAttributes = new GenericAttribute[] {
					imageType,
					new TextAttribute(MpDb.doc.Image_description),
					new TextAttribute(MpDb.doc.Image_collector),
					new TextAttribute(MpDb.doc.Image_scale, true, true, false)
				};
			}
		}
		
		final DetailsPanel<Image> imagePanel = new DetailsPanel<Image>(imageAttributes,
				new Button[] {});
		imagePanel.edit(image);

		String wizardTitle;
		final Command dialog_finish;
		if (newImage) {
			dialog_finish = new Command() {
				public void execute() {
					r.execute(uploadImage.getImage());
				}
			};
			wizardTitle = "Upload Image";
		}
		else {
			dialog_finish = new Command() {
				public void execute() {
					imagePanel.validateEdit();
				}
			};
			wizardTitle = "Edit Image";
		}

		this.addDialogFinishListener(dialog_finish);
		this.addStep(imagePanel, 0, wizardTitle);
	}

}
