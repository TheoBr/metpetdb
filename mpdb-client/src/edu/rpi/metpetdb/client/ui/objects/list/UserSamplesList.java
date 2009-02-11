package edu.rpi.metpetdb.client.ui.objects.list;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.Hidden;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.widgetideas.table.client.FixedWidthFlexTable;

import edu.rpi.metpetdb.client.locale.LocaleHandler;
import edu.rpi.metpetdb.client.model.Project;
import edu.rpi.metpetdb.client.model.Sample;
import edu.rpi.metpetdb.client.model.Subsample;
import edu.rpi.metpetdb.client.paging.Column;
import edu.rpi.metpetdb.client.paging.PaginationParameters;
import edu.rpi.metpetdb.client.paging.Results;
import edu.rpi.metpetdb.client.ui.CSS;
import edu.rpi.metpetdb.client.ui.MpDb;
import edu.rpi.metpetdb.client.ui.ServerOp;
import edu.rpi.metpetdb.client.ui.TokenSpace;
import edu.rpi.metpetdb.client.ui.VoidServerOp;
import edu.rpi.metpetdb.client.ui.dialogs.ConfirmationDialogBox;
import edu.rpi.metpetdb.client.ui.dialogs.CustomTableView;
import edu.rpi.metpetdb.client.ui.widgets.MCheckBox;
import edu.rpi.metpetdb.client.ui.widgets.MLink;
import edu.rpi.metpetdb.client.ui.widgets.MPagePanel;

public class UserSamplesList extends MPagePanel implements ClickListener {
	private static final String cookieString = "UserSamplesList";
	private static final String samplesParameter = "Samples";
	private static final String urlParameter = "url";
	private Label errMsg = new Label();
	private final FlexTable sampleDisplayFilters = new FlexTable();
	private SampleListEx list;
	private final FlowPanel footerContainer = new FlowPanel();
	private final SimplePanel footerWrapper = new SimplePanel();
	private Set<Project> projectsList;
	private final ListBox projectListBox = new ListBox();
	private final ListBox selectListBox = new ListBox();
	private FlowPanel samplesContainer = new FlowPanel();
	private final FlowPanel columnViewPanel = new FlowPanel();
	private MLink customCols;
	private MLink exportExcel;
	private MLink exportGoogleEarth;

	public UserSamplesList() {
	}

	protected void addPageHeader() {
		setPageTitle("My Samples");

		final MLink addSample = new MLink("Add Sample", TokenSpace.enterSample);
		final MLink bulkUpload = new MLink("Upload Data",
				TokenSpace.bulkUpload);

		addSample.setStylePrimaryName(CSS.LINK_LARGE_ICON);
		addSample.addStyleName(CSS.LINK_ADD);

		bulkUpload.setStylePrimaryName(CSS.LINK_LARGE_ICON);
		bulkUpload.addStyleName(CSS.LINK_UPLOAD_MULTI);

		addPageActionItem(addSample);
		addPageActionItem(bulkUpload);
	}

	private void doExportGoogleEarth() {
		new ServerOp<List<Sample>>() {
			@Override
			public void begin() {
				long id = (long) (MpDb.currentUser().getId());
				MpDb.sample_svc.allSamplesForUser(id, this);
			}
			public void onSuccess(List<Sample> result) {
				final FormPanel fp = new FormPanel();
				fp.setMethod(FormPanel.METHOD_GET);
				fp.setEncoding(FormPanel.ENCODING_URLENCODED);
				final HorizontalPanel hp = new HorizontalPanel();
				for (Sample s : result){
					Hidden sample = new Hidden(samplesParameter,String.valueOf(s.getId()));
					hp.add(sample);
				}
				Hidden url = new Hidden(urlParameter,GWT.getModuleBaseURL() + "#" + 
						LocaleHandler.lc_entity.TokenSpace_Sample_Details() + LocaleHandler.lc_text.tokenSeparater());
				hp.add(url);
				fp.add(hp);
				fp.setAction(GWT.getModuleBaseURL() + "BasicKML.kml?");
				fp.setVisible(false);
				add(fp);
				fp.submit();
			}
		}.begin();	
	}
	
