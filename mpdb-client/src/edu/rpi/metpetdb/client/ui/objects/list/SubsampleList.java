package edu.rpi.metpetdb.client.ui.objects.list;

import com.google.gwt.gen2.table.client.SelectionGrid.SelectionPolicy;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.locale.LocaleEntity;
import edu.rpi.metpetdb.client.locale.LocaleHandler;
import edu.rpi.metpetdb.client.model.Sample;
import edu.rpi.metpetdb.client.model.Subsample;
import edu.rpi.metpetdb.client.model.User;
import edu.rpi.metpetdb.client.model.properties.SampleProperty;
import edu.rpi.metpetdb.client.model.properties.SubsampleProperty;
import edu.rpi.metpetdb.client.ui.CSS;
import edu.rpi.metpetdb.client.ui.TokenSpace;
import edu.rpi.metpetdb.client.ui.widgets.MLink;
import edu.rpi.metpetdb.client.ui.widgets.paging.DataList;
import edu.rpi.metpetdb.client.ui.widgets.paging.columns.Column;
import edu.rpi.metpetdb.client.ui.widgets.paging.columns.ColumnDefinition;
import edu.rpi.metpetdb.client.ui.widgets.paging.columns.StringColumn;


public abstract class SubsampleList extends DataList<Subsample> {

	private static final LocaleEntity enttxt = LocaleHandler.lc_entity;

	private static ColumnDefinition<Subsample> columns;
	private static ColumnDefinition<Subsample> defaultColumns;
	static {
		columns = new ColumnDefinition<Subsample>();
		defaultColumns = new ColumnDefinition<Subsample>();
		// subsample name (link to subsample details)
		{
			Column<Subsample, MLink> col = new Column<Subsample, MLink>(enttxt.Subsample_name(),
					SubsampleProperty.name) {
				@Override
				public MLink getCellValue(Subsample rowValue) {
					return new MLink((String) rowValue.mGet(SubsampleProperty.name),
							TokenSpace.detailsOf((Subsample) rowValue));
				}
			};
			col.setColumnSortable(true);
			col.setMinimumColumnWidth(50);
			col.setPreferredColumnWidth(120);
			col.setOptional(false);
			columns.addColumn(col);
			defaultColumns.addColumn(col);
		}
		
		// public or private
		{
			Column<Subsample, Image> col = new Column<Subsample, Image>(enttxt.Sample_publicData(),
					SubsampleProperty.publicData) {
				public Image getCellValue(Subsample rowValue) {
					if ((Boolean) rowValue.mGet(SubsampleProperty.publicData)) {
						Image img = new Image("images/checkmark.jpg") {
							public String toString() {
								return "True";
							}
						};
						img.getElement().setAttribute("alt", "This subsample is public");
						return img;
					}
					Image img = new Image("images/xmark.jpg") {
						public String toString() {
							return "False";
						}
					};
					img.getElement().setAttribute("alt", "This subsample is private");
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
		
		// subsample type
		{
			StringColumn<Subsample> col = new StringColumn<Subsample>(enttxt.Subsample_subsampleType(),
					SubsampleProperty.subsampleType);
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
			StringColumn<Subsample> col = new StringColumn<Subsample>(img, SubsampleProperty.imageCount);
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
			StringColumn<Subsample> col = new StringColumn<Subsample>(img, SubsampleProperty.analysisCount);
			col.setColumnSortable(true);
			col.setMinimumColumnWidth(30);
			col.setPreferredColumnWidth(30);
			col.setOptional(true);
			columns.addColumn(col);
			defaultColumns.addColumn(col);
		}
		
		// owner (link to owner profile)
		{
			Column<Subsample, MLink> col = new Column<Subsample, MLink>(enttxt.Subsample_owner(),
					SubsampleProperty.owner) {
				public MLink getCellValue(final Subsample rowValue) {
					return new MLink(((User) rowValue.mGet(SubsampleProperty.owner))
							.getName(), TokenSpace.detailsOf((User) rowValue
							.mGet(SubsampleProperty.owner)));
				}
			};
			col.setColumnSortable(true);
			col.setMinimumColumnWidth(60);
			col.setPreferredColumnWidth(120);
			col.setOptional(true);
			columns.addColumn(col);
			defaultColumns.addColumn(col);
		}
		
		// image map
		{
			//using SubsampleProperty.images because it can't be null and show up in the default list
			Column<Subsample, MLink> col = new Column<Subsample, MLink>("Image Map", SubsampleProperty.images) {
				public MLink getCellValue(final Subsample rowValue) {
					if(rowValue.getGrid() == null){
						return new MLink("Create Map", TokenSpace.createNewImageMap(rowValue));
					} else {
						return new MLink("View Map", TokenSpace.detailsOf(rowValue.getGrid()));
					}
				}
				
			};
			col.setColumnSortable(false);
			col.setMinimumColumnWidth(60);
			col.setPreferredColumnWidth(120);
			col.setOptional(true);
			columns.addColumn(col);
			defaultColumns.addColumn(col);
		}

	}
	
	@Override
	public String getListName() {
		return "subsampleList";
	}
	
	
	
	public void initialize() {
		super.initialize();
		setTableActions(new SubsampleListActions(this));
	}

	public SubsampleList() {
		super(columns);
		getDataTable().setSelectionPolicy(SelectionPolicy.CHECKBOX);
		getDataTable().setSelectionEnabled(true);
		initialize();
	}

	@Override
	public String getDefaultSortParameter() {
		return "name";
	}

	@Override
	protected Widget getNoResultsWidget() {
		HTML w = new HTML("No Subsamples Found");
		w.setStyleName(CSS.NULLSET);
		return w;
	}

	
	protected ColumnDefinition<Subsample> getDefaultColumns() {
		return defaultColumns;
	}
	
	@Override
	protected Object getId(Subsample ss){
		return ss.getId();
	}

}
