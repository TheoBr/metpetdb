package edu.rpi.metpetdb.client.ui.objects.list;

import org.postgis.Point;

import com.google.gwt.gen2.table.client.SelectionGrid.SelectionPolicy;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.locale.LocaleEntity;
import edu.rpi.metpetdb.client.locale.LocaleHandler;
import edu.rpi.metpetdb.client.model.Sample;
import edu.rpi.metpetdb.client.model.User;
import edu.rpi.metpetdb.client.model.properties.SampleProperty;
import edu.rpi.metpetdb.client.ui.CSS;
import edu.rpi.metpetdb.client.ui.TokenSpace;
import edu.rpi.metpetdb.client.ui.widgets.MLink;
import edu.rpi.metpetdb.client.ui.widgets.TooltipListener;
import edu.rpi.metpetdb.client.ui.widgets.paging.DataList;
import edu.rpi.metpetdb.client.ui.widgets.paging.columns.CollapsedColumn;
import edu.rpi.metpetdb.client.ui.widgets.paging.columns.Column;
import edu.rpi.metpetdb.client.ui.widgets.paging.columns.ColumnDefinition;
import edu.rpi.metpetdb.client.ui.widgets.paging.columns.StringColumn;
import edu.rpi.metpetdb.client.ui.widgets.paging.columns.CollapsedColumn.TruncateMethod;

public abstract class SampleList extends DataList<Sample> {

	private static final LocaleEntity enttxt = LocaleHandler.lc_entity;

