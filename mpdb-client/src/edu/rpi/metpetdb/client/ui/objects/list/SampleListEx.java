package edu.rpi.metpetdb.client.ui.objects.list;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Set;

import org.postgis.Point;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.locale.LocaleEntity;
import edu.rpi.metpetdb.client.locale.LocaleHandler;
import edu.rpi.metpetdb.client.model.MetamorphicGrade;
import edu.rpi.metpetdb.client.model.Reference;
import edu.rpi.metpetdb.client.model.Region;
import edu.rpi.metpetdb.client.model.RockType;
import edu.rpi.metpetdb.client.model.Sample;
import edu.rpi.metpetdb.client.model.SampleMineral;
import edu.rpi.metpetdb.client.model.User;
import edu.rpi.metpetdb.client.model.interfaces.MObject;
import edu.rpi.metpetdb.client.model.properties.SampleProperty;
import edu.rpi.metpetdb.client.paging.Column;
import edu.rpi.metpetdb.client.paging.PaginationParameters;
import edu.rpi.metpetdb.client.paging.Results;
import edu.rpi.metpetdb.client.ui.MpDb;
import edu.rpi.metpetdb.client.ui.TokenSpace;
import edu.rpi.metpetdb.client.ui.input.attributes.DateAttribute;
import edu.rpi.metpetdb.client.ui.widgets.MCheckBox;
import edu.rpi.metpetdb.client.ui.widgets.MCollapsedText;
import edu.rpi.metpetdb.client.ui.widgets.MLink;
import edu.rpi.metpetdb.client.ui.widgets.MText;
import edu.rpi.metpetdb.client.ui.widgets.TooltipListener;

public abstract class SampleListEx extends ListEx<Sample> {

	public SampleListEx() {
		super(new ArrayList<Column>(Arrays.asList(columns)));
	}

	public SampleListEx(final String noResultsMessage) {
		super(new ArrayList<Column>(Arrays.asList(columns)), noResultsMessage);
	}

	private static final LocaleEntity enttxt = LocaleHandler.lc_entity;

