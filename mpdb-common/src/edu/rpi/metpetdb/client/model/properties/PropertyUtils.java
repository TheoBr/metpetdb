package edu.rpi.metpetdb.client.model.properties;

public class PropertyUtils {

	public static Float convertToFloat(final Object o) {
		if (o instanceof Float) {
			return (Float) o;
		} else if (o instanceof String) {
			return Float.parseFloat(o.toString());
		} else if (o instanceof Double){
			return ((Double) o).floatValue();
		} else if (o instanceof Integer){
			return ((Integer) o).floatValue();
		} else
			return null;
	}
	
	public static Double convertToDouble(final Object o) {
		if (o instanceof Double) {
			return (Double) o;
		} else if (o instanceof String) {
			return Double.parseDouble(o.toString());
		} else if (o instanceof Float) {
			return ((Float) o).doubleValue();
		} else if (o instanceof Integer) {
			return ((Integer) o).doubleValue();
		}  else
			return null;
	}

	public static Integer convertToInteger(final Object o) {
		if (o instanceof Integer) {
			return (Integer) o;
		} else if (o instanceof String) {
			try {
				return Integer.parseInt(o.toString());
			} catch (NumberFormatException nfe){
				return new Float(Float.parseFloat(o.toString())).intValue();
			}
		} else if (o instanceof Float) {
			return ((Float) o).intValue();
		} else if (o instanceof Double) {
			return ((Double) o).intValue();
		} else
			return 0; //if we cant convert it just return 0
		
	}

}
