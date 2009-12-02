package edu.rpi.metpetdb.client.ui.bulk.upload;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.MissingResourceException;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FormHandler;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormSubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormSubmitEvent;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.widgetideas.client.ProgressBar;

import edu.rpi.metpetdb.client.error.LoginRequiredException;
import edu.rpi.metpetdb.client.locale.LocaleEntity;
import edu.rpi.metpetdb.client.locale.LocaleHandler;
import edu.rpi.metpetdb.client.locale.LocaleText;
import edu.rpi.metpetdb.client.model.bulk.upload.BulkUploadError;
import edu.rpi.metpetdb.client.model.bulk.upload.BulkUploadHeader;
import edu.rpi.metpetdb.client.model.bulk.upload.BulkUploadResult;
import edu.rpi.metpetdb.client.model.bulk.upload.BulkUploadResultCount;
import edu.rpi.metpetdb.client.service.bulk.upload.BulkUploadServiceAsync;
import edu.rpi.metpetdb.client.ui.CSS;
import edu.rpi.metpetdb.client.ui.MpDb;
import edu.rpi.metpetdb.client.ui.TokenSpace;
import edu.rpi.metpetdb.client.ui.commands.ServerOp;
import edu.rpi.metpetdb.client.ui.widgets.MButton;
import edu.rpi.metpetdb.client.ui.widgets.MHtmlList;
import edu.rpi.metpetdb.client.ui.widgets.MLink;
import edu.rpi.metpetdb.client.ui.widgets.MText;
import edu.rpi.metpetdb.client.ui.widgets.panels.MNoticePanel;
import edu.rpi.metpetdb.client.ui.widgets.panels.MPagePanel;
import edu.rpi.metpetdb.client.ui.widgets.panels.MTabPanel;
import edu.rpi.metpetdb.client.ui.widgets.panels.MNoticePanel.NoticeType;

public class BulkUploadPanel extends MPagePanel implements FormHandler {

	private static final LocaleText lc_text = LocaleHandler.lc_text;
	private static final LocaleEntity lc_entity = LocaleHandler.lc_entity;
	private final MText desc = new MText(lc_text.bulkUpload_Desc(), "p");
	private final MLink help = new MLink(lc_text.bulkUpload_Help(),
			"docs/mpdb-bulkupload-help.pdf", true);

	private final FlowPanel main = new FlowPanel();
	private final FormPanel form = new FormPanel();
	private final FileUpload file = new FileUpload();
	private final MNoticePanel formMessage = new MNoticePanel();

	private final MTabPanel resultsPanel = new MTabPanel();
	private final MNoticePanel resultStatus = new MNoticePanel();

	private final FlowPanel progressContainer = new FlowPanel();
	private final ProgressBar uploadProgress = new ProgressBar();
	private final Timer progressTimer;
	private final MLink cancelProgress;

	private final MHtmlList uploadTypeList = new MHtmlList();
	private final RadioButton samplesRadio = new RadioButton("type", lc_text
			.bulkUpload_Samples());
	private final RadioButton analysesRadio = new RadioButton("type", lc_text
			.bulkUpload_ChemicalAnalyses());
	private final RadioButton imagesRadio = new RadioButton("type", lc_text
			.bulkUpload_Images());
	private final RadioButton referenceRadio = new RadioButton("type", lc_text
			.bulkUpload_References());

	private final MButton uploadButton = new MButton(lc_text
			.bulkUpload_Upload());
	private final MButton commitButton = new MButton(lc_text
			.bulkUpload_SubmitData());

	private final FlowPanel nextStepPanel = new FlowPanel();
	private final MLink resetLink = new MLink(lc_text.message_resetForm(),
			TokenSpace.bulkUpload);
	private final MText nextStepText = new MText(lc_text
			.bulkUpload_LooksGoodSoCommit(), "p");

	private final FlowPanel errorPanel = new FlowPanel();
	private final Grid errorGrid = new Grid();
	private final HTML errorTab = new HTML("Errors <span>0</span>");

	private final FlowPanel matchedColsPanel = new FlowPanel();
	private final Grid matchedColsGrid = new Grid();

	private final FlowPanel warningsPanel = new FlowPanel();
	private final HTML warningTab = new HTML("Warnings <span>0</span>");
	private final Grid warningGrid = new Grid();

