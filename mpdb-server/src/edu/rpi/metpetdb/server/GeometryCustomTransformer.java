package edu.rpi.metpetdb.server;

import java.sql.SQLException;
import java.util.Map;

import net.sf.beanlib.PropertyInfo;
import net.sf.beanlib.spi.BeanTransformerSpi;
import net.sf.beanlib.spi.CustomBeanTransformerSpi;

import org.postgis.Geometry;
import org.postgis.Point;
import org.postgis.Polygon;

/**
 * Timestamp transformer. Needed to keep nanoseconds part (by default, beanLib
 * will convert it to simple Date if the setter only declares an argument of
 * type java.util.Date)
 * 
 * @author anthony
 * 
 */
public class GeometryCustomTransformer implements CustomBeanTransformerSpi {
	// ----
	// Attributes
	// ----
	private BeanTransformerSpi _beanTransformer;

	/**
	 * Constructor
	 * 
	 * @param beanTransformer
	 */
	public GeometryCustomTransformer(final BeanTransformerSpi beanTransformer) {
		_beanTransformer = beanTransformer;
	}

	/**
	 * Filter method
	 */
	

	public  boolean isTransformable(Object from, Class toClass,
			PropertyInfo info) {
		if ((from instanceof Point) && (toClass == Geometry.class)) {
			return true;
		} else if ((from instanceof Polygon) && (toClass == Geometry.class)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Transformation method
	 */
	@SuppressWarnings("unchecked")
	public <T> T transform(Object in, Class<T> toClass, PropertyInfo info) {
		Map<Object, Object> cloneMap = _beanTransformer.getClonedMap();
		Object clone = cloneMap.get(in);

		if (clone != null) {
			return (T) clone;
		}
		if (in instanceof Point) {
			Point point = (Point) in;

			try {
				clone = new Point(point.toString());
			} catch (SQLException sqle) {
				clone = new Point();
			}
		} else if (in instanceof Polygon) {
			Polygon polygon = (Polygon) in;

			try {
				clone = new Polygon(polygon.toString());
			} catch (SQLException sqle) {
				clone = new Polygon();
			}
		}
		cloneMap.put(in, clone);

		return (T) clone;
	}
}
