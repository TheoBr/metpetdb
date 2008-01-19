package edu.rpi.metpetdb.client.ui.bulk.upload;

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
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.locale.LocaleHandler;
import edu.rpi.metpetdb.client.ui.Styles;
import edu.rpi.metpetdb.client.ui.widgets.MDialogBox;

public class BulkUploadPanel extends MDialogBox
		implements
			ClickListener {

	private final FormPanel fp;
	private final FileUpload fu;
	private final Button submit;
	private final VerticalPanel vp;
	private final Label error;
	private final HorizontalPanel hp;

	public BulkUploadPanel() {
		submit = new Button(LocaleHandler.lc_text.buttonUploadImage(), this);
		submit.setStyleName(Styles.PRIMARY_BUTTON);

		fp = new FormPanel();
		fp.setMethod(FormPanel.METHOD_POST);
		fp.setEncoding(FormPanel.ENCODING_MULTIPART);
		fp.setAction(GWT.getModuleBaseURL() + "/bulkUpload");

		fu = new FileUpload();
		fu.setName("bulkUpload");
		fu.setWidth("300");
		fu.setStyleName(Styles.REQUIRED_FIELD);

		hp = new HorizontalPanel();
		hp.add(submit);
		
		error = new Label("...");

		vp = new VerticalPanel();
		vp.add(error);
		vp.add(fu);
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
			}

			public void onSubmit(FormSubmitEvent event) {
				// This event is fired just before the form is submitted. We can
				// take
				// this opportunity to perform validation.
				if (fu.getFilename().length() == 0) {
					error.setText("Please select a file");
					fu.setStyleName(Styles.INVALID_REQUIRED_FIELD);
					event.setCancelled(true);
				}
			}
		});

		final FocusPanel f = new FocusPanel();
		f.setWidget(fp);

		setWidget(f);
	}

	protected void onLoad() {
		super.onLoad();
	}

	public void onClick(final Widget sender) {
		if (submit == sender)
			doUpload();
	}

	public void doUpload() {
		fp.submit();
	}

}