	private final FlowPanel summaryPanel = new FlowPanel();
	private String contentType;
	private BulkUploadServiceAsync service;
	private String fileOnServer;

	{
		setStylePrimaryName(CSS.BULK_UPLOAD);
		setPageTitle("Bulk Upload");
		setPageDescription(desc);

		addPageActionItem(help);
		help.setStyleName(CSS.LINK_LARGE_ICON);
		help.addStyleName(CSS.LINK_INFO);
		help.setTitle("Instructional PDF on how to use Bulk Upload.");

		add(main);
		main.setStyleName(CSS.MAIN);

		main.add(formMessage);

		main.add(form);
		form.setMethod(FormPanel.METHOD_POST);
		form.setEncoding(FormPanel.ENCODING_MULTIPART);
		form.setAction(GWT.getModuleBaseURL() + "/spreadsheetUpload");
		form.addFormHandler(this);
		form.setStyleName(CSS.BULK_UPLOAD_FORM);
		form.setWidget(file);
		file.setName("bulkUpload");
		file.getElement().setAttribute("size", "61");

		main.add(uploadTypeList);
		uploadTypeList.setStyleName(CSS.BULK_TYPES);
		uploadTypeList.add(samplesRadio);
		uploadTypeList.add(analysesRadio);
		uploadTypeList.add(imagesRadio);
		referenceRadio.setStyleName(CSS.BETA);
		uploadTypeList.add(referenceRadio);

		main.add(uploadButton);
		uploadButton.addClickListener(new ClickListener() {
			public void onClick(Widget sender) {
				updateUploadType();
				doUploadAndParse();
				commitButton.setEnabled(true);
			}
		});

		main.add(resultStatus);

		main.add(progressContainer);
		progressContainer.setStyleName(CSS.PROGRESSBAR_CONTAINER);

		cancelProgress = new MLink("Cancel", new ClickListener() {
			public void onClick(Widget sender) {
			// TODO Cancel progress
			}
		});
		progressContainer.add(cancelProgress);
		progressContainer.add(uploadProgress);
		uploadProgress.setMinProgress(0);
		uploadProgress.setMaxProgress(1);
		progressTimer = new Timer() {
			public void run() {
				RequestBuilder builder = new RequestBuilder(RequestBuilder.GET,
						GWT.getModuleBaseURL() + "//spreadsheetUpload");
				try {
					builder.sendRequest("", new RequestCallback() {
						public void onError(Request request, Throwable exception) {}
						public void onResponseReceived(Request request,
								Response response) {
							try {
								uploadProgress.setProgress(Double
										.parseDouble(response.getText()));
							} catch (Exception e) {

							}
						}
					});
				} catch (RequestException e) {}
			}
		};

		main.add(resultsPanel);
		resultsPanel.addStyleName(CSS.BULK_RESULTS);

		resultsPanel.add(summaryPanel, new MText("Summary", "div"));
		summaryPanel.setStyleName(CSS.BULK_RESULTS_SUMMARY);

		resultsPanel.add(matchedColsPanel, new MText("Matched Columns", "div"));
		matchedColsPanel.setStyleName(CSS.BULK_RESULTS_PARSED);

		resultsPanel.add(errorPanel, errorTab);
		errorPanel.setStyleName(CSS.BULK_RESULTS_ERRORS);

		resultsPanel.add(warningsPanel, warningTab);
		warningsPanel.setStyleName(CSS.BULK_RESULTS_WARNINGS);

		main.add(nextStepPanel);
		nextStepPanel.setStyleName(CSS.BULK_NEXTSTEP);
		nextStepPanel.add(nextStepText);
		nextStepPanel.add(commitButton);
		commitButton.addStyleName(CSS.SUBMIT);
		commitButton.addClickListener(new ClickListener() {
			public void onClick(Widget sender) {
				doCommit();
			}
		});
		
		resetLink.addClickListener(new ClickListener() {
			public void onClick(Widget sender){
				clearResults();
			}
		});

		nextStepPanel.add(resetLink);
	}

	public BulkUploadPanel() {
		samplesRadio.setChecked(true);
		updateUploadType();
		resultsPanel.selectTab(0);
		clearResults();
	}

	private void doUploadAndParse() {
		form.submit();
	}

