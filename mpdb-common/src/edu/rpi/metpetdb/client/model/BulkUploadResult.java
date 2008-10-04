package edu.rpi.metpetdb.client.model;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.user.client.rpc.IsSerializable;

import edu.rpi.metpetdb.client.error.ValidationException;

public class BulkUploadResult implements IsSerializable {

	private Map<Integer, String[]> headers;
	private Map<Integer, ValidationException> errors;
	/**
	 * the key is the name of the object, i.e. Sample, the value is a
	 * ResultCount class that contains the counts of fresh, invalid, and old
	 * objects
	 */
	private Map<String, BulkUploadResultCount> resultCounts;

	public BulkUploadResult() {

	}

	public Map<Integer, String[]> getHeaders() {
		return headers;
	}
	public void setHeaders(Map<Integer, String[]> headers) {
		this.headers = headers;
	}
	public Map<Integer, ValidationException> getErrors() {
		return errors;
	}
	public void setErrors(Map<Integer, ValidationException> errors) {
		this.errors = errors;
	}
	public Map<String, BulkUploadResultCount> getResultCounts() {
		return resultCounts;
	}
	public void setResultCounts(Map<String, BulkUploadResultCount> resultCounts) {
		this.resultCounts = resultCounts;
	}
	public void addResultCount(final String key, final BulkUploadResultCount results) {
		if (resultCounts == null) 
			resultCounts = new HashMap<String, BulkUploadResultCount>();
		resultCounts.put(key, results);
	}

	
}
