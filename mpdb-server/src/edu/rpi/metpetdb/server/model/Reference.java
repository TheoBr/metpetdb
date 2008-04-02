package edu.rpi.metpetdb.server.model;

//import java.util.Set;

public class Reference extends MObject {
	private static final long serialVersionUID = 1L;

	private short id;

	private String name;

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

	public boolean equals(final Object o) {
		return name != null && o instanceof Reference
				&& name.equals(((Reference) o).name)
				&& ((Reference) o).getId() == id;
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
