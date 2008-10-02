package edu.rpi.metpetdb.client.ui.bulk.upload;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.MissingResourceException;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
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
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.widgetideas.client.ProgressBar;

import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.locale.LocaleEntity;
import edu.rpi.metpetdb.client.locale.LocaleHandler;
import edu.rpi.metpetdb.client.ui.CSS;
import edu.rpi.metpetdb.client.ui.MpDb;
import edu.rpi.metpetdb.client.ui.ServerOp;
import edu.rpi.metpetdb.client.ui.TokenSpace;
import edu.rpi.metpetdb.client.ui.widgets.MLink;
import edu.rpi.metpetdb.client.ui.widgets.MNoticePanel;
import edu.rpi.metpetdb.client.ui.widgets.MPagePanel;
import edu.rpi.metpetdb.client.ui.widgets.MText;
import edu.rpi.metpetdb.client.ui.widgets.MTwoColPanel;
import edu.rpi.metpetdb.client.ui.widgets.MHtmlList;
import edu.rpi.metpetdb.client.ui.widgets.MNoticePanel.NoticeType;

public class BulkUploadPanel extends MPagePanel implements ClickListener, FormHandler {
	
	private final MText intro = new MText("Upload a large amount of data at once using the form below.","p");
	private final MLink help = new MLink("Bulk Upload Instructions", "docs/mpdb-bulkupload-help.pdf", true);
	
	private final FlowPanel main = new FlowPanel();
	private final FormPanel form = new FormPanel();
	private final FileUpload file = new FileUpload();
	
	private final MNoticePanel status = new MNoticePanel();
	
	private final FlowPanel resultsPanel = new FlowPanel();
	private final MLink detailsLink = new MLink("Show details",this);
	
	private final FlowPanel progressContainer = new FlowPanel();
	private final ProgressBar uploadProgress = new ProgressBar();
	private final Timer progressTimer;
	private final MLink cancelLink = new MLink("Cancel", this);

	private final MHtmlList uploadTypeList = new MHtmlList();
	private final RadioButton samplesRadio = new RadioButton("type", "Samples");
	private final RadioButton analysesRadio = new RadioButton("type", "Chemical Analyses");
	private final RadioButton imagesRadio = new RadioButton("type", "Images");
	
	private final Button parseButton = new Button("Parse Samples", this);
	private final Button uploadButton = new Button("Upload Samples", this);
	private final Button reuploadButton = new Button("Try Uploading Again", this);
	private final Button retryButton = new Button("Retry", this);
	private final MLink restartLink = new MLink("Start over", TokenSpace.bulkUpload);
	
	private final Grid errorgrid = new Grid();
	
	private final FlowPanel parsedPanel = new FlowPanel();
	private final MText parsedHeading = new MText("Below is a list of columns from the uploaded spreadsheet and how MetPetDB understands them.","p");
	private final FlexTable parsed = new FlexTable();
	
	private final FlowPanel summaryPanel = new FlowPanel();
	private final MText summaryHeading = new MText("Parse Results","h2");
	private final FlexTable summary = new FlexTable();
	
	private String contentType;

	private static final LocaleEntity enttxt = LocaleHandler.lc_entity;
	private static final int PARSE = 0;
	private static final int UPLOAD = 1;
	private static int mode;

