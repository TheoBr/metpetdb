package edu.rpi.metpetdb.client.ui.objects.list;

import java.util.Iterator;

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
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.widgetideas.table.client.FixedWidthFlexTable;

import edu.rpi.metpetdb.client.model.ProjectDTO;
import edu.rpi.metpetdb.client.model.SampleDTO;
import edu.rpi.metpetdb.client.paging.PaginationParameters;
import edu.rpi.metpetdb.client.paging.Results;
import edu.rpi.metpetdb.client.ui.MpDb;
import edu.rpi.metpetdb.client.ui.TokenSpace;
import edu.rpi.metpetdb.client.ui.dialogs.CustomTableView;
import edu.rpi.metpetdb.client.ui.widgets.MLink;
import edu.rpi.metpetdb.client.ui.widgets.MTabBar;

public class UserSamplesList extends FlowPanel implements ClickListener {
	private FlexTable header1;
	private FlexTable projects;
	private ScrollPanel projectScroll;
	private SampleListEx list;

	public UserSamplesList() {

	}

	private void addTopRows() {
		header1 = new FlexTable();
		FlexTable tabHolder = new FlexTable();

		header1.setWidth("100%");

		final MLink uploadSample = new MLink("Enter Sample",
				TokenSpace.enterSample);

		final MLink bulkUpload = new MLink("Bulk Upload", TokenSpace.bulkUpload);

		final MLink recentlyAdded = new MLink("Recently Added",
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

		final Label mySamples_label = new Label("My Samples");
		final Label quickfilters_label = new Label("Quick Filters:");
		final Label changeView_label = new Label("Change View:");
		uploadSample.addStyleName("addlink");
		bulkUpload.addStyleName("addlink");
		final MTabBar tb = new MTabBar();
		tb.addTab("All");
		tb.addTab("Newest");
		tb.addTab("Favorites");
		tb.selectTab(0);

		tabHolder.addStyleName("beta");
		recentlyAdded.addStyleName("beta");
		simple.addStyleName("beta");
		detailed.addStyleName("beta");
		createView.addStyleName("beta");

		tabHolder.setWidget(0, 0, tb);

		header1.setWidget(0, 0, mySamples_label);
		header1.setWidget(0, 1, tabHolder);
		header1.setWidget(1, 0, uploadSample);
		header1.setWidget(1, 1, bulkUpload);

		header1.setWidget(2, 0, quickfilters_label);
		header1.setWidget(2, 1, recentlyAdded);
		populateProjects();
		header1.setWidget(2, 2, projectScroll);
		header1.setWidget(2, 3, changeView_label);
		header1.setWidget(2, 4, simple);
		header1.setWidget(2, 5, detailed);
		header1.setWidget(2, 6, createView);

		header1.getFlexCellFormatter().setColSpan(0, 0, 3);
		header1.getFlexCellFormatter().setColSpan(0, 1, 4);
		header1.getFlexCellFormatter().setColSpan(1, 1, 2);
		header1.getFlexCellFormatter().setWidth(1, 0, "100px");
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
		mySamples_label.setStyleName("big");
		uploadSample.setStyleName("addlink");
		bulkUpload.setStyleName("addlink");
		this.add(header1);
	}

	public void onClick(Widget sender) {
		CustomTableView myView = new CustomTableView(list, this);
	}

	private void populateProjects() {
		projectScroll = new ScrollPanel();
		projects = new FlexTable();
		projects.setCellSpacing(10);
		projectScroll.setWidth("550px");
		Iterator<ProjectDTO> it = MpDb.currentUser().getProjects().iterator();
		int i = 0;
		while (it.hasNext()) {
			final ProjectDTO project = (ProjectDTO) it.next();
			projects.setWidget(0, i, new MLink("In " + project.getName() + " ",
					new ClickListener() {
						public void onClick(Widget sender) {
						}
					}));
			projects.getFlexCellFormatter().setWordWrap(0, i, false);
			projects.getFlexCellFormatter().setAlignment(0, i,
					HasHorizontalAlignment.ALIGN_CENTER,
					HasVerticalAlignment.ALIGN_BOTTOM);
			i++;
		}
		projectScroll.add(projects);

	}

	private void addSamples() {
		list = new SampleListEx() {
			public void update(final PaginationParameters p,
					final AsyncCallback<Results<SampleDTO>> ac) {
				long id = (long) (MpDb.currentUser().getId());
				MpDb.sample_svc.allSamplesForUser(p, id, ac);
			}
		};
		final FlexTable Samples_ft = new FlexTable();
		Samples_ft.setWidth("100%");
		Samples_ft.setWidget(0, 0, list);

		FixedWidthFlexTable footer = new FixedWidthFlexTable();
		CheckBox cb = new CheckBox("Select All");
		cb.addClickListener(new ClickListener() {
			public void onClick(Widget sender) {
				for (int i = 0; i < list.scrollTable.getDataTable()
						.getRowCount(); i++)
					((CheckBox) list.scrollTable.getDataTable().getWidget(i, 0))
							.setChecked(((CheckBox) sender).isChecked());
			}
		});
		ListBox lb = new ListBox();

		Iterator<ProjectDTO> it = MpDb.currentUser().getProjects().iterator();
		while (it.hasNext()) {
			final ProjectDTO project = (ProjectDTO) it.next();
			lb.addItem("Add to '" + project.getName() + "'");
		}
		lb.addItem("Remove");
		lb.addItem("Make Public");

		Button btn = new Button("Apply to Selected");
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
		list.scrollTable.setFooterTable(footer);

		this.add(Samples_ft);
	}

	public UserSamplesList display() {
		addTopRows();
		addSamples();
		return this;
	}
}
