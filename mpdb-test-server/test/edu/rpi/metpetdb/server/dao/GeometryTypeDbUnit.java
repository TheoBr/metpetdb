package edu.rpi.metpetdb.server.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.dbunit.dataset.datatype.BlobDataType;
import org.dbunit.dataset.datatype.TypeCastException;
import org.postgis.PGgeometry;
import org.postgis.Point;
import org.postgis.binary.BinaryParser;
import org.postgis.binary.BinaryWriter;

/**
 * Required so that DbUnit can interface with the geometry type
 * @author anthony
 *
 */
public class GeometryTypeDbUnit extends BlobDataType {
	@Override
	public Object getSqlValue(int column, ResultSet resultSet)
			throws SQLException, TypeCastException {
		final String geom = resultSet.getString(column);
		if (geom != null) {
			BinaryParser parser = new BinaryParser();
			return ((Point) parser.parse(geom)).toString();
		}
		return null;
	}

	@Override
	public void setSqlValue(Object value, int column,
			PreparedStatement statement) throws SQLException, TypeCastException {
		if (value == null)
			statement.setBytes(column, null);
		else {
			final Point p =  (Point) PGgeometry.geomFromString((String)value);
			final BinaryWriter bw = new BinaryWriter();
			final byte[] bytes = bw.writeBinary(p);
			statement.setBytes(column, bytes);
		}
	}
}