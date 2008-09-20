package com.google.gwt.user.client.rpc.core.org.postgis;

import org.postgis.LinearRing;
import org.postgis.Point;
import org.postgis.Polygon;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.SerializationStreamReader;
import com.google.gwt.user.client.rpc.SerializationStreamWriter;

import edu.rpi.metpetdb.client.service.MpDbConstants;

public class Polygon_CustomFieldSerializer {
	public static void deserialize(
			final SerializationStreamReader streamReader, final Polygon instance) {
	}

	public static Polygon instantiate(
			final SerializationStreamReader streamReader)
			throws SerializationException {
		final int numberOfPoints = streamReader.readInt();
		final Point[] points = new Point[numberOfPoints];
		for (int i = 0; i < numberOfPoints; ++i) {
			final Point point = new Point();
			point.x = streamReader.readDouble();
			point.y = streamReader.readDouble();
			points[i] = point;
		}
		final LinearRing[] ringArray = new LinearRing[1];
		final LinearRing ring = new LinearRing(points);
		ringArray[0] = ring;
		final Polygon p = new Polygon(ringArray);
		p.dimension = 2;
		p.srid = MpDbConstants.WGS84;
		return p;
	}

	public static void serialize(final SerializationStreamWriter streamWriter,
			final Polygon p) throws SerializationException {
		if (p.dimension == 2 && p.srid == MpDbConstants.WGS84) {
			final LinearRing ring = p.getRing(0);
			streamWriter.writeInt(ring.numPoints());
			for (int i = 0; i < ring.numPoints(); ++i) {
				final Point point = ring.getPoint(i);
				streamWriter.writeDouble(point.x);
				streamWriter.writeDouble(point.y);
			}
			return;
		}
		throw new SerializationException("Unsupported geometry: " + p);
	}
}
