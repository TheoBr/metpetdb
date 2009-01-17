package edu.rpi.metpetdb.client.model.validation;

import org.postgis.Geometry;
import org.postgis.Point;
import org.postgis.Polygon;

import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.error.validation.InvalidGeometryException;
import edu.rpi.metpetdb.client.service.MpDbConstants;

/**
 * Applies to any PostGIS {@link Geometry} instance.
 * <p>
 * Verifies the geometry is in WGS 84 (our standard coordinate system) and has
 * only two dimensions. It also checks that the geometry is supported, as we
 * don't support/implement all types available under PostGIS.
 * </p>
 */
public class GeometryConstraint extends PropertyConstraint {
	public void validateValue(final Object value) throws ValidationException {
		super.validateValue(value);
		if (value == null)
			return;
		final Geometry g = (Geometry) value;
		if (g.srid != MpDbConstants.WGS84)
			throw new InvalidGeometryException(this);
		if (g.dimension != 2)
			throw new InvalidGeometryException(this);

		if (g instanceof Point || g instanceof Polygon) {
			if (g instanceof Point) {
				//Check lat/long coordinates
				if (((Point)g).x > 180 || ((Point)g).x < -180)
					throw new InvalidGeometryException(this);
				if (((Point)g).y > 90 || ((Point)g).y < -90)
					throw new InvalidGeometryException(this);
			}
		} else
			throw new InvalidGeometryException(this);
	}
}
