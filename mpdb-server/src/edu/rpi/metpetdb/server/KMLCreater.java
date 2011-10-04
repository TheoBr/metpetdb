package edu.rpi.metpetdb.server;

import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.postgis.Point;

import edu.rpi.metpetdb.client.model.MetamorphicGrade;
import edu.rpi.metpetdb.client.model.MetamorphicRegion;
import edu.rpi.metpetdb.client.model.Sample;
import edu.rpi.metpetdb.server.dao.impl.MetamorphicRegionDAO;

public class KMLCreater {
	private static Double lat;
	private static Double lng;
	private static Double latErrMeters;
	private static Double lngErrMeters;

	static public String createKML(final List<Sample> samples, final String baseURL){

		String KML = "";
		KML += "<Document id='doc0'>\n";
		KML += "<Style id='RedPerimeter'>\n" + "<LineStyle>\n"
		+ "<width>2</width>\n"
		+ "<color>7d0000ff</color>\n" + "</LineStyle>\n"
		+ "<PolyStyle>\n" + "<color>7d0000ff</color>\n"
		+ "</PolyStyle>\n" + "</Style>\n";

		for (int i = 0; i < samples.size(); i++) {
			Sample theSample = (Sample) samples.get(i);
			lat = ((Point) theSample.getLocation()).y;
			lng = ((Point) theSample.getLocation()).x;
			if (theSample.getLocationError() != null) {
				//location error is in meters, convert it to degrees
				latErrMeters = theSample.getLocationError().doubleValue() * .000009;
				lngErrMeters = theSample.getLocationError().doubleValue() * .000009;

			} else {
				latErrMeters = 0D;
				lngErrMeters = 0D;
			}
			KML += " <Folder>\n";
			KML += "<name>" + theSample.getNumber() + "</name>\n";
			KML += " <Placemark>\n";
			KML += "<name>" + theSample.getNumber() + "</name>\n";
			KML += " <description>";
			KML += "<![CDATA[\n" + "<a href='" + baseURL
			+ theSample.getId() + "'>"
			+ theSample.getNumber() + "</a> ";

			/* Add Sample info that isn't null */
			if (theSample.getDescription() != null) {
				KML += "Description: " + theSample.getDescription();
			} 
			if (theSample.getCollector() != null) {
				KML += "&lt;br&gt; Collector: " + theSample.getCollector();
			}
			if (theSample.getCollectionDate() != null) {
				KML += "&lt;br&gt; Collection Date: " + Sample.dateToString((Date)theSample.getCollectionDate(), theSample.getDatePrecision());
			}
			if (theSample.getRockType() != null) {
				KML += "&lt;br&gt; Rock Type: " + theSample.getRockType();
			}
			if (theSample.getMetamorphicGrades() != null) {
				KML += "&lt;br&gt; Metamorphic Grade: ";
				for (MetamorphicGrade m : theSample.getMetamorphicGrades()){
					KML += m.getName() + ", ";
				}
				KML = KML.substring(0, KML.length()-2);
			}
			if (theSample.getSesarNumber() != null) {
				KML += "&lt;br&gt; IGSN: " + theSample.getSesarNumber();
			}

			KML += "]]> </description>\n";
			KML += " <Point>\n";

			KML += " <coordinates>" + lng + "," + lat + ",0"
			+ "</coordinates>\n";
			KML += " </Point>\n";
			KML += " </Placemark>\n";

			KML += " <Placemark>\n";
			KML += " <name>Error Perimeter</name>\n";
			KML += " <description>This is the area that "
				+ theSample.getNumber()
				+ " can be found in based on it's latitutde and longitude errors </description>\n";
			KML += " <styleUrl>#RedPerimeter</styleUrl>\n ";
			KML += " <LineString>\n";
			KML += " <extrude>1</extrude>\n";
			KML += " <tessellate>1</tessellate>\n";
			KML += " <altitudeMode>clampToGround</altitudeMode>\n";
			KML += "<coordinates> " + (lng - lngErrMeters) + ","
			+ (lat - latErrMeters) + "\n";
			KML += lng + lngErrMeters + "," + (lat - latErrMeters) + "\n";
			KML += lng + lngErrMeters + "," + (lat + latErrMeters) + "\n";
			KML += lng - lngErrMeters + "," + (lat + latErrMeters) + "\n";
			KML += lng - lngErrMeters + "," + (lat - latErrMeters) + "\n";

			KML += " </coordinates>\n";
			KML += " </LineString>\n";
			KML += " </Placemark>\n";
			KML += " </Folder>\n";
		}
		KML += "</Document>";
		return KML;
	}
	static public String createKMLMetamorphicRegions(){
		try {
			String KML = "";
			KML += "<Document id='doc0'>\n";
			List<MetamorphicRegion> mr;
			MetamorphicRegionDAO mrDAO;
			mr = new ArrayList(DataStore.getInstance().getDatabaseObjectConstraints().Sample_metamorphicRegions.getValues());

			KML += " <Folder>\n";
			KML += "<name> Metamorphic Belts </name>\n";
			for(MetamorphicRegion region: mr){
				List<Double> latitudes= new ArrayList();
				List<Double> longitudes= new ArrayList();
				org.postgis.Polygon pg = (org.postgis.Polygon) region.getShape();

				for (int j = 0; j < pg.getRing(0).numPoints(); j++){
					Point p = pg.getRing(0).getPoint(j);
					latitudes.add(p.y);
					longitudes.add(p.x);
				}

					


				org.postgis.Point labelLoc = (org.postgis.Point) region.getLabelLocation();
				
			
				KML += "<Placemark>\n";
				KML += "<name>" + region.getName() + "</name>\n";
				KML += "<description>" + region.getDescription() + "</description>\n";
	
				if (labelLoc != null)
				{
				KML += "<Point>\n <coordinates>";
				KML += labelLoc.x +","+ labelLoc.y + ",0 </coordinates>\n</Point>\n";
				}
				else
				{
				KML += "<Point>\n <coordinates>";
				KML += "0,0,0 </coordinates>\n</Point>\n";
				}
				
				KML += "</Placemark>\n";

				KML += " <Placemark>\n";
				KML += "<name>" + region.getName() + "</name>\n";
				KML += "<description>"+region.getDescription();
				KML += "<![CDATA[\n";
				KML += "<html><head><script>";
				KML += "function addCriteria(region){";
				KML += "$wnd.alert(region);";
				KML += "var myfunc=@edu.rpi.metpetdb.client.ui.input.attributes.specific.search.searchMetamorphicRegionsAttribute::addRegion(Ljava/lang/String;)";
				KML += "myfunc(region);}</script></head><body>";
				//KML += "<br/><br/><br/><a onclick= addCriteria("+region.getName()+"); href=#>Add to Search Criteria</a>";
				KML += "</body></html>]]>";
				KML+= "</description>";
				KML += " <Polygon>\n";
				KML += "<outerBoundaryIs>\n";
				KML += "<LinearRing>\n";
				KML += " <coordinates>\n";
				for(int k=0; k<latitudes.size(); k++){
					KML +=  longitudes.get(k) + "," + latitudes.get(k) + ",0\n";
				}

				KML	+= "</coordinates>\n";
				KML += "</LinearRing>\n";
				KML += "</outerBoundaryIs>\n";
				KML += " </Polygon>\n";
				KML += "<Style>\n";
				KML += "<PolyStyle>\n";
				KML += "<color>7f3300FF</color>\n";
				KML += "</PolyStyle>\n";
				KML += "<LineStyle>\n";
				KML += "<color>ff000000</color>\n";
				KML += "<width>4</width>\n";
				KML += "</LineStyle>\n";
				KML += "</Style>\n";
				KML += " </Placemark>\n";
			}
			KML += " </Folder>\n";
			KML += "</Document>";
			

			return KML;
		}
		catch (Exception e) {
			e.printStackTrace();
			
			return "";
		}
	
	}
}
