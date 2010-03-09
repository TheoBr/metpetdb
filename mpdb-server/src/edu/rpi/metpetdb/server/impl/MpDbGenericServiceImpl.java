package edu.rpi.metpetdb.server.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import edu.rpi.metpetdb.client.model.ResumeSessionResponse;
import edu.rpi.metpetdb.client.model.User;
import edu.rpi.metpetdb.client.service.MpDbGenericService;
import edu.rpi.metpetdb.server.DataStore;
import edu.rpi.metpetdb.server.EmailSupport;
import edu.rpi.metpetdb.server.MpDbServlet;
import edu.rpi.metpetdb.server.dao.impl.ChemicalAnalysisDAO;
import edu.rpi.metpetdb.server.dao.impl.GeoReferenceDAO;
import edu.rpi.metpetdb.server.dao.impl.ImageDAO;
import edu.rpi.metpetdb.server.dao.impl.SampleDAO;

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

	public User getAutomaticLoginUser() {
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
			final User u = new User();
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
	
	public long getCurrentTime(){
		final Date now = new Date();
		return now.getTime();
	}

	public List<List> getStatistics(){
		List<List> stats = new ArrayList<List>();
		List labels = new ArrayList();
		List counts = new ArrayList();
		stats.add(labels);
		stats.add(counts);
		
		DataStore.disableSecurityFilters(this.currentSession());
		
		labels.add(" Publication Samples");
		counts.add(new SampleDAO(this.currentSession()).getPublicationCount());
		labels.add(" Publication Chemical Analyses");
		counts.add(new ChemicalAnalysisDAO(this.currentSession()).getPublicationCount());
		labels.add(" Publication Images");
		counts.add(new ImageDAO(this.currentSession()).getPublicationCount());
		
		labels.add(" Public Samples");
		counts.add(new SampleDAO(this.currentSession()).getPublicCount());
		labels.add(" Public Chemical Analyses");
		counts.add(new ChemicalAnalysisDAO(this.currentSession()).getPublicCount());
		labels.add(" Public Images");
		counts.add(new ImageDAO(this.currentSession()).getPublicCount());

		labels.add(" Private Samples");
		counts.add(new SampleDAO(this.currentSession()).getPrivateCount());
		labels.add(" Private Chemical Analyses");
		counts.add(new ChemicalAnalysisDAO(this.currentSession()).getPrivateCount());
		labels.add(" Private Images");
		counts.add(new ImageDAO(this.currentSession()).getPrivateCount());

		labels.add(" Referenced Publicationss");
		counts.add(new GeoReferenceDAO(this.currentSession()).getCount());
		
		DataStore.enableSecurityFilters(this.currentSession(), this.currentUserIdIfExists());
		
		return stats;
	}
}
