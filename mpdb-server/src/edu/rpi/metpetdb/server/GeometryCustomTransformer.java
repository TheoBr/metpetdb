package edu.rpi.metpetdb.server;

import java.sql.SQLException;
import java.util.Map;

import net.sf.beanlib.PropertyInfo;
import net.sf.beanlib.spi.BeanTransformerSpi;
import net.sf.beanlib.spi.CustomBeanTransformerSpi;

import org.postgis.Geometry;
import org.postgis.Point;

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
	public <T> boolean isTransformable(Object from, Class<T> toClass,
			PropertyInfo info) {
		return ((from instanceof Point) && (toClass == Geometry.class));
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

		Point point = (Point) in;
		try {
			clone = new Point(point.toString());
		} catch (SQLException sqle) {
			clone = new Point();
		}

		cloneMap.put(in, clone);

		return (T) clone;
	}
}
