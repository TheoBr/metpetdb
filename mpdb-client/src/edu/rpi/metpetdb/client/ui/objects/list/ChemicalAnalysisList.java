package edu.rpi.metpetdb.client.ui.objects.list;

import com.google.gwt.gen2.table.client.SelectionGrid.SelectionPolicy;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.locale.LocaleEntity;
import edu.rpi.metpetdb.client.locale.LocaleHandler;
import edu.rpi.metpetdb.client.model.ChemicalAnalysis;
import edu.rpi.metpetdb.client.model.properties.ChemicalAnalysisProperty;
import edu.rpi.metpetdb.client.ui.CSS;
import edu.rpi.metpetdb.client.ui.TokenSpace;
import edu.rpi.metpetdb.client.ui.plot.PlotInterface;
import edu.rpi.metpetdb.client.ui.widgets.MLink;
import edu.rpi.metpetdb.client.ui.widgets.paging.DataList;
import edu.rpi.metpetdb.client.ui.widgets.paging.columns.Column;
import edu.rpi.metpetdb.client.ui.widgets.paging.columns.ColumnDefinition;
import edu.rpi.metpetdb.client.ui.widgets.paging.columns.StringColumn;

public abstract class ChemicalAnalysisList extends DataList<ChemicalAnalysis> {

	private static final LocaleEntity enttxt = LocaleHandler.lc_entity;

	private static ColumnDefinition<ChemicalAnalysis> columns;
	private static ColumnDefinition<ChemicalAnalysis> defaultColumns;
	
