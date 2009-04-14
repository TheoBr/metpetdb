package edu.rpi.metpetdb.client.model;

import java.sql.Timestamp;
import java.util.Date;

import edu.rpi.metpetdb.client.model.interfaces.HasOwner;
import edu.rpi.metpetdb.client.model.interfaces.HasSample;

public class SampleComment extends Comment implements HasSample, HasOwner {

	private static final long serialVersionUID = 1L;
	public static final long EDIT_TIMER = 15*60*1000; // 15 minutes
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
		String minDisplay = String.valueOf(min);
		if (min < 10 && min > 0){
			minDisplay = "0"+min;
		} else if (min == 0){
			minDisplay = "00";
		}
		return month + "/" + day + "/" + year + " " + hour + ":" + minDisplay;
	}

	public void setDateAdded(Timestamp dateAdded) {
		this.dateAdded = dateAdded;
	}
}
