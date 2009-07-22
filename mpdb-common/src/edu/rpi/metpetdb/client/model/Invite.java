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
	
	private int project_id;
	
	private int user_id;
	
	@Field(index = Index.UN_TOKENIZED)
	@DateBridge(resolution = Resolution.DAY)
	private Timestamp action_timestamp; 
	
	private String status;

	public boolean mIsNew() {
		return true;
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
}
