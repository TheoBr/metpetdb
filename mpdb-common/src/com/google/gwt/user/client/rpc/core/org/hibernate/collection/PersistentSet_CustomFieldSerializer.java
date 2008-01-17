package com.google.gwt.user.client.rpc.core.org.hibernate.collection;

import org.hibernate.Hibernate;
import org.hibernate.collection.PersistentSet;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.SerializationStreamReader;
import com.google.gwt.user.client.rpc.SerializationStreamWriter;

/**
 * Serializer for {@link org.hibernate.collection.PersistentSet}.
 * <p>
 * Initialized sets may not be sent to the client.
 */
public final class PersistentSet_CustomFieldSerializer {
	public static void deserialize(final SerializationStreamReader r,
			final PersistentSet c) throws SerializationException {
	}

	public static void serialize(final SerializationStreamWriter w,
			final PersistentSet c) throws SerializationException {
		if (Hibernate.isInitialized(c))
			throw new SerializationException("PersistentSet: " + c.getRole());
	}
}