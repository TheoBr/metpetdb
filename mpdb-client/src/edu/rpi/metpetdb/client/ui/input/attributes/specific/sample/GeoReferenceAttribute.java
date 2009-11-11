package edu.rpi.metpetdb.client.ui.input.attributes.specific.sample;

import java.util.Iterator;
import java.util.Set;

import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.model.GeoReference;
import edu.rpi.metpetdb.client.model.Sample;
import edu.rpi.metpetdb.client.model.interfaces.MObject;
import edu.rpi.metpetdb.client.model.validation.PropertyConstraint;
import edu.rpi.metpetdb.client.ui.input.attributes.GenericAttribute;
import edu.rpi.metpetdb.client.ui.widgets.MHtmlList;
import edu.rpi.metpetdb.client.ui.widgets.TooltipListener;

public class GeoReferenceAttribute extends GenericAttribute {

	public GeoReferenceAttribute(PropertyConstraint pc) {
		super(pc);
	}
	
	private Set get(final MObject obj) {
		return (Set) mGet(obj);
	}

	@Override
	public Widget[] createDisplayWidget(final MObject obj) {
		final MHtmlList list = new MHtmlList();
		
		if(obj instanceof Sample){
			Set refSet = get(obj);
			if(refSet != null){
				Iterator itr = refSet.iterator();
				while(itr.hasNext()){
					final GeoReference ref = (GeoReference) itr.next();
					Label refLabel = new Label(((GeoReference) ref).getTitle());
					final Image infoImage = new Image("images/icon-info-small.png");
					infoImage.addMouseListener(new TooltipListener("Loading...", 0, "sample-infobox", true){
						public String getTooltipContents() {
							return processTooltipData(ref);
						}
					});
					Grid row = new Grid(1,2);
					row.setWidget(0, 0, refLabel);
					row.setWidget(0, 1, infoImage);
					list.add(row);
				}
			}
		}
		
		return new Widget[] {
			list
		};
	}
	
	private static String processTooltipData(GeoReference data){
		String tooltipData = "<table class=\"info\" cellspacing=\"0\"><tbody>";
		
		if(data.getTitle() != null) 
			tooltipData += "<tr><th>Title:</th><td>" + data.getTitle() + "</td></tr>";
		if(data.getJournal_name() != null) 
			tooltipData += "<tr><th>Journal:</th><td>" + data.getJournal_name() + "</td></tr>";
		if(data.getFirst_author() != null) 
			tooltipData += "<tr><th>First Author:</th><td>" + data.getFirst_author() + "</td></tr>";
		if(data.getSecond_authors() != null) 
			tooltipData += "<tr><th>Second Authors:</th><td>" + data.getSecond_authors() + "</td></tr>";
		if(data.getFull_text() != null) 
			tooltipData += "<tr><th>Full Citation:</th><td>" + data.getFull_text() + "</td></tr>";
		
		tooltipData += "</tbody></table>";
		return tooltipData;
	}

	@Override
	protected void set(MObject obj, Object o) {
		mSet(obj, o);
	}

}
