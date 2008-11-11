package edu.rpi.metpetdb.client.ui.objects.list;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;

import org.postgis.Point;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Image;

import edu.rpi.metpetdb.client.locale.LocaleEntity;
import edu.rpi.metpetdb.client.locale.LocaleHandler;
import edu.rpi.metpetdb.client.model.Region;
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
import edu.rpi.metpetdb.client.ui.widgets.MLink;
import edu.rpi.metpetdb.client.ui.widgets.MText;

public abstract class SampleListEx extends ListEx<Sample> {

	public SampleListEx() {
		super(new ArrayList<Column>(Arrays.asList(columns)));
	}

	public SampleListEx(final String noResultsMessage) {
		super(new ArrayList<Column>(Arrays.asList(columns)), noResultsMessage);
	}

	private static final LocaleEntity enttxt = LocaleHandler.lc_entity;

	private static Column[] columns = {
			new Column(true,"Check", true, true) {
				protected Object getWidget(final MObject data,
						final int currentRow) {
					return new MCheckBox(data);
				}
			},
			new Column(true,enttxt.Sample_alias(), SampleProperty.alias, true) {
				protected Object getWidget(final MObject data,
						final int currentRow) {
					return new MLink((String) data.mGet(SampleProperty.alias),
							TokenSpace.detailsOf((Sample) data));
				}
			},
			new Column(false,enttxt.Sample_sesarNumber(), SampleProperty.sesarNumber,
					true) {
				protected Object getWidget(final MObject data,
						final int currentRow) {
					return new MLink((String) data
							.mGet(SampleProperty.sesarNumber), TokenSpace
							.detailsOf((Sample) data));
				}
			},
			new Column(false,enttxt.Sample_owner(), SampleProperty.owner, true) {
				protected Object getWidget(final MObject data,
						final int currentRow) {
					return new MLink(((User) data.mGet(SampleProperty.owner))
							.getName(), TokenSpace
							.detailsOf((User) data.mGet(SampleProperty.owner)));
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
					DateAttribute dateTemp = new DateAttribute(
							MpDb.doc.Sample_collectionDate,
							MpDb.doc.Sample_datePrecision);
					return ((MText) dateTemp.createDisplayWidget(data)[0])
							.getText();
				}
			},
			new Column(true,enttxt.Sample_rockType(), SampleProperty.rockType),
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
			new Column(false, "Location", SampleProperty.location, true) {
				protected Object getText(final MObject data,
						final int currentRow) {
					final Point location = (Point) data
							.mGet(SampleProperty.location);
					return "(" + format(location.x) + "," + format(location.y)
							+ ")";
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
			new Column(true,enttxt.Sample_regions(), SampleProperty.regions, true) {
				protected Object getWidget(final MObject data,
						final int currentRow) {
					Set<Region> regions = ((Set<Region>) data.mGet(SampleProperty.regions));
					String text = "";
					for (Region r : regions){
						text += r.getName() + ", ";
					}
					if (text.equals("")){
						text = "------";
					} else
						text = text.substring(0,text.length()-2);
					return new MText(text);

				}
			},
			new Column(true,enttxt.Sample_minerals(), SampleProperty.minerals, true) {
				protected Object getWidget(final MObject data,
						final int currentRow) {
					Set<SampleMineral> minerals = ((Set<SampleMineral>) data.mGet(SampleProperty.minerals));
					String text = "";
					for (SampleMineral m : minerals){
						text += m.getName() + ", ";
					}
					if (text.equals("")){
						text = "------";
					} else
						text = text.substring(0,text.length()-2);
					return new MText(text);

				}
			},
			new Column(true,enttxt.Sample_subsampleCount(), SampleProperty.subsampleCount),
			
			new Column(false,enttxt.Sample_imageCount(),SampleProperty.imageCount),
							
			new Column(false,enttxt.Sample_analysisCount(),SampleProperty.analysisCount),
	};
	public abstract void update(final PaginationParameters p,
			final AsyncCallback<Results<Sample>> ac);

	public String getDefaultSortParameter() {
		return SampleProperty.alias.name();
	}

	public static String format(final double loc) {
		final String locStr = String.valueOf(loc);
		final int decPos = locStr.toString().indexOf(".");
		if (locStr.length() < decPos + 4 && decPos >= 0)
			return locStr;
		return locStr.substring(0, decPos + 4);
	}

}
