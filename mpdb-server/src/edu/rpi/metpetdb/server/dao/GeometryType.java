package edu.rpi.metpetdb.server.dao;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.hibernate.HibernateException;
import org.hibernate.usertype.UserType;
import org.postgis.Geometry;
import org.postgis.Point;
import org.postgis.binary.BinaryParser;
import org.postgis.binary.BinaryWriter;

import edu.rpi.metpetdb.client.service.MpDbConstants;

public class GeometryType implements UserType {
	private static final int[] SQL_TYPES = {Types.BLOB};

	public Object deepCopy(Object value) throws HibernateException {
		return value;
	}

	public boolean equals(Object x, Object y) throws HibernateException {
		if (x == y)
			return true;
		else if (x == null || y == null)
			return false;
		else
			return x.equals(y);
	}

	public int hashCode(Object arg0) throws HibernateException {
		return arg0 != null ? arg0.hashCode() : 0;
	}

	public boolean isMutable() {
		return false;
	}

	public Object nullSafeGet(ResultSet resultSet, String[] names, Object owner)
			throws HibernateException, SQLException {
		final String geom = resultSet.getString(names[0]);
		if (geom != null) {
			BinaryParser parser = new BinaryParser();
			return (Point) parser.parse(geom);
		}
		return null;
	}

	public Point pointFromGeom(Geometry geom) {
		Point p = (Point) geom;
		return p;
	}

	/*
	 * // BinaryParser class method: private parsePoint(Valuegetter, boolean,
	 * boolean) // private Point parsePoint(org.postgis.binary.ValueGetter arg0, //
	 * boolean arg1, boolean arg2) public Point pointFromGeotype() { Point p;
	 * 
	 * BinaryParser parser = new BinaryParser(); parser.parse(arg0); }
	 */

	public void nullSafeSet(PreparedStatement statement, Object value, int index)
			throws HibernateException, SQLException {
		if (value == null)
			statement.setBytes(index, null);
		else {
			final BinaryWriter bw = new BinaryWriter();
			final byte[] bytes = bw.writeBinary((Point) value);
			statement.setBytes(index, bytes);
		}
	}

	public String getTheGeomFromText(Geometry geometry) {
		String returnVal = "null";
		if (geometry instanceof Point) {
			Point point = (Point) geometry;
			returnVal = "GeomFromText('POINT(" + point.getX() + " "
					+ point.getY() + ")'," + MpDbConstants.WGS84 + ")";
		}
		return returnVal;
	}

	public Object replace(Object original, Object target, Object owner)
			throws HibernateException {
		return original;
	}

	public Class returnedClass() {
		return Geometry.class;
	}

	public int[] sqlTypes() {
		return GeometryType.SQL_TYPES;
	}

	public Object assemble(Serializable cached, Object owner)
			throws HibernateException {
		return cached;
	}

	public Serializable disassemble(Object value) throws HibernateException {
		return (Serializable) value;
	}
}