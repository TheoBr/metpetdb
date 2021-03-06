package edu.rpi.metpetdb.client.model;

import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Store;

//import java.util.Set;

public class Region extends MObject implements Comparable {
	private static final long serialVersionUID = 1L;

	private short id;

	@Field(index = Index.TOKENIZED, store = Store.NO)
	private String name;
	
	public Region() {
		
	}
	
	public Region(final String name) {
		this.name = name;
	}

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
		return name != null && o instanceof Region
				&& name.equals(((Region) o).name) && ((Region) o).getId() == id;
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
	
	public int compareTo(Object r) {
		if(!(r instanceof Region))
			throw new ClassCastException("Region object expected");
		return this.getName().compareTo(((Region) r).getName());
	}
}
