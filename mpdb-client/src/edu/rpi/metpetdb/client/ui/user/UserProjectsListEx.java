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
import edu.rpi.metpetdb.client.ui.MpDb;
import edu.rpi.metpetdb.client.ui.TokenSpace;
import edu.rpi.metpetdb.client.ui.commands.LoggedInServerOp;
import edu.rpi.metpetdb.client.ui.commands.ServerOp;
import edu.rpi.metpetdb.client.ui.dialogs.CustomTableView;
import edu.rpi.metpetdb.client.ui.objects.list.ProjectList;
import edu.rpi.metpetdb.client.ui.objects.list.ProjectListEx;
import edu.rpi.metpetdb.client.ui.widgets.MCheckBox;
import edu.rpi.metpetdb.client.ui.widgets.MLink;

public class UserProjectsListEx extends FlowPanel implements ClickListener {
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

		final MLink recentlyAdded = new MLink("Recent Changes",
				new ClickListener() {
					public void onClick(Widget sender) {
					}
				});
		
		//Look for invites and display a link only if invites exist
		new ServerOp() {
			public void begin(){
				MpDb.project_svc.getInvitesForUser(MpDb.currentUser().getId(), this);
			}
			public void onSuccess(final Object result){
				//copy the results. Can't modify in place the list received from the callback
				List<Invite> invites = new ArrayList<Invite>();
				
				//Take out any invites that are not New because no action would be needed on those
				for(Invite i: (List<Invite>) result){
					if(i.getStatus().equals("New"))
						invites.add(i);
				}
				//If there are any new invites, create a link to the My Invites page
				if(invites != null && invites.size() > 0){
					final MLink newInvites = new MLink("You have " + invites.size() + " project invite" +
							(invites.size() > 1 ? "s!" : "!"), 
							new ClickListener(){
								public void onClick(Widget sender) {
									new LoggedInServerOp<Subsample>() {
										@Override
										public void command() {
											History.newItem(TokenSpace.viewMyInvites(MpDb.currentUser()));
										}
									}.begin();
								}	
					});
					header1.setWidget(0, 1, newInvites);
				}
			}
		}.begin();

		final MLink simple = new MLink("Simple", new ClickListener() {
			public void onClick(Widget sender) {
			}
		});

		final MLink detailed = new MLink("Detailed", new ClickListener() {
			public void onClick(Widget sender) {
			}
		});

		final MLink createView = new MLink("Create New View", this);

		final Label myProjectsSamples_label = new Label("My Projects");
		final Label quickfilters_label = new Label("Quick Filters:");
		final Label changeView_label = new Label("Change View:");
		createProject.addStyleName("addlink");

		recentlyAdded.addStyleName("beta");
		simple.addStyleName("beta");
		detailed.addStyleName("beta");
		createView.addStyleName("beta");

		header1.setWidget(0, 0, myProjectsSamples_label);
		header1.setWidget(1, 0, createProject);

		header1.setWidget(2, 0, quickfilters_label);
		header1.setWidget(2, 1, recentlyAdded);
		header1.setWidget(2, 3, changeView_label);
		header1.setWidget(2, 4, simple);
		header1.setWidget(2, 5, detailed);
		header1.setWidget(2, 6, createView);

		header1.getFlexCellFormatter().setColSpan(0, 0, 3);
		header1.getFlexCellFormatter().setColSpan(0, 1, 4);
		header1.getFlexCellFormatter().setColSpan(1, 1, 2);
		header1.getFlexCellFormatter().setWidth(1, 0, "130px");
		header1.getFlexCellFormatter().setAlignment(0, 1,
				HasHorizontalAlignment.ALIGN_RIGHT,
				HasVerticalAlignment.ALIGN_MIDDLE);
		header1.getFlexCellFormatter().setWidth(2, 2, "50%");
		header1.getFlexCellFormatter().setAlignment(2, 0,
				HasHorizontalAlignment.ALIGN_LEFT,
				HasVerticalAlignment.ALIGN_MIDDLE);
		header1.getFlexCellFormatter().setAlignment(2, 1,
				HasHorizontalAlignment.ALIGN_LEFT,
				HasVerticalAlignment.ALIGN_MIDDLE);
		header1.getFlexCellFormatter().setAlignment(2, 2,
				HasHorizontalAlignment.ALIGN_LEFT,
				HasVerticalAlignment.ALIGN_MIDDLE);
		header1.getFlexCellFormatter().setAlignment(2, 3,
				HasHorizontalAlignment.ALIGN_RIGHT,
				HasVerticalAlignment.ALIGN_MIDDLE);
		header1.getFlexCellFormatter().setAlignment(2, 4,
				HasHorizontalAlignment.ALIGN_RIGHT,
				HasVerticalAlignment.ALIGN_MIDDLE);
		header1.getFlexCellFormatter().setAlignment(2, 5,
				HasHorizontalAlignment.ALIGN_RIGHT,
				HasVerticalAlignment.ALIGN_MIDDLE);
		header1.getFlexCellFormatter().setAlignment(2, 6,
				HasHorizontalAlignment.ALIGN_RIGHT,
				HasVerticalAlignment.ALIGN_MIDDLE);
		header1.setCellSpacing(10);
		header1.addStyleName("mpdb-dataTableUserSamples");
		myProjectsSamples_label.setStyleName("big");
		this.add(header1);
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
				long id = (long) (MpDb.currentUser().getId());
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
		new ServerOp() {
			@Override
			public void begin() {
				MpDb.project_svc.all(MpDb.currentUser().getId(), this);
			}
			public void onSuccess(Object result) {
				adpRows();
				addProjects();
			}
		}.begin();
		return this;
	}

	private List<Project> getCheckedProjects() {
		List<Project> results = new ArrayList<Project>();
		for (int i = 0; i < list.getScrollTable().getDataTable().getRowCount(); i++) {
			if (((MCheckBox) list.getScrollTable().getDataTable().getWidget(i,
					0)).isChecked())
				results.add((Project) (((MCheckBox) list.getScrollTable()
						.getDataTable().getWidget(i, 0)).getValue()));
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
