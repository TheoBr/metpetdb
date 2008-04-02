package edu.rpi.metpetdb.server.model;

import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Store;

//import java.util.Set;

@Indexed
public class MetamorphicGrade extends MObject {
	private static final long serialVersionUID = 1L;

	@DocumentId
	private short id;

	@Field(index = Index.TOKENIZED, store = Store.NO)
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
		return name != null && o instanceof MetamorphicGrade
				&& name.equals(((MetamorphicGrade) o).name)
				&& ((MetamorphicGrade) o).getId() == id;
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
