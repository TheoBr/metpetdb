package edu.rpi.metpetdb.client.model;

import java.util.HashSet;
import java.util.Set;

import edu.rpi.metpetdb.client.error.InvalidPropertyException;

public class User extends MObject {
	public static final int P_username = 0;
	public static final int P_emailAddress = 1;

	private int id;
	private int version;
	private String username;
	private String emailAddress;
	/**
	 * @gwt.typeArgs <edu.rpi.metpetdb.client.model.Project>
	 */
	private Set projects;
	/**
	 * @gwt.typeArgs <edu.rpi.metpetdb.client.model.Sample>
	 */
	private Set samples;
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

	public Set getProjects() {
		if (projects == null)
			projects = new HashSet();
		return projects;
	}

	public void setProjects(final Set c) {
		projects = c;
	}

	public Set getSamples() {
		if (samples == null)
			samples = new HashSet();
		return samples;
	}

	public void setSamples(final Set s) {
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

	protected Object mSetGet(final int propertyId, final Object newValue) {
		switch (propertyId) {
			case P_username :
				if (newValue != GET_ONLY)
					setUsername((String) newValue);
				return getUsername();

			case P_emailAddress :
				if (newValue != GET_ONLY)
					setEmailAddress((String) newValue);
				return getEmailAddress();
		}
		throw new InvalidPropertyException(propertyId);
	}
}
