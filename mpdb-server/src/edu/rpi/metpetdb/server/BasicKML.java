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

import edu.rpi.metpetdb.client.model.Sample;
public class BasicKML extends HttpServlet {
	private static final long serialVersionUID = 1L;
	// private static final Double metersInLatDegree = 110874.40;
	private static final String samplesParameter = "Samples";
	private static final String urlParameter = "url";
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
			response.getWriter().write("<Document id='doc0'>\n");
			response.getWriter().write(
					"<Style id='RedPerimeter'>\n" + "<LineStyle>\n"
							+ "<width>2</width>\n"
							+ "<color>7d0000ff</color>\n" + "</LineStyle>\n"
							+ "<PolyStyle>\n" + "<color>7d0000ff</color>\n"
							+ "</PolyStyle>\n" + "</Style>\n");

			for (int i = 0; i < samples.size(); i++) {
				Sample theSample = (Sample) samples.get(i);
				lat = ((Point) theSample.getLocation()).y;
				lng = ((Point) theSample.getLocation()).x;
				if (theSample.getLocationError() != null) {
					latErr = theSample.getLocationError().doubleValue();
					lngErr = theSample.getLocationError().doubleValue();
				} else {
					latErr = 0D;
					lngErr = 0D;
				}
				response.getWriter().write(" <Folder>\n");
				response.getWriter().write(
						"<name>" + theSample.getNumber() + "</name>\n");
				response.getWriter().write(" <Placemark>\n");
				response.getWriter().write(
						"<name>" + theSample.getNumber() + "</name>\n");

				response.getWriter().write(" <description>");
				response.getWriter().write(
						"<![CDATA[\n" + "<a href='" + baseURL
								+ theSample.getId() + "'>"
								+ theSample.getNumber() + "</a>  ]]>");

				/* Add Sample info that isn't null */
				if (theSample.getDescription() != null) {
					response.getWriter().write(
							"Description: " + theSample.getDescription());
				}
				if (theSample.getCollector() != null) {
					response.getWriter()
							.write(
									"&lt;br&gt; Collector: "
											+ theSample.getCollector());
				}
				if (theSample.getCollectionDate() != null) {
					response.getWriter().write(
							"&lt;br&gt; Collection Date: "
									+ theSample.getCollectionDate());
				}
				if (theSample.getRockType() != null) {
					response.getWriter().write(
							"&lt;br&gt; Rock Type: " + theSample.getRockType());
				}
				if (theSample.getSesarNumber() != null) {
					response.getWriter().write(
							"&lt;br&gt; IGSN: " + theSample.getSesarNumber());
				}

				response.getWriter().write("</description>\n");
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
										+ theSample.getNumber()
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
