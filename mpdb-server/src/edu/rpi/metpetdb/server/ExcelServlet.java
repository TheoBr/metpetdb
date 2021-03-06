package edu.rpi.metpetdb.server;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Query;
import org.hibernate.Session;
import org.postgis.Point;

import edu.rpi.metpetdb.client.model.MObject;
import edu.rpi.metpetdb.client.model.Sample;
import edu.rpi.metpetdb.client.model.SampleMineral;
import edu.rpi.metpetdb.client.model.properties.MetamorphicGradeProperty;
import edu.rpi.metpetdb.client.model.properties.Property;
import edu.rpi.metpetdb.client.model.properties.ReferenceProperty;
import edu.rpi.metpetdb.client.model.properties.RegionProperty;
import edu.rpi.metpetdb.client.model.properties.SampleMineralProperty;

public class ExcelServlet extends HttpServlet{
	private static final long serialVersionUID = 1L;
	private static final String samplesParameter = "Samples";

	@Override
	protected void doGet(final HttpServletRequest request,
			final HttpServletResponse response) throws ServletException {
		response.setContentType("attachment/tab-separated-values");
		response.addHeader("Content-Disposition", "inline;filename=results.tsv");

		Session session = DataStore.open();
		try {
			session.beginTransaction();
			
			List<Sample> samples = new LinkedList<Sample>();
			Query q;
			
			// If there is a GET string, fetch by ids
			if (request.getParameter(samplesParameter) != null) {
				String ids[] = request.getParameterValues(samplesParameter);
				q = session.getNamedQuery("Sample.byId");
				for (int i = 0; i < ids.length; i++) {
					q.setParameter("id", Long.parseLong(ids[i]));
					samples.add((Sample) q.uniqueResult());
				}
			}
			
		
			// output the column headers
			if (request.getParameter("column") != null) {
				String headers[] = request.getParameterValues("column");
				for (int i = 0; i < headers.length; i++) {
					response.getWriter().write(headers[i] + "\t");
				}
			}
			response.getWriter().write("\n");
			for (Sample s : samples){
				response.getWriter().write(nullToEmptyString(s.getNumber()) + "\t");
				response.getWriter().write(boolToString(s.isPublicData()) + "\t");
				response.getWriter().write(s.getSubsampleCount() + "\t");
				response.getWriter().write(s.getImageCount() + "\t");
				response.getWriter().write(s.getAnalysisCount() + "\t");
				response.getWriter().write(s.getOwner().getName() + "\t");
				response.getWriter().write(setToString(s.getRegions(),RegionProperty.name) + "\t");			
				response.getWriter().write(nullToEmptyString(s.getCountry()) + "\t");
				response.getWriter().write(nullToEmptyString(s.getRockType()) + "\t");
				response.getWriter().write(setToString(s.getMetamorphicGrades(),MetamorphicGradeProperty.name) + "\t");
				response.getWriter().write(setSampleMineralsToString(s.getMinerals()) + "\t");
				response.getWriter().write(setToString(s.getReferences(),ReferenceProperty.name) + "\t");
				response.getWriter().write(formatlatlng(((Point)s.getLocation()).y) +"\t");
				response.getWriter().write(formatlatlng(((Point)s.getLocation()).x) +"\t");
				response.getWriter().write(nullToEmptyString(s.getSesarNumber()) + "\t");
				response.getWriter().write(nullToEmptyString(s.getCollector()) + "\t");
				response.getWriter().write(Sample.dateToString(s.getCollectionDate(), s.getDatePrecision()) + "\t");
				response.getWriter().write(nullToEmptyString(s.getLocationText()) + "\t");
				response.getWriter().write("\n");
			}
		} catch (final IOException ioe) {
			throw new IllegalStateException(ioe.getMessage());
		} finally {
			session.close();
		}
		
		return;
	}
	
	private String boolToString(final Boolean b){
		return (b) ? "yes" : "no";
	}
	
	private String nullToEmptyString(final Object o){
		return (o == null) ? "" : o.toString();
	}

	private String setToString(final Set<?> values, final Property property){
		String text = "";
		for (Object o : values){
			text += ((MObject) o).mGet(property) + ", ";
		}
		if (!text.equals("")){
			text = text.substring(0,text.length()-2);
		}
		return text;
	}
	
	private String setSampleMineralsToString(final Set<SampleMineral> minerals){
		String text = "";
		for (SampleMineral sm : minerals){
			text += sm.toString() + ", ";		
		}
		if (!text.equals("")){
			text = text.substring(0,text.length()-2);
		}
		return text;
	}
	
	
	private static String formatlatlng(final double loc) {
		final int latlngDigits = 5;
		final String locStr = String.valueOf(loc);
		final int decPos = locStr.toString().indexOf(".");
		if (locStr.length() <= decPos + latlngDigits && decPos >= 0)
			return locStr;
		return locStr.substring(0, decPos + latlngDigits);
	}
}

