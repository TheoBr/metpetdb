package edu.rpi.metpetdb.client.ui.objects.list;

import com.google.gwt.gen2.table.client.SelectionGrid.SelectionPolicy;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.locale.LocaleEntity;
import edu.rpi.metpetdb.client.locale.LocaleHandler;
import edu.rpi.metpetdb.client.model.Sample;
import edu.rpi.metpetdb.client.model.Subsample;
import edu.rpi.metpetdb.client.model.User;
import edu.rpi.metpetdb.client.model.properties.SampleProperty;
import edu.rpi.metpetdb.client.model.properties.SubsampleProperty;
import edu.rpi.metpetdb.client.model.properties.UserProperty;
import edu.rpi.metpetdb.client.paging.PaginationParameters;
import edu.rpi.metpetdb.client.paging.Results;
import edu.rpi.metpetdb.client.ui.CSS;
import edu.rpi.metpetdb.client.ui.TokenSpace;
import edu.rpi.metpetdb.client.ui.widgets.MLink;
import edu.rpi.metpetdb.client.ui.widgets.paging.DataList;
import edu.rpi.metpetdb.client.ui.widgets.paging.columns.Column;
import edu.rpi.metpetdb.client.ui.widgets.paging.columns.ColumnDefinition;
import edu.rpi.metpetdb.client.ui.widgets.paging.columns.StringColumn;

public abstract class ProjectMemberList extends DataList<User> {
	
	private static final LocaleEntity enttxt = LocaleHandler.lc_entity;
	
	private static ColumnDefinition<User> columns;
	private static ColumnDefinition<User> defaultColumns;
	
	static {
		columns = new ColumnDefinition<User>();
		defaultColumns = new ColumnDefinition<User>();
		
		//User name
		{
			Column<User, MLink> col = new Column<User, MLink>(enttxt.User_name(),
					UserProperty.name) {
				@Override
				public MLink getCellValue(User rowValue) {
					return new MLink((String) rowValue.mGet(UserProperty.name),
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
		// email address
		{
			StringColumn<User> col = new StringColumn<User>(enttxt.User_emailAddress(),
					UserProperty.emailAddress);
			col.setColumnSortable(true);
			col.setMinimumColumnWidth(50);
			col.setPreferredColumnWidth(150);
			col.setOptional(false);
			columns.addColumn(col);
			defaultColumns.addColumn(col);
		}
	}
	
	public void initialize() {
		super.initialize();
		setTableActions(new ProjectMemberListActions(this));
	}
	
	public ProjectMemberList() {
		super(columns);	
		getDataTable().setSelectionPolicy(SelectionPolicy.CHECKBOX);
		getDataTable().setSelectionEnabled(true);
		initialize();
	}
	
	@Override
	protected String getListName() {
		return "memberList";
	}

	@Override
	protected ColumnDefinition<User> getDefaultColumns() {
		return defaultColumns;
	}

	@Override
	public String getDefaultSortParameter() {
		return "name";
	}

	@Override
	protected Widget getNoResultsWidget() {
		HTML w = new HTML("No Members Found");
		w.setStyleName(CSS.NULLSET);
		return w;
	}

}