	public BulkUploadPanel() {
		setStylePrimaryName(CSS.BULK_UPLOAD);
		addPageHeader();
		setPageTitle("Bulk Upload");
				
		addPageHeaderListItem(help);
		help.setStyleName(CSS.LINK_LARGE_ICON);
		help.addStyleName(CSS.LINK_HELP);
		help.setTitle("Instructional PDF on how to use Bulk Upload.");
		
		add(main);
		main.setStyleName(CSS.MAIN);
		
		main.add(intro);
		intro.addStyleName(CSS.ELEMENT_MARGIN);
		
		main.add(form);
		form.setMethod(FormPanel.METHOD_POST);
		form.setEncoding(FormPanel.ENCODING_MULTIPART);
		form.setAction(GWT.getModuleBaseURL() + "/spreadsheetUpload");
		form.addFormHandler(this);
		form.setStyleName(CSS.BULK_UPLOAD_FORM);
		form.setWidget(file);
		file.setName("bulkUpload");
		file.getElement().setAttribute("size","40");
		
		main.add(uploadTypeList);
		uploadTypeList.setStyleName(CSS.BULK_TYPES);
		
		uploadTypeList.add(samplesRadio);
		samplesRadio.setChecked(true);
		samplesRadio.addClickListener(this);
		updateContentType(samplesRadio.getText());
		
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
						public void onError(Request request, Throwable exception) {}
						public void onResponseReceived(Request request, Response response) {
							uploadProgress.setProgress(Double.parseDouble(response.getText()));
						}
					});
				} catch (RequestException e) {}
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
		
		resultsPanel.add(parsedPanel);
		parsedPanel.setStyleName(CSS.BULK_RESULTS_PARSED);
		hide(parsedPanel);
		parsedPanel.add(parsedHeading);
		parsedPanel.add(parsed);
		
		main.add(uploadButton);
		uploadButton.addStyleName(CSS.PRIMARY_BUTTON);
		hide(uploadButton);
		
		main.add(errorgrid);
		hide(errorgrid);
		
		main.add(retryButton);
		hide(retryButton);
		
		main.add(restartLink);
		hide(restartLink);

		clearResults();
	}

	public void onClick(final Widget sender) {
		if (uploadButton == sender) {
			mode = UPLOAD;
			doUpload();
		} else if (parseButton == sender) {
			mode = PARSE;
			uploadButton.setText("Upload " + contentType);
			doUpload();
		} else if (sender == samplesRadio) {
			updateContentType(samplesRadio.getText());
		} else if (sender == analysesRadio) {
			updateContentType(analysesRadio.getText());
		} else if (sender == imagesRadio) {
			updateContentType(imagesRadio.getText());
		} else if (sender == detailsLink) {
			if (detailsLink.getText() == "Show details") {
				parsedPanel.removeStyleName(CSS.HIDE);
				detailsLink.setText("Hide details");
			} else {
				parsedPanel.addStyleName(CSS.HIDE);
				detailsLink.setText("Show details");
			}
		} else if (sender == retryButton) {
			clearResults();
			hide(retryButton);
			show(parseButton);
		} else if (sender == reuploadButton) {
			mode = UPLOAD;
			hide(errorgrid);
			hide(reuploadButton);
			hide(restartLink);
			doUpload();
		}
	}
	
	public void updateContentType() {
		if (samplesRadio.isChecked())
			updateContentType(samplesRadio.getText());
		else if (analysesRadio.isChecked())
			updateContentType(analysesRadio.getText());
		else if (imagesRadio.isChecked())
			updateContentType(imagesRadio.getText());
	}
	
	public void updateContentType(String type) {
		contentType = type;
		parseButton.setText("Parse " + type);
	}

	public void doUpload() {
		form.submit();
		clearResults();
		show(progressContainer);
		progressTimer.scheduleRepeating(1000);
	}

	public void onSubmitComplete(final FormSubmitCompleteEvent event) {
		final String results = event.getResults();
		progressTimer.cancel();
		uploadProgress.setProgress(1.0d);
		if (results != "NO-SCRIPT-DATA" && results != "") {
		
			// TODO: This is bad! It's to strip the <pre> and </pre>
			// tags that get stuck on the string for some reason
			final String fileOnServer = results.substring(5, results.length() - 6);
		
			if (mode == UPLOAD) {
				hide(uploadButton);
				status.sendNotice(NoticeType.WORKING, "Uploading "+contentType+", please wait...");
				new ServerOp<Map<Integer, ValidationException>>() {
					public void begin() {
						if (samplesRadio.isChecked()) {
							MpDb.bulkUpload_svc.saveSamplesFromSpreadsheet(fileOnServer, this);
						} else if (analysesRadio.isChecked()) {
							MpDb.bulkUploadChemicalAnalyses_svc.saveAnalysesFromSpreadsheet(fileOnServer,this);
						} else {
							MpDb.bulkUploadImages_svc.saveImagesFromZip(fileOnServer, this);
						}
					}

					public void onSuccess(final Map<Integer, ValidationException> result) {
						hide(progressContainer);
						if (result == null) {
							status.sendNotice(NoticeType.SUCCESS, contentType + " uploaded successfully.");
							restartLink.setText("Upload more data");
							show(restartLink);
						} else {
							errorgrid.resize(result.size() + 1, 2);
							int i = 1;
							errorgrid.setText(0, 0, "Row");
							errorgrid.setText(0, 1, "Error");
							for (Map.Entry<Integer, ValidationException> e : result
									.entrySet()) {
								errorgrid.setText(i, 0, e.getKey().toString());
								errorgrid.setText(i++, 1, e.getValue().format());
							}
							status.sendNotice(NoticeType.ERROR, "There were some errors in the spreadsheet:");
							show(errorgrid);
							show(reuploadButton);
							restartLink.setText("Start over");
							show(restartLink);
						}
					}
				}.begin();
				
			} else if (mode == PARSE) {
				//hide(parseButton);
				status.sendNotice(NoticeType.WORKING, "Parsing "+contentType+", please wait...");
				new ServerOp<Map<Integer, String[]>>() {
					public void begin() {
						if (samplesRadio.isChecked()) {
							MpDb.bulkUpload_svc.getHeaderMapping(fileOnServer, this);
						} else if (analysesRadio.isChecked()) {
							MpDb.bulkUploadChemicalAnalyses_svc.getHeaderMapping(fileOnServer, this);
						} else {
							MpDb.bulkUploadImages_svc.getHeaderMapping(fileOnServer, this);
						}
					}

					public void onSuccess(final Map<Integer, String[]> headers) {
						if (headers == null) {
							status.sendNotice(NoticeType.ERROR, "Could not parse "+contentType+". Please see the errors below.");
							// TODO: this should prevent or cancel the 'finding new additions' operation 
							hide(progressContainer);
							show(retryButton);
							show(restartLink);
						} else {
							status.sendNotice(NoticeType.WORKING, contentType+" parsed. Compiling results...");
							populateParsedTable(headers);
						}
					}
				}.begin();

				// Find New additions
				new ServerOp<Map<String, Integer[]>>() {
					public void begin() {
						if (samplesRadio.isChecked()) {
							MpDb.bulkUpload_svc.getAdditions(fileOnServer, this);
						} else if (analysesRadio.isChecked()) {
							MpDb.bulkUploadChemicalAnalyses_svc.getAdditions(fileOnServer, this);
						} else {
							MpDb.bulkUploadImages_svc.getAdditions(fileOnServer, this);
						}
					}

					public void onSuccess(final Map<String, Integer[]> additions) {
						hide(progressContainer);
						if (additions == null) {
							status.sendNotice(NoticeType.ERROR, "Could not compile results.");
							// TODO: populate errorPanel and show it now
							show(retryButton);
							restartLink.setText("Start over");
							show(restartLink);
						} else {
							status.hide();
							populateSummaryTable(additions);
							show(resultsPanel);
							show(uploadButton);
						}
					}
				}.begin();
			}
		}
	}

	public void onSubmit(final FormSubmitEvent event) {
		// This event is fired just before the form is submitted. We can
		// take this opportunity to perform validation.
		if (file.getFilename().length() == 0) {
			status.sendNotice(NoticeType.WARNING, "Please select a file");
			file.setStyleName(CSS.INVALID_FIELD);
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
		parsed.setText(0,1,"Spreadsheet");
		parsed.setText(0,2,"MetPetDB");
		parsed.getRowFormatter().setStyleName(0,CSS.TYPE_SMALL_CAPS);
		
		int i = 1;
		ArrayList<Integer> keys = new ArrayList<Integer>(headers.keySet());
		Collections.sort(keys);
		for (Integer k : keys) {
			parsed.setText(i,0, (k+1)+".");
			parsed.getFlexCellFormatter().setStyleName(i, 0, CSS.BULK_RESULTS_SSCOLNUM);

			parsed.setWidget(i, 1, new HTML(headers.get(k)[0]));
			parsed.getFlexCellFormatter().setStyleName(i, 1, CSS.BULK_RESULTS_SSCOL);

			String matched = "";
			if (headers.get(k)[1].length() > 0) {
				try {
					matched = enttxt.getString(headers.get(k)[1]);
				} catch (MissingResourceException mre) {
					//TODO throw an error to developers
					matched = headers.get(k).toString();
				}
			}
			parsed.setWidget(i, 2, new HTML(matched));
			parsed.getFlexCellFormatter().setStyleName(i, 2, CSS.BULK_RESULTS_MPDBCOL);
			i++;
		}
	}
	
	protected void populateSummaryTable(final Map<String, Integer[]> additions) {
		ArrayList<String> keys = new ArrayList<String>(additions.keySet());
		int invalidCount=0, newCount=0, oldCount=0, total=0;
		for (String k : keys) {
			invalidCount = (Integer) additions.get(k)[0];
			newCount = (Integer) additions.get(k)[1];
			oldCount = (Integer) additions.get(k)[2];
		}
		total = newCount + oldCount + invalidCount;
		
		addSummaryItem(0, newCount, "new", CSS.BULK_RESULTS_NEW);
		addSummaryItem(1, oldCount, "old", CSS.BULK_RESULTS_OLD);
		addSummaryItem(2, invalidCount, "invalid", CSS.BULK_RESULTS_INVALID);
		addSummaryItem(3, total, "total", CSS.BULK_RESULTS_TOTAL);
	}
	
	protected void addSummaryItem(int index, int count, String title, String styleName) {
		summary.setWidget(0,index,new HTML("<div class=\""+ CSS.TYPE_SMALL_CAPS +"\">"+title+"</div>" +
				"<div class=\""+ CSS.TYPE_LARGE_NUMBER +"\">"+count+"</div>"));
		summary.getFlexCellFormatter().setStyleName(0,index,styleName);
		if (count == 0) summary.getFlexCellFormatter().addStyleName(0,index,CSS.EMPTY);
	}

}
