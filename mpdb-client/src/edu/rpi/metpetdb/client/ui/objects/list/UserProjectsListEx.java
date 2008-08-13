package edu.rpi.metpetdb.client.ui.objects.list;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
import edu.rpi.metpetdb.client.model.ProjectDTO;
import edu.rpi.metpetdb.client.paging.PaginationParameters;
import edu.rpi.metpetdb.client.paging.Results;
import edu.rpi.metpetdb.client.ui.MpDb;
import edu.rpi.metpetdb.client.ui.ServerOp;
import edu.rpi.metpetdb.client.ui.TokenSpace;
import edu.rpi.metpetdb.client.ui.dialogs.CustomTableView;
import edu.rpi.metpetdb.client.ui.widgets.MCheckBox;
import edu.rpi.metpetdb.client.ui.widgets.MLink;

public class UserProjectsListEx extends FlowPanel implements ClickListener {
	private static final String cookieString = "UserProjectsListEx";
	private FlexTable header1;
	private ProjectListEx list;
	private ListBox lb;
	private FlexTable Projects_ft;

	public UserProjectsListEx() {
	}

	private void addTopRows() {
		header1 = new FlexTable();

		header1.setWidth("100%");

		final MLink createProject = new MLink("Create New Project",
				TokenSpace.newProject);

		final MLink recentlyAdded = new MLink("Recent Changes",
				new ClickListener() {
					public void onClick(Widget sender) {
					}
				});

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
		CustomTableView myView = new CustomTableView(list, cookieString);
	}

	private void addProjects() {
		list = new ProjectListEx(LocaleHandler.lc_text.noProjectsFound()) {
			public void update(final PaginationParameters p,
					final AsyncCallback<Results<ProjectDTO>> ac) {
				long id = (long) (MpDb.currentUser().getId());
				MpDb.project_svc.all(p, id, ac);
			}
		};
		Projects_ft = new FlexTable();
		Projects_ft.setWidth("100%");
		Projects_ft.setWidget(0, 0, list);

		FixedWidthFlexTable footer = new FixedWidthFlexTable();
		CheckBox cb = new CheckBox("Select All");
		cb.addClickListener(new ClickListener() {
			public void onClick(Widget sender) {
				for (int i = 0; i < list.getScrollTable().getDataTable()
						.getRowCount(); i++)
					((MCheckBox) list.getScrollTable().getDataTable()
							.getWidget(i, 0)).setChecked(((CheckBox) sender)
							.isChecked());
			}
		});
		lb = new ListBox();

		lb.addItem("Remove");
		lb.addItem("Duplicate");

		Button btn = new Button("Apply to Selected", new ClickListener() {
			public void onClick(Widget sender) {
				if (lb.getItemText(lb.getSelectedIndex()).equals("Remove")) {
					deleteSelected();
				} else if (lb.getItemText(lb.getSelectedIndex()).equals(
						"Duplicate")) {
					DuplicateSelected();
				}
			}
		});
		btn.setHeight("30px");
		FlexTable realFooter = new FlexTable();

		realFooter.setWidget(0, 0, cb);
		realFooter.setWidget(0, 1, lb);
		realFooter.setWidget(0, 2, btn);
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
		list.getScrollTable().setFooterTable(footer);

		this.add(Projects_ft);
	}

	public UserProjectsListEx display() {
		new ServerOp() {
			@Override
			public void begin() {
				MpDb.project_svc.all(MpDb.currentUser().getId(), this);
			}
			public void onSuccess(Object result) {
				addTopRows();
				addProjects();
			}
		}.begin();
		return this;
	}

	private List<ProjectDTO> getCheckedProjects() {
		List<ProjectDTO> results = new ArrayList<ProjectDTO>();
		for (int i = 0; i < list.getScrollTable().getDataTable().getRowCount(); i++) {
			if (((MCheckBox) list.getScrollTable().getDataTable().getWidget(i,
					0)).isChecked())
				results.add((ProjectDTO) (((MCheckBox) list.getScrollTable()
						.getDataTable().getWidget(i, 0)).getValue()));
		}
		return results;
	}

	private void deleteSelected() {
		new ServerOp() {
			@Override
			public void begin() {
				List<ProjectDTO> CheckedProjects = getCheckedProjects();
				Iterator<ProjectDTO> itr = CheckedProjects.iterator();
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
