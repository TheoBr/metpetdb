package edu.rpi.metpetdb.client.model.properties;

public class PropertyUtils {

	public static Float convertToFloat(final Object o) {
		if (o instanceof Float) {
			return (Float) o;
		} else if (o instanceof String) {
			return Float.parseFloat(o.toString());
		} else
			return null;
	}

	public static Integer convertToInteger(final Object o) {
		if (o instanceof Integer) {
			return (Integer) o;
		} else if (o instanceof String) {
			return Integer.parseInt(o.toString());
		} else
			return null;
	}

}