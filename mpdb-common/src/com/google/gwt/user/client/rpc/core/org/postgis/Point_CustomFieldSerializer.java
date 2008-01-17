package com.google.gwt.user.client.rpc.core.org.postgis;

import org.postgis.Point;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.SerializationStreamReader;
import com.google.gwt.user.client.rpc.SerializationStreamWriter;

import edu.rpi.metpetdb.client.service.MpDbConstants;

/** Custom field serializer for {@link org.postgis.Geometry}. */
public final class Point_CustomFieldSerializer {
	public static void deserialize(
			final SerializationStreamReader streamReader, final Point instance) {
		// No fields
	}

	public static Point instantiate(final SerializationStreamReader streamReader)
			throws SerializationException {
		final Point p = new Point();
		p.dimension = 2;
		p.srid = MpDbConstants.WGS84;
		p.x = streamReader.readDouble();
		p.y = streamReader.readDouble();
		return p;
	}

	public static void serialize(final SerializationStreamWriter streamWriter,
			final Point p) throws SerializationException {
		if (p.dimension == 2 && p.srid == MpDbConstants.WGS84) {
			streamWriter.writeDouble(p.x);
			streamWriter.writeDouble(p.y);
			return;
		}
		throw new SerializationException("Unsupported geometry: " + p);
	}
}