package org.hibernate;

/** Emulation of Hibernate's Hibernate for browsers. */
public class Hibernate {
	public static boolean isInitialized(final Object obj) {
		// Objects that made it to the client are not initialized,
		// thereby allowing our custom deserializer to work.
		//
		return false;
	}

	private Hibernate() {
	}
}