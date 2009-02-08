package edu.rpi.metpetdb.client.ui;

public class Util {
	
	/**
	 * Converts \n to <pre></br></pre>
	 * @param data
	 * @return
	 */
	public static String convertForWeb(final String data) {
		return data.replaceAll("\\n", "<br/>");
	}

}
