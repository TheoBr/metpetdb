package edu.rpi.metpetdb.client.model;

import java.sql.Timestamp;
import java.util.Date;

import org.hibernate.search.annotations.DateBridge;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Resolution;

import edu.rpi.metpetdb.client.model.properties.Property;

public class Invite extends MObject {
	private static final long serialVersionUID = 1L;
	
	private int id;
	
	private int project_id;
	
	private int user_id;
	
	private Timestamp action_timestamp; 
	
	private String status;
	
	private static final String[] months = {
		"January", "February", "March", "April", "May", "June", "July",
		"August", "September", "October", "November", "December",
	};

	public boolean mIsNew() {
		return (id == 0);
	}
	
	public void setId(int id){
		this.id = id;
	}
	
	public int getId() {
		return id;
	}

	public void setProject_id(int id){
		project_id = id;
	}
	
	public int getProject_id(){
		return project_id;
	}
	
	public void setUser_id(int id){
		user_id = id;
	}
	
	public int getUser_id(){
		return user_id;
	}
	
	public Timestamp getAction_timestamp() {
		return action_timestamp;
	}
	
	public void setAction_timestamp(Timestamp action_timestamp){
		this.action_timestamp = action_timestamp;
	}

	public String getStatus() {
		return status;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}
	
	public String timeAsString(){
		if(action_timestamp == null) return "";
		final int year = action_timestamp.getYear() + 1900;
		final int month = action_timestamp.getMonth();
		final int day = action_timestamp.getDay();
		
		int hour = action_timestamp.getHours();
		final int minute = action_timestamp.getMinutes();
		final int seconds = action_timestamp.getSeconds();
		String m = months[month];
		
		String daytime = (hour >=12 ? "PM" : "AM");
		hour = (hour > 12 ? hour - 12 : hour);
		
		return (m + " " + String.valueOf(day) + " " + String.valueOf(year) + " " + 
			String.valueOf(hour) + ":" + String.valueOf(minute) + ":" + String.valueOf(seconds) + " " + daytime); 
	}
}
