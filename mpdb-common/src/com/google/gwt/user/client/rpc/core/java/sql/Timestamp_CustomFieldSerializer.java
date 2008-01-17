package com.google.gwt.user.client.rpc.core.java.sql;

import java.sql.Timestamp;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.SerializationStreamReader;
import com.google.gwt.user.client.rpc.SerializationStreamWriter;

/** Custom field serializer for {@link java.sql.Timestamp}. */
public final class Timestamp_CustomFieldSerializer {
	public static void deserialize(
			final SerializationStreamReader streamReader,
			final Timestamp instance) {
		// No fields
	}

	public static Timestamp instantiate(
			final SerializationStreamReader streamReader)
			throws SerializationException {
		return new Timestamp(streamReader.readLong());
	}

	public static void serialize(final SerializationStreamWriter streamWriter,
			final Timestamp instance) throws SerializationException {
		streamWriter.writeLong(instance.getTime());
	}
}