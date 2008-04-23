package edu.rpi.metpetdb.client.ui.bulk.upload;

import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.FormHandler;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormSubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormSubmitEvent;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.locale.LocaleHandler;
import edu.rpi.metpetdb.client.ui.MpDb;
import edu.rpi.metpetdb.client.ui.ServerOp;
import edu.rpi.metpetdb.client.ui.Styles;
import edu.rpi.metpetdb.client.ui.dialogs.MDialogBox;

public class BulkUploadPanel extends MDialogBox implements ClickListener {

	private final FormPanel fp;
	private final FileUpload fu;
	private final Button upload;
	private final VerticalPanel vp;
	private final Label error;
	private final HorizontalPanel hp;
	private final RadioButton samples;
	private final RadioButton analyses;
	private final Grid grid;

	public BulkUploadPanel() {
		upload = new Button(LocaleHandler.lc_text.buttonUploadSpreadsheet(),
				this);
		upload.setStyleName(Styles.PRIMARY_BUTTON);

		fp = new FormPanel();
		fp.setMethod(FormPanel.METHOD_POST);
		fp.setEncoding(FormPanel.ENCODING_MULTIPART);
		fp.setAction(GWT.getModuleBaseURL() + "/spreadsheetUpload");

		fu = new FileUpload();
		fu.setName("bulkUpload");
		fu.setWidth("300");
		fu.setStyleName(Styles.REQUIRED_FIELD);

		samples = new RadioButton("type", "Samples");
		samples.setChecked(true);
		analyses = new RadioButton("type", "Chemical Analyses");
		hp = new HorizontalPanel();
		hp.add(samples);
		hp.add(analyses);
		hp.add(upload);

		error = new Label("...");

		grid = new Grid();

		vp = new VerticalPanel();
		vp.add(error);
		vp.add(fu);
		vp.add(hp);
		vp.add(grid);

		fp.setWidget(vp);

		fp.addFormHandler(new FormHandler() {
			public void onSubmitComplete(final FormSubmitCompleteEvent event) {
				// When the form submission is successfully completed, this
				// event is
				// fired. Assuming the service returned a response of type
				// text/plain,
				// we can get the result text here (see the FormPanel
				// documentation for
				// further explanation).
				final String results = event.getResults();
				if (results != "NO-SCRIPT-DATA" && results != "") {
					error.setText("File uploaded successfully, please"
							+ " wait while it is saved to the database...");
					// TODO: This is bad! It's to strip the <pre> and </pre>
					// tags that get stuck on the string for some reason
					final String fileOnServer = results.substring(5, results
							.length() - 6);
					new ServerOp<Map<Integer, ValidationException>>() {
						public void begin() {
							if (samples.isChecked()) {
								MpDb.bulkUpload_svc.saveSamplesFromSpreadsheet(
										fileOnServer, this);
							} else {
								// TODO: chemical analyses
								MpDb.bulkUploadChemicalAnalyses_svc
										.saveAnalysesFromSpreadsheet(
												fileOnServer, this);
							}
						}

						public void onSuccess(
								final Map<Integer, ValidationException> result) {
							if (result == null)
								error.setText("Items Saved Successfully");
							else {
								grid.resize(result.size() + 1, 2);
								int i = 1;
								grid.setText(0, 0, "Spreadsheet Line:");
								grid.setText(0, 1, "Error:");
								for (Map.Entry<Integer, ValidationException> e : result
										.entrySet()) {
									grid.setText(i, 0, e.getKey().toString());
									grid.setText(i++, 1, e.getValue().format());
								}
							}
						}
					}.begin();
				}
			}

			public void onSubmit(final FormSubmitEvent event) {
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
		if (upload == sender)
			doUpload();
	}

	public void doUpload() {
		fp.submit();
	}

}
