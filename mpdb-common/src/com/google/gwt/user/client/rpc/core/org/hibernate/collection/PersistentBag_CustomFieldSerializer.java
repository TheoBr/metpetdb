package com.google.gwt.user.client.rpc.core.org.hibernate.collection;

import org.hibernate.Hibernate;
import org.hibernate.collection.PersistentBag;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.SerializationStreamReader;
import com.google.gwt.user.client.rpc.SerializationStreamWriter;

/**
 * Serializer for {@link org.hibernate.collection.PersistentBag}.
 * <p>
 * Initialized bags may not be sent to the client.
 */
public final class PersistentBag_CustomFieldSerializer {
	public static void deserialize(final SerializationStreamReader r,
			final PersistentBag c) throws SerializationException {
	}

	public static void serialize(final SerializationStreamWriter w,
			final PersistentBag c) throws SerializationException {
		if (Hibernate.isInitialized(c))
			throw new SerializationException("PersistentBag: " + c.getRole());
	}
}
