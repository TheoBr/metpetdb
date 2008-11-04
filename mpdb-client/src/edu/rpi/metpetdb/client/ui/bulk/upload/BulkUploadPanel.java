package edu.rpi.metpetdb.client.ui.bulk.upload;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.MissingResourceException;

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
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FormHandler;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormSubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormSubmitEvent;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.widgetideas.client.ProgressBar;

import edu.rpi.metpetdb.client.error.DAOException;
import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.locale.LocaleEntity;
import edu.rpi.metpetdb.client.locale.LocaleHandler;
import edu.rpi.metpetdb.client.model.BulkUploadResult;
import edu.rpi.metpetdb.client.model.BulkUploadResultCount;
import edu.rpi.metpetdb.client.service.bulk.upload.BulkUploadServiceAsync;
import edu.rpi.metpetdb.client.ui.CSS;
import edu.rpi.metpetdb.client.ui.MpDb;
import edu.rpi.metpetdb.client.ui.ServerOp;
import edu.rpi.metpetdb.client.ui.TokenSpace;
import edu.rpi.metpetdb.client.ui.VoidServerOp;
import edu.rpi.metpetdb.client.ui.widgets.MButton;
import edu.rpi.metpetdb.client.ui.widgets.MHtmlList;
import edu.rpi.metpetdb.client.ui.widgets.MLink;
import edu.rpi.metpetdb.client.ui.widgets.MNoticePanel;
import edu.rpi.metpetdb.client.ui.widgets.MPagePanel;
import edu.rpi.metpetdb.client.ui.widgets.MTabPanel;
import edu.rpi.metpetdb.client.ui.widgets.MText;
import edu.rpi.metpetdb.client.ui.widgets.MNoticePanel.NoticeType;

