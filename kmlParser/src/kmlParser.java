import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import com.sun.corba.se.impl.orbutil.graph.Node;
import java.util.regex.*;

public class kmlParser {
	public static void main(String args[]) {
		// File inputFile= new File("C://MetPetDB//Adirondack_Mountain.kml");
		// String xmlText= getContents(inputFile);
		// Node root;
		// xmlText = stripBeginningComments(xmlText);
		try {
			/*
			 * DocumentBuilderFactory dbf =
			 * DocumentBuilderFactory.newInstance(); DocumentBuilder db =
			 * dbf.newDocumentBuilder();
			 * 
			 * //parse using builder to get DOM representation of the XML file
			 * Document dom= db.newDocument();
			 * dom.setDocumentURI("C://MetPetDB//Adirondack_Mountain.kml");
			 * Element e= dom.getDocumentElement(); NodeList nl=
			 * e.getElementsByTagName("coordinates");
			 */

			// prompt the user for the complete path of the file they would like
			// to use

			File file = new File(args[0]);
			FileOutputStream fos = new FileOutputStream(args[1], true);
			PrintStream p = new PrintStream(fos);
			// OutputStreamWriter out = new OutputStreamWriter(fos, "UTF-8");

			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(file);
			doc.getDocumentElement().normalize();
			// System.out.println("Root element " +
			// doc.getDocumentElement().getNodeName());
			doc.getDocumentElement().getNodeName();
			NodeList nameList = doc.getElementsByTagName("name");
			NodeList coordinates = doc.getElementsByTagName("Coordinates");
			NodeList placemarks = doc.getElementsByTagName("Placemark");
			if (placemarks != null && placemarks.getLength() > 0) {
				System.out.print(placemarks.getLength());
				for (int i = 0; i < placemarks.getLength(); i++) {
					p.print("insert into metamorphic_regions(metamorphic_region_id, name, description, shape) VALUES (nextval('metamorphic_regions_seq'), '");
					// String regionName= nameList.item(i).getTextContent();
					NodeList children = placemarks.item(i).getChildNodes();
					// loop through the children nodes of the placemark looking
					// for the name and description
					String regionName = "";
					String description = "";
					String coords = "";
					for (int k = 0; k < children.getLength(); k++) {
						if (children.item(k).getNodeName().equals("name")) {
							regionName = children.item(k).getTextContent();
							p.print(regionName);
							p.print("','");
						}
						if (children.item(k).getNodeName().equals("description")) {
							if(!description.equals(null)){
								description = children.item(k).getTextContent();
							}
							else{
								description="";
							}
							p.print(description);
							p.print("',\r\n GeomFromText('POLYGON((");
						}

						if (children.item(k).getNodeName().equals("Polygon")) {
							// get the children of the polygon tag so we can get
							// the coordinate tag
							NodeList polygonChildren = children.item(k)
									.getChildNodes();
							for (int j = 0; j < polygonChildren.getLength(); j++) {
								// the first child of polygonChildren will be
								// the linearRing
								// the first child of linearRing will be
								// coordinates
								if (polygonChildren.item(j).getNodeName().equals("outerBoundaryIs")) {
									NodeList boundaryChildren = polygonChildren.item(j).getChildNodes();
									for (int q = 0; q < boundaryChildren.getLength(); q++) {
										if (boundaryChildren.item(q).getNodeName().equals("LinearRing")) {
											NodeList linearRingChildren = boundaryChildren.item(q).getChildNodes();
											for (int w = 0; w < linearRingChildren.getLength(); w++) {
												if (linearRingChildren.item(w).getNodeName().equals("coordinates")) {
													coords = linearRingChildren.item(w).getTextContent();
													System.out.println(coords);
													Scanner scanner = new Scanner(coords);

													String parsedCoords = coords.replace(",0 ",", ");

													// the strings must be
													// rearranged so that the
													// latitude comes before the
													// longitude
													Pattern pat = Pattern.compile("[\\s,\\s]+");
													// Split input with the
													// pattern
													String[] result = pat
															.split(parsedCoords);

													p.print(result[1]);
													p.print(" ");
													p.print(result[2]);
													// p.print(",");
													int z = 3;
													while (z + 1 < result.length) {
														p.print(", ");
														p.print(result[z]);
														p.print(" ");
														p.print(result[z + 1]);
														z = z + 2;
													}
													p.print("))', 4326));");

												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
			p.close();
		}

		catch (Exception e) {
			throw new IllegalStateException(e.getMessage());
		} finally {
		}

		// System.out.print(outputString);
	}

	static public String getContents(File aFile) {
		// ...checks on aFile are elided
		StringBuilder contents = new StringBuilder();

		try {
			// use buffering, reading one line at a time
			// FileReader always assumes default encoding is OK!
			BufferedReader input = new BufferedReader(new FileReader(aFile));
			try {
				String line = null; // not declared within while loop

				while ((line = input.readLine()) != null) {
					contents.append(line);
					contents.append(System.getProperty("line.separator"));
				}
			} finally {
				input.close();
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}

		return contents.toString();
	}
	/*
	 * public static String stripBeginningComments(String xmlText) { xmlText =
	 * xmlText.substring(xmlText.indexOf("<?xml version=")); return xmlText; }
	 */

}
