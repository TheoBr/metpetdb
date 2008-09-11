package edu.rpi.metpetdb.client.ui.bulk.upload;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FileUpload;
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
import com.google.gwt.widgetideas.client.ProgressBar;

import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.locale.LocaleEntity;
import edu.rpi.metpetdb.client.locale.LocaleHandler;
import edu.rpi.metpetdb.client.ui.MpDb;
import edu.rpi.metpetdb.client.ui.ServerOp;
import edu.rpi.metpetdb.client.ui.CSS;
import edu.rpi.metpetdb.client.ui.dialogs.MDialogBox;

public class BulkUploadPanel extends MDialogBox implements ClickListener,
		FormHandler {

	private final FormPanel fp;
	private final FileUpload fu;
	private final Button submitButton;
	private final VerticalPanel vp;
	private final Label status;
	private final ProgressBar uploadProgress;
	private final HorizontalPanel hp;
	private final RadioButton samples;
	private final RadioButton analyses;
	private final RadioButton images;
	private final RadioButton justparse;
	private final RadioButton upload;
	private final Grid errorgrid;
	private final Grid headergrid;
	private final Grid additionsgrid;
	private final Label errorGridLabel;
	private final Label headerGridLabel;
	private final Label additionsGridLabel;

	private final Timer progressTimer;

	private static final LocaleEntity enttxt = LocaleHandler.lc_entity;

	public BulkUploadPanel() {
		submitButton = new Button(LocaleHandler.lc_text
				.buttonUploadSpreadsheet(), this);
		submitButton.setStyleName(CSS.PRIMARY_BUTTON);

		fp = new FormPanel();
		fp.setMethod(FormPanel.METHOD_POST);
		fp.setEncoding(FormPanel.ENCODING_MULTIPART);
		fp.setAction(GWT.getModuleBaseURL() + "/spreadsheetUpload");

		fu = new FileUpload();
		fu.setName("bulkUpload");
		fu.setWidth("300");
		fu.setStyleName(CSS.REQUIRED_FIELD);

		samples = new RadioButton("type", "Samples");
		samples.setChecked(true);
		analyses = new RadioButton("type", "Chemical Analyses");
		analyses.addStyleName("beta");
		images = new RadioButton("type", "Images");
		images.addStyleName("beta");
		hp = new HorizontalPanel();
		hp.add(samples);
		hp.add(analyses);
		hp.add(images);
		hp.add(submitButton);

		justparse = new RadioButton("submitType", "Parse");
		justparse.setChecked(true);
		upload = new RadioButton("submitType", "Commit");
		hp.add(justparse);
		hp.add(upload);

		status = new Label("...");

		errorgrid = new Grid();
		errorgrid.setBorderWidth(3);
		headergrid = new Grid();
		headergrid.setBorderWidth(3);
		additionsgrid = new Grid();
		additionsgrid.setBorderWidth(3);
		errorGridLabel = new Label("Errors Listed Below:");
		errorGridLabel.setStylePrimaryName("bold");
		headerGridLabel = new Label("Headers Listed Below:");
		headerGridLabel.setStylePrimaryName("bold");
		additionsGridLabel = new Label("Additions Listed Below:");
		additionsGridLabel.setStylePrimaryName("bold");

		uploadProgress = new ProgressBar();
		uploadProgress.setMinProgress(0);
		uploadProgress.setMaxProgress(1);
		progressTimer = new Timer() {
			public void run() {
				RequestBuilder builder = new RequestBuilder(RequestBuilder.GET,
						GWT.getModuleBaseURL() + "/spreadsheetUpload");
				try {
					builder.sendRequest("", new RequestCallback() {
						public void onError(Request request, Throwable exception) {
						}

						public void onResponseReceived(Request request,
								Response response) {
							uploadProgress.setProgress(Double
									.parseDouble(response.getText()));
						}
					});
				} catch (RequestException e) {
				}
			}
		};
		
		vp = new VerticalPanel();
		vp.add(status);
		vp.add(fp);
		vp.add(uploadProgress);
		vp.add(hp);
		vp.add(errorGridLabel);
		vp.add(errorgrid);
		vp.add(additionsGridLabel);
		vp.add(additionsgrid);
		vp.add(headerGridLabel);
		vp.add(headergrid);

		fp.setWidget(fu);

		fp.addFormHandler(this);

		setWidget(vp);
	}

	public void onClick(final Widget sender) {
		if (submitButton == sender)
			doUpload();
	}

	public void doUpload() {
		fp.submit();
		// Update progress every second
		progressTimer.scheduleRepeating(1000);
	}

	public void onSubmitComplete(final FormSubmitCompleteEvent event) {
		// When the form submission is successfully completed, this event is
		// fired. Assuming the service returned a response of type text/plain,
		// we can get the result text here (see the FormPanel documentation for
		// further explanation).
		final String results = event.getResults();
		progressTimer.cancel();
		uploadProgress.setProgress(1.0d);
		if (results != "NO-SCRIPT-DATA" && results != "") {
			status.setText("File uploaded successfully, please"
					+ " wait while it is saved to the database...");
			// TODO: This is bad! It's to strip the <pre> and </pre>
			// tags that get stuck on the string for some reason
			final String fileOnServer = results.substring(5,
					results.length() - 6);

			// What are we being asked to do? Choices are:
			// *upload => Actually perform the upload
			// *justparse => provide some feedback
			if (upload.isChecked()) {
				new ServerOp<Map<Integer, ValidationException>>() {
					public void begin() {
						if (samples.isChecked()) {
							MpDb.bulkUpload_svc.saveSamplesFromSpreadsheet(
									fileOnServer, this);
						} else if (analyses.isChecked()) {
							MpDb.bulkUploadChemicalAnalyses_svc
									.saveAnalysesFromSpreadsheet(fileOnServer,
											this);
						} else {
							MpDb.bulkUploadImages_svc.saveImagesFromZip(
									fileOnServer, this);
						}
					}

					public void onSuccess(
							final Map<Integer, ValidationException> result) {
						if (result == null)
							status.setText("Items Saved Successfully");
						else {
							errorgrid.resize(result.size() + 1, 2);
							int i = 1;
							errorgrid.setText(0, 0, "Spreadsheet Line:");
							errorgrid.setText(0, 1, "Error:");
							for (Map.Entry<Integer, ValidationException> e : result
									.entrySet()) {
								errorgrid.setText(i, 0, e.getKey().toString());
								errorgrid
										.setText(i++, 1, e.getValue().format());
							}
						}
					}
				}.begin();
			} else if (justparse.isChecked()) {
				// Parse Column Headers
				new ServerOp<Map<Integer, String[]>>() {
					public void begin() {
						if (samples.isChecked()) {
							MpDb.bulkUpload_svc.getHeaderMapping(fileOnServer,
									this);
						} else if (analyses.isChecked()) {
							MpDb.bulkUploadChemicalAnalyses_svc
									.getHeaderMapping(fileOnServer, this);
						} else {
							MpDb.bulkUploadImages_svc.getHeaderMapping(
									fileOnServer, this);
						}
					}

					public void onSuccess(final Map<Integer, String[]> headers) {
						if (headers == null) {
							status
									.setText("Spreadsheet Not Parsed Successfully");
						} else {
							status.setText("Spreadsheet Parsed Successfully");
							headergrid.resize(headers.size() + 1, 3);
							headergrid.setText(0, 0, "Location");
							headergrid.setText(0, 1, "Header Text");
							headergrid.setText(0, 2, "My Understanding");
							int i = 1;
							ArrayList<Integer> keys = new ArrayList<Integer>(
									headers.keySet());
							Collections.sort(keys);

							for (Integer k : keys) {
								headergrid.setText(i, 0, k.toString());
								headergrid.setText(i, 1, headers.get(k)[0]);
								if (headers.get(k)[1].length() > 0) {
									headergrid.setText(i, 2, enttxt
											.getString(headers.get(k)[1]));
								}
								i++;
							}

						}
					}
				}.begin();

				// Find New additions
				new ServerOp<Map<String, Integer[]>>() {
					public void begin() {
						if (samples.isChecked()) {
							MpDb.bulkUpload_svc
									.getAdditions(fileOnServer, this);
						} else if (analyses.isChecked()) {
							MpDb.bulkUploadChemicalAnalyses_svc.getAdditions(
									fileOnServer, this);
						} else {
							MpDb.bulkUploadImages_svc.getAdditions(
									fileOnServer, this);
						}
					}

					public void onSuccess(final Map<String, Integer[]> additions) {
						if (additions == null) {
							status
									.setText("Spreadsheet Not Parsed Successfully");
						} else {
							status.setText("Spreadsheet Parsed Successfully");

							additionsgrid.resize(additions.size() + 1, 4);
							additionsgrid.setText(0, 0, "Type");
							additionsgrid.setText(0, 1, "Invalid");
							additionsgrid.setText(0, 2, "New");
							additionsgrid.setText(0, 3, "Old");
							ArrayList<String> keys = new ArrayList<String>(
									additions.keySet());

							int i = 1;
							for (String k : keys) {
								additionsgrid
										.setText(i, 0, enttxt.getString(k));
								additionsgrid.setText(i, 1, additions.get(k)[0]
										.toString());
								additionsgrid.setText(i, 2, additions.get(k)[1]
										.toString());
								additionsgrid.setText(i, 3, additions.get(k)[2]
										.toString());
								i++;
							}

						}
					}
				}.begin();
			}
		}
	}

	public void onSubmit(final FormSubmitEvent event) {
		// This event is fired just before the form is submitted. We can
		// take
		// this opportunity to perform validation.
		if (fu.getFilename().length() == 0) {
			status.setText("Please select a file");
			fu.setStyleName(CSS.INVALID_REQUIRED_FIELD);
			event.setCancelled(true);
		}

		// Clear out grids from any previous runs
		errorgrid.resize(0, 0);
		headergrid.resize(0, 0);
		additionsgrid.resize(0, 0);
		uploadProgress.setProgress(0.0d);
	}

}
