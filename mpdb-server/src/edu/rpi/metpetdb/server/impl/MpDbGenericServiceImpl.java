package edu.rpi.metpetdb.server.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import edu.rpi.metpetdb.client.model.UserDTO;
import edu.rpi.metpetdb.client.service.MpDbGenericService;
import edu.rpi.metpetdb.client.service.ResumeSessionResponse;
import edu.rpi.metpetdb.server.DataStore;
import edu.rpi.metpetdb.server.EmailSupport;
import edu.rpi.metpetdb.server.MpDbServlet;

public class MpDbGenericServiceImpl extends MpDbServlet implements
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

	public UserDTO getAutomaticLoginUser() {
		final String propFile = "autologin.properties";
		final InputStream i = EmailSupport.class.getClassLoader()
				.getResourceAsStream(propFile);
		if (i == null)
			return null;
		final Properties props = new Properties();
		try {
			props.load(i);
			i.close();
		} catch (IOException ioe) {
			return null;
		}
		final boolean enabled = (Integer.parseInt(props.getProperty("enabled")) == 1 ? true
				: false);
		if (enabled) {
			final int userId = Integer.parseInt(props.getProperty("userid"));

			final String username = props.getProperty("username");
			final String emailAddress = props.getProperty("emailaddress");
			final UserDTO u = new UserDTO();
			u.setEmailAddress(emailAddress);
			u.setId(userId);
			u.setVersion(0);
			return u;
		} else {
			return null;
		}
	}

	public ResumeSessionResponse regenerateConstraints() {
		DataStore.getInstance().resetConstraints();
		final ResumeSessionResponse r = new ResumeSessionResponse();
		doc = DataStore.getInstance().getDatabaseObjectConstraints();
		oc = DataStore.getInstance().getObjectConstraints();
		r.databaseObjectConstraints = doc;
		r.objectConstraints = oc;
		return r;
	}

}
