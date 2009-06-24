package edu.rpi.metpetdb.client.model;

import java.util.HashSet;
import java.util.Set;

public class Project extends MObject {
	private static final long serialVersionUID = 1L;

	private int id;
	private int version;

	private String name;
	
	private String description;

	private User owner;

	private Set<User> members;

	private Set<Sample> samples;
	
	private Set<User> invites;

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
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(final String d) {
		description = d;
	}

	public User getOwner() {
		return owner;
	}

	public void setOwner(final User u) {
		owner = u;
	}

	public Set<User> getMembers() {
		if (members == null)
			members = new HashSet<User>();
		return members;
	}

	public void setMembers(final Set<User> c) {
		members = c;
	}

	public Set<Sample> getSamples() {
		if (samples == null)
			samples = new HashSet<Sample>();
		return samples;
	}

	public void setSamples(final Set<Sample> c) {
		samples = c;
	}
	
	public Set<User> getInvites() {
		if(invites == null)
			invites = new HashSet<User>();
		return invites;
	}
	
	public void setInvites(final Set<User> i) {
		invites = i;
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

}
