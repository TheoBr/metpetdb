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
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.widgetideas.client.ProgressBar;

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

	private final MNoticePanel status = new MNoticePanel();

	private final FlowPanel resultsPanel = new FlowPanel();
	private final MLink detailsLink = new MLink("Show details", this);

	private final FlowPanel progressContainer = new FlowPanel();
	private final ProgressBar uploadProgress = new ProgressBar();
	private final Timer progressTimer;
	private final MLink cancelLink = new MLink("Cancel", this);

	private final MHtmlList uploadTypeList = new MHtmlList();
	private final RadioButton samplesRadio = new RadioButton("type", "Samples");
	private final RadioButton analysesRadio = new RadioButton("type",
			"Chemical Analyses");
	private final RadioButton imagesRadio = new RadioButton("type", "Images");

	private final MButton parseButton = new MButton("Parse Samples", this);
	private final MButton uploadButton = new MButton("Upload Samples", this);
	private final MButton reuploadButton = new MButton("Try Uploading Again",
			this);
	private final MButton retryButton = new MButton("Retry", this);
	private static final String RESET_ID = "reset-link";
	private final HTMLPanel resetPanel = new HTMLPanel("or <span id=\""+RESET_ID+"\"></span>");
	private final MLink resetLink = new MLink("reset the form",
			TokenSpace.bulkUpload);

	private final Grid errorgrid = new Grid();

	private final FlowPanel parsedPanel = new FlowPanel();
	private final MText parsedHeading = new MText(
			"Below is a list of columns from the uploaded spreadsheet and how MetPetDB understands them.",
			"p");
	private final FlexTable parsed = new FlexTable();

	private final FlowPanel summaryPanel = new FlowPanel();
	private final MText summaryHeading = new MText("Parse Results", "h2");
	private final FlexTable summary = new FlexTable();

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

		main.add(form);
		form.setMethod(FormPanel.METHOD_POST);
		form.setEncoding(FormPanel.ENCODING_MULTIPART);
		form.setAction(GWT.getModuleBaseURL() + "/spreadsheetUpload");
		form.addFormHandler(this);
		form.setStyleName(CSS.BULK_UPLOAD_FORM);
		form.setWidget(file);
		file.setName("bulkUpload");
		file.getElement().setAttribute("size", "40");

		main.add(uploadTypeList);
		uploadTypeList.setStyleName(CSS.BULK_TYPES);

		uploadTypeList.add(samplesRadio);
		samplesRadio.setChecked(true);
		samplesRadio.addClickListener(this);
		updateContentType();

		uploadTypeList.add(analysesRadio);
		analysesRadio.addClickListener(this);

		uploadTypeList.add(imagesRadio);
		imagesRadio.addClickListener(this);

		main.add(parseButton);

		main.add(status);

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

		progressContainer.add(cancelLink);

		main.add(resultsPanel);
		resultsPanel.addStyleName(CSS.BULK_RESULTS);
		hide(resultsPanel);

		resultsPanel.add(summaryPanel);
		summaryPanel.setStyleName(CSS.BULK_RESULTS_SUMMARY);
		summaryPanel.add(summaryHeading);
		summaryPanel.add(summary);
		summaryPanel.add(detailsLink);
		summaryPanel.add(uploadButton);
		uploadButton.addStyleName(CSS.PRIMARY_BUTTON);
		hide(uploadButton);

		resultsPanel.add(parsedPanel);
		parsedPanel.setStyleName(CSS.BULK_RESULTS_PARSED);
		hide(parsedPanel);
		parsedPanel.add(parsedHeading);
		parsedPanel.add(parsed);

		main.add(errorgrid);
		hide(errorgrid);

		main.add(retryButton);
		hide(retryButton);

		main.add(resetPanel);
		resetPanel.setStyleName(CSS.SHOW_INLINE);
		if (resetPanel.getElementById(RESET_ID) != null) resetPanel.addAndReplaceElement(resetLink, RESET_ID);
		hide(resetPanel);

		clearResults();
	}

	public void onClick(final Widget sender) {
		if (uploadButton == sender) {
			doUpload();
		} else if (parseButton == sender) {
			uploadButton.setText("Upload " + contentType);
			doParse();
		} else if (sender == detailsLink) {
			if (detailsLink.getText() == "Show details") {
				parsedPanel.removeStyleName(CSS.HIDE);
				detailsLink.setText("Hide details");
			} else {
				parsedPanel.addStyleName(CSS.HIDE);
				detailsLink.setText("Show details");
			}
		} else if (sender == retryButton) {
			hide(retryButton);
			hide(resetPanel);
			show(parseButton);
			doParse();
		} else if (sender == reuploadButton) {
			hide(errorgrid);
			hide(reuploadButton);
			hide(resetPanel);
			doUpload();
		} else {
			updateContentType();
		}

	}

	public void updateContentType() {
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
		parseButton.setText("Parse " + type);
	}

	private void doUpload() {
		hide(uploadButton);
		status.sendNotice(NoticeType.WORKING, "Uploading " + contentType
				+ ", please wait...");
		new VoidServerOp() {
			public void begin() {
				service.commit(fileOnServer, this);
			}

			public void onSuccess() {
				status.sendNotice(NoticeType.SUCCESS, contentType
						+ " uploaded successfully.");
				resetLink.setText("upload more data");
				show(resetPanel);
			}
		}.begin();
	}
	
	private void doParse() {
		form.submit();
		clearResults();
		parseButton.setText("Parsing...");
		parseButton.setEnabled(false);
		show(progressContainer);
		progressTimer.scheduleRepeating(3000);
	}

	private void parse() {
		status.sendNotice(NoticeType.WORKING, "Parsing " + contentType
				+ ", please wait...");
		new ServerOp<BulkUploadResult>() {
			public void begin() {
				service.parser(fileOnServer, this);
			}

			public void onSuccess(final BulkUploadResult results) {
				hide(progressContainer);
				status.hide();
				populateParsedTable(results.getHeaders());
				populateSummaryTable(results.getResultCounts());
				show(resultsPanel);
				hide(parseButton);
				parseButton.setEnabled(true);
				show(uploadButton);
				final Map<Integer, ValidationException> errors = results
						.getErrors();
				if (!errors.isEmpty()) {
					errorgrid.resize(errors.size() + 1, 2);
					int i = 1;
					errorgrid.setText(0, 0, "Row");
					errorgrid.setText(0, 1, "Error");
					for (Map.Entry<Integer, ValidationException> e : errors
							.entrySet()) {
						errorgrid.setText(i, 0, e.getKey().toString());
						errorgrid.setText(i++, 1, e.getValue().format());
					}
					status.sendNotice(NoticeType.ERROR,
							"There were some errors in the spreadsheet:");
					show(errorgrid);
					show(reuploadButton);
					resetLink.setText("reset the form");
					show(resetPanel);
					uploadButton.setEnabled(false);
				} else {
					//TODO need to disbale this button if there are old ones
					uploadButton.setEnabled(true);
				}
			}
			public void onFailure(final Throwable e) {
				e.printStackTrace();
				status.sendNotice(NoticeType.ERROR, "Could not parse "
						+ contentType + ". Please see the errors below.");
				hide(progressContainer);
				hide(parseButton);
				parseButton.setEnabled(true);
				show(retryButton);
				show(resetPanel);
			}
		}.begin();
	}

	public void onSubmitComplete(final FormSubmitCompleteEvent event) {
		final String results = event.getResults();
		progressTimer.cancel();
		uploadProgress.setProgress(1.0d);
		if (results != "NO-SCRIPT-DATA" && results != "") {
			// TODO: This is bad! It's to strip the <pre> and </pre>
			// tags that get stuck on the string for some reason
			fileOnServer = results.substring(5, results.length() - 6);
			parse();
		}
	}

	public void onSubmit(final FormSubmitEvent event) {
		// This event is fired just before the form is submitted. We can
		// take this opportunity to perform validation.
		if (file.getFilename().length() == 0) {
			status.sendNotice(NoticeType.WARNING, "Please select a file");
			file.setStyleName(CSS.INVALID);
			event.setCancelled(true);
		}
		clearResults();
	}

	public void clearResults() {
		errorgrid.resize(0, 0);
		parsed.clear();
		summary.clear();
		uploadProgress.setProgress(0.0d);
		hide(resultsPanel);
		status.hide();
		hide(progressContainer);
	}

	protected void populateParsedTable(final Map<Integer, String[]> headers) {
		parsed.setText(0, 1, "Spreadsheet");
		parsed.setText(0, 2, "MetPetDB");
		parsed.getRowFormatter().setStyleName(0, CSS.TYPE_SMALL_CAPS);

		int i = 1;
		ArrayList<Integer> keys = new ArrayList<Integer>(headers.keySet());
		Collections.sort(keys);
		for (Integer k : keys) {
			parsed.setText(i, 0, (k + 1) + ".");
			parsed.getFlexCellFormatter().setStyleName(i, 0,
					CSS.BULK_RESULTS_SSCOLNUM);

			parsed.setWidget(i, 1, new HTML(headers.get(k)[0]));
			parsed.getFlexCellFormatter().setStyleName(i, 1,
					CSS.BULK_RESULTS_SSCOL);

			String matched = "";
			if (headers.get(k)[1].length() > 0) {
				try {
					matched = enttxt.getString(headers.get(k)[1]);
				} catch (MissingResourceException mre) {
					matched = headers.get(k)[1].toString();
				}
			}
			parsed.setWidget(i, 2, new HTML(matched));
			parsed.getFlexCellFormatter().setStyleName(i, 2,
					CSS.BULK_RESULTS_MPDBCOL);
			i++;
		}
	}

	protected void populateSummaryTable(final Map<String, BulkUploadResultCount> additions) {
		final Iterator<String> objItr = additions.keySet().iterator();
		summary.setHTML(0, 1, "<div class=\"" + CSS.TYPE_SMALL_CAPS + "\">new</div>");
		summary.setHTML(0, 2, "<div class=\"" + CSS.TYPE_SMALL_CAPS + "\">duplicate</div>");
		summary.setHTML(0, 3, "<div class=\"" + CSS.TYPE_SMALL_CAPS + "\">invalid</div>");
		summary.setHTML(0, 4, "<div class=\"" + CSS.TYPE_SMALL_CAPS + "\">total</div>");
		int row = 1;
		while(objItr.hasNext()) {
			final String objType = objItr.next();
			final int fresh = additions.get(objType).getFresh();
			final int dup = additions.get(objType).getDuplicate();
			final int invalid = additions.get(objType).getInvalid();
			final int total = fresh + dup + invalid;
			summary.setText(row, 0, objType);
			addSummaryItem(row, 1, fresh, CSS.BULK_RESULTS_NEW);
			addSummaryItem(row, 2, dup, CSS.BULK_RESULTS_DUPLICATE);
			addSummaryItem(row, 3, invalid, CSS.BULK_RESULTS_INVALID);
			addSummaryItem(row, 4, total, CSS.BULK_RESULTS_TOTAL);
			++row;
		}
	}

	protected void addSummaryItem(int row, int index, int count, String styleName) {
		summary.setHTML(row, index, "<div class=\"" + CSS.TYPE_LARGE_NUMBER + "\">" + count + "</div>");
		summary.getFlexCellFormatter().setStyleName(row, index, styleName);
		if (count == 0) summary.getFlexCellFormatter().addStyleName(row, index, CSS.EMPTY);
	}

}