	private void doExportExcel(){
		
		final SampleListEx listEx = new SampleListEx(LocaleHandler.lc_text.noSamplesFound()) {
			public void update(final PaginationParameters p,
					final AsyncCallback<Results<Sample>> ac) {
				long id = (long) (MpDb.currentUser().getId());
				MpDb.sample_svc.allSamplesForUser(p, id, ac);
			}
		};	
		listEx.setPageSize(list.getScrollTable().getNumPages()*list.getPageSize());
		listEx.newView(list.getDisplayColumns());
		new ServerOp() {
			@Override
			public void begin() {
				final PaginationParameters p = new PaginationParameters();
				p.setAscending(true);
				p.setParameter(list.getDefaultSortParameter());
				listEx.update(p, this);
			}

			public void onSuccess(final Object result) {
				final FormPanel fp = new FormPanel();
				fp.setMethod(FormPanel.METHOD_GET);
				fp.setEncoding(FormPanel.ENCODING_URLENCODED);
				String values = "";
				
				for (int i = 1; i < listEx.getDisplayColumns().size(); i++){
					values+=listEx.getDisplayColumns().get(i).getTitle() +"\t";
				}
				values+="\n";
				for(int i = 0; i <listEx.getScrollTable().getDataTable().getRowCount(); i++) {
					for (int j = 1; j < list.getScrollTable().getDataTable().getColumnCount(); j++){
						if (listEx.getScrollTable().getDataTable().getWidget(i, j) instanceof Image){
							values+=listEx.getScrollTable().getDataTable().getWidget(i, j).toString() +"\t";
						} else {
							values+=listEx.getScrollTable().getDataTable().getText(i, j) +"\t";
						}
					}
					values+="\n";
				}
				
				Hidden data = new Hidden("excel",values);
				fp.add(data);
				fp.setAction(GWT.getModuleBaseURL() + "excel.svc");
				fp.setVisible(false);
				add(fp);
				fp.submit();
			}
		}.begin();
	}

	private void projectSamples(final long projectId) {
		list = new SampleListEx(LocaleHandler.lc_text.noSamplesFound()) {
			public void update(final PaginationParameters p,
					final AsyncCallback<Results<Sample>> ac) {
				MpDb.sample_svc.projectSamples(p, projectId, ac);
			}
		};
	}

	private void userSamples() {
		list = new SampleListEx(LocaleHandler.lc_text.noSamplesFound()) {
			public void update(final PaginationParameters p,
					final AsyncCallback<Results<Sample>> ac) {
				long id = (long) (MpDb.currentUser().getId());
				MpDb.sample_svc.allSamplesForUser(p, id, ac);
			}
		};
		list.simpleView();
	}

	private void addSamples() {
		createViewFromCookie();
		buildSampleFilters();
		samplesContainer.add(columnViewPanel);
		buildSampleFooter();
		samplesContainer.add(list);
		samplesContainer.setStylePrimaryName(CSS.SAMPLES_CONTAINER);
		this.add(samplesContainer);
	}

