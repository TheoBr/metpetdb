package edu.rpi.metpetdb.server.model;

import java.util.HashSet;
import java.util.Set;

public class User extends MObject {
	private static final long serialVersionUID = 1L;
	public static final int P_username = 0;
	public static final int P_emailAddress = 1;

	private int id;
	private int version;
	private String username;
	private String emailAddress;
	private Set<Project> projects;
	private Set<Sample> samples;
	private transient byte[] encryptedPassword;

	public int getId() {
		return id;
	}

	public void setId(final int v) {
		id = v;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(final int v) {
		version = v;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(final String n) {
		username = n;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(final String n) {
		emailAddress = n;
	}

	public byte[] getEncryptedPassword() {
		return encryptedPassword;
	}

	public void setEncryptedPassword(final byte[] p) {
		encryptedPassword = p;
	}

	public Set<Project> getProjects() {
		if (projects == null)
			projects = new HashSet<Project>();
		return projects;
	}

	public void setProjects(final Set<Project> c) {
		projects = c;
	}

	public Set<Sample> getSamples() {
		if (samples == null)
			samples = new HashSet<Sample>();
		return samples;
	}

	public void setSamples(final Set<Sample> s) {
		samples = s;
	}

	public int hashCode() {
		return id;
	}

	public boolean equals(final Object o) {
		return o instanceof User && id == ((User) o).id;
	}

	public String toString() {
		return this.username;
	}

	public boolean mIsNew() {
		return id == 0;
	}
}
