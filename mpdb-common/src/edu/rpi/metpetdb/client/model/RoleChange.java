package edu.rpi.metpetdb.client.model;

import java.sql.Timestamp;
import java.util.Date;

public class RoleChange extends MObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int id;
	private Timestamp requestDate;
	private Timestamp finalizeDate;
	private Boolean granted;
	private String grantReason;
	private String requestReason;
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

	public Timestamp getFinalizeDate() {
		return finalizeDate;
	}

	public void setFinalizeDate(Timestamp finalizeDate) {
		this.finalizeDate = finalizeDate;
	}

	public Boolean getGranted() {
		return granted;
	}

	public void setGranted(Boolean granted) {
		this.granted = granted;
	}

	public String getGrantReason() {
		return grantReason;
	}

	public void setGrantReason(String grantReason) {
		this.grantReason = grantReason;
	}

	public String getRequestReason() {
		return requestReason;
	}

	public void setRequestReason(String requestReason) {
		this.requestReason = requestReason;
	}

	@Override
	public boolean mIsNew() {
		return id == 0;
	}
	
	public boolean equals(Object other) {
		return other instanceof RoleChange && ((RoleChange)other).getId() == id;
	}
	
	public int hashCode() {
		return id;
	}

}
