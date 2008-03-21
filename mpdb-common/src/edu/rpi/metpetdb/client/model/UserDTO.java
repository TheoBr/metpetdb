package edu.rpi.metpetdb.client.model;

import java.util.HashSet;
import java.util.Set;

public class UserDTO extends MObjectDTO {

	private static final long serialVersionUID = 1L;
	private int id;
	private int version;
	private String username;
	private String emailAddress;
	private Set<ProjectDTO> projects;
	private Set<SampleDTO> samples;
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

	public Set<ProjectDTO> getProjects() {
		if (projects == null)
			projects = new HashSet<ProjectDTO>();
		return projects;
	}

	public void setProjects(final Set<ProjectDTO> c) {
		projects = c;
	}

	public Set<SampleDTO> getSamples() {
		if (samples == null)
			samples = new HashSet<SampleDTO>();
		return samples;
	}

	public void setSamples(final Set<SampleDTO> s) {
		samples = s;
	}

	public int hashCode() {
		return id;
	}

	public boolean equals(final Object o) {
		return o instanceof UserDTO && id == ((UserDTO) o).id;
	}

	public String toString() {
		return this.username;
	}

	public boolean mIsNew() {
		return id == 0;
	}
}