	private void buildSampleFooter() {

		final Label selectLabel = new Label("Select:");
		selectLabel.setStylePrimaryName(CSS.DATATABLE_FOOTER_LABEL);
		selectLabel.addStyleName(CSS.DATATABLE_FOOTER_ITEM);

		selectListBox.addItem("---");
		selectListBox.addItem("None");
		selectListBox.addItem("Private");
		selectListBox.addItem("Public");
		selectListBox.addItem("All");
		selectListBox.addChangeListener(new ChangeListener() {
			public void onChange(Widget sender) {
				if (selectListBox.getSelectedIndex() == 1) {
					for (int i = 0; i < list.getScrollTable().getDataTable()
							.getRowCount(); i++)
						((MCheckBox) list.getScrollTable().getDataTable()
								.getWidget(i, 0)).setChecked(false);
				} else if (selectListBox.getSelectedIndex() == 2) {
					// TODO select only private samples
				} else if (selectListBox.getSelectedIndex() == 3) {
					// TODO select only public samples
				} else if (selectListBox.getSelectedIndex() == 4) {
					for (int i = 0; i < list.getScrollTable().getDataTable()
							.getRowCount(); i++)
						((MCheckBox) list.getScrollTable().getDataTable()
								.getWidget(i, 0)).setChecked(true);
				}
				new Timer() {
					public void run() {
						selectListBox.setSelectedIndex(0);						
					}
				}.schedule(500);
			}
		});

		final Label adProjectLabel = new Label("Add to:");
		adProjectLabel.setStylePrimaryName(CSS.DATATABLE_FOOTER_ITEM);
		adProjectLabel.addStyleName(CSS.DATATABLE_FOOTER_LABEL);

		projectListBox.addItem("Choose Project...");
		Iterator<Project> it = projectsList.iterator();
		while (it.hasNext()) {
			final Project project = (Project) it.next();
			projectListBox.addItem(project.getName(), String.valueOf(project
					.getId()));
		}
		projectListBox.addChangeListener(new ChangeListener() {
			public void onChange(Widget sender) {
				if (projectListBox.getSelectedIndex() > 0){
					final List<Sample> CheckedSamples = getCheckedSamples();
					if (CheckedSamples.size() > 0){
						new ServerOp<Boolean>(){
							public void begin() {
								new ConfirmationDialogBox(LocaleHandler.lc_text.confirmation_AddToProject(), true, this);
							}
		
							public void onSuccess(final Boolean result) {
								if (result)
									UserSamplesList.this.AdProjectSelected(CheckedSamples);
							}
						}.begin();
					} else {
						errMsg.setText(LocaleHandler.lc_text
								.message_ChooseSamples());
						UserSamplesList.this.insert(errMsg, 0);
					}
				}
			}
		});

		final Label exportLabel = new Label("Export:");
		exportLabel.setStylePrimaryName(CSS.DATATABLE_FOOTER_ITEM);
		exportLabel.addStyleName(CSS.DATATABLE_FOOTER_LABEL);

		exportExcel = new MLink(LocaleHandler.lc_text.buttonExportExcel(),
				new ClickListener() {
					public void onClick(Widget sender) {
						doExportExcel();
					}
				});
		exportExcel.addStyleName(CSS.DATATABLE_FOOTER_SUBITEM);

		exportGoogleEarth = new MLink(LocaleHandler.lc_text.buttonExportKML(),
				new ClickListener() {
					public void onClick(Widget sender) {
						doExportGoogleEarth();
					}
				});
		exportGoogleEarth.addStyleName(CSS.DATATABLE_FOOTER_SUBITEM);

		final MLink makePublic = new MLink("Make Public", new ClickListener() {
			public void onClick(Widget sender) {
				final List<Sample> CheckedSamples = getCheckedSamples();
				if (CheckedSamples.size() > 0){
					new ServerOp<Boolean>(){
						public void begin() {
							new ConfirmationDialogBox(LocaleHandler.lc_text.confirmation_MakePublic(), true, this);
						}
	
						public void onSuccess(final Boolean result) {
							if (result)
								UserSamplesList.this.MakePublicSelected(CheckedSamples);
						}
					}.begin();
				} else {
					errMsg.setText(LocaleHandler.lc_text
							.message_ChooseSamples());
					UserSamplesList.this.insert(errMsg, 0);
				}
			}
		});
		makePublic.addStyleName(CSS.DATATABLE_FOOTER_ITEM);

		final MLink remove = new MLink("Remove", new ClickListener() {
			public void onClick(Widget sender) {
				final List<Sample> CheckedSamples = getCheckedSamples();
				if (CheckedSamples.size() > 0){
					new ServerOp<Boolean>(){
						public void begin() {
							new ConfirmationDialogBox(LocaleHandler.lc_text.confirmation_Delete(), true, this);
						}
	
						public void onSuccess(final Boolean result) {
							if (result)
								UserSamplesList.this.deleteSelected(CheckedSamples);
						}
					}.begin();
				} else {
					errMsg.setText(LocaleHandler.lc_text
							.message_ChooseSamples());
					UserSamplesList.this.insert(errMsg, 0);
				}
			}
		});
		remove.addStyleName(CSS.DATATABLE_FOOTER_ITEM);

		footerContainer.add(selectLabel);
		footerContainer.add(selectListBox);
		if (!projectsList.isEmpty()) {
			footerContainer.add(adProjectLabel);
			footerContainer.add(projectListBox);
		}
		footerContainer.add(exportLabel);
		footerContainer.add(exportExcel);
		footerContainer.add(exportGoogleEarth);
		footerContainer.add(makePublic);
		footerContainer.add(remove);
		footerContainer.setStylePrimaryName(CSS.DATATABLE_FOOTER);
		footerWrapper.add(footerContainer);
		footerWrapper.setStylePrimaryName(CSS.DATATABLE_FOOTER_WRAPPER);

		final FixedWidthFlexTable footerTable = new FixedWidthFlexTable();
		footerTable.setWidget(0, 0, footerWrapper);
		footerTable.getFlexCellFormatter().setColSpan(0, 0, 4);
		footerTable.setWidth("100%");
		list.getScrollTable().setFooterTable(footerTable);
	}