	private static Column[] columns = {
			new Column(true,"Check", false, true) {
				protected Object getWidget(final MObject data,
						final int currentRow) {
					return new MCheckBox(data);
				}
			},
			new Column(true,enttxt.Sample_number(), SampleProperty.number, true) {
				protected Object getWidget(final MObject data,
						final int currentRow) {
					MLink sampleNumber = new MLink((String) data.mGet(SampleProperty.number),
							TokenSpace.detailsOf((Sample) data));
					return sampleNumber;
				}
				public Object getTooltipRepresentation(final MObject data,
						final int currentRow) {
					return new MLink((String) data.mGet(SampleProperty.number),
							TokenSpace.detailsOf((Sample) data));
				}
			},
			new Column(true,"Info", false, true) {
				protected Object getWidget(final MObject data,
						final int currentRow) {
					Image infoImage = new Image("images/icon-info-small.png"){
						public String toString(){
							return "";
						}
					};
					infoImage.addMouseListener(new TooltipListener("Loading...", 0, "sample-infobox", true){
						public String getTooltipContents() {
							return processTooltipData(data);
						}
					});
					return infoImage;
				}
			},
			new Column(true,enttxt.Sample_publicData(), SampleProperty.publicData,
					true) {
				protected Object getWidget(final MObject data,
						final int currentRow) {
					if ((Boolean) data.mGet(SampleProperty.publicData)) { 
						return new Image("images/checkmark.jpg"){
							public String toString(){
								return "True";
							}
						};
					}
					return new Image("images/xmark.jpg"){
						public String toString(){
							return "False";
						}
					};
				}
			},
			new Column(true,enttxt.Sample_subsampleCount(), SampleProperty.subsampleCount),
			
			new Column(false,enttxt.Sample_imageCount(),SampleProperty.imageCount),
							
			new Column(false,enttxt.Sample_analysisCount(),SampleProperty.analysisCount),
	
			new Column(false,enttxt.Sample_owner(), SampleProperty.owner, true) {
				protected Object getWidget(final MObject data,
						final int currentRow) {
					return new MLink(((User) data.mGet(SampleProperty.owner))
							.getName(), TokenSpace
							.detailsOf((User) data.mGet(SampleProperty.owner)));
				}
			},
			new Column(true,enttxt.Sample_regions(), SampleProperty.regions, false, true) {
				protected Object getWidget(final MObject data,
						final int currentRow) {					
					return new MCollapsedText(getText(data));
				}
				public Object getTooltipRepresentation(final MObject data,
						final int currentRow) {
					return getText(data);
				}
				private String getText(final MObject data) {
					Set<Region> regions = ((Set<Region>) data.mGet(SampleProperty.regions));
					String text = "";
					for (Region r : regions){
						text += r.getName() + ", ";
					}
					if (text.equals("")){
						text = "------";
					} else
						text = text.substring(0,text.length()-2);
					return text;
				}
			},
			new Column(true,enttxt.Sample_country(), SampleProperty.country, true) {
				protected Object getWidget(final MObject data,
						final int currentRow) {
					String text = ((String) data.mGet(SampleProperty.country));
					if (text == null)
						text = "------";
					return new MText(text);

				}
			},
			new Column(true,enttxt.Sample_rockType(), SampleProperty.rockType),
			new Column(true,enttxt.Sample_metamorphicGrades(), SampleProperty.metamorphicGrades, false, true) {
				protected Object getWidget(final MObject data,
						final int currentRow) {
					Set<MetamorphicGrade> metamorphicGrades = ((Set<MetamorphicGrade>) data.mGet(SampleProperty.metamorphicGrades));
					String text = "";
					for (MetamorphicGrade mg : metamorphicGrades){
						text += mg.getName() + ", ";
					}
					if (text.equals("")){
						text = "------";
					} else
						text = text.substring(0,text.length()-2);
					return new MText(text);

				}
			},
			new Column(true,enttxt.Sample_minerals(), SampleProperty.minerals, false, true) {
				protected Object getWidget(final MObject data,
						final int currentRow) {
					String text = getText(data);
					return new MCollapsedText(text);
				}
				public Object getTooltipRepresentation(final MObject data,
						final int currentRow) {
					return getText(data); 
				}
				private String getText(final MObject data) {
					Set<SampleMineral> minerals = ((Set<SampleMineral>) data.mGet(SampleProperty.minerals));
					String text = "";
					for (SampleMineral m : minerals){
						if (!m.getAmount().equals("")) {
							text += m.getName() + " " + m.getAmount()+ ", ";
						} else {
							text += m.getName() + ", "; 
						}
					}
					if (text.equals("")){
						text = "------";
					} else
						text = text.substring(0,text.length()-2);
					return text;
				}
			},
			new Column(true,enttxt.Sample_references(), SampleProperty.references, false, true) {
				protected Object getWidget(final MObject data,
						final int currentRow) {
					Set<Reference> references = ((Set<Reference>) data.mGet(SampleProperty.references));
					String text = "";
					for (Reference r : references){
						text += r.getName() + ", ";
					}
					if (text.equals("")){
						text = "------";
					} else
						text = text.substring(0,text.length()-2);
					return new MText(text);

				}
			},
			new Column(false, "Longitude, Latitude", SampleProperty.location, false, true) {
				protected Object getText(final MObject data,
						final int currentRow) {
					final Point location = (Point) data
							.mGet(SampleProperty.location);
					return "(" + ListExUtil.formatDouble(location.x, ListExUtil.latlngDigits) + "," + 
						ListExUtil.formatDouble(location.y,ListExUtil.latlngDigits) + ")";
				}
			},
			// TODO lat long error
			new Column(false,enttxt.Sample_sesarNumber(), SampleProperty.sesarNumber,
					true) {
				protected Object getWidget(final MObject data,
						final int currentRow) {
					return new MLink((String) data
							.mGet(SampleProperty.sesarNumber), TokenSpace
							.detailsOf((Sample) data));
				}
			},
			new Column(false, enttxt.Sample_collector(), SampleProperty.collector,
					true) {
				protected Object getWidget(final MObject data,
						final int currentRow) {
					String text = ((String) data.mGet(SampleProperty.collector));
					if (text == null)
						text = "------";
					return new MText(text);

				}
			},
			new Column(false, enttxt.Sample_collectionDate(),
					SampleProperty.collectionDate, true) {
				protected Object getWidget(final MObject data,
						final int currentRow) {
					return Sample.dateToString(((Sample) data).getCollectionDate(),((Sample) data).getDatePrecision());
				}
			},
			new Column(false, enttxt.Sample_locationText(), SampleProperty.locationText,
					true) {
				protected Object getWidget(final MObject data,
						final int currentRow) {
					String text = ((String) data.mGet(SampleProperty.locationText));
					if (text == null)
						text = "------";
					return new MText(text);

				}
			},
			// TODO comments
			
	};
	public abstract void update(final PaginationParameters p,
			final AsyncCallback<Results<Sample>> ac);

	public String getDefaultSortParameter() {
		return SampleProperty.number.name();
	}
	
	
	private static String processTooltipData(MObject data){
		String tooltipData = "<table class=\"info\" cellspacing=\"0\"><tbody>";
		
		for(Column c: columns){	
			
			if(c.getTitle() == "Check" || c.getTitle() == "") {
				continue;
			} else {
				String value = c.getTooltipRepresentation(data, 0).toString();
				tooltipData += "<tr><th>"+ c.getTitle() + "</th><td>";
				//null values or special case for IGSN
				if (value == "" || (value.length() == 40 && c.getTitle() == "IGSN"))
					tooltipData += "&#8211;";
				else
					tooltipData += value;
				tooltipData += "</td></tr>";
			}				
		}
		tooltipData += "</tbody></table>";
		return tooltipData;
	}
	
}
