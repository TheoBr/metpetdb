package edu.rpi.metpetdb.client.ui.input.attributes.specific;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormHandler;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormSubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormSubmitEvent;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.error.validation.InvalidImageException;
import edu.rpi.metpetdb.client.model.ImageDTO;
import edu.rpi.metpetdb.client.model.MObjectDTO;
import edu.rpi.metpetdb.client.model.validation.ObjectConstraint;
import edu.rpi.metpetdb.client.ui.MpDb;
import edu.rpi.metpetdb.client.ui.ServerOp;
import edu.rpi.metpetdb.client.ui.CSS;
import edu.rpi.metpetdb.client.ui.input.CurrentError;
import edu.rpi.metpetdb.client.ui.input.attributes.GenericAttribute;

public class UploadImageAttribute extends GenericAttribute {

	private final FormPanel fp;
	private final FileUpload fu;
	private final Label error;
	private MObjectDTO obj;
	private ServerOp continuation;
	private boolean hasBeenUploaded = false;
	private boolean hasBeenErrored = false;
	private String previousFilename;

	public UploadImageAttribute(final ObjectConstraint ic) {
		super(ic);
		final VerticalPanel vp = new VerticalPanel();

		fp = new FormPanel();
		fp.setMethod(FormPanel.METHOD_POST);
		fp.setEncoding(FormPanel.ENCODING_MULTIPART);
		fp.setAction(GWT.getModuleBaseURL() + "/imageUpload");

		fu = new FileUpload();
		fu.setName("imageUpload");
		fu.setWidth("300");
		fu.setStyleName(CSS.REQUIRED_FIELD);

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
					final edu.rpi.metpetdb.client.model.ImageDTO image = (ImageDTO) obj;

					image.setChecksum(results.split(",")[0].trim());
					image.setChecksum64x64(results.split(",")[1].trim());
					image.setChecksumHalf(results.split(",")[2].trim());
					image.setWidth(Integer.parseInt(results.split(",")[3]
							.trim()));
					image.setHeight(Integer.parseInt(results.split(",")[4]
							.trim()));
					image.setFilename(results.split(",")[5].trim());
					error.setText("Image Upload Successfully");
					hasBeenUploaded = true;
					hasBeenErrored = false;
					if (continuation != null) {
						continuation.onSuccess(image);
					}
				} else {
					hasBeenUploaded = false;
					hasBeenErrored = true;
					continuation.onFailure(new InvalidImageException(results));
				}
			}

			public void onSubmit(FormSubmitEvent event) {
				if (previousFilename != null
						&& !previousFilename.equals(fu.getFilename())
						&& obj != null && !previousFilename.equals("")
						&& !hasBeenErrored) {
					// User uploaded a different image, so delete the old one
					new ServerOp() {
						public void begin() {
							MpDb.image_svc.delete((ImageDTO) obj, this);
						}
						public void onSuccess(final Object result) {

						}
					}.begin();
				} else if (hasBeenUploaded) {
					if (continuation != null)
						continuation.onSuccess(obj);
					event.setCancelled(true);
				} else {

				}
				if (fu.getFilename().length() == 0) {
					fu.setStyleName(CSS.INVALID_REQUIRED_FIELD);
					event.setCancelled(true);
					if (continuation != null) {
						continuation.onFailure(new InvalidImageException());
					}
				}
				previousFilename = fu.getFilename();
			}
		});
	}
	public void submitForm(final ServerOp r) {
		continuation = r;
		fp.submit();
	}

	public Widget[] createDisplayWidget(final MObjectDTO obj) {
		final Object image = get(obj);
		if (image != null) {
			return new Widget[] {
				new com.google.gwt.user.client.ui.Image(((ImageDTO) image)
						.get64x64ServerPath())
			};
		} else
			return new Widget[] {
				new Label("No Images")
			};
	}

	public Widget[] createEditWidget(final MObjectDTO obj, final String id) {
		this.obj = obj;
		return new Widget[] {
			fp
		};
	}

	public void commitEdit(final MObjectDTO obj, final Widget[] editWidget,
			final CurrentError err, final ServerOp r) {
		new ServerOp() {
			public void begin() {
				UploadImageAttribute.this.submitForm(this);
			}
			public void onSuccess(final Object result) {
				applyStyle(editWidget, true);
				err.setText("");
				r.onSuccess(null);
			}
			public void onFailure(final Throwable whybad) {
				r.onFailure((ValidationException) whybad);
				showError(editWidget, err, (ValidationException) whybad);
			}
		}.begin();

	}

	public void getStatus(final ServerOp r) {
		if (r != null) {
			if (obj != null)
				r.onSuccess(obj);
		}

	}

	protected Object get(final Widget editWidget) {
		return get(obj);
	}
	protected String get(final MObjectDTO obj) {
		return null;
	}
	protected void set(final MObjectDTO obj, final Object images) {

	}
}
