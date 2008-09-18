package edu.rpi.metpetdb.client.model;

import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Store;

public class RockTypeDTO extends MObjectDTO {
	private static final long serialVersionUID = 1L;

	private short id;
	@Field(index = Index.TOKENIZED, store = Store.NO)
	private String rockType;
	
	public RockTypeDTO() {
		
	}

	public short getId() {
		return id;
	}

	public void setId(final short i) {
		id = i;
	}

	public String getRockType() {
		return rockType;
	}

	public void setRockType(final String s) {
		rockType = s;
	}

	public boolean equals(final Object o) {
		return rockType != null && o instanceof RockTypeDTO
				&& rockType.toLowerCase().equals(((RockTypeDTO) o).rockType.toLowerCase());
	}

	public int hashCode() {
		return rockType != null ? rockType.hashCode() : 0;
	}

	public String toString() {
		return rockType;
	}

	public boolean mIsNew() {
		return id == 0;
	}
}
