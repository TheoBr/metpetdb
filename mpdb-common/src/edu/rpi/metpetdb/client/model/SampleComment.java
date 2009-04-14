package edu.rpi.metpetdb.client.model;

import java.sql.Timestamp;
import java.util.Date;

import edu.rpi.metpetdb.client.model.interfaces.HasOwner;
import edu.rpi.metpetdb.client.model.interfaces.HasSample;

public class SampleComment extends Comment implements HasSample, HasOwner {

	private static final long serialVersionUID = 1L;
	private Sample sample;
	private User owner;
	private String ownerName;
	private Timestamp dateAdded;
	
	public SampleComment() {
		
	}
	
	public SampleComment(final String text) {
		setText(text);
	}

	public Sample getSample() {
		return sample;
	}

	public void setSample(Sample sample) {
		this.sample = sample;
	}
	
	public User getOwner() {
		return owner;
	}
	
	public void setOwner(final User u) {
		owner = u;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public Timestamp getDateAdded() {
		if (dateAdded == null)
			return new Timestamp((new Date()).getTime());
		return dateAdded;
	}
	
	public String getDateAddedDisplay() {
		int day = getDateAdded().getDate();
		int month = getDateAdded().getMonth() + 1;
		int year = getDateAdded().getYear() + 1900;
		int hour = getDateAdded().getHours();
		int min = getDateAdded().getMinutes();
		return month + "/" + day + "/" + year + " " + hour + ":" + min;
	}

	public void setDateAdded(Timestamp dateAdded) {
		this.dateAdded = dateAdded;
	}
}
