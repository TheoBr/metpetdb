package edu.rpi.metpetdb.gwt.client.screen;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteHandler;

import edu.rpi.metpetdb.gwt.client.UploadService;
import edu.rpi.metpetdb.gwt.client.UploadServiceAsync;
import edu.rpi.metpetdb.gwt.client.dto.UserDTO;

public class UploadScreen extends AbsolutePanel {

	private final UploadServiceAsync uploadService = (UploadServiceAsync) GWT
	.create(UploadService.class);

	private HTML messages = new HTML();
	/**
	 * Unfortunately in order to facilitate the FileUpload behavior, you have to use a FormPanel and submit to a non-GWT Servlet
	 * @param userDTO
	 */
	public UploadScreen(final UserDTO userDTO) {

		final FormPanel formPanel = new FormPanel();
		
		final AbsolutePanel parentPanel = this;
		formPanel.setAction("./upload");
		formPanel.setEncoding(FormPanel.ENCODING_MULTIPART);
		formPanel.setMethod(FormPanel.METHOD_POST);
		
		final FileUpload fileUpload = new FileUpload();
		//TODO: You have to name it something otherwise it doesn't get posted
		fileUpload.setName("foo");
		
		formPanel.add(fileUpload);
		
		formPanel.addSubmitCompleteHandler(new SubmitCompleteHandler() {
			
			@Override
			public void onSubmitComplete(SubmitCompleteEvent event) {
				parentPanel.remove(messages);
				messages = new HTML( event.getResults().replaceAll("<.{0,1}pre>", "").replaceAll("&lt;", "<").replaceAll("&gt;", ">"));
				parentPanel.add(messages);
				
				
			}
		});

		this.add(formPanel);
		
		
		
		Button uploadButton = new Button("Upload", new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {			
				
				formPanel.submit();
			}
		});

		this.add(uploadButton);

	}

}
