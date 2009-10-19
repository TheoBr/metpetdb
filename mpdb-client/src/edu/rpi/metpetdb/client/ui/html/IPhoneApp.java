package edu.rpi.metpetdb.client.ui.html;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;

import edu.rpi.metpetdb.client.ui.MpDb;

public class IPhoneApp extends FlowPanel {
	
	public IPhoneApp() {
		this.add(new HTML(
						"<img src=\"images/iphone-sample.png\" style = \"margin:5px\" alt=\"\" class=\"r\">" +
						"<div style = \"width:50%\">" +
						"<h1> iPhone App Released! </h1> " +
						"<p> Download it <a href=\""+MpDb.DOWNLOAD_IPHONE_APP_URL+"\" title=\"Download MetPetDB\">here</a>.</p>" +
						"<p> The MetPetDB iPhone app is an application for students, teachers, and researchers interested in Geosciences, especially Metamorphic Petrology. It is meant to provide tools needed for field work in Earth Sciences.</p>" +
						"<p> It provides access to information contained in the MetPetDB system which contains information about rock samples, subsamples, images, and chemical analyses. Users can search for samples close to their location, at a specific latitude and longitude, or any specifically named geographic location. In addition, searches can be further refined by rock type, metamorphic grade, sample owner and minerals available in the sample. For each sample, users can view all the relevant data available in the database.</p>" +
						"<p> <a href=\""+MpDb.IPHONE_APP_WIKI+"\" title=\"More about iPhone app\">Learn More</a>.</p>" +
						"</div>"));
	}
}
