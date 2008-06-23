package edu.rpi.metpetdb.client.ui.bulk.upload;

import java.util.ArrayList;
import java.util.Collections;
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
import edu.rpi.metpetdb.client.locale.LocaleEntity;
import edu.rpi.metpetdb.client.locale.LocaleHandler;
import edu.rpi.metpetdb.client.ui.MpDb;
import edu.rpi.metpetdb.client.ui.ServerOp;
import edu.rpi.metpetdb.client.ui.Styles;
import edu.rpi.metpetdb.client.ui.dialogs.MDialogBox;

public class BulkUploadPanel extends MDialogBox implements ClickListener {

	private final FormPanel fp;
	private final FileUpload fu;
	private final Button submit_button;
	private final VerticalPanel vp;
	private final Label error;
	private final HorizontalPanel hp;
	private final RadioButton samples;
	private final RadioButton analyses;
	private final RadioButton justparse;
	private final RadioButton upload;
	private final Grid errorgrid;
	private final Grid headergrid;
	private final Label errorgrid_label;
	private final Label headergrid_label;

	private static final LocaleEntity enttxt = LocaleHandler.lc_entity;

	public BulkUploadPanel() {
		submit_button = new Button(LocaleHandler.lc_text
				.buttonUploadSpreadsheet(), this);
		submit_button.setStyleName(Styles.PRIMARY_BUTTON);

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
		analyses.addStyleName("beta");
		hp.add(analyses);
		hp.add(submit_button);

		justparse = new RadioButton("submitType", "Parse");
		justparse.setChecked(true);
		upload = new RadioButton("submitType", "Commit");
		hp.add(justparse);
		hp.add(upload);

		error = new Label("...");

		errorgrid = new Grid();
		errorgrid.setBorderWidth(3);
		headergrid = new Grid();
		headergrid.setBorderWidth(3);
		errorgrid_label = new Label("Errors Listed Below:");
		errorgrid_label.setStylePrimaryName("bold");
		headergrid_label = new Label("Headers Listed Below:");
		headergrid_label.setStylePrimaryName("bold");

		vp = new VerticalPanel();
		vp.add(error);
		vp.add(fu);
		vp.add(hp);
		vp.add(errorgrid_label);
		vp.add(errorgrid);
		vp.add(headergrid_label);
		vp.add(headergrid);

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

					// What are we being asked to do? Choices are:
					// *upload => Actually perform the upload
					// *justparse => provide some feedback
					if (upload.isChecked()) {
						new ServerOp<Map<Integer, ValidationException>>() {
							public void begin() {
								if (samples.isChecked()) {
									MpDb.bulkUpload_svc
											.saveSamplesFromSpreadsheet(
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
									errorgrid.resize(result.size() + 1, 2);
									int i = 1;
									errorgrid
											.setText(0, 0, "Spreadsheet Line:");
									errorgrid.setText(0, 1, "Error:");
									for (Map.Entry<Integer, ValidationException> e : result
											.entrySet()) {
										errorgrid.setText(i, 0, e.getKey()
												.toString());
										errorgrid.setText(i++, 1, e.getValue()
												.format());
									}
								}
							}
						}.begin();
					} else if (justparse.isChecked()) {
						new ServerOp<Map<Integer, String[]>>() {
							public void begin() {
								if (samples.isChecked()) {
									MpDb.bulkUpload_svc.getHeaderMapping(
											fileOnServer, this);
								} else {
									MpDb.bulkUploadChemicalAnalyses_svc
											.getHeaderMapping(fileOnServer,
													this);
								}
							}

							public void onSuccess(
									final Map<Integer, String[]> headers) {
								if (headers == null) {
									error
											.setText("Spreadsheet Not Parsed Successfully");
								} else {
									error
											.setText("Spreadsheet Parsed Successfully");
									headergrid.resize(headers.size() + 1, 3);
									headergrid.setText(0, 0, "Location");
									headergrid.setText(0, 1, "Header Text");
									headergrid
											.setText(0, 2, "My Understanding");
									int i = 1;
									ArrayList<Integer> keys = new ArrayList<Integer>(
											headers.keySet());
									Collections.sort(keys);

									for (Integer k : keys) {
										headergrid.setText(i, 0, k.toString());
										headergrid.setText(i, 1,
												headers.get(k)[0]);
										if (headers.get(k)[1].length() > 0) {
											headergrid.setText(i, 2,
													enttxt.getString(headers
															.get(k)[1]));
										}
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
					error.setText("Please select a file");
					fu.setStyleName(Styles.INVALID_REQUIRED_FIELD);
					event.setCancelled(true);
				}

				// Clear out grids from any previous runs
				errorgrid.resize(0, 0);
				headergrid.resize(0, 0);
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
		if (submit_button == sender)
			doUpload();
	}

	public void doUpload() {
		fp.submit();
	}

}
