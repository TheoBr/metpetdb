package edu.rpi.metpetdb.server;

import java.util.List;

import org.postgis.Point;

import edu.rpi.metpetdb.client.model.Sample;

public class KMLCreater {
	private static Double lat;
	private static Double lng;
	private static Double latErr;
	private static Double lngErr;
	
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
				latErr = theSample.getLocationError().doubleValue();
				lngErr = theSample.getLocationError().doubleValue();
			} else {
				latErr = 0D;
				lngErr = 0D;
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
				KML += "&lt;br&gt; Collection Date: " + theSample.getCollectionDate();
			}
			if (theSample.getRockType() != null) {
				KML += "&lt;br&gt; Rock Type: " + theSample.getRockType();
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
			KML += "<coordinates> " + (lng - lngErr) + ","
							+ (lat - latErr) + "\n";
			KML += lng + lngErr + "," + (lat - latErr) + "\n";
			KML += lng + lngErr + "," + (lat + latErr) + "\n";
			KML += lng - lngErr + "," + (lat + latErr) + "\n";
			KML += lng - lngErr + "," + (lat - latErr) + "\n";

			KML += " </coordinates>\n";
			KML += " </LineString>\n";
			KML += " </Placemark>\n";
			KML += " </Folder>\n";
		}
		KML += "</Document>";
		return KML;
	}
}
