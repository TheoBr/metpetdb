package org.hibernate.collection;

import java.util.HashSet;

/** Emulation of Hibernate's PersistentSet for browsers. */
public class PersistentSet extends HashSet implements PersistentCollection {
	public Object getOwner() {
		return null;
	}
	public String getRole() {
		return null;
	}
}