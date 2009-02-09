package edu.rpi.metpetdb.client.model;

import java.sql.Timestamp;
import java.util.Date;

public class PendingRole extends MObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int id;
	private Timestamp requestDate;
	private User user;
	private User sponsor;
	private Role role;

	public int getId() {
		return id;
	}

	public Timestamp getRequestDate() {
		if (requestDate != null)
			return requestDate;
		else
			return new Timestamp(new Date().getTime());
	}

	public User getUser() {
		return user;
	}

	public User getSponsor() {
		return sponsor;
	}

	public Role getRole() {
		return role;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setRequestDate(Timestamp requestDate) {
		this.requestDate = requestDate;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public void setSponsor(User sponsor) {
		this.sponsor = sponsor;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	@Override
	public boolean mIsNew() {
		return false;
	}

}
