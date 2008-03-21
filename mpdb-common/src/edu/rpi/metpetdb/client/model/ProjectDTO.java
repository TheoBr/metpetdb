package edu.rpi.metpetdb.client.model;

import java.util.HashSet;
import java.util.Set;

public class ProjectDTO extends MObjectDTO {

	private static final long serialVersionUID = 1L;
	private int id;
	private int version;

	private String name;

	private UserDTO owner;

	private Set<UserDTO> members;

	private Set<SampleDTO> samples;

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

	public UserDTO getOwner() {
		return owner;
	}

	public void setOwner(final UserDTO u) {
		owner = u;
	}

	public Set<UserDTO> getMembers() {
		if (members == null)
			members = new HashSet<UserDTO>();
		return members;
	}

	public void setMembers(final Set<UserDTO> c) {
		members = c;
	}

	public Set<SampleDTO> getSamples() {
		if (samples == null)
			samples = new HashSet<SampleDTO>();
		return samples;
	}

	public void setSamples(final Set<SampleDTO> c) {
		samples = c;
	}

	public int hashCode() {
		return id;
	}

	public boolean equals(final Object o) {
		return o instanceof ProjectDTO && id == ((ProjectDTO) o).id;
	}

	public boolean mIsNew() {
		return id == 0;
	}
}
