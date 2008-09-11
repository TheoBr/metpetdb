package edu.rpi.metpetdb.server.model;

public class AdminUser extends MObject {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;
	private User user;
	
	

	public int getId() {
		return id;
	}



	public void setId(int id) {
		this.id = id;
	}



	public User getUser() {
		return user;
	}



	public void setUser(User user) {
		this.user = user;
	}



	@Override
	public boolean mIsNew() {
		return id == 0;
	}
	
	

}
