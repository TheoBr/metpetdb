package edu.rpi.metpetdb.client.ui.image.browser.dialogs;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.FormHandler;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormSubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormSubmitEvent;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.locale.LocaleHandler;
import edu.rpi.metpetdb.client.ui.ServerOp;
import edu.rpi.metpetdb.client.ui.CSS;
import edu.rpi.metpetdb.client.ui.dialogs.MDialogBox;

public class AddImageDialog extends MDialogBox implements ClickListener,
		KeyboardListener {

	private final ServerOp continuation;
	private final FormPanel fp;
	private final FileUpload fu;
	private final Button submit;
	private final Button cancel;
	private final VerticalPanel vp;
	private final Label error;
	private final HorizontalPanel hp;
	private final ListBox imageTypes;

	private static String[] IMAGE_TYPES = {
			"Transmitted Unpolarized", "Transmitted Plane polarized",
			"Transmitted Crossed Polars", "Reflected Unpolarized",
			"Reflected Plane polarized", "Reflected Crossed Polars",
			"SE Secondary Electron", "BSE Backscattered Electron",
			"CL Cathodoluminescence", "X-ray",
	};

	public AddImageDialog(final ServerOp r) {
		continuation = r;
		submit = new Button(LocaleHandler.lc_text.buttonUploadImage(), this);
		submit.setStyleName(CSS.PRIMARY_BUTTON);

		cancel = new Button(LocaleHandler.lc_text.buttonCancel(), this);
		cancel.setStyleName(CSS.SECONDARY_BUTTON);

		fp = new FormPanel();
		fp.setMethod(FormPanel.METHOD_POST);
		fp.setEncoding(FormPanel.ENCODING_MULTIPART);
		fp.setAction(GWT.getModuleBaseURL() + "/imageUpload");

		fu = new FileUpload();
		fu.setName("imageUpload");
		fu.setWidth("300");
		fu.setStyleName(CSS.REQUIRED_FIELD);

		error = new Label("Upload an image");

		imageTypes = new ListBox();
		imageTypes.setVisibleItemCount(1);
		for (int i = 0; i < IMAGE_TYPES.length; ++i) {
			imageTypes.addItem(IMAGE_TYPES[i]);
		}

		hp = new HorizontalPanel();
		hp.add(submit);
		hp.add(cancel);

		vp = new VerticalPanel();
		vp.add(error);
		vp.add(fu);
		vp.add(imageTypes);
		vp.add(hp);

		fp.setWidget(vp);

		fp.addFormHandler(new FormHandler() {
			public void onSubmitComplete(FormSubmitCompleteEvent event) {
				// When the form submission is successfully completed, this
				// event is
				// fired. Assuming the service returned a response of type
				// text/plain,
				// we can get the result text here (see the FormPanel
				// documentation for
				// further explanation).
				error.setText(event.getResults());
				hide();
				if (continuation != null)
					continuation.onSuccess(event.getResults());
			}

			public void onSubmit(FormSubmitEvent event) {
				// This event is fired just before the form is submitted. We can
				// take
				// this opportunity to perform validation.
				if (fu.getFilename().length() == 0) {
					error.setText("Please select a file");
					fu.setStyleName(CSS.INVALID_REQUIRED_FIELD);
					event.setCancelled(true);
				}
			}
		});

		final FocusPanel f = new FocusPanel();
		f.addKeyboardListener(this);
		f.setWidget(fp);

		setWidget(f);
		this.setStyleName(CSS.ADD_IMAGE_DIALOG);
	}

	protected void onLoad() {
		super.onLoad();
	}

	public void onClick(final Widget sender) {
		if (submit == sender)
			doUpload();
		if (cancel == sender)
			cancel();
	}

	public void doUpload() {
		fp.submit();
	}

	public void onKeyPress(final Widget sender, final char kc, final int mod) {

	}

	public void onKeyDown(final Widget sender, final char kc, final int mod) {
	}

	public void onKeyUp(final Widget sender, final char kc, final int mod) {
	}

	private void cancel() {
		hide();
		if (continuation != null)
			continuation.cancel();
	}

}
