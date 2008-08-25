package edu.rpi.metpetdb.client.ui.objects.list;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Hidden;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.widgetideas.table.client.FixedWidthFlexTable;

import edu.rpi.metpetdb.client.locale.LocaleHandler;
import edu.rpi.metpetdb.client.model.ProjectDTO;
import edu.rpi.metpetdb.client.model.SampleDTO;
import edu.rpi.metpetdb.client.paging.Column;
import edu.rpi.metpetdb.client.paging.PaginationParameters;
import edu.rpi.metpetdb.client.paging.Results;
import edu.rpi.metpetdb.client.ui.MpDb;
import edu.rpi.metpetdb.client.ui.ServerOp;
import edu.rpi.metpetdb.client.ui.Styles;
import edu.rpi.metpetdb.client.ui.TokenSpace;
import edu.rpi.metpetdb.client.ui.dialogs.CustomTableView;
import edu.rpi.metpetdb.client.ui.left.side.MySamples;
import edu.rpi.metpetdb.client.ui.widgets.MCheckBox;
import edu.rpi.metpetdb.client.ui.widgets.MLink;
import edu.rpi.metpetdb.client.ui.widgets.MPagePanel;

public class UserSamplesList extends MPagePanel implements ClickListener {
	private static final String cookieString = "UserSamplesList";
	private static final String samplesParameter = "Samples";
	private Label errMsg = new Label();
	private final FlexTable sampleDisplayFilters = new FlexTable();;
	private SampleListEx list;
	private MySamples mysamples;
	private final SimplePanel footerContainer = new SimplePanel();
	private final SimplePanel footerWrapper = new SimplePanel();
	private final FlexTable footer = new FlexTable();
	private Set<ProjectDTO> projectsList;
	private final ListBox projectListBox = new ListBox();
	private FlowPanel samplesContainer = new FlowPanel();
	private MLink createView;
	private MLink exportExcel;
	private MLink exportGoogleEarth;

	public UserSamplesList() {
	}

	protected void addPageHeader() {
		super.addPageHeader();
		setPageTitle("My Samples");

		final MLink uploadSample = new MLink("Upload Sample",
				TokenSpace.enterSample);
		final MLink bulkUpload = new MLink("Bulk Upload", TokenSpace.bulkUpload);

		uploadSample.setStylePrimaryName(Styles.LINK_LARGE_ICON);
		uploadSample.addStyleName(Styles.LINK_UPLOAD);

		bulkUpload.setStylePrimaryName(Styles.LINK_LARGE_ICON);
		bulkUpload.addStyleName(Styles.LINK_UPLOAD_MULTI);

		addActionListItem(uploadSample);
		addActionListItem(bulkUpload);
	}

	private void doExportGoogleEarth() {
		final FormPanel fp = new FormPanel();
		fp.setMethod(FormPanel.METHOD_GET);
		fp.setEncoding(FormPanel.ENCODING_URLENCODED);
		final HorizontalPanel hp = new HorizontalPanel();
		int currentpage = list.getScrollTable().getCurrentPage();
		for (int page = 0; page < list.getScrollTable().getNumPages(); page++) {
			list.getScrollTable().gotoPage(page, false);
			int i = 0;
			while (list.getScrollTable().getRowValue(i) != null) {
				Hidden sample = new Hidden(samplesParameter, String
						.valueOf(list.getScrollTable().getRowValue(i).getId()));
				hp.add(sample);
				i++;
			}
		}
		list.getScrollTable().gotoPage(currentpage, true);
		fp.add(hp);
		fp.setAction(GWT.getModuleBaseURL() + "BasicKML.kml?");
		fp.setVisible(false);
		add(fp);
		fp.submit();
	}

	private void projectSamples(final long projectId) {
		list = new SampleListEx(LocaleHandler.lc_text.noSamplesFound()) {
			public void update(final PaginationParameters p,
					final AsyncCallback<Results<SampleDTO>> ac) {
				MpDb.sample_svc.projectSamples(p, projectId, ac);
			}
		};
	}

	private void userSamples() {
		list = new SampleListEx(LocaleHandler.lc_text.noSamplesFound()) {
			public void update(final PaginationParameters p,
					final AsyncCallback<Results<SampleDTO>> ac) {
				long id = (long) (MpDb.currentUser().getId());
				MpDb.sample_svc.allSamplesForUser(p, id, ac);
			}
		};
	}

