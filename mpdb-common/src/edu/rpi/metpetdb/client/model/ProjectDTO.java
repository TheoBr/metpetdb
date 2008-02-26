package edu.rpi.metpetdb.client.model;

import java.util.HashSet;
import java.util.Set;

import edu.rpi.metpetdb.client.error.InvalidPropertyException;

public class ProjectDTO extends MObjectDTO {
	public static final int P_name = 0;
	public static final int P_owner = 1;

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
		return o instanceof ProjectDTO && id == ((ProjectDTO) o).id;
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
					setOwner((UserDTO) newValue);
				return getOwner();
		}
		throw new InvalidPropertyException(propertyId);
	}
}