package edu.rpi.metpetdb.client.model.bulk.upload;

import com.google.gwt.user.client.rpc.IsSerializable;

public class BulkUploadHeader implements IsSerializable {

	/** The text in the actual spreadsheet */
	private String headerText;
	/** What we interpret it as */
	private String interpretedHeaderText;

	public BulkUploadHeader() {

	}

	public BulkUploadHeader(final String headerText,
			final String interpretedHeaderText) {
		this.headerText = headerText;
		this.interpretedHeaderText = interpretedHeaderText;
	}

	public String getHeaderText() {
		return headerText;
	}

	public void setHeaderText(String headerText) {
		this.headerText = headerText;
	}

	public String getInterpretedHeaderText() {
		return interpretedHeaderText;
	}

	public void setInterpretedHeaderText(String interpretedHeaderText) {
		this.interpretedHeaderText = interpretedHeaderText;
	}

}