public class BulkUploadPanel extends MPagePanel implements ClickListener,
		FormHandler {

	private final MText desc = new MText("Upload collections of data using the form below.", "p");
	private final MLink help = new MLink("Bulk Upload Instructions (PDF)",
			"docs/mpdb-bulkupload-help.pdf", true);

	private final FlowPanel main = new FlowPanel();
	private final FormPanel form = new FormPanel();
	private final FileUpload file = new FileUpload();
	private final MNoticePanel formMessage = new MNoticePanel();
	
	private final MTabPanel resultsPanel = new MTabPanel();
	private final MNoticePanel resultStatus = new MNoticePanel();

	private final FlowPanel progressContainer = new FlowPanel();
	private final ProgressBar uploadProgress = new ProgressBar();
	private final MNoticePanel uploadStatus = new MNoticePanel();
	private final Timer progressTimer;
	private final MButton cancelProgress = new MButton("Cancel", this);

	private final MHtmlList uploadTypeList = new MHtmlList();
	private final RadioButton samplesRadio = new RadioButton("type", "Samples");
	private final RadioButton analysesRadio = new RadioButton("type", "Chemical Analyses");
	private final RadioButton imagesRadio = new RadioButton("type", "Images");

	private final MButton uploadButton = new MButton("Upload", this);
	private final MButton commitButton = new MButton("Submit Data", this);

	private final FlowPanel nextStepPanel = new FlowPanel();
	private final MLink resetLink = new MLink("Reset the form", TokenSpace.bulkUpload);
	private final MText nextStepText = new MText("Looks good, go ahead and commit!", "p");

	private final FlowPanel errorPanel = new FlowPanel();
	private final Grid errorGrid = new Grid();
	private final HTML errorTab = new HTML("Errors <span>0</span>");

	private final FlowPanel matchedColsPanel = new FlowPanel();
	private final Grid matchedColsGrid = new Grid();
	
	private final FlowPanel summaryPanel = new FlowPanel();
	private String contentType;
	private BulkUploadServiceAsync service;
	private static final LocaleEntity enttxt = LocaleHandler.lc_entity;
	private String fileOnServer;

	public BulkUploadPanel() {
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
		samplesRadio.setChecked(true);
		samplesRadio.addClickListener(this);
		updateUploadType();

		uploadTypeList.add(analysesRadio);
		analysesRadio.addClickListener(this);

		uploadTypeList.add(imagesRadio);
		imagesRadio.addClickListener(this);

		main.add(uploadButton);

		main.add(uploadStatus);
		main.add(progressContainer);
		progressContainer.setStyleName(CSS.PROGRESSBAR_CONTAINER);
		hide(progressContainer);

		progressContainer.add(uploadProgress);
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
		progressContainer.add(cancelProgress);
		
		main.add(resultStatus);

		main.add(resultsPanel);
		resultsPanel.addStyleName(CSS.BULK_RESULTS);
		hide(resultsPanel);

		resultsPanel.add(summaryPanel, new MText("Summary", "div"));
		summaryPanel.setStyleName(CSS.BULK_RESULTS_SUMMARY);
		resultsPanel.selectTab(0);
		
		resultsPanel.add(matchedColsPanel, new MText("Matched Columns", "div"));
		matchedColsPanel.setStyleName(CSS.BULK_RESULTS_PARSED);
		
		resultsPanel.add(errorPanel, errorTab);
		errorPanel.setStyleName(CSS.BULK_RESULTS_ERRORS);

		main.add(nextStepPanel);
		nextStepPanel.setStyleName(CSS.BULK_NEXTSTEP);
		nextStepPanel.add(nextStepText);
		nextStepPanel.add(commitButton);
		commitButton.addStyleName(CSS.PRIMARY_BUTTON);
		hide(commitButton);
		nextStepPanel.add(resetLink);
		hide(nextStepPanel);
		
		clearResults();
	}

	public void onClick(final Widget sender) {
		if (commitButton == sender) {
			doCommit();
		} else if (uploadButton == sender) {
			commitButton.setText("Upload " + contentType);
			doUploadAndParse();
		} else {
			updateUploadType();
		}
	}

	public void onSubmitComplete(final FormSubmitCompleteEvent event) {
		final String results = event.getResults();
		progressTimer.cancel();
		uploadProgress.setProgress(1.0d);
		if (results != "NO-SCRIPT-DATA" && results != "") {
			// TODO: This is bad! It's to strip the <pre> and </pre>
			// tags that get stuck on the string for some reason
			fileOnServer = results.substring(5, results.length() - 6);
			uploadStatus.sendNotice(NoticeType.WORKING, "Upload complete. Parsing " + contentType
					+ ", please wait...");
			parse();
		}
	}

	public void onSubmit(final FormSubmitEvent event) {
		// This event is fired just before the form is submitted. We can
		// take this opportunity to perform validation.
		if (file.getFilename().length() == 0) {
			formMessage.sendNotice(NoticeType.WARNING, "Please select a file");
			file.setStyleName(CSS.INVALID);
			event.setCancelled(true);
		}
		clearResults();
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
		}
	}

	private void updateContentType(String type) {
		contentType = type;
	}

	private void doCommit() {
		resultStatus.sendNotice(NoticeType.WORKING, "Submitting " + contentType
				+ " to MetPetDB. Please wait...");
		commitButton.setText("Submitting data...");
		commitButton.setEnabled(false);
		new VoidServerOp() {
			public void begin() {
				service.commit(fileOnServer, this);
			}

			public void onSuccess() {
				resultStatus.sendNotice(NoticeType.SUCCESS, contentType
						+ " added to MetPetDB.");
				resetLink.setText("Upload more data");
				show(nextStepPanel);
			}
			public void onFailure(final Throwable e) {
				// TODO better error handling
				resultStatus.sendNotice(NoticeType.ERROR, "There was an error submitting the data.");
				nextStepText.setText("Please submit a bug report to the developers. We are very sorry for the inconvenience. ");
				resetLink.setText("Reset the form");
				show(nextStepPanel);
			}
		}.begin();
	}
	
	private void doUploadAndParse() {
		form.submit();
		clearResults();
		uploadButton.setText("Uploading...");
		uploadButton.setEnabled(false);
		show(progressContainer);
		progressTimer.scheduleRepeating(3000);
	}
	
	private void parse() {
		new ServerOp<BulkUploadResult>() {
			public void begin() {
				service.parser(fileOnServer, this);
			}

			public void onSuccess(final BulkUploadResult results) {
				hide(progressContainer);
				uploadStatus.hide();
				uploadButton.setText("Upload");
				uploadButton.setEnabled(true);
				populateMatchedColsPanel(results.getHeaders());
				final Map<Integer, ValidationException> errors = results.getErrors();
				populateSummaryPanel(results.getResultCounts(), errors.size());
				if (!errors.isEmpty()) {
					populateErrorPanel(errors);
					resultStatus.sendNotice(NoticeType.WARNING, "Upload complete, but the file contains errors.");
					nextStepText.setText("Please fix the errors and re-upload.");
					resultsPanel.selectTab(2);
				} else {
					setErrorTabStyle(0);
					resultStatus.sendNotice(NoticeType.SUCCESS, "Upload complete.");
					nextStepText.setText("Looks good! Remember to double-check the matched columns before submitting.");
					resultsPanel.selectTab(0);
					show(commitButton);
				}
				show(resultsPanel);
				show(nextStepPanel);
			}
			public void onFailure(final Throwable e) {
				e.printStackTrace();
				hide(progressContainer);
				uploadButton.setText("Upload");
				uploadButton.setEnabled(true);
				uploadStatus.hide();
				// TODO better error handling
				resultStatus.sendNotice(NoticeType.ERROR, "There was an error parsing.");
				nextStepText.setText("Please submit a bug report to the developers. We are very sorry for the inconvenience. ");
				show(nextStepPanel);
			}
		}.begin();
	}

	public void clearResults() {
		errorGrid.resize(0, 0);
		matchedColsGrid.resize(0,0);
		summaryPanel.clear();
		uploadProgress.setProgress(0.0d);
		hide(resultsPanel);
		hide(nextStepPanel);
		hide(commitButton);
		uploadStatus.hide();
		formMessage.hide();
		resultStatus.hide();
		hide(progressContainer);
	}

	private void populateMatchedColsPanel(final Map<Integer, String[]> headers) {
		matchedColsPanel.clear();
		matchedColsPanel.add(new MText("Below is a list of columns from the uploaded spreadsheet " +
				"and how MetPetDB understands them.","p"));
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
			
			matchedColsGrid.setWidget(i, 1, new HTML(headers.get(k)[0]));
			matchedColsGrid.getCellFormatter().setStyleName(i, 1,
					CSS.BULK_RESULTS_SSCOL);

			String matched = "";
			if (headers.get(k)[1].length() > 0) {
				try {
					matched = enttxt.getString(headers.get(k)[1]);
				} catch (MissingResourceException mre) {
					matched = headers.get(k)[1].toString();
				}
				matchedColsGrid.getCellFormatter().setStyleName(i, 2,
						CSS.BULK_RESULTS_MPDBCOL);
			} else {
				matched = "--";
				matchedColsGrid.getCellFormatter().addStyleName(i, 2,
						CSS.EMPTY);
			}
			matchedColsGrid.setText(i, 2, matched);
			
			i++;
		}
	}

	private void populateSummaryPanel(final Map<String, BulkUploadResultCount> additions, int numErrors) {
		summaryPanel.clear();
		final Iterator<String> objItr = additions.keySet().iterator();
		while(objItr.hasNext()) {
			final String objType = objItr.next();
			final int fresh = additions.get(objType).getFresh();
			final int dup = additions.get(objType).getDuplicate();
			final int invalid = additions.get(objType).getInvalid();
			if (fresh > 0) summaryPanel.add(new MText("Found "+fresh+" new "+getPlural(objType, fresh)+".", "p"));
			if (dup > 0) summaryPanel.add(new MText("Found "+dup+" duplicate "+getPlural(objType, dup)+".", "p"));
			if (invalid > 0) summaryPanel.add(new MText("Found "+invalid+" invalid "+getPlural(objType, invalid)+".", "p"));
		}
		// TODO provide some summary of matched columns here
	}

	private void populateErrorPanel(Map<Integer, ValidationException> errors) {
		errorPanel.clear();
		String msg = "There were "+errors.size()+" errors:";
		if (errors.size() == 1) msg = "There was one error:";
		errorPanel.add(new MText(msg, "p"));
		errorPanel.add(errorGrid);
		errorGrid.resize(errors.size() + 1, 2);
		errorGrid.getRowFormatter().setStyleName(0, CSS.TYPE_SMALL_CAPS);
		
		int i = 0;
		errorGrid.setText(0, 0, "Row");
		errorGrid.setText(0, 1, "Error Message");
		// TODO sort errors by line number (key)
		for (Map.Entry<Integer, ValidationException> e : errors.entrySet()) {
			errorGrid.setText(++i, 0, e.getKey().toString());
			errorGrid.setText(i, 1, e.getValue().format());
		}
		setErrorTabStyle(i);
	}
	
	private void setErrorTabStyle(int numErrors) {
		errorTab.setHTML("Errors <span>"+numErrors+"</span>");
		if (numErrors > 0) {
			errorTab.addStyleName("has-errors");
			errorTab.removeStyleName(CSS.EMPTY);
		} else {
			errorTab.removeStyleName("has-errors");
			errorTab.addStyleName(CSS.EMPTY);
		}
	}
	
	private String getPlural(String in, int num) {
		String plural = in;
		if (num != 1) {
			if (in.equalsIgnoreCase("Sample")) plural = "Samples";
			if (in.equalsIgnoreCase("Chemical Analysis")) plural = "Chemical Analyses";
			if (in.equalsIgnoreCase("Image")) plural = "Images";
		}
		return plural.toLowerCase();
	}
	
}
