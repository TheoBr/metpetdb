package edu.rpi.metpetdb.server.model;

import java.util.Set;

import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Store;

import edu.rpi.metpetdb.client.model.interfaces.IHasChildren;
import edu.rpi.metpetdb.client.model.interfaces.IHasName;

public class Mineral extends MObject implements IHasName {
	private static final long serialVersionUID = 1L;
	private short id;
	private Short parentId;
	@Field(index = Index.TOKENIZED, store = Store.NO)
	private String name;

	private Set<IHasChildren<Mineral>> children;

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

	public void setChildren(final Set<IHasChildren<Mineral>> c) {
		children = c;
	}

	public Set<IHasChildren<Mineral>> getChildren() {
		return children;
	}

	public boolean equals(final Object o) {
		if (o instanceof Mineral)
			return name != null && o instanceof Mineral
					&& name.equals(((Mineral) o).name)
					&& ((Mineral) o).getId() == id;
		else if (o instanceof SampleMineral) {
			return name != null && name.equals(((SampleMineral) o).getName())
					&& ((SampleMineral) o).getMineral().getId() == id;
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
}
