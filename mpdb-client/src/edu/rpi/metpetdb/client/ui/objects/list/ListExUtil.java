package edu.rpi.metpetdb.client.ui.objects.list;

public class ListExUtil {
	public final static int latlngDigits = 5;
	public final static int defaultDigits = 3;
	
	public static String formatDouble(final double loc, final int decimalPlaces) {
		final String locStr = String.valueOf(loc);
		final int decPos = locStr.toString().indexOf(".");
		if (locStr.length() <= decPos + decimalPlaces && decPos >= 0)
			return locStr;
		return locStr.substring(0, decPos + decimalPlaces);
	}
}
