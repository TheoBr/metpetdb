package edu.rpi.metpetdb.client.ui.user;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.widgetideas.table.client.FixedWidthFlexTable;

import edu.rpi.metpetdb.client.locale.LocaleHandler;
import edu.rpi.metpetdb.client.model.Invite;
import edu.rpi.metpetdb.client.model.Project;
import edu.rpi.metpetdb.client.model.Subsample;
import edu.rpi.metpetdb.client.paging.PaginationParameters;
import edu.rpi.metpetdb.client.paging.Results;
import edu.rpi.metpetdb.client.ui.CSS;
import edu.rpi.metpetdb.client.ui.MpDb;
import edu.rpi.metpetdb.client.ui.TokenSpace;
import edu.rpi.metpetdb.client.ui.commands.LoggedInServerOp;
import edu.rpi.metpetdb.client.ui.commands.ServerOp;
import edu.rpi.metpetdb.client.ui.dialogs.CustomTableView;
import edu.rpi.metpetdb.client.ui.objects.list.ProjectList;
import edu.rpi.metpetdb.client.ui.objects.list.ProjectListEx;
import edu.rpi.metpetdb.client.ui.widgets.MCheckBox;
import edu.rpi.metpetdb.client.ui.widgets.MLink;
import edu.rpi.metpetdb.client.ui.widgets.panels.MPagePanel;

public class UserProjectsListEx extends MPagePanel implements ClickListener {
	private static final String cookieString = "UserProjectsListEx";
	private FlexTable header1;
	private ProjectList list;
	private ListBox lb;
	private FlexTable Projects_ft;

	public UserProjectsListEx() {
	}

	private void adpRows() {
		header1 = new FlexTable();

		header1.setWidth("100%");

		final MLink createProject = new MLink("Create New Project",
				TokenSpace.newProject);
		

		setPageTitle("My Projects");
		createProject.setStylePrimaryName(CSS.LINK_LARGE_ICON);
		createProject.addStyleName(CSS.LINK_ADD);
		addPageActionItem(createProject);
	}

	public void onClick(Widget sender) {
		//CustomTableView myView = new CustomTableView(list, cookieString);
	}

	private void addProjects() {
		list = new ProjectList() {
			public void update(final PaginationParameters p,
					final AsyncCallback<Results<Project>> ac) {
				long id = (long) (MpDb.currentUser().getId());
				MpDb.project_svc.all(p, id, ac);
			}

			@Override
			public void getAllIds(AsyncCallback<Map<Object, Boolean>> ac) {
				int id = MpDb.currentUser().getId();
				MpDb.project_svc.allIdsForUser(id, ac);	
			}			
		};		
		Projects_ft = new FlexTable();
		Projects_ft.setWidth("100%");
		Projects_ft.setWidget(0, 0, list);

		FixedWidthFlexTable footer = new FixedWidthFlexTable();

		FlexTable realFooter = new FlexTable();

		realFooter.addStyleName("mpdb-dataTableBlue");
		realFooter.getFlexCellFormatter().setWidth(0, 0, "85px");
		realFooter.getFlexCellFormatter().setWidth(0, 1, "100px");
		realFooter.getFlexCellFormatter().setAlignment(0, 0,
				HasHorizontalAlignment.ALIGN_LEFT,
				HasVerticalAlignment.ALIGN_MIDDLE);
		realFooter.getFlexCellFormatter().setAlignment(0, 1,
				HasHorizontalAlignment.ALIGN_LEFT,
				HasVerticalAlignment.ALIGN_MIDDLE);
		realFooter.setWidth("100%");
		realFooter.setCellSpacing(5);
		footer.setWidget(0, 0, realFooter);
		footer.getFlexCellFormatter().setColSpan(0, 0, 4);
		footer.setWidth("100%");

		this.add(Projects_ft);
	}

	public UserProjectsListEx display() {
		adpRows();
		addProjects();
		list.getScrollTable().reloadPage();
		
		/*new ServerOp() {
			@Override
			public void begin() {
				MpDb.project_svc.all(MpDb.currentUser().getId(), this);
			}
			public void onSuccess(Object result) {
				adpRows();
				addProjects();
		
				List<Project> projects = (List<Project>)result;

			}
		}.begin();*/
		return this;
	}

	private List<Project> getCheckedProjects() {
		List<Project> results = new ArrayList<Project>();
		for (int i = 0; i < list.getScrollTable().getDataTable().getRowCount(); i++) {
			if (((MCheckBox) list.getScrollTable().getDataTable().getWidget(i,
					0)).isChecked())
				results.add((Project) (((MCheckBox) list.getScrollTable()
						.getDataTable().getWidget(i, 0)).getObjectValue()));
		}
		return results;
	}

	private void deleteSelected() {
		new ServerOp() {
			@Override
			public void begin() {
				List<Project> CheckedProjects = getCheckedProjects();
				Iterator<Project> itr = CheckedProjects.iterator();
				while (itr.hasNext()) {
					// MpDb.project_svc.delete(itr.next().getId(), this);
				}
			}
			public void onSuccess(Object result) {
				list.getScrollTable().reloadPage();
			}
		}.begin();
	}
	private void DuplicateSelected() {

	}
}