	private void addSamples() {
		createViewFromCookie();
		buildSampleFilters();
		samplesContainer.add(sampleDisplayFilters);
		buildSampleFooter();
		samplesContainer.add(list);
		samplesContainer.setStylePrimaryName(Styles.SAMPLES_CONTAINER);
		this.add(samplesContainer);
	}

	private void buildSampleFooter() {
		final MLink selectAll = new MLink("All", new ClickListener() {
			public void onClick(Widget sender) {
				for (int i = 0; i < list.getScrollTable().getDataTable()
						.getRowCount(); i++)
					((MCheckBox) list.getScrollTable().getDataTable()
							.getWidget(i, 0)).setChecked(true);
			}
		});

		final MLink selectNone = new MLink("None", new ClickListener() {
			public void onClick(Widget sender) {
				for (int i = 0; i < list.getScrollTable().getDataTable()
						.getRowCount(); i++)
					((MCheckBox) list.getScrollTable().getDataTable()
							.getWidget(i, 0)).setChecked(false);
			}
		});

		/* TODO: add functionality */
		final Label selectPrivate = new Label("Private");
		selectPrivate.addStyleName(Styles.BETA);

		/* TODO: add functionality */
		final Label selectPublic = new Label("Public");
		selectPublic.addStyleName(Styles.BETA);

		final Label selectLabel = new Label("Select:");
		selectLabel.setStylePrimaryName(Styles.DATATABLE_FOOTER_LABEL);

		final FlowPanel selectPanel = new FlowPanel();
		selectPanel.add(selectLabel);
		selectPanel.add(selectAll);
		selectPanel.add(selectNone);
		selectPanel.add(selectPrivate);
		selectPanel.add(selectPublic);
		selectPanel.setStylePrimaryName(Styles.DATATABLE_FOOTER_SELECT);

		final Label withSelected = new Label("With Selected:");
		withSelected.setStylePrimaryName(Styles.DATATABLE_FOOTER_LABEL);

		final Button btnRemove = new Button("Remove", new ClickListener() {
			public void onClick(Widget sender) {
				deleteSelected();
			}
		});

		final Button btnPublic = new Button("Make Public", new ClickListener() {
			public void onClick(Widget sender) {
				MakePublicSelected();
			}
		});

		final Label addToProjectLabel = new Label("Add to");
		addToProjectLabel
				.setStylePrimaryName(Styles.DATATABLE_FOOTER_ACTION_ITEM);

		projectListBox.addItem("Choose Project...");
		Iterator<ProjectDTO> it = projectsList.iterator();
		while (it.hasNext()) {
			final ProjectDTO project = (ProjectDTO) it.next();
			projectListBox.addItem(project.getName(), String.valueOf(project
					.getId()));
		}
		projectListBox.addChangeListener(new ChangeListener() {
			public void onChange(Widget sender) {
				if (projectListBox.getSelectedIndex() > 0)
					AddToProjectSelected();
			}
		});

		final Label exportLabel = new Label("Export:");
		exportLabel.setStylePrimaryName(Styles.DATATABLE_FOOTER_ACTION_ITEM);

		exportExcel = new MLink(LocaleHandler.lc_text.buttonExportExcel(),
				new ClickListener() {
					public void onClick(Widget sender) {
					}
				});
		exportExcel.addStyleName(Styles.BETA);

		exportGoogleEarth = new MLink(LocaleHandler.lc_text.buttonExportKML(),
				new ClickListener() {
					public void onClick(Widget sender) {
						doExportGoogleEarth();
					}
				});

		final FlowPanel withSelectedPanel = new FlowPanel();
		withSelectedPanel.add(withSelected);
		withSelectedPanel.add(btnRemove);
		withSelectedPanel.add(btnPublic);
		withSelectedPanel.add(addToProjectLabel);
		withSelectedPanel.add(projectListBox);
		withSelectedPanel.add(exportLabel);
		withSelectedPanel.add(exportExcel);
		withSelectedPanel.add(exportGoogleEarth);
		withSelectedPanel.setStylePrimaryName(Styles.DATATABLE_FOOTER_ACTIONS);

		footer.setWidget(0, 0, selectPanel);
		footer.setWidget(0, 1, withSelectedPanel);

		footerContainer.add(footer);
		footerContainer.setStylePrimaryName(Styles.DATATABLE_FOOTER);
		footerWrapper.add(footerContainer);
		footerWrapper.setStylePrimaryName(Styles.DATATABLE_FOOTER_WRAPPER);
		final FixedWidthFlexTable footerTable = new FixedWidthFlexTable();
		footerTable.setWidget(0, 0, footerWrapper);
		footerTable.getFlexCellFormatter().setColSpan(0, 0, 4);
		footerTable.setWidth("100%");
		list.getScrollTable().setFooterTable(footerTable);
	}