	public void onSubmitComplete(final FormSubmitCompleteEvent event) {
		final String results = event.getResults();
		progressTimer.cancel();
		uploadProgress.setProgress(1.0d);
		if (results != "NO-SCRIPT-DATA" && results != "") {
			// TODO: This is bad! It's to strip the <pre> and </pre>
			// tags that get stuck on the string for some reason
			fileOnServer = results.replaceAll("<.*?>", "");
			resultStatus.sendNotice(NoticeType.WORKING,
					"Upload complete. Parsing " + contentType
							+ ", please wait...");
			parse();
		}
	}

	public void onSubmit(final FormSubmitEvent event) {
		// This event is fired just before the form is submitted. We can
		// take this opportunity to perform validation.

		// TODO check valid file types and send warning notice if invalid
		clearResults();
		if (file.getFilename().length() == 0) {
			formMessage.sendNotice(NoticeType.WARNING, "Please select a file");
			file.setStyleName(CSS.INVALID);
			event.setCancelled(true);
		} else {
			uploadButton.setText("Parsing...");
			uploadButton.setEnabled(false);
			show(progressContainer);
			progressTimer.scheduleRepeating(3000);
		}

	}

	public void updateUploadType() {
		if (samplesRadio.isChecked()) {
			updateContentType(samplesRadio.getText());
			service = MpDb.bulkUploadSamples_svc;
		} else if (analysesRadio.isChecked()) {
			updateContentType(analysesRadio.getText());
			service = MpDb.bulkUploadChemicalAnalyses_svc;
		} else if (imagesRadio.isChecked()) {
			updateContentType(imagesRadio.getText());
			service = MpDb.bulkUploadImages_svc;
		} else if (referenceRadio.isChecked()) {
			updateContentType(referenceRadio.getText());
			service = MpDb.bulkUploadReferences_svc;
		}
	}

	private void updateContentType(String type) {
		contentType = type;
	}

	private void doCommit() {
		resultStatus.sendNotice(NoticeType.WORKING, "Submitting " + contentType
				+ " to MetPetDB. This may take a few moments...");
		commitButton.setText("Submitting data...");
		commitButton.setEnabled(false);
		new ServerOp<BulkUploadResult>() {
			public void begin() {
				service.parser(fileOnServer, true, this);
			}

			public void onSuccess(BulkUploadResult result) {
				hide(progressContainer);
				commitButton.setText(lc_text.bulkUpload_SubmitData());
				handleCommitErrors(result);
			}
			public void onFailure(final Throwable e) {
				hide(progressContainer);
				// TODO better error handling
				resultStatus.sendNotice(NoticeType.ERROR,
						"There was an error submitting the data.");
				nextStepText
						.setText("Please submit a bug report to the developers. We are very sorry for the inconvenience.");
				resetLink.setText("Reset the form");
				commitButton.setText(lc_text.bulkUpload_SubmitData());
				commitButton.setEnabled(true);
				show(nextStepPanel);
			}
		}.begin();
	}

	private void parse() {
		new ServerOp<BulkUploadResult>() {
			public void begin() {
				service.parser(fileOnServer, false, this);
			}

			public void onSuccess(final BulkUploadResult results) {
				hide(progressContainer);
				resultStatus.hide();
				uploadButton.setText("Parse File for Upload");
				uploadButton.setEnabled(true);
				if (results.getHeaders() != null)
					populateMatchedColsPanel(results.getHeaders());
				populateSummaryPanel(results.getResultCounts(), results
						.getErrors().size());
				handleParseErrors(results);
				show(resultsPanel);
				show(nextStepPanel);
			}
			public void onFailure(final Throwable e) {
				if (e instanceof LoginRequiredException)
					super.onFailure(e);
				else {
					e.printStackTrace();
					hide(progressContainer);
					uploadButton.setText("Parse File for Upload");
					uploadButton.setEnabled(true);
					// TODO better error handling
					resultStatus.sendNotice(NoticeType.ERROR,
							"There was an error parsing.");
					nextStepText
							.setText("Please submit a bug report to the developers. We are very sorry for the inconvenience.");
					show(nextStepPanel);
				}
			}
		}.begin();
	}

