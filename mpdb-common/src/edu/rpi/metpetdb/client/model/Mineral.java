package edu.rpi.metpetdb.client.model;

import java.util.Set;

import edu.rpi.metpetdb.client.error.InvalidPropertyException;
import edu.rpi.metpetdb.client.model.interfaces.IHasChildren;
import edu.rpi.metpetdb.client.model.interfaces.IHasName;

public class Mineral extends MObject implements IHasChildren, IHasName {
	public static final int P_name = 0;

	private short id;
	private Short parentId;
	private String name;
	private Set children;

	public short getId() {
		return id;
	}

	public void setId(final short i) {
		id = i;
	}

	public String getName() {
		return name;
	}

	public void setName(final String s) {
		name = s;
	}

	public Short getParentId() {
		return parentId;
	}

	public void setParentId(final Short i) {
		parentId = i;
	}

	public void setChildren(final Set c) {
		children = c;
	}

	public Set getChildren() {
		return children;
	}

	public boolean equals(final Object o) {
		if (o instanceof Mineral)
			return name != null && o instanceof Mineral
					&& name.equals(((Mineral) o).name)
					&& ((Mineral) o).getId() == id;
		else if (o instanceof SampleMineral) {
			return name != null && name.equals(((SampleMineral) o).getName())
					&& ((SampleMineral) o).getId() == id;
		} else
			return false;
	}

	public int hashCode() {
		return name != null ? name.hashCode() + id : 0;
	}

	public String toString() {
		return name;
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
		}
		throw new InvalidPropertyException(propertyId);
	}
}
