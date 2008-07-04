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
import org.postgis.Point;

import edu.rpi.metpetdb.server.model.Sample;

public class BasicKML extends HttpServlet {
	private static final long serialVersionUID = 1L;
	// private static final Double metersInLatDegree = 110874.40;

	// private static final String baseURL =
	// "http://localhost:8888/edu.rpi.metpetdb.MetPetDBApplication/MetPetDBApplication.html#SampleDetails-";
	private static final String baseURL = "http://samana.cs.rpi.edu:8080/metpetwebtst/#SampleDetails-";
	private Double lat;
	private Double lng;
	private Double latErr;
	private Double lngErr;

	protected void doGet(final HttpServletRequest request,
			final HttpServletResponse response) throws ServletException {

		// This method takes a list of sample IDs and creates KML from them
		// Ex: http://example.com/metpetdb/BasicKML.kml?1,2,3 would get samples
		// 1, 2, and 3

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
			response.getWriter().write(
					"<Style id='RedPerimeter'>\n" + "<LineStyle>\n"
							+ "<width>2</width>\n"
							+ "<color>7d0000ff</color>\n" + "</LineStyle>\n"
							+ "<PolyStyle>\n" + "<color>7d0000ff</color>\n"
							+ "</PolyStyle>\n" + "</Style>\n");

			for (int i = 0; i < samples.size(); i++) {
				Sample theSample = (Sample) samples.get(i);
				lat = ((Point) theSample.getLocation()).x;
				lng = ((Point) theSample.getLocation()).y;
				if (theSample.getLatitudeError() != null)
					latErr = theSample.getLatitudeError().doubleValue();
				else
					latErr = 0D;
				if (theSample.getLongitudeError() != null)
					lngErr = theSample.getLongitudeError().doubleValue();
				else
					lngErr = 0D;

				response.getWriter().write(" <Folder>\n");
				response.getWriter().write(
						"<name>" + theSample.getAlias() + "</name>\n");
				response.getWriter().write(" <Placemark>\n");
				response.getWriter().write(
						"<name>" + theSample.getAlias() + "</name>\n");

				response.getWriter().write(" <description>");
				response.getWriter().write(
						"<![CDATA[\n" + "<a href='" + baseURL
								+ theSample.getId() + "'>"
								+ theSample.getAlias() + "</a>  ]]>");
				response.getWriter().write(
						": " + theSample.getDescription() + "</description>\n");
				response.getWriter().write(" <Point>\n");

				response.getWriter().write(
						" <coordinates>" + lng + "," + lat + ",0"
								+ "</coordinates>\n");
				response.getWriter().write(" </Point>\n");
				response.getWriter().write(" </Placemark>\n");

				response.getWriter().write(" <Placemark>\n");
				response.getWriter().write(" <name>Error Perimeter</name>\n");
				response
						.getWriter()
						.write(
								" <description>This is the area that "
										+ theSample.getAlias()
										+ " can be found in based on it's latitutde and longitude errors </description>\n");
				response.getWriter().write(
						" <styleUrl>#RedPerimeter</styleUrl>\n ");
				response.getWriter().write(" <LineString>\n");
				response.getWriter().write(" <extrude>1</extrude>\n");
				response.getWriter().write(" <tessellate>1</tessellate>\n");
				response.getWriter().write(
						" <altitudeMode>clampToGround</altitudeMode>\n");
				response.getWriter().write(
						"<coordinates> " + (lng - lngErr) + ","
								+ (lat - latErr) + "\n");
				response.getWriter().write(
						lng + lngErr + "," + (lat - latErr) + "\n");
				response.getWriter().write(
						lng + lngErr + "," + (lat + latErr) + "\n");
				response.getWriter().write(
						lng - lngErr + "," + (lat + latErr) + "\n");
				response.getWriter().write(
						lng - lngErr + "," + (lat - latErr) + "\n");

				response.getWriter().write(" </coordinates>\n");
				response.getWriter().write(" </LineString>\n");
				response.getWriter().write(" </Placemark>\n");
				response.getWriter().write(" </Folder>\n");
			}
			response.getWriter().write("</Document>");
		} catch (final IOException ioe) {
			throw new IllegalStateException(ioe.getMessage());
		}
		return;
	}
}
