package edu.rpi.metpetdb.client.model;

import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Store;

//import java.util.Set;

public class Reference extends MObject implements Comparable {
	private static final long serialVersionUID = 1L;

	private long id;

	@Field(index = Index.TOKENIZED, store = Store.NO)
	private String name;
	
	private GeoReference georef = null;
	
	public Reference() {
		
	}
	
	public Reference(final String name) {
		this.name = name;
	}

	public long getId() {
		return id;
	}

	public void setId(final long i) {
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
		return name != null ? name.hashCode() + (int)id : 0;
	}

	public String toString() {
		return name;
	}

	public boolean mIsNew() {
		return id == 0;
	}
	
	public int compareTo(Object r) {
		if(!(r instanceof Reference))
			throw new ClassCastException("Reference object expected");
		return this.getName().compareTo(((Reference) r).getName());
	}

	public GeoReference getGeoref() {
		return georef;
	}

	public void setGeoref(GeoReference georef) {
		this.georef = georef;
	}
}
