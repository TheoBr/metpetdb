package edu.rpi.metpetdb.client.ui.input.attributes.specific.sample;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.model.GeoReference;
import edu.rpi.metpetdb.client.model.Reference;
import edu.rpi.metpetdb.client.model.Sample;
import edu.rpi.metpetdb.client.model.interfaces.MObject;
import edu.rpi.metpetdb.client.model.validation.PropertyConstraint;
import edu.rpi.metpetdb.client.ui.commands.MCommand;
import edu.rpi.metpetdb.client.ui.input.attributes.GenericAttribute;
import edu.rpi.metpetdb.client.ui.widgets.MHtmlList;
import edu.rpi.metpetdb.client.ui.widgets.TooltipListener;

public class GeoReferenceAttribute extends GenericAttribute implements ClickListener {
	
	private final Button addReference = new Button("Add Reference", this);
	private Set<GeoReference> references;
	private MHtmlList list = new MHtmlList();
	private final VerticalPanel vp = new VerticalPanel();;

	public GeoReferenceAttribute(PropertyConstraint pc) {
		super(pc);
	}
	
	private Set get(final MObject obj) {
		return ((Sample) obj).getGeoReferences();
	}
	
	private MHtmlList createReferenceList(final MObject obj){
		//make sure the reference list is neat and empty before adding things to it
		if(list != null){
			vp.remove(list);
		}
		
		list = new MHtmlList();
		
		if(obj instanceof Sample){
			Set refSet = get(obj);
			if(refSet != null){
				Iterator itr = refSet.iterator();
				while(itr.hasNext()){
					final GeoReference ref = (GeoReference) itr.next();
					Label refLabel = new Label(((GeoReference) ref).getFirstAuthor() + ": " +
							((GeoReference) ref).getTitle());
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
				vp.add(list);
			}
		}
		
		return list;
	}

	@Override
	public Widget[] createDisplayWidget(final MObject obj) {
		vp.remove(addReference); //don't display the add button while not editing
		vp.add(createReferenceList(obj));
		
		return new Widget[] {
			vp
		};
	}
	
	@Override
	public Widget[] createEditWidget(final MObject obj, final String id) {
		vp.clear();
		vp.add(addReference);
		vp.add(createReferenceList(obj));
		if (get(obj) != null)
			references = get(obj);
		else
			references = new HashSet<GeoReference>();
		return new Widget[] {
			vp
		};
	}
	
	private static String processTooltipData(GeoReference data){
		String tooltipData = "<table class=\"info\" cellspacing=\"0\"><tbody>";
		
		if(data.getTitle() != null) 
			tooltipData += "<tr><th>Title:</th><td>" + data.getTitle() + "</td></tr>";
		if(data.getJournalName() != null) 
			tooltipData += "<tr><th>Journal:</th><td>" + data.getJournalName() + "</td></tr>";
		if(data.getFirstAuthor() != null) 
			tooltipData += "<tr><th>First Author:</th><td>" + data.getFirstAuthor() + "</td></tr>";
		if(data.getSecondAuthors() != null) 
			tooltipData += "<tr><th>Second Authors:</th><td>" + data.getSecondAuthors() + "</td></tr>";
		if(data.getFullText() != null) 
			tooltipData += "<tr><th>Abstract:</th><td>" + data.getFullText() + "</td></tr>";
		
		tooltipData += "</tbody></table>";
		return tooltipData;
	}

	@Override
	protected void set(MObject obj, Object o) {
		
		Set<Reference> refs = ((Sample) obj).getReferences();
		
		Set<GeoReference> geoRefs = new HashSet<GeoReference>();
		
		for (Reference currRef : refs)
		{
			geoRefs.add(currRef.getGeoref());
		}
		
		
		mSet(obj, geoRefs);
	}

	public void onClick(Widget sender) {
		if (sender == addReference) {
			new AddReferenceWizard(new MCommand<GeoReference>() {
				@Override
				public void execute(final GeoReference result) {
					references.add(result);
					//Add new reference to current list
					Label refLabel = new Label(result.getTitle());
					final Image infoImage = new Image("images/icon-info-small.png");
					infoImage.addMouseListener(new TooltipListener("Loading...", 0, "sample-infobox", true){
						public String getTooltipContents() {
							return processTooltipData(result);
						}
					});
					Grid row = new Grid(1,2);
					row.setWidget(0, 0, refLabel);
					row.setWidget(0, 1, infoImage);
					list.add(row);
				}
			}).show();
		}
	}

}
