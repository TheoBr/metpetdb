package edu.rpi.metpetdb.client.ui.html;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;

import edu.rpi.metpetdb.client.ui.MpDb;
import edu.rpi.metpetdb.client.ui.TokenSpace;
import edu.rpi.metpetdb.client.ui.commands.ServerOp;
import edu.rpi.metpetdb.client.ui.widgets.ImageHyperlink;
import edu.rpi.metpetdb.client.ui.widgets.panels.MTwoColPanel;

public class Homepage extends FlowPanel {
	
	public Homepage() {
		final MTwoColPanel bigContainer = new MTwoColPanel();
		bigContainer.setRightColWidth("40%");		
		bigContainer.setLeftColWidth("50%");
		this.getElement().setId("homepageWrapper");
		bigContainer.getLeftCol().add(new HTML(
						"<h1>Welcome to MetPetDB</h1> " +
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
		
		ImageHyperlink appLink = new ImageHyperlink(new com.google.gwt.user.client.ui.Image("images/app-store.png"), 
				"", TokenSpace.iphoneApp.getName(), false);		
		appLink.getElement().setId("imageHyperlink");
		appLink.addStyleName("l");
		
		bigContainer.getLeftCol().add(appLink);
		bigContainer.getLeftCol().add(new HTML(
						"<div class=\"smallColumn\">" +
						"<a href=\"http://www.earthchem.org\" title=\"EarthChem\"><img class=\"linkIcon\" src=\"images/earthchem-logo.jpg\" alt=\"EarthChem\"></a></li>" +
						"<a href=\"http://www.petdb.org\" title=\"PETDB\"><img class=\"linkIcon\" src=\"images/petdb-logo.jpg\" alt=\"PETDB\"></a>" +
						"</div>" +
						// "<a href=\"http://navdat.kgs.ku.edu\" title=\"NAVDAT\"><img class=\"linkIcon\" src=\"images/navdat-logo.jpg\" alt=\"NAVDAT\"></a>" +
						"<div class=\"smallColumn\">" +
						"<a href=\"http://georoc.mpch-mainz.gwdg.de/georoc/Start.asp\" title=\"GEOROC\"><img class=\"linkIcon\" src=\"images/georoc-logo.jpg\" alt=\"GEOROC\"></a>" +
						"<a href=\"http://www.geosamples.org\" title=\"SESAR\"><img class=\"linkIcon\" src=\"images/sesar-logo.jpg\" alt=\"SESAR\"></a>" +
						"</div>"));
		
		new ServerOp<List<List>>() {
			@Override
			public void begin() {
				MpDb.mpdbGeneric_svc.getStatistics(this);
			}

			public void onSuccess(final List<List> result) {
				// Places the image on the right side of the page
				bigContainer.getRightCol().add(new HTML("<img class=\"image-slices\" src=\"images/slices.jpg\">"));
				
				result.get(0);
				FlexTable holdTable = new FlexTable();
				
				// Loads the data into the table on the right
				for (int i = 0; i < result.get(0).size(); i++) {
					int row = holdTable.getRowCount();
					Label listNumber = new Label(result.get(1).get(i).toString());
					Label listText = new Label(result.get(0).get(i).toString());
					holdTable.setWidget(i, 0, listNumber);
					holdTable.setWidget(i, 1, listText);
					
					// Styles each cell and the even and odd rows
					holdTable.getCellFormatter().addStyleName(row, 0, "numberPart");
					holdTable.getCellFormatter().addStyleName(row, 1, "wordPart");
					if (i % 2 == 0) holdTable.getRowFormatter().addStyleName(row, "evenRow");
					else holdTable.getRowFormatter().addStyleName(row, "oddRow");

					// Adds each row table to the right column
					bigContainer.getRightCol().add(holdTable);
				}
			}
		}.begin();
		
		this.add(bigContainer);
	}
}
