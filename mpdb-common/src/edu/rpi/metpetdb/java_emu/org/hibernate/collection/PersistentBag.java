package org.hibernate.collection;

import java.util.ArrayList;

/** Emulation of Hibernate's PersistentBag for browsers. */
public class PersistentBag extends ArrayList implements PersistentCollection {
	public Object getOwner() {
		return null;
	}
	public String getRole() {
		return null;
	}
}