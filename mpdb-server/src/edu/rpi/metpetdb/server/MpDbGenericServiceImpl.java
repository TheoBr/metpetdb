package edu.rpi.metpetdb.server;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import edu.rpi.metpetdb.client.service.MpDbGenericService;

public class MpDbGenericServiceImpl extends MpDbServlet
		implements
			MpDbGenericService {

	private static final long serialVersionUID = 1L;

	/**
	 * Just returns the date the ant script was executed, hence the build date
	 * of the project
	 * 
	 * @return build date if successful, otherwise it returns ""
	 */

	public String getBuildDate() {
		final String propFile = "builddate.properties";
		final InputStream i = EmailSupport.class.getClassLoader()
				.getResourceAsStream(propFile);
		if (i == null)
			return "";
		final Properties props = new Properties();
		try {
			props.load(i);
			i.close();
		} catch (IOException ioe) {
			return ioe.getMessage();
		}
		final String buildDateString = props.getProperty("builddate");
		return buildDateString;
	}

}