	private static ColumnDefinition<Sample> columns;
	private static ColumnDefinition<Sample> defaultColumns;
	static {
		columns = new ColumnDefinition<Sample>();
		defaultColumns = new ColumnDefinition<Sample>();
		// sample name (link to sample details)
		{
			Column<Sample, MLink> col = new Column<Sample, MLink>(enttxt.Sample_number(),
					SampleProperty.number) {
				@Override
				public MLink getCellValue(Sample rowValue) {
					return new MLink((String) rowValue.mGet(SampleProperty.number),
							TokenSpace.detailsOf(rowValue));
				}
			};
			col.setColumnSortable(true);
			col.setMinimumColumnWidth(50);
			col.setPreferredColumnWidth(150);
			col.setOptional(false);
			columns.addColumn(col);
			defaultColumns.addColumn(col);
		}
		
		// info icon (click to get sample info popup)
		{
			Column<Sample, Image> col = new Column<Sample, Image>("Info", null){
				@Override
				public Image getCellValue(final Sample rowValue) {
					Image infoImage = new Image("images/icon-info-small.png"){
						public String toString(){
							return "";
						}
					};
					infoImage.addMouseListener(new TooltipListener("Loading...", 0, "sample-infobox", true){
						public String getTooltipContents() {
							return processTooltipData(rowValue);
						}
					});
					return infoImage;
				}
			};
			col.setColumnSortable(false);
			col.setMinimumColumnWidth(30);
			col.setMaximumColumnWidth(30);
			col.setPreferredColumnWidth(30);
			col.setOptional(false);
			col.setName("info");
			columns.addColumn(col);
			defaultColumns.addColumn(col);
		}
		
		// public or private
		{
			Column<Sample, Image> col = new Column<Sample, Image>(enttxt.Sample_publicData(),
					SampleProperty.publicData) {
				public Image getCellValue(Sample rowValue) {
					if ((Boolean) rowValue.mGet(SampleProperty.publicData)) {
						Image img = new Image("images/checkmark.jpg") {
							public String toString() {
								return "True";
							}
						};
						img.getElement().setAttribute("alt", "This sample is public");
						return img;
					}
					Image img = new Image("images/xmark.jpg") {
						public String toString() {
							return "False";
						}
					};
					img.getElement().setAttribute("alt", "This sample is private");
					return img;
				}
			};
			col.setColumnSortable(true);
			col.setMinimumColumnWidth(50);
			col.setMaximumColumnWidth(50);
			col.setPreferredColumnWidth(50);
			col.setOptional(true);
			columns.addColumn(col);
			defaultColumns.addColumn(col);
		}
		
		// subsample count
		{
			Image img = new Image("images/icon-subsample.png");
			img.getElement().setAttribute("alt", "Subsamples");
			StringColumn<Sample> col = new StringColumn<Sample>(img, SampleProperty.subsampleCount);
			col.setColumnSortable(true);
			col.setMinimumColumnWidth(30);
			col.setPreferredColumnWidth(30);
			col.setOptional(true);
			columns.addColumn(col);
			defaultColumns.addColumn(col);
		}
		
		// image count
		{
			Image img = new Image("images/icon-image.png");
			img.getElement().setAttribute("alt", "Images");
			StringColumn<Sample> col = new StringColumn<Sample>(img, SampleProperty.imageCount);
			col.setColumnSortable(true);
			col.setMinimumColumnWidth(30);
			col.setPreferredColumnWidth(30);
			col.setOptional(true);
			columns.addColumn(col);
			defaultColumns.addColumn(col);
		}
		
		// chemical analysis count
		{
			Image img = new Image("images/icon-chemical-analysis.png");
			img.getElement().setAttribute("alt", "Chemical Analyses");
			StringColumn<Sample> col = new StringColumn<Sample>(img, SampleProperty.analysisCount);
			col.setColumnSortable(true);
			col.setMinimumColumnWidth(30);
			col.setPreferredColumnWidth(30);
			col.setOptional(true);
			columns.addColumn(col);
		}
		
		// owner (link to owner profile)
		{
			Column<Sample, MLink> col = new Column<Sample, MLink>(enttxt.Sample_owner(),
					SampleProperty.owner) {
				public MLink getCellValue(final Sample rowValue) {
					return new MLink(((User) rowValue.mGet(SampleProperty.owner))
							.getName(), TokenSpace.detailsOf((User) rowValue
							.mGet(SampleProperty.owner)));
				}
			};
			col.setColumnSortable(true);
			col.setMinimumColumnWidth(60);
			col.setPreferredColumnWidth(120);
			col.setOptional(true);
			columns.addColumn(col);
			defaultColumns.addColumn(col);
		}
		
		// regions
		{
			CollapsedColumn<Sample> col = new CollapsedColumn<Sample>(enttxt.Sample_regions(),
					SampleProperty.regions);
			col.setTruncateOptions(TruncateMethod.ITEM_COUNT, 3);
			col.setColumnSortable(false);
			col.setMinimumColumnWidth(50);
			col.setPreferredColumnWidth(100);
			col.setOptional(true);
			columns.addColumn(col);
		}
		
		// country
		{
			StringColumn<Sample> col = new StringColumn<Sample>(enttxt.Sample_country(),
					SampleProperty.country);
			col.setColumnSortable(true);
			col.setMinimumColumnWidth(55);
			col.setPreferredColumnWidth(100);
			col.setOptional(true);
			columns.addColumn(col);
		}

		// rock type
		{
			StringColumn<Sample> col = new StringColumn<Sample>(enttxt.Sample_rockType(),
					SampleProperty.rockType);
			col.setColumnSortable(true);
			col.setMinimumColumnWidth(70);
			col.setPreferredColumnWidth(140);
			col.setOptional(true);
			columns.addColumn(col);
			defaultColumns.addColumn(col);
		}
		
		// metamorphic grades
		{
			CollapsedColumn<Sample> col = new CollapsedColumn<Sample>(enttxt
					.Sample_metamorphicGrades(), SampleProperty.metamorphicGrades);
			col.setTruncateOptions(TruncateMethod.ITEM_COUNT, 2);
			col.setColumnSortable(false);
			col.setMinimumColumnWidth(120);
			col.setPreferredColumnWidth(120);
			col.setOptional(true);
			columns.addColumn(col);
		}
		
		// minerals
		{
			CollapsedColumn<Sample> col = new CollapsedColumn<Sample>(enttxt.Sample_minerals(),
					SampleProperty.minerals);
			col.setTruncateOptions(TruncateMethod.ITEM_COUNT, 3);
			col.setColumnSortable(false);
			col.setMinimumColumnWidth(50);
			col.setPreferredColumnWidth(100);
			col.setOptional(true);
			columns.addColumn(col);
			defaultColumns.addColumn(col);
		}
		
		// references
		{
			CollapsedColumn<Sample> col = new CollapsedColumn<Sample>(
					enttxt.Sample_references(), SampleProperty.references);
			col.setTruncateOptions(TruncateMethod.ITEM_COUNT, 3);
			col.setColumnSortable(false);
			col.setMinimumColumnWidth(130);
			col.setPreferredColumnWidth(200);
			col.setOptional(true);
			columns.addColumn(col);
		}
		
		// lat/long
		{
			StringColumn<Sample> col = new StringColumn<Sample>("Long/Lat",
					SampleProperty.location) {
				@Override
				public String getCellValue(Sample rowValue) {
					final Point location = (Point) rowValue
							.mGet(SampleProperty.location);
					return "("
							+ ListExUtil.formatDouble(location.x,
									ListExUtil.latlngDigits)
							+ ", "
							+ ListExUtil.formatDouble(location.y,
									ListExUtil.latlngDigits) + ")";
				}
			};
			col.setColumnSortable(false);
			col.setMinimumColumnWidth(60);
			col.setPreferredColumnWidth(130);
			col.setOptional(true);
			columns.addColumn(col);
		}

		// SESAR number (link to sample details)
		{
			Column<Sample, MLink> col = new Column<Sample, MLink>(enttxt.Sample_sesarNumber(),
					SampleProperty.sesarNumber) {
				@Override
				public MLink getCellValue(final Sample rowValue) {
					return new MLink((String) rowValue
							.mGet(SampleProperty.sesarNumber), TokenSpace
							.detailsOf(rowValue));
				}
			};
			col.setColumnSortable(true);
			col.setMinimumColumnWidth(40);
			col.setPreferredColumnWidth(70);
			col.setOptional(true);
			columns.addColumn(col);
		}
		
		// collector
		{
			StringColumn<Sample> col = new StringColumn<Sample>(enttxt.Sample_collector(),
					SampleProperty.collector);
			col.setColumnSortable(true);
			col.setMinimumColumnWidth(55);
			col.setPreferredColumnWidth(100);
			col.setOptional(true);
			columns.addColumn(col);
		}
		
		// collection date
		{
			StringColumn<Sample> col = new StringColumn<Sample>(enttxt.Sample_collectionDate(),
					SampleProperty.collectionDate) {
				@Override
				public String getCellValue(Sample rowValue) {
					return Sample.dateToString(rowValue.getCollectionDate(),
							rowValue.getDatePrecision());
				}
			};
			// TODO: actually make this sortable in the chronological sense. 
			// perhaps a DateColumn is in order
			col.setColumnSortable(true);
			col.setMinimumColumnWidth(85);
			col.setPreferredColumnWidth(110);
			col.setOptional(true);
			columns.addColumn(col);
			defaultColumns.addColumn(col);
		}
		
		// current location
		{
			StringColumn<Sample> col = new StringColumn<Sample>(enttxt.Sample_locationText(),
					SampleProperty.locationText);
			col.setColumnSortable(true);
			col.setMinimumColumnWidth(140);
			col.setPreferredColumnWidth(170);
			col.setOptional(true);
			columns.addColumn(col);
		}

	}
	
