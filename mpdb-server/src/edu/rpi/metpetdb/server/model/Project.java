package edu.rpi.metpetdb.server.model;

import java.util.HashSet;
import java.util.Set;

import edu.rpi.metpetdb.client.error.InvalidPropertyException;

public class Project extends MObject {
	public static final int P_name = 0;
	public static final int P_owner = 1;

	private int id;
	private int version;
	
	private String name;
	
	private User owner;
	
	private Set<User> members;
	
   	private Set<Sample> samples;

	public int getId() {
		return id;
	}
	public void setId(final int i) {
		id = i;
	}

	public int getVersion() {
		return version;
	}
	public void setVersion(final int v) {
		version = v;
	}

	public String getName() {
		return name;
	}
	public void setName(final String s) {
		name = s;
	}

	public User getOwner() {
		return owner;
	}
	public void setOwner(final User u) {
		owner = u;
	}

	public Set getMembers() {
		if (members == null)
			members = new HashSet();
		return members;
	}
	public void setMembers(final Set c) {
		members = c;
	}

	public Set getSamples() {
		if (samples == null)
			samples = new HashSet();
		return samples;
	}
	public void setSamples(final Set c) {
		samples = c;
	}

	public int hashCode() {
		return id;
	}
	public boolean equals(final Object o) {
		return o instanceof Project && id == ((Project) o).id;
	}

	public boolean mIsNew() {
		return id == 0;
	}
	protected Object mSetGet(final int propertyId, final Object newValue) {
		switch (propertyId) {
			case P_name :
				if (newValue != GET_ONLY)
					setName((String) newValue);
				return getName();

			case P_owner :
				if (newValue != GET_ONLY)
					setOwner((User) newValue);
				return getOwner();
		}
		throw new InvalidPropertyException(propertyId);
	}
}
