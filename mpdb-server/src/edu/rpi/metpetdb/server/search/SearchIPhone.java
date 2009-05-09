package edu.rpi.metpetdb.server.search;

import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Query;
import org.postgis.LinearRing;
import org.postgis.Point;

import edu.rpi.metpetdb.client.model.Sample;
import edu.rpi.metpetdb.client.model.SearchSample;
import edu.rpi.metpetdb.client.model.User;
import edu.rpi.metpetdb.client.paging.Results;
import edu.rpi.metpetdb.client.service.MpDbConstants;

public class SearchIPhone extends HttpServlet{
	private static final long serialVersionUID = 1L;
	private static final String LAT_PARAMETER = "lat";
	private static final String LONG_PARAMETER = "long";

	@Override
	protected void doGet(final HttpServletRequest request,
			final HttpServletResponse response) throws ServletException {
		response.setContentType("text/plain");
		
		double lat = 0;
		double lng = 0;
		
		// If there is a GET string
		if (request.getParameter(LAT_PARAMETER) != null && request.getParameter(LONG_PARAMETER) != null) {
			lat = Double.parseDouble(request.getParameterValues(LAT_PARAMETER)[0]);
			lng = Double.parseDouble(request.getParameterValues(LONG_PARAMETER)[0]);
		}

		try {
			SearchSample s = new SearchSample();	
			final LinearRing[] ringArray = new LinearRing[1];
			final Point[] points = new Point[5];
			final Point p1 = new Point();
			p1.x = lng;
			p1.y = lat;

			final Point p2 = new Point();
			p2.x = lng;
			p2.y = lat+30;

			final Point p3 = new Point();
			p3.x = lng+30;
			p3.y = lat+30;

			final Point p4 = new Point();
			p4.x = lng+30;
			p4.y = lat;
			
			points[0] = p1;
			points[1] = p2;
			points[2] = p3;
			points[3] = p4;
			points[4] = p1;
			final LinearRing ring = new LinearRing(points);
			ringArray[0] = ring;
			org.postgis.Polygon boundingBox = new org.postgis.Polygon(ringArray);
			boundingBox.srid = MpDbConstants.WGS84;
			boundingBox.dimension = 2;
			s.setBoundingBox(boundingBox);
			Results<Sample> results = SearchDb.sampleSearch(null, s, null);
			for (Sample sample : results.getList()){
				response.getWriter().write(sample.getName() + " \n");
				response.getWriter().write(((Point)sample.getLocation()).x + " \n");
				response.getWriter().write(((Point)sample.getLocation()).y + " \n");
			}
		} catch (final Exception ioe) {
			throw new IllegalStateException(ioe.getMessage());
		}
		
		return;
	}
}
