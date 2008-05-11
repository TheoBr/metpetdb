package edu.rpi.metpetdb.server;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Query;
import org.hibernate.Session;

import edu.rpi.metpetdb.server.model.Sample;

public class BasicKML extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(final HttpServletRequest request,
			final HttpServletResponse response) throws ServletException {

		// This method takes a list of sample IDs and creates KML from them
		// Ex: http://example.com/metpetdb/BasicKML.kml?1,2,3 would get samples
		// 1, 2, and 3

		// TODO: change this to use theSample.getLocation() instead of latitude
		// and longitude errors
		// TODO: extend the description of the placemarks generated

		// response.setContentType("text/plain"); // Useful for testing
		response.setContentType("application/vnd.google-earth.kml+xml");

		Session session = DataStore.open();
		session.beginTransaction();

		List<Sample> samples = new LinkedList<Sample>();
		Query q;

		// If there is a GET string, fetch by ids
		if (request.getQueryString() != null) {
			String ids[] = request.getQueryString().split(",");
			q = session.getNamedQuery("Sample.byId");
			for (int i = 0; i < ids.length; i++) {
				q.setParameter("id", Long.parseLong(ids[i]));
				samples.add((Sample) q.uniqueResult());
			}
		}
		// Otherwise get all samples
		else {
			samples = session.createQuery("from Sample").list();
		}

		try {
			response.getWriter().write("<Document id='doc0'>\n");
			for (int i = 0; i < samples.size(); i++) {
				Sample theSample = (Sample) samples.get(i);
				response.getWriter().write(" <Placemark>\n");
				response.getWriter().write(
						"<name>" + theSample.getAlias() + "</name>\n");
				response.getWriter().write(" <Point>\n");

				response.getWriter().write(
						" <coordinates>" + theSample.getLongitudeError() + ","
								+ theSample.getLatitudeError() + ",0"
								+ "</coordinates>\n");
				response.getWriter().write(" </Point>\n");
				response.getWriter().write(" </Placemark>\n");
			}
			response.getWriter().write("</Document>");
		} catch (final IOException ioe) {
			throw new IllegalStateException(ioe.getMessage());
		}
		return;
	}
}
