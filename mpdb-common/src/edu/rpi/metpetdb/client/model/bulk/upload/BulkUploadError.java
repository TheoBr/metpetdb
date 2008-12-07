package edu.rpi.metpetdb.client.model.bulk.upload;

import com.google.gwt.user.client.rpc.IsSerializable;

import edu.rpi.metpetdb.client.error.MpDbException;

public class BulkUploadError implements IsSerializable {

	private MpDbException exception;
	private int row;
	private int column;

	public BulkUploadError() {

	}
	
	public BulkUploadError(final int row, final int column, final MpDbException e) {
		this.row = row;
		this.column = column;
		this.exception = e;
	}

	public MpDbException getException() {
		return exception;
	}

	public void setException(MpDbException exception) {
		this.exception = exception;
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getColumn() {
		return column;
	}

	public void setColumn(int column) {
		this.column = column;
	}

}