	private void handleParseErrors(final BulkUploadResult results) {
		final Map<Integer, List<BulkUploadError>> errors = results.getErrors();
		populateErrorPanel(errors);
		populateWarningPanel(results.getWarnings());
		if (!errors.isEmpty()) {
			resultStatus.sendNotice(NoticeType.WARNING,
					"Parse complete, but the file contains errors.");
			nextStepText.setText("Please fix the errors and re-upload.");
			resultsPanel.selectTab(2);
		} else {
			resultStatus.sendNotice(NoticeType.SUCCESS,
					"Parsing completed successfully. <strong>Your data is not in MetPetDB yet</strong>. " +
					"Please review the results below and click <strong>Submit</strong> if everything is correct.");
			nextStepText
					.setText("Looks good! Remember to double-check the matched columns before submitting.");
			resultsPanel.selectTab(1);
			show(commitButton);
		}
	}

	private void handleCommitErrors(final BulkUploadResult results) {
		final Map<Integer, List<BulkUploadError>> errors = results.getErrors();
		populateErrorPanel(errors);
		populateWarningPanel(results.getWarnings());
		if (!errors.isEmpty()) {
			resultStatus.sendNotice(NoticeType.WARNING, "Could not add "
					+ getPlural(contentType) + " to MetPetDB.");
			nextStepText.setText("Please fix the errors and re-upload.");
			commitButton.setText(lc_text.bulkUpload_SubmitData());
			commitButton.setEnabled(true);
			resetLink.setText("Reset the form");
			resultsPanel.selectTab(2);
			show(nextStepPanel);
		} else {
			resultStatus.sendNotice(NoticeType.SUCCESS, "Successfully added "
					+ getPlural(contentType) + " to MetPetDB.");
			hide(resultsPanel);
			hide(nextStepPanel);
		}
	}

	public void clearResults() {
		errorGrid.resize(0, 0);
		warningGrid.resize(0, 0);
		matchedColsGrid.resize(0, 0);
		summaryPanel.clear();
		uploadProgress.setProgress(0.0d);
		hide(resultsPanel);
		hide(nextStepPanel);
		hide(commitButton);
		formMessage.hide();
		resultStatus.hide();
		hide(progressContainer);
	}

	private void populateMatchedColsPanel(
			final Map<Integer, BulkUploadHeader> headers) {
		matchedColsPanel.clear();
		matchedColsPanel.add(new MText(
				"Below is a list of columns from the uploaded spreadsheet "
						+ "and how MetPetDB understands them.", "p"));
		matchedColsPanel.add(matchedColsGrid);

		ArrayList<Integer> keys = new ArrayList<Integer>(headers.keySet());
		matchedColsGrid.resize(keys.size() + 1, 3);
		matchedColsGrid.setText(0, 1, "Spreadsheet");
		matchedColsGrid.setText(0, 2, "MetPetDB");
		matchedColsGrid.getRowFormatter().setStyleName(0, CSS.TYPE_SMALL_CAPS);

		int i = 1;
		Collections.sort(keys);
		for (Integer k : keys) {
			matchedColsGrid.setText(i, 0, (k + 1) + ".");
			matchedColsGrid.getCellFormatter().setStyleName(i, 0,
					CSS.BULK_RESULTS_SSCOLNUM);

			matchedColsGrid.setWidget(i, 1, new HTML(headers.get(k)
					.getHeaderText()));
			matchedColsGrid.getCellFormatter().setStyleName(i, 1,
					CSS.BULK_RESULTS_SSCOL);

			String matched = "";
			if (headers.get(k).getInterpretedHeaderText().length() > 0) {
				try {
					matched = lc_entity.getString(headers.get(k)
							.getInterpretedHeaderText());
				} catch (MissingResourceException mre) {
					matched = headers.get(k).getInterpretedHeaderText()
							.toString();
				}
				matchedColsGrid.getCellFormatter().setStyleName(i, 2,
						CSS.BULK_RESULTS_MPDBCOL);
			} else {
				matched = "--";
				matchedColsGrid.getCellFormatter()
						.addStyleName(i, 2, CSS.EMPTY);
			}
			matchedColsGrid.setText(i, 2, matched);

			i++;
		}
	}

