package edu.rpi.metpetdb.client.ui.input.attributes.specific.image;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormHandler;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormSubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormSubmitEvent;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.error.UnableToUploadImageException;
import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.error.validation.InvalidImageException;
import edu.rpi.metpetdb.client.model.Image;
import edu.rpi.metpetdb.client.model.validation.ObjectConstraint;
import edu.rpi.metpetdb.client.ui.CSS;
import edu.rpi.metpetdb.client.ui.MpDb;
import edu.rpi.metpetdb.client.ui.commands.MCommand;
import edu.rpi.metpetdb.client.ui.commands.VoidServerOp;
import edu.rpi.metpetdb.client.ui.input.CurrentError;
import edu.rpi.metpetdb.client.ui.input.attributes.GenericAttribute;

public class UploadImageAttribute extends GenericAttribute<Image> {

	private final FormPanel fp;
	private final FileUpload fu;
	private final Label error;
	private Image obj;
	private MCommand<Image> continuation;
	private boolean hasBeenUploaded = false;
	private boolean hasBeenErrored = false;
	private String previousFilename;

	public UploadImageAttribute(final ObjectConstraint<Image> ic) {
		super(ic);
		final VerticalPanel vp = new VerticalPanel();

		fp = new FormPanel();
		fp.setMethod(FormPanel.METHOD_POST);
		fp.setEncoding(FormPanel.ENCODING_MULTIPART);
		fp.setAction(GWT.getModuleBaseURL() + "imageUpload");

		fu = new FileUpload();
		fu.setName("imageUpload");
		fu.setWidth("300");
		fu.setStyleName(CSS.REQUIRED);

		error = new Label("Upload an image");

		vp.add(error);
		vp.add(fu);

		fp.setWidget(vp);

		fp.addFormHandler(new FormHandler() {
			public void onSubmitComplete(FormSubmitCompleteEvent event) {
				String results = event.getResults();
				if (results.indexOf("{OK}") != -1) {
					results = results.substring(results.indexOf("[") + 1,
							results.indexOf("]"));
					final edu.rpi.metpetdb.client.model.Image image = (Image) obj;

					image.setChecksum(results.split(",")[0].trim());
					image.setChecksum64x64(results.split(",")[1].trim());
					image.setChecksumMobile(results.split(",")[2].trim());
					image.setChecksumHalf(results.split(",")[3].trim());
					image.setWidth(Integer.parseInt(results.split(",")[4]
							.trim()));
					image.setHeight(Integer.parseInt(results.split(",")[5]
							.trim()));
					image.setFilename(results.split(",")[6].trim());
					image.setOwner(MpDb.currentUser());
					error.setText("Image Upload Successfully");
					hasBeenUploaded = true;
					hasBeenErrored = false;
					if (continuation != null) {
						continuation.execute(image);
					}
				} else {
					continuation.onFailure(new UnableToUploadImageException(results));
					hasBeenUploaded = false;
					hasBeenErrored = true;
				}
			}

			public void onSubmit(FormSubmitEvent event) {
				if (previousFilename != null
						&& !previousFilename.equals(fu.getFilename())
						&& obj != null && !previousFilename.equals("")
						&& !hasBeenErrored) {
					// User uploaded a different image, so delete the old one
					new VoidServerOp() {
						public void begin() {
							MpDb.image_svc.delete(obj, this);
						}
						@Override
						public void onSuccess() {}
					}.begin();
				} else if (hasBeenUploaded) {
					continuation.execute(obj);
					event.setCancelled(true);
				} else {

				}
				if (fu.getFilename().length() == 0) {
					fu.setStyleName(CSS.INVALID);
					event.setCancelled(true);
					if (continuation != null) {
						continuation.onFailure(new InvalidImageException());
					}
				}
				previousFilename = fu.getFilename();
			}
		});
	}

	/**
	 * Upload image attribute doesn't display anything so we don't implement
	 * this method
	 */
	public Widget[] createDisplayWidget(final Image obj) {
		return new Widget[] {};
	}

	public Widget[] createEditWidget(final Image obj, final String id) {
		this.obj = obj;
		return new Widget[] {
			fp
		};
	}

	@Override
	public void commitEdit(final Image obj, final Widget[] editWidget,
			final CurrentError err, final Command r) {
		continuation = new MCommand<Image>() {
			public void onFailure(final Throwable whybad) {
				showError(editWidget, err, (ValidationException) whybad);
			}
			@Override
			public void execute(Image object) {
				applyStyle(editWidget, true);
				err.setText("");
				r.execute();
			}
		};
		fp.submit();
	}

	public Image getImage() {
		return obj;
	}

	protected Object get(final Widget editWidget) {
		return obj;
	}
	protected void set(final Image obj, final Object images) {

	}
}
