package edu.rpi.metpetdb.client.ui.html;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;

import edu.rpi.metpetdb.client.ui.MpDb;
import edu.rpi.metpetdb.client.ui.TokenSpace;
import edu.rpi.metpetdb.client.ui.commands.ServerOp;
import edu.rpi.metpetdb.client.ui.widgets.ImageHyperlink;

public class Homepage extends FlowPanel {
	
	public Homepage() {
		this.getElement().setId("homepageWrapper");
		this.add(new HTML(
						"<h1>Welcome to MetPetDB</h1> " +
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
						"<p>Read more about the MetPetDB project <a href=\""+MpDb.WIKI_URL+"\" title=\"More about MetPetDB\">here</a>.</p>")); 
		
		new ServerOp<List<List>>() {
			@Override
			public void begin() {
				MpDb.mpdbGeneric_svc.getStatistics(this);
			}

			public void onSuccess(final List<List> result) {
				result.get(0);

				for (int i = 0; i < result.get(0).size(); i++){
					Label temp = new Label(result.get(1).get(i).toString() + result.get(0).get(i).toString());
					temp.setStyleName("statList");
					Homepage.this.add(temp);
				}
					
				
		/*		Homepage.this.add(new HTML("<p><a href=\"\"><img class=\"noUnderline\" src=\"images/app-store.png\" alt=\"iTunes App Store\" title=\"iTunes App Store\" /></a></p>"));*/
				ImageHyperlink appLink = new ImageHyperlink(new com.google.gwt.user.client.ui.Image(GWT.getModuleBaseURL()+"/images/app-store.png"), 
						"", TokenSpace.iphoneApp.getName(), false);		
				appLink.setStyleName("noUnderline");
				Homepage.this.add(appLink);
				
				Homepage.this.add(new HTML(
						"<h3>Other \"Word\" Links</h3>" +
						"<ul class=\"noBullets\">" +
						"<li><span><a href=\"http://www.earthchem.org\" title=\"EarthChem\"><img src=\"images/earthchem-logo.jpg\" alt=\"EarthChem\"></a>Advanced Data Management in Solid Earth Geochemistry</span></li>" +
						"<li><span><a href=\"http://www.petdb.org\" title=\"PETDB\"><img src=\"images/petdb-logo.jpg\" alt=\"PETDB\"></a>Petrological Database of the Ocean Floor</span></li>" +
						"<li><span><a href=\"http://navdat.kgs.ku.edu\" title=\"NAVDAT\"><img src=\"images/navdat-logo.jpg\" alt=\"NAVDAT\"></a>The Western North American Volcanic and Intrusive Rock Database</span></li>" +
						"<li><span><a href=\"http://georoc.mpch-mainz.gwdg.de/georoc/Start.asp\" title=\"GEOROC\"><img src=\"images/georoc-logo.jpg\" alt=\"GEOROC\"></a>Geochemistry of Rocks of the Ocean and Continents</span></li>" +
						"<li><span><a href=\"http://www.geosamples.org\" title=\"SESAR\"><img src=\"images/sesar-logo.jpg\" alt=\"SESAR\"></a>System for Earth Sample Registration</span></li>" +
						"</ul>"));
			}
		}.begin();
	}
}
