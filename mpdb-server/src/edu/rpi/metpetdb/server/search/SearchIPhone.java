package edu.rpi.metpetdb.server.search;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.postgis.LinearRing;
import org.postgis.Point;

import com.thoughtworks.xstream.XStream;

import edu.rpi.metpetdb.client.model.MetamorphicGrade;
import edu.rpi.metpetdb.client.model.Sample;
import edu.rpi.metpetdb.client.model.SampleMineral;
import edu.rpi.metpetdb.client.model.SearchSample;
import edu.rpi.metpetdb.client.model.validation.DatabaseObjectConstraints;
import edu.rpi.metpetdb.client.paging.Results;
import edu.rpi.metpetdb.client.service.MpDbConstants;
import edu.rpi.metpetdb.server.DataStore;
import edu.rpi.metpetdb.server.impl.RegionServiceImpl;
import edu.rpi.metpetdb.server.impl.SampleServiceImpl;


public class SearchIPhone extends HttpServlet{
	private static final long serialVersionUID = 1L;
	private static final String LAT_PARAMETER = "lat";
	private static final String LONG_PARAMETER = "long";
	private static final String SAMPLE_ID = "sampleId";
	private static final String REGIONS = "regions";
	private static final String ROCK_TYPES = "rockTypes";
	private static final String SEARCH_REGIONS = "searchRegion";

	@Override
	protected void doGet(final HttpServletRequest request,
			final HttpServletResponse response) throws ServletException {
		response.setContentType("text/xml");
		
		List<Long> sampleIds = new ArrayList<Long>();
		
		// If there is a GET string for latitude and longitude then it is a search
		if (request.getParameter(LAT_PARAMETER) != null && request.getParameter(LONG_PARAMETER) != null) {
			double lat = Double.parseDouble(request.getParameterValues(LAT_PARAMETER)[0]);
			double lng = Double.parseDouble(request.getParameterValues(LONG_PARAMETER)[0]);
			System.out.println("iPhone query: lat = " + lat + "long = " + lng);
			outputSearchXML(search(lat,lng),response);
		} else if (request.getParameter(SEARCH_REGIONS) != null){
			Set<String> regions = new HashSet<String>();
			for (String s : request.getParameterValues(SEARCH_REGIONS)){
				if (s.length() > 2 && s.substring(0, 1).equals("'") && s.substring(s.length()-1, s.length()).equals("'")){
					regions.add(s.substring(1, s.length()-1));
				}
			}
			outputSearchXML(search(regions),response);
		} else if (request.getParameter(SAMPLE_ID) != null){
			for (String id : request.getParameterValues(SAMPLE_ID))
				sampleIds.add(Long.parseLong(id));
			sampleInfo(sampleIds,response);
		} else if (request.getParameter(REGIONS) != null){
			if (request.getParameterValues(REGIONS)[0].equalsIgnoreCase("t")){
				regions(response);
			}
		} else if (request.getParameter(ROCK_TYPES) != null){
			if (request.getParameterValues(ROCK_TYPES)[0].equalsIgnoreCase("t")){
				rockTypes(response);
			}
		}
		return;
	}
		
	private void rockTypes(HttpServletResponse response){
		try {
			DatabaseObjectConstraints doc = DataStore.getInstance().getDatabaseObjectConstraints();		
			final XStream x = new XStream();
			x.toXML(doc.Sample_rockType.getValues(),response.getWriter());
		} catch(final Exception ioe){
			throw new IllegalStateException(ioe.getMessage());
		}
	}
	
	private void regions(HttpServletResponse response){
		try {
			RegionServiceImpl service = new RegionServiceImpl();
			final XStream x = new XStream();
			x.toXML(service.allNames(),response.getWriter());
		} catch(final Exception ioe){
			throw new IllegalStateException(ioe.getMessage());
		}
	}
	
	private void sampleInfo(List<Long> sampleIds, final HttpServletResponse response){
		try {
			SampleServiceImpl s = new SampleServiceImpl();
			final XStream x = new XStream();
			response.getWriter().write("<set>");
			for (Long id : sampleIds){
				x.toXML(s.details(id),response.getWriter());
			}
			response.getWriter().write("</set>");
		} catch (final Exception ioe){
			throw new IllegalStateException(ioe.getMessage());
		}
	}
	
	private void outputSearchXML(final Results<Sample> results, final HttpServletResponse response){
		try{
			final XStream x = new XStream();
			response.getWriter().write("<set>");
			for (Sample sample : results.getList()){			
				response.getWriter().write("<sample>");
				response.getWriter().write(createXMLElement("number",x.toXML(sample.getNumber())));
				response.getWriter().write(createXMLElement("id",x.toXML(sample.getId())));
				response.getWriter().write(createXMLElement("rockType",x.toXML(sample.getRockType())));
				response.getWriter().write("<minerals>");
				for (SampleMineral m : sample.getMinerals())
					x.toXML(m.getName(),response.getWriter());
				response.getWriter().write("</minerals>");
				response.getWriter().write("<metamorphicGrades>");
				for (MetamorphicGrade m : sample.getMetamorphicGrades())
					x.toXML(m.getName(),response.getWriter());
				response.getWriter().write("</metamorphicGrades>");
				response.getWriter().write(createXMLElement("publicData",x.toXML(sample.isPublicData())));
				x.toXML(sample.getLocation(),response.getWriter());
				response.getWriter().write(createXMLElement("owner",x.toXML(sample.getOwner().getName())));
				response.getWriter().write("</sample>");
			}
			response.getWriter().write("</set>");
		} catch (final Exception ioe){
			throw new IllegalStateException(ioe.getMessage());
		}
	}
	
	private Results<Sample> search(final Collection<String> regions){
		try{
			SearchSample s = new SearchSample();
			for (String r : regions){
				s.addRegion(r);
			}
			return SearchDb.sampleSearch(null, s, null);
		}
		catch (Exception e){
			throw new IllegalStateException(e.getMessage());
		}
	}

	private Results<Sample> search(final Double lat, final Double lng){
		try{
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
			return SearchDb.sampleSearch(null, s, null);

		} catch (final Exception ioe){
			throw new IllegalStateException(ioe.getMessage());
		}
	}
	private String createXMLElement(final String tag, final String value){
		return "<"+ tag + ">" + value + "</" + tag + ">";
	}
}