	private void buildSampleFilters() {
		final MLink simple = new MLink("Simple", new ClickListener() {
			public void onClick(Widget sender) {
				list.simpleView();
			}
		});

		final MLink detailed = new MLink("Detailed", new ClickListener() {
			public void onClick(Widget sender) {
				list.newView(list.getOriginalColumns());
			}
		});

		customCols = new MLink("Custom", new ClickListener() {
			public void onClick(Widget sender) {
				CustomTableView myView = new CustomTableView(list, cookieString);
			}
		});

		final Label columnsLabel = new Label("Columns:");

		columnViewPanel.add(columnsLabel);
		columnViewPanel.add(simple);
		columnViewPanel.add(detailed);
		columnViewPanel.add(customCols);

		columnViewPanel.addStyleName(CSS.DATATABLE_HEADER_FILTERS);

	}

	public UserSamplesList display() {
		new ServerOp() {
			@Override
			public void begin() {
				MpDb.project_svc.all(MpDb.currentUser().getId(), this);
			}
			public void onSuccess(Object result) {
				projectsList = new HashSet<Project>((List<Project>) result);
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
				projectsList = new HashSet<Project>((List<Project>) result);
				addPageHeader();
				projectSamples(projectId);
				addSamples();
			}
		}.begin();
		return this;
	}

	private List<Sample> getCheckedSamples() {
		List<Sample> results = new ArrayList<Sample>();
		for (int i = 0; i < list.getScrollTable().getDataTable().getRowCount(); i++) {
			if (((MCheckBox) list.getScrollTable().getDataTable().getWidget(i,
					0)).isChecked())
				results.add((Sample) (((MCheckBox) list.getScrollTable()
						.getDataTable().getWidget(i, 0)).getValue()));
		}
		return results;
	}

	private void deleteSelected(final List<Sample> CheckedSamples) {
		new VoidServerOp() {
			@Override
			public void begin() {
				ArrayList<Sample> publicSamples = new ArrayList<Sample>();
				Iterator<Sample> itr = CheckedSamples.iterator();
				/* Check if we are trying to delete any public samples */
				while (itr.hasNext()) {
					final Sample s = itr.next();
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
				/* if there are public samples, highlight them in the table */
				if (publicSamples.size() > 0) {
					for (int i = 0; i < list.getScrollTable().getDataTable()
							.getRowCount(); i++) {
						final Sample s = (Sample) ((MCheckBox) list
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
					Iterator<Sample> itr2 = CheckedSamples.iterator();
					final ArrayList<Long> ids = new ArrayList<Long>();
					while (itr2.hasNext()) {
						ids.add(itr2.next().getId());
					}
					MpDb.sample_svc.deleteAll(ids, this);
				}

			}
			public void onSuccess() {
				UserSamplesList.this.remove(errMsg);
				selectListBox.setSelectedIndex(0);
				list.getScrollTable().reloadPage();
			}
		}.begin();
	}

	private void MakePublicSelected(final List<Sample> CheckedSamples) {
		new ServerOp() {
			@Override
			public void begin() {
				Iterator<Sample> itr = CheckedSamples.iterator();
				while (itr.hasNext()) {
					Sample current = itr.next();
					current.setPublicData(true);
					MpDb.sample_svc.save(current, this);
				}
			}
			public void onSuccess(Object result) {
				UserSamplesList.this.remove(errMsg);
				selectListBox.setSelectedIndex(0);
				list.getScrollTable().reloadPage();
			}
		}.begin();
	}

	private void AdProjectSelected(final List<Sample> CheckedSamples) {
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
						Iterator<Sample> itr = CheckedSamples.iterator();
						while (itr.hasNext()) {
							Sample current = itr.next();
							current.getProjects().add((Project) result);
							MpDb.sample_svc.save(current, this);
						}
					}
					public void onSuccess(Object result2) {
						UserSamplesList.this.remove(errMsg);
						projectListBox.setSelectedIndex(0);
						selectListBox.setSelectedIndex(0);
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
		if (errMsg.isAttached())
			UserSamplesList.this.remove(errMsg);
		for (int i = 0; i < list.getScrollTable().getDataTable().getRowCount(); i++) {
			list.getScrollTable().getDataTable().getRowFormatter()
					.removeStyleName(i, "highlighted-row");
		}
	}

}
