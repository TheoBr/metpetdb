package edu.rpi.metpetdb.server;

import java.io.IOException;
import java.security.Principal;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Query;
import org.hibernate.Session;

import edu.rpi.metpetdb.client.model.Sample;
import edu.rpi.metpetdb.client.model.User;
import edu.rpi.metpetdb.server.MpDbServlet.Req;
public class BasicKML extends HttpServlet {
	private static final long serialVersionUID = 1L;
	// private static final Double metersInLatDegree = 110874.40;
	private static final String samplesParameter = "Samples";
	private static final String urlParameter = "url";
	
	

	protected void doGet(final HttpServletRequest request,
			final HttpServletResponse response) throws ServletException {

		// This method takes a list of sample IDs and creates KML from them
		// Ex: http://example.com/metpetdb/BasicKML.kml?1,2,3 would get samples
		// 1, 2, and 3
		
		// response.setContentType("text/plain"); // Useful for testing
		response.setContentType("application/vnd.google-earth.kml+xml");
		
		final User u = (User) request.getSession().getAttribute("user");
		final int userId;
		if (u == null)
			userId = 0;
		else
			userId = u.getId();

		Session session = DataStore.open();
		DataStore.enableSecurityFilters(session, userId);
		session.beginTransaction();

		List<Sample> samples = new LinkedList<Sample>();
		Query q;
		String baseURL = "";
		
		if (request.getParameter(urlParameter) != null) {
			baseURL = request.getParameterValues(urlParameter)[0];
		}

		

		// If there is a GET string, fetch by ids
		if (request.getParameter(samplesParameter) != null) {
			String ids[] = request.getParameterValues(samplesParameter);
			q = session.getNamedQuery("Sample.byId");
			for (int i = 0; i < ids.length; i++) {
				q.setParameter("id", Long.parseLong(ids[i]));
				samples.add((Sample) q.uniqueResult());
			}
		}
		// Otherwise get all samplesrequest.getQueryString()
		else {
			samples = session.createQuery("from Sample").list();
		}
		
		session.close();

		try {
			response.getWriter().write(KMLCreater.createKML(samples, baseURL));
		} catch (final IOException ioe) {
			throw new IllegalStateException(ioe.getMessage());
		}
		return;
	}

}
