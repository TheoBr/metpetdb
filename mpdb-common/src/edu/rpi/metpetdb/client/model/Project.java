package edu.rpi.metpetdb.client.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Entity;
import org.hibernate.annotations.Index;
import org.hibernate.search.annotations.ContainedIn;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;

import edu.rpi.metpetdb.client.error.InvalidPropertyException;

@Entity
@Indexed(index="indices/Project")
public class Project extends MObject {
	public static final int P_name = 0;
	public static final int P_owner = 1;

    @Id
    @DocumentId
	private int id;
	private int version;
	
	@Field
	private String name;
	
	@ManyToOne
	@IndexedEmbedded(depth = 1)
	private User owner;
	
	@ManyToMany
	@IndexedEmbedded(depth = 1)
	private Set<User> members;
	
    @ContainedIn
    @ManyToMany(mappedBy="address")
	//????????????? do it in both
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
