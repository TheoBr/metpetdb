package org.hibernate.collection;

/** Emulation of Hibernate's PersistentCollection for browsers. */
public interface PersistentCollection {
	public Object getOwner();
	public String getRole();
}