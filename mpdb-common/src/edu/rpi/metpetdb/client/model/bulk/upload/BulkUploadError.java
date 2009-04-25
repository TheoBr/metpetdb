package edu.rpi.metpetdb.client.model.bulk.upload;

import com.google.gwt.user.client.rpc.IsSerializable;

import edu.rpi.metpetdb.client.error.MpDbException;

public class BulkUploadError implements IsSerializable {

	private MpDbException exception;
	private int row;
	private String column;
	private String cellData;

	public BulkUploadError() {

	}
	
	public BulkUploadError(final int row, final String column, final MpDbException e, final String cellData) {
		this.row = row;
		this.column = column;
		this.exception = e;
		this.cellData = cellData;
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

	public String getColumn() {
		return column;
	}

	public void setColumn(String column) {
		this.column = column;
	}

	public String getCellData() {
		return cellData;
	}

	public void setCellData(String cellData) {
		this.cellData = cellData;
	}
}
