package edu.rpi.metpetdb.client.model;

import java.util.Map;

import com.google.gwt.user.client.rpc.IsSerializable;

import edu.rpi.metpetdb.client.error.ValidationException;

public class BulkUploadResult implements IsSerializable {
	
	private Map<Integer, String[]> headers;
	private Map<String, Integer[]> additions;
	private Map<Integer, ValidationException> errors;
	
	public BulkUploadResult() {
		
	}
	
	public Map<Integer, String[]> getHeaders() {
		return headers;
	}
	public void setHeaders(Map<Integer, String[]> headers) {
		this.headers = headers;
	}
	public Map<String, Integer[]> getAdditions() {
		return additions;
	}
	public void setAdditions(Map<String, Integer[]> additions) {
		this.additions = additions;
	}
	public Map<Integer, ValidationException> getErrors() {
		return errors;
	}
	public void setErrors(Map<Integer, ValidationException> errors) {
		this.errors = errors;
	}
	
	

}
