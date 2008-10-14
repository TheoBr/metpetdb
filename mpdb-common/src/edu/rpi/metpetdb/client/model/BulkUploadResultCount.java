package edu.rpi.metpetdb.client.model;

import com.google.gwt.user.client.rpc.IsSerializable;

public class BulkUploadResultCount implements IsSerializable {
	private int fresh;
	private int invalid;
	private int old;
	
	public BulkUploadResultCount() {
		
	}

	public int getFresh() {
		return fresh;
	}
	public void setFresh(int fresh) {
		this.fresh = fresh;
	}
	public int getInvalid() {
		return invalid;
	}
	public void setInvalid(int invalid) {
		this.invalid = invalid;
	}
	public int getDuplicate() {
		return old;
	}
	public void setOld(int old) {
		this.old = old;
	}
	public void incrementOld() {
		old++;
	}
	public void incrementFresh() {
		fresh++;
	}
	public void incrementInvalid() {
		invalid++;
	}
}