	@Override
	public String getListName() {
		return "sampleList";
	}
	
	
	
	public void initialize() {
		super.initialize();
		setTableActions(new SampleListActions(this));
	}

	public SampleList() {
		super(columns);
		getDataTable().setSelectionPolicy(SelectionPolicy.CHECKBOX);
		getDataTable().setSelectionEnabled(true);
		initialize();
	}

	@Override
	public String getDefaultSortParameter() {
		return "number";
	}

	@Override
	protected Widget getNoResultsWidget() {
		HTML w = new HTML("No Samples Found");
		w.setStyleName(CSS.NULLSET);
		return w;
	}
	
	private static String processTooltipData(Sample data){
		String tooltipData = "<table class=\"info\" cellspacing=\"0\"><tbody>";
		
		for(Column<Sample, ?> c: columns){	
			
			if(c.getHeader() == "") {
				continue;
			} else {
				String value = c.getCellTooltipValue(data).toString();
				tooltipData += "<tr><th>"+ c.getHeader() + "</th><td>";
				//null values or special case for IGSN
				if (value == "" || (value.length() == 40 && c.getHeader() == "IGSN"))
					tooltipData += "&#8211;";
				else
					tooltipData += value;
				tooltipData += "</td></tr>";
			}				
		}
		tooltipData += "</tbody></table>";
		return tooltipData;
	}
	
	protected ColumnDefinition<Sample> getDefaultColumns() {
		return defaultColumns;
	}

}
