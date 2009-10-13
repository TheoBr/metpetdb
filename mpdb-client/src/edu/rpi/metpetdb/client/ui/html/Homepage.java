package edu.rpi.metpetdb.client.ui.html;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;

import edu.rpi.metpetdb.client.ui.MpDb;

public class Homepage extends FlowPanel {
	
	public Homepage() {
		this.add(new HTML(
						"<h1>Welcome to MetPetDB</h1> " +
						"<div class=\"top-row\">" +
						"<div class=\"left-col\">" +
						"<img src=\"images/slices.jpg\" alt=\"\" class=\"r\">" +
						"<p><strong>MetPetDB</strong> is a database for metamorphic petrology that is being designed and built by a global community of metamorphic petrologists in collaboration with computer scientists at Rensselaer Polytechnic Institute as part of the National Cyberinfrastructure Initiative and supported by the National Science Foundation.</p>" +
						"<p>This project will support the development, implementation and population of MetPetDB with the purpose of:</p>" +
						"<ol>" +
						"<li><span>archiving published data,</span></li>" +
						"<li><span>storing new data for ready access to researchers and students,</span></li>" +
						"<li><span>facilitating the gathering of information for researchers beginning new projects,</span></li>" +
						"<li><span>providing a search mechanism for data relating to anywhere on the globe,</span></li>" +
						"<li><span>providing a platform for collaborative studies among researchers, and</span></li>" +
						"<li><span>serving as a portal for students beginning their studies of metamorphic geology.</span></li>" +
						"</ol>" +
						"<p>Read more about the MetPetDB project <a href=\""+MpDb.WIKI_URL+"\" title=\"More about MetPetDB\">here</a>.</p>" +
						"</div>" +
						"<div class=\"right-col\">" +
						"<h1> iPhone App Released! </h1> " +
						"<img src=\"images/iphone-sample.png\" style = \"margin:5px\" alt=\"\" class=\"r\">" +
						"<p> Download it <a href=\""+MpDb.DOWNLOAD_IPHONE_APP_URL+"\" title=\"Download MetPetDB\">here</a>.</p>" +
						"<p> The MetPetDB iPhone app is an application for students, teachers, and researchers interested in Geosciences, especially Metamorphic Petrology. It is meant to provide tools needed for field work in Earth Sciences.</p>" +
						"<p> It provides access to information contained in the MetPetDB system which contains information about rock samples, subsamples, images, and chemical analyses. Users can search for samples close to their location, at a specific latitude and longitude, or any specifically named geographic location. In addition, searches can be further refined by rock type, metamorphic grade, sample owner and minerals available in the sample. For each sample, users can view all the relevant data available in the database.</p>" +
						"<p> <a href=\""+MpDb.IPHONE_APP_WIKI+"\" title=\"More about iPhone app\">Learn More</a>.</p>" +
						"</div>" +
						"<div class=\"bot-row\">" +
						"<h2>Other Databases</h2>" + 
						"<table id=\"dblist\" cellspacing=\"0\"><tbody>" +
						"<tr><td><a href=\"http://www.earthchem.org\" title=\"EarthChem\"><img src=\"images/earthchem-logo.jpg\" alt=\"EarthChem\"></a>Advanced Data Management in Solid Earth Geochemistry</td>" +
						"<td><a href=\"http://www.petdb.org\" title=\"PETDB\"><img src=\"images/petdb-logo.jpg\" alt=\"PETDB\"></a>Petrological Database of the Ocean Floor</td>" +
						"<td><a href=\"http://navdat.kgs.ku.edu\" title=\"NAVDAT\"><img src=\"images/navdat-logo.jpg\" alt=\"NAVDAT\"></a>The Western North American Volcanic and Intrusive Rock Database</td>" +
						"<td><a href=\"http://georoc.mpch-mainz.gwdg.de/georoc/Start.asp\" title=\"GEOROC\"><img src=\"images/georoc-logo.jpg\" alt=\"GEOROC\"></a>Geochemistry of Rocks of the Ocean and Continents</td>" +
						"<td><a href=\"http://www.geosamples.org\" title=\"SESAR\"><img src=\"images/sesar-logo.jpg\" alt=\"SESAR\"></a>System for Earth Sample Registration</td></tr>" +
						"</tbody></table>" +
						"</div>"));
	}

}
