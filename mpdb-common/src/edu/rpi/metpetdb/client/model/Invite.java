package edu.rpi.metpetdb.client.model;

import edu.rpi.metpetdb.client.model.properties.Property;

public class Invite extends MObject {
	private static final long serialVersionUID = 1L;
	
	private int project_id;
	
	private int member_id;

	public boolean mIsNew() {
		return true;
	}

	public void setProject_id(int id){
		project_id = id;
	}
	
	public int getProject_id(){
		return project_id;
	}
	
	public void setMember_id(int id){
		member_id = id;
	}
	
	public int getMember_id(){
		return member_id;
	}
}