	private void populateSummaryPanel(
			final Map<String, BulkUploadResultCount> additions, int numErrors) {
		summaryPanel.clear();
		if (additions != null) {
			final Iterator<String> objItr = additions.keySet().iterator();
			while (objItr.hasNext()) {
				final String objType = objItr.next();
				final int fresh = additions.get(objType).getFresh();
				final int dup = additions.get(objType).getDuplicate();
				final int invalid = additions.get(objType).getInvalid();
				if (fresh > 0)
					summaryPanel.add(new MText("Found " + fresh + " new "
							+ getPlural(objType, fresh) + ".", "p"));
				if (dup > 0)
					summaryPanel.add(new MText("Found " + dup + " duplicate "
							+ getPlural(objType, dup) + ".", "p"));
				if (invalid > 0)
					summaryPanel.add(new MText("Found " + invalid + " invalid "
							+ getPlural(objType, invalid) + ".", "p"));
			}
		}
	}

	private void populateErrorPanel(Map<Integer, List<BulkUploadError>> errors) {
		populatePanel(errors, errorPanel, errorGrid, "error", "errors");
		if (errors != null) {
			setErrorTabStyle(errors.size());
		}
	}
	
	private void populateWarningPanel(Map<Integer, List<BulkUploadError>> warnings) {
		populatePanel(warnings, warningsPanel, warningGrid, "warning", "warnings");
		if (warnings != null) {
			setWarningTabStyle(warnings.size());
		}
	}

	private void populatePanel(Map<Integer, List<BulkUploadError>> messages,
			FlowPanel panel, Grid grid, String singular, String plural) {
		if (messages != null) {
			panel.clear();
			String msg = "There were " + messages.size() + " " +  plural + ":";
			if (messages.size() == 1)
				msg = "There was one " + singular + ":";
			if (messages.size() == 0)
				msg = "No " + plural + ". Congratulations!";
			panel.add(new MText(msg, "p"));
			if (!messages.isEmpty()) {
				panel.add(grid);
				grid.resize(messages.size() + 1, 2);
				grid.getRowFormatter().setStyleName(0, CSS.TYPE_SMALL_CAPS);

				int i = 0;
				grid.setText(0, 0, "Cell");
				grid.setText(0, 1, singular + " Message");
				final List<Integer> rowNumbers = new ArrayList<Integer>();
				rowNumbers.addAll(messages.keySet());
				Collections.sort(rowNumbers);
				for (Integer row : rowNumbers) {
					if (row == -1)
						grid.setHTML(++i, 0, "Unknown");
					else
						grid.setHTML(++i, 0, "<span class=\"cell\">"+((BulkUploadError) messages.get(row).get(0)).getColumn()+"</span>");
					grid.setHTML(i, 1, explode(messages.get(row)));
				}
			}
		}
	}

	private String explode(final Collection<BulkUploadError> exceptions) {
		String text = "";
		if (exceptions != null) {
			final Iterator<BulkUploadError> itr = exceptions.iterator();
			while (itr.hasNext()) {
				final BulkUploadError err = itr.next();
				if (err.getException() != null)
					text += "<span class=\"error-message\">" + err.getException().format() + "</span>";
				if (err.getCellData() != null && err.getCellData().length() > 0)
					text += "<span class=\"cell-contents\">Cell Contents: " + err.getCellData() + "</span>";
			}
		}
		return text;
	}

	private void setErrorTabStyle(int numErrors) {
		errorTab.setHTML("Errors <span>" + numErrors + "</span>");
		if (numErrors > 0) {
			errorTab.addStyleName("has-errors");
			errorTab.removeStyleName(CSS.EMPTY);
		} else {
			errorTab.removeStyleName("has-errors");
			errorTab.addStyleName(CSS.EMPTY);
		}
	}
	
	private void setWarningTabStyle(int numErrors) {
		warningTab.setHTML("Warnings <span>" + numErrors + "</span>");
		if (numErrors > 0) {
			warningTab.addStyleName("has-warnings");
			warningTab.removeStyleName(CSS.EMPTY);
		} else {
			warningTab.removeStyleName("has-warnings");
			warningTab.addStyleName(CSS.EMPTY);
		}
	}

	private String getPlural(String in) {
		return getPlural(in, 0);
	}
	private String getPlural(String in, int num) {
		String plural = in;
		if (num != 1) {
			if (in.equalsIgnoreCase("Sample"))
				plural = "Samples";
			if (in.equalsIgnoreCase("Chemical Analysis"))
				plural = "Chemical Analyses";
			if (in.equalsIgnoreCase("Image"))
				plural = "Images";
		}
		return plural.toLowerCase();
	}

}
