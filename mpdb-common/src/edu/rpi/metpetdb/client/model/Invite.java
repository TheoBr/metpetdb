package edu.rpi.metpetdb.client.model;

import java.sql.Timestamp;

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
		
		java.util.Date date = new java.util.Date(action_timestamp.getTime());
		return date.toString();
		
	}
}