	static {
		
		columns = new ColumnDefinition<ChemicalAnalysis>();
		defaultColumns = new ColumnDefinition<ChemicalAnalysis>();
		
		// Chemical Analysis name (link to Chemical Analysis details)
		{
			Column<ChemicalAnalysis, MLink> col = new Column<ChemicalAnalysis, MLink>(enttxt.ChemicalAnalysis_spotId(),
					ChemicalAnalysisProperty.spotId) {
				@Override
				public MLink getCellValue(ChemicalAnalysis rowValue) {
					return new MLink((String) rowValue.mGet(ChemicalAnalysisProperty.spotId),
							TokenSpace.detailsOf((ChemicalAnalysis) rowValue));
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
			Column<ChemicalAnalysis, Image> col = new Column<ChemicalAnalysis, Image>(enttxt.Sample_publicData(),
					ChemicalAnalysisProperty.publicData) {
				public Image getCellValue(ChemicalAnalysis rowValue) {
					if ((Boolean) rowValue.mGet(ChemicalAnalysisProperty.publicData)) {
						Image img = new Image("images/checkmark.jpg") {
							public String toString() {
								return "True";
							}
						};
						img.getElement().setAttribute("alt", "This analysis is public");
						return img;
					}
					Image img = new Image("images/xmark.jpg") {
						public String toString() {
							return "False";
						}
					};
					img.getElement().setAttribute("alt", "This analysis is private");
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
		
		// Analysis Method
		{
			StringColumn<ChemicalAnalysis> col = new StringColumn<ChemicalAnalysis>(enttxt.ChemicalAnalysis_analysisMethod(),
					ChemicalAnalysisProperty.analysisMethod);
			col.setColumnSortable(true);
			col.setMinimumColumnWidth(30);
			col.setPreferredColumnWidth(30);
			col.setOptional(true);
			columns.addColumn(col);
			defaultColumns.addColumn(col);
		}
		
		// AnalysisMaterial (Mineral/Bulk Rock)
		{
			StringColumn<ChemicalAnalysis> col = new StringColumn<ChemicalAnalysis>(enttxt.ChemicalAnalysis_mineral(),
					ChemicalAnalysisProperty.analysisMaterial);
			col.setColumnSortable(true);
			col.setMinimumColumnWidth(30);
			col.setPreferredColumnWidth(30);
			col.setOptional(true);
			columns.addColumn(col);
			defaultColumns.addColumn(col);
		}
		
		// Location
		{
			StringColumn<ChemicalAnalysis> col = new StringColumn<ChemicalAnalysis>(enttxt.ChemicalAnalysis_location(),
					ChemicalAnalysisProperty.location);
			col.setColumnSortable(true);
			col.setMinimumColumnWidth(30);
			col.setPreferredColumnWidth(30);
			col.setOptional(true);
			columns.addColumn(col);
			defaultColumns.addColumn(col);
		}
		
		// Analyst
		{
			StringColumn<ChemicalAnalysis> col = new StringColumn<ChemicalAnalysis>(enttxt.ChemicalAnalysis_analyst(),
					ChemicalAnalysisProperty.analyst);
			col.setColumnSortable(true);
			col.setMinimumColumnWidth(30);
			col.setPreferredColumnWidth(30);
			col.setOptional(true);
			columns.addColumn(col);
			defaultColumns.addColumn(col);
		}
		
		// Analysis Date
		{
			StringColumn<ChemicalAnalysis> col = new StringColumn<ChemicalAnalysis>(enttxt.ChemicalAnalysis_analysisDate(),
					ChemicalAnalysisProperty.analysisDate);
			col.setColumnSortable(true);
			col.setMinimumColumnWidth(30);
			col.setPreferredColumnWidth(30);
			col.setOptional(true);
			columns.addColumn(col);
			defaultColumns.addColumn(col);
		}
		
		// Reference
		{
			StringColumn<ChemicalAnalysis> col = new StringColumn<ChemicalAnalysis>(enttxt.ChemicalAnalysis_reference(),
					ChemicalAnalysisProperty.reference);
			col.setColumnSortable(true);
			col.setMinimumColumnWidth(30);
			col.setPreferredColumnWidth(30);
			col.setOptional(true);
			columns.addColumn(col);
			defaultColumns.addColumn(col);
		}
		
		// Reference X
		{
			StringColumn<ChemicalAnalysis> col = new StringColumn<ChemicalAnalysis>(enttxt.ChemicalAnalysis_referenceX(),
					ChemicalAnalysisProperty.referenceX);
			col.setColumnSortable(false);
			col.setMinimumColumnWidth(30);
			col.setPreferredColumnWidth(30);
			col.setOptional(true);
			columns.addColumn(col);
			defaultColumns.addColumn(col);
		}
		
		// Reference Y
		{
			StringColumn<ChemicalAnalysis> col = new StringColumn<ChemicalAnalysis>(enttxt.ChemicalAnalysis_referenceY(),
					ChemicalAnalysisProperty.referenceY);
			col.setColumnSortable(false);
			col.setMinimumColumnWidth(30);
			col.setPreferredColumnWidth(30);
			col.setOptional(true);
			columns.addColumn(col);
			defaultColumns.addColumn(col);
		}
		
		// Image
		//TODO image column
		
		// Total
		{
			StringColumn<ChemicalAnalysis> col = new StringColumn<ChemicalAnalysis>(enttxt.ChemicalAnalysis_total(),
					ChemicalAnalysisProperty.total);
			col.setColumnSortable(true);
			col.setMinimumColumnWidth(30);
			col.setPreferredColumnWidth(30);
			col.setOptional(true);
			columns.addColumn(col);
			defaultColumns.addColumn(col);
		}
		
		

	}
	
	@Override
	public String getListName() {
		return "chemicalAnalysisList";
	}
	
	
	
	public void initialize() {
		super.initialize();
		setTableActions(new ChemicalAnalysisListActions(this));
		add(new PlotInterface(this).getWidget());
	}

	public ChemicalAnalysisList() {
		super(columns);
		getDataTable().setSelectionPolicy(SelectionPolicy.CHECKBOX);
		getDataTable().setSelectionEnabled(true);
		initialize();
	}

	@Override
	public String getDefaultSortParameter() {
		return "spotId";
	}

	@Override
	protected Widget getNoResultsWidget() {
		HTML w = new HTML("No Analyses Found");
		w.setStyleName(CSS.NULLSET);
		return w;
	}

	
	protected ColumnDefinition<ChemicalAnalysis> getDefaultColumns() {
		return defaultColumns;
	}
	
	@Override
	protected Object getId(ChemicalAnalysis ca){
		return ca.getId();
	}
	
}
