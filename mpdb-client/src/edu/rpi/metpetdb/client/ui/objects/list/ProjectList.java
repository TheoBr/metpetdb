package edu.rpi.metpetdb.client.ui.objects.list;

import com.google.gwt.gen2.table.client.SelectionGrid.SelectionPolicy;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.locale.LocaleEntity;
import edu.rpi.metpetdb.client.locale.LocaleHandler;
import edu.rpi.metpetdb.client.model.Project;
import edu.rpi.metpetdb.client.model.Sample;
import edu.rpi.metpetdb.client.model.User;
import edu.rpi.metpetdb.client.model.properties.ProjectProperty;
import edu.rpi.metpetdb.client.ui.CSS;
import edu.rpi.metpetdb.client.ui.TokenSpace;
import edu.rpi.metpetdb.client.ui.widgets.MLink;
import edu.rpi.metpetdb.client.ui.widgets.paging.DataList;
import edu.rpi.metpetdb.client.ui.widgets.paging.columns.Column;
import edu.rpi.metpetdb.client.ui.widgets.paging.columns.ColumnDefinition;
import edu.rpi.metpetdb.client.ui.widgets.paging.columns.StringColumn;

public abstract class ProjectList extends DataList<Project> {
	
	private static final LocaleEntity enttxt = LocaleHandler.lc_entity;
	
	private static ColumnDefinition<Project> columns;
	private static ColumnDefinition<Project> defaultColumns;
	
	static {
		columns = new ColumnDefinition<Project>();
		defaultColumns = new ColumnDefinition<Project>();
		
		//Project name
		{
			Column<Project, MLink> col = new Column<Project, MLink>(enttxt.Project_name(),
					ProjectProperty.name) {
				@Override
				public MLink getCellValue(Project rowValue) {
					return new MLink((String) rowValue.mGet(ProjectProperty.name), 
							TokenSpace.samplesOf((Project) rowValue));
				}
			};
			col.setColumnSortable(true);
			col.setMinimumColumnWidth(50);
			col.setPreferredColumnWidth(150);
			col.setOptional(false);
			columns.addColumn(col);
			defaultColumns.addColumn(col);
		}
		//Project Owner
		{
			Column<Project, MLink> col = new Column<Project, MLink>(enttxt.Project_Owner(),
					ProjectProperty.owner) {
				@Override
				public MLink getCellValue(Project rowValue) {
					return new MLink((String) ((User) rowValue.mGet(ProjectProperty.owner)).getName(), 
							TokenSpace.detailsOf((User) rowValue.mGet(ProjectProperty.owner)));
				}
			};
			col.setColumnSortable(true);
			col.setMinimumColumnWidth(50);
			col.setPreferredColumnWidth(150);
			col.setOptional(true);
			columns.addColumn(col);
			defaultColumns.addColumn(col);
		}
		//Member Count
		{
			StringColumn<Project> col = new StringColumn<Project>(enttxt.Project_MemberCount(),
					ProjectProperty.memberCount);
			col.setColumnSortable(false);
			col.setMinimumColumnWidth(50);
			col.setPreferredColumnWidth(50);
			col.setOptional(true);
			columns.addColumn(col);
			defaultColumns.addColumn(col);
		}
		//View Details/Members
		{
			Column<Project, MLink> col = new Column<Project, MLink>(enttxt.Project_ViewDetails(),
					ProjectProperty.description) {
				@Override
				public MLink getCellValue(final Project rowValue) {
					return new MLink("View Details", 
							TokenSpace.descriptionOf((Project) rowValue));
				}
			};
			col.setColumnSortable(false);
			col.setMinimumColumnWidth(50);
			col.setPreferredColumnWidth(50);
			col.setOptional(true);
			columns.addColumn(col);
			defaultColumns.addColumn(col);
		}
	}
	
	public void initialize() {
		super.initialize();
		setTableActions(new ProjectListActions(this));
	}
	
	public ProjectList() {
		super(columns);	
		getDataTable().setSelectionPolicy(SelectionPolicy.CHECKBOX);
		getDataTable().setSelectionEnabled(true);
		initialize();
		this.getScrollTable().reloadPage();
	}
	
	@Override
	protected String getListName() {
		return "projectList";
	}

	@Override
	protected ColumnDefinition<Project> getDefaultColumns() {
		return defaultColumns;
	}

	@Override
	public String getDefaultSortParameter() {
		return "name";
	}

	@Override
	protected Widget getNoResultsWidget() {
		HTML w = new HTML("No Projects Found");
		w.setStyleName(CSS.NULLSET);
		return w;
	}
	
	@Override
	protected Object getId(Project p){
		return p.getId();
	}

}