	private void buildSampleFilters() {
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
				list.newView(list.getOriginalColumns());
			}
		});

		createView = new MLink("Create New View", this);

		final Label quickfilters_label = new Label("Quick Filters:");
		final Label changeView_label = new Label("Change View:");

		recentlyAdded.addStyleName("beta");
		simple.addStyleName("beta");

		sampleDisplayFilters.setWidth("100%");

		sampleDisplayFilters.setWidget(0, 0, quickfilters_label);
		sampleDisplayFilters.setWidget(0, 1, recentlyAdded);
		sampleDisplayFilters.setWidget(0, 2, changeView_label);
		sampleDisplayFilters.setWidget(0, 3, simple);
		sampleDisplayFilters.setWidget(0, 4, detailed);
		sampleDisplayFilters.setWidget(0, 5, createView);

		sampleDisplayFilters.getFlexCellFormatter().setWidth(0, 1, "50%");
		sampleDisplayFilters.getFlexCellFormatter().setAlignment(0, 0,
				HasHorizontalAlignment.ALIGN_LEFT,
				HasVerticalAlignment.ALIGN_MIDDLE);
		sampleDisplayFilters.getFlexCellFormatter().setAlignment(0, 1,
				HasHorizontalAlignment.ALIGN_LEFT,
				HasVerticalAlignment.ALIGN_MIDDLE);
		sampleDisplayFilters.getFlexCellFormatter().setAlignment(0, 2,
				HasHorizontalAlignment.ALIGN_RIGHT,
				HasVerticalAlignment.ALIGN_MIDDLE);
		sampleDisplayFilters.getFlexCellFormatter().setAlignment(0, 3,
				HasHorizontalAlignment.ALIGN_RIGHT,
				HasVerticalAlignment.ALIGN_MIDDLE);
		sampleDisplayFilters.getFlexCellFormatter().setAlignment(0, 4,
				HasHorizontalAlignment.ALIGN_RIGHT,
				HasVerticalAlignment.ALIGN_MIDDLE);
		sampleDisplayFilters.getFlexCellFormatter().setAlignment(0, 5,
				HasHorizontalAlignment.ALIGN_RIGHT,
				HasVerticalAlignment.ALIGN_MIDDLE);
		sampleDisplayFilters.setCellSpacing(10);
		sampleDisplayFilters.addStyleName("mpdb-dataTableUserSamples");

	}

	public UserSamplesList display() {
		new ServerOp() {
			@Override
			public void begin() {
				MpDb.project_svc.all(MpDb.currentUser().getId(), this);
			}
			public void onSuccess(Object result) {
				projectsList = new HashSet<ProjectDTO>(
						(List<ProjectDTO>) result);
				addPageHeader();
				userSamples();
				addSamples();
			}
		}.begin();
		return this;
	}
	public UserSamplesList display(final long projectId) {
		new ServerOp() {
			@Override
			public void begin() {
				MpDb.project_svc.all(MpDb.currentUser().getId(), this);
			}
			public void onSuccess(Object result) {
				projectsList = new HashSet<ProjectDTO>(
						(List<ProjectDTO>) result);
				addPageHeader();
				projectSamples(projectId);
				addSamples();
			}
		}.begin();
		return this;
	}

	private List<SampleDTO> getCheckedSamples() {
		List<SampleDTO> results = new ArrayList<SampleDTO>();
		for (int i = 0; i < list.getScrollTable().getDataTable().getRowCount(); i++) {
			if (((MCheckBox) list.getScrollTable().getDataTable().getWidget(i,
					0)).isChecked())
				results.add((SampleDTO) (((MCheckBox) list.getScrollTable()
						.getDataTable().getWidget(i, 0)).getValue()));
		}
		return results;
	}

	private void deleteSelected() {
		new ServerOp() {
			@Override
			public void begin() {
				ArrayList<SampleDTO> publicSamples = new ArrayList<SampleDTO>();
				List<SampleDTO> CheckedSamples = getCheckedSamples();
				Iterator<SampleDTO> itr = CheckedSamples.iterator();
				/* Check if we are trying to delete any public samples */
				while (itr.hasNext()) {
					final SampleDTO s = itr.next();
					if (s.isPublicData()) {
						publicSamples.add(s);
					}
				}
				/* unhighlight everything */
				for (int i = 0; i < list.getScrollTable().getDataTable()
						.getRowCount(); i++) {
					list.getScrollTable().getDataTable().getRowFormatter()
							.removeStyleName(i, "highlighted-row");
				}
				/*
				 * if no samples were selected, just make sure to reset the
				 * errMsg
				 */
				if (CheckedSamples.size() == 0) {
					UserSamplesList.this.remove(errMsg);
				}
				/* if there are public samples, highlight them in the table */
				else if (publicSamples.size() > 0) {
					for (int i = 0; i < list.getScrollTable().getDataTable()
							.getRowCount(); i++) {
						final SampleDTO s = (SampleDTO) ((MCheckBox) list
								.getScrollTable().getDataTable()
								.getWidget(i, 0)).getValue();
						for (int j = 0; j < publicSamples.size(); j++) {
							if (publicSamples.get(j).getId() == s.getId()) {
								list.getScrollTable().getDataTable()
										.getRowFormatter().addStyleName(i,
												"highlighted-row");
							}
						}
						errMsg.setText(LocaleHandler.lc_text
								.cannotDeletePublicSamples());
						UserSamplesList.this.insert(errMsg, 0);
					}
				}
				/* If they're all private, delete them */
				else if (publicSamples.size() == 0) {
					Iterator<SampleDTO> itr2 = CheckedSamples.iterator();
					while (itr2.hasNext()) {
						MpDb.sample_svc.delete(itr2.next().getId(), this);
					}
				}

			}
			public void onSuccess(Object result) {
				UserSamplesList.this.remove(errMsg);
				list.getScrollTable().reloadPage();
			}
		}.begin();
	}
	private void MakePublicSelected() {
		new ServerOp() {
			@Override
			public void begin() {
				List<SampleDTO> CheckedSamples = getCheckedSamples();
				Iterator<SampleDTO> itr = CheckedSamples.iterator();
				while (itr.hasNext()) {
					SampleDTO current = itr.next();
					current.setPublicData(true);
					MpDb.sample_svc.save(current, this);
				}
			}
			public void onSuccess(Object result) {
				UserSamplesList.this.remove(errMsg);
				list.getScrollTable().reloadPage();
			}
		}.begin();
	}
	private void AddToProjectSelected() {
		new ServerOp() {
			@Override
			public void begin() {
				MpDb.project_svc.details(Integer.parseInt(projectListBox
						.getValue(projectListBox.getSelectedIndex())), this);
			}
			public void onSuccess(final Object result) {
				new ServerOp() {
					@Override
					public void begin() {
						List<SampleDTO> CheckedSamples = getCheckedSamples();
						Iterator<SampleDTO> itr = CheckedSamples.iterator();
						while (itr.hasNext()) {
							SampleDTO current = itr.next();
							current.getProjects().add((ProjectDTO) result);
							MpDb.sample_svc.save(current, this);
						}
					}
					public void onSuccess(Object result2) {
						UserSamplesList.this.remove(errMsg);
						list.getScrollTable().reloadPage();
					}
				}.begin();
			}
		}.begin();
	}

	private void createViewFromCookie() {
		final ArrayList<Column> originalColumns = new ArrayList<Column>(list
				.getOriginalColumns());
		final ArrayList<String> cookiedColumns;
		final ArrayList<Column> displayColumns = new ArrayList<Column>();
		if (Cookies.getCookie(cookieString) != null) {
			cookiedColumns = new ArrayList<String>(Arrays.asList(Cookies
					.getCookie(cookieString).split(",")));
			Iterator<Column> itr = originalColumns.iterator();
			while (itr.hasNext()) {
				final Column col = itr.next();
				if (cookiedColumns.contains(col.getTitle()))
					displayColumns.add(col);
			}
			list.newView(displayColumns);
		}
	}

	public void onClick(Widget sender) {
		if (sender == createView) {
			CustomTableView myView = new CustomTableView(list, cookieString);
		}

		for (int i = 0; i < list.getScrollTable().getDataTable().getRowCount(); i++) {
			list.getScrollTable().getDataTable().getRowFormatter()
					.removeStyleName(i, "highlighted-row");
		}
	}

}
