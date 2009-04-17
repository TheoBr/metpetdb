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
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.Hidden;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.locale.LocaleHandler;
import edu.rpi.metpetdb.client.model.Project;
import edu.rpi.metpetdb.client.model.Sample;
import edu.rpi.metpetdb.client.paging.PaginationParameters;
import edu.rpi.metpetdb.client.paging.Results;
import edu.rpi.metpetdb.client.ui.CSS;
import edu.rpi.metpetdb.client.ui.MpDb;
import edu.rpi.metpetdb.client.ui.TokenSpace;
import edu.rpi.metpetdb.client.ui.commands.ServerOp;
import edu.rpi.metpetdb.client.ui.commands.VoidServerOp;
import edu.rpi.metpetdb.client.ui.dialogs.ConfirmationDialogBox;
import edu.rpi.metpetdb.client.ui.dialogs.MakePublicDialog;
import edu.rpi.metpetdb.client.ui.excel.ExcelUtil;
import edu.rpi.metpetdb.client.ui.widgets.MCheckBox;
import edu.rpi.metpetdb.client.ui.widgets.MGoogleEarthPopUp;
import edu.rpi.metpetdb.client.ui.widgets.MLink;
import edu.rpi.metpetdb.client.ui.widgets.panels.MPagePanel;

public class UserSamplesList extends MPagePanel implements ClickListener {
	private static final String cookieString = "UserSamplesList";
	private static final String samplesParameter = "Samples";
	private static final String urlParameter = "url";
	private Label errMsg = new Label();
	private SampleList list;
	private final FlowPanel tableActions = new FlowPanel();
	private Set<Project> projectsList;
	private final ListBox projectListBox = new ListBox();
	private final ListBox selectListBox = new ListBox();
	private final FlowPanel columnViewPanel = new FlowPanel();
	private MLink customCols;
	private MLink exportExcel;
	private MLink exportGoogleEarth;
	private MLink viewGoogleEarth;
	private MGoogleEarthPopUp earthPopup = new MGoogleEarthPopUp();

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

	private void doViewGoogleEarth() {
		new ServerOp<List<Sample>>() {
			@Override
			public void begin() {
				int id = (int) (MpDb.currentUser().getId());
				List<Sample> checked = getCheckedSamples();
				if (checked.size() == 0)
					MpDb.sample_svc.allSamplesForUser(id, this);
				else {
					onSuccess(checked);
				}
			}
			public void onSuccess(List<Sample> result) {
				earthPopup.createUI(new ArrayList(result));
				earthPopup.show();
			}
		}.begin();	
		
	}
	
	private void doExportGoogleEarth() {
		new ServerOp<List<Sample>>() {
			@Override
			public void begin() {
				int id = (int) (MpDb.currentUser().getId());
				List<Sample> checked = getCheckedSamples();
				if (checked.size() == 0)
					MpDb.sample_svc.allSamplesForUser(id, this);
				else {
					onSuccess(checked);
				}
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
		final FormPanel fp = new FormPanel();
		fp.setMethod(FormPanel.METHOD_GET);
		fp.setEncoding(FormPanel.ENCODING_URLENCODED);
		final HorizontalPanel hp = new HorizontalPanel();
		
		new ServerOp<List<Sample>>() {
			@Override
			public void begin() {
				long id = (long) (MpDb.currentUser().getId());
				List<Sample> checked = getCheckedSamples();
				if (checked.size() == 0)
					MpDb.sample_svc.allSamplesForUser(id, this);
				else {
					onSuccess(checked);
				}
			}
			public void onSuccess(List<Sample> result) {
				for (String columnHeader : ExcelUtil.columnHeaders){
					hp.add(new Hidden(ExcelUtil.columnHeaderParameter, columnHeader));
				}
				for (int i = 0; i < result.size(); i++) {
					Hidden sample = new Hidden(samplesParameter, String
							.valueOf(result.get(i).getId()));
					hp.add(sample);
				}
				Hidden url = new Hidden(urlParameter,GWT.getModuleBaseURL() + "#" + 
						LocaleHandler.lc_entity.TokenSpace_Sample_Details() + LocaleHandler.lc_text.tokenSeparater());
				hp.add(url);
				fp.add(hp);
				fp.setAction(GWT.getModuleBaseURL() + "excel.svc?");
				fp.setVisible(false);
				add(fp);
				fp.submit();
			}
		}.begin();
	}

//	private void projectSamples(final long projectId) {
//		list = new SampleListEx(LocaleHandler.lc_text.noSamplesFound()) {
//			public void update(final PaginationParameters p,
//					final AsyncCallback<Results<Sample>> ac) {
//				MpDb.sample_svc.projectSamples(p, projectId, ac);
//			}
//		};
//	}

	private void userSamples() {
		list = new SampleList() {
			public void update(final PaginationParameters p,
					final AsyncCallback<Results<Sample>> ac) {
				long id = (long) (MpDb.currentUser().getId());
				MpDb.sample_svc.allSamplesForUser(p, id, ac);
			}
		};
	}

	private void addSamples() {
		createViewFromCookie();
		buildSampleFilters();
		buildSampleActions();
		this.add(list);
	}

	private void buildSampleActions() {

		final InlineLabel selectLabel = new InlineLabel("Select:");
		selectLabel.setStylePrimaryName("label");
		selectLabel.addStyleName("item");

		selectListBox.addItem("---");
		selectListBox.addItem("None");
		selectListBox.addItem("Private");
		selectListBox.addItem("Public");
		selectListBox.addItem("All");
		selectListBox.addChangeListener(new ChangeListener() {
			public void onChange(Widget sender) {
				if (selectListBox.getSelectedIndex() == 1) {
					//None
					list.getDataTable().deselectAllRows();
				} else if (selectListBox.getSelectedIndex() == 2) {
					//Private
					for (int i = 0; i < list.getDataTable().getRowCount(); i++)
						if (!list.getRowValue(i).isPublicData())
							list.getDataTable().selectRow(i, false);
						else
							list.getDataTable().deselectRow(i);
				} else if (selectListBox.getSelectedIndex() == 3) {
					//Public
					for (int i = 0; i < list.getDataTable().getRowCount(); i++)
						if (list.getRowValue(i).isPublicData())
							list.getDataTable().selectRow(i, false);
						else
							list.getDataTable().deselectRow(i);
				} else if (selectListBox.getSelectedIndex() == 4) {
					//All
					list.getDataTable().selectAllRows();
				}
			}
		});

		final InlineLabel adProjectLabel = new InlineLabel("Add to:");
		adProjectLabel.setStylePrimaryName("item");
		adProjectLabel.addStyleName("label");

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

		final InlineLabel exportLabel = new InlineLabel("Export:");
		exportLabel.setStylePrimaryName("item");
		exportLabel.addStyleName("label");

		exportExcel = new MLink(LocaleHandler.lc_text.buttonExportExcel(),
				new ClickListener() {
					public void onClick(Widget sender) {
						doExportExcel();
					}
				});
		exportExcel.addStyleName("subitem");

		exportGoogleEarth = new MLink(LocaleHandler.lc_text.buttonExportKML(),
				new ClickListener() {
					public void onClick(Widget sender) {
						doExportGoogleEarth();
					}
				});
		exportGoogleEarth.addStyleName("subitem");
		
		viewGoogleEarth = new MLink(LocaleHandler.lc_text.search_viewGoogleEarth(),
				new ClickListener() {
					public void onClick(Widget sender) {
						doViewGoogleEarth();
					}
				});
		viewGoogleEarth.addStyleName("subitem");

		final MLink makePublic = new MLink("Make Public", new ClickListener() {
			public void onClick(Widget sender) {
				final List<Sample> CheckedSamples = getCheckedSamples();
				if (CheckedSamples.size() > 0){
					UserSamplesList.this.MakePublicSelected(CheckedSamples);
				} else {
					errMsg.setText(LocaleHandler.lc_text
							.message_ChooseSamples());
					UserSamplesList.this.insert(errMsg, 0);
				}
			}
		});
		makePublic.addStyleName("item");

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
		remove.addStyleName("item");

		tableActions.add(selectLabel);
		tableActions.add(selectListBox);
		if (!projectsList.isEmpty()) {
			tableActions.add(adProjectLabel);
			tableActions.add(projectListBox);
		}
		tableActions.add(viewGoogleEarth);
		tableActions.add(exportLabel);
		tableActions.add(exportExcel);
		tableActions.add(exportGoogleEarth);
		tableActions.add(makePublic);
		tableActions.add(remove);
		tableActions.setStylePrimaryName("scrolltable-actions");

	}

	private void buildSampleFilters() {

		customCols = new MLink("Custom", new ClickListener() {
			public void onClick(Widget sender) {
				//CustomTableView<Sample> myView = new CustomTableView<Sample>(list, cookieString) {
					
				//};
			}
		});

		final Label columnsLabel = new Label("Columns:");

		columnViewPanel.add(columnsLabel);
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
//		new ServerOp() {
//			@Override
//			public void begin() {
//				MpDb.project_svc.all(MpDb.currentUser().getId(), this);
//			}
//			public void onSuccess(Object result) {
//				projectsList = new HashSet<Project>((List<Project>) result);
//				addPageHeader();
//				projectSamples(projectId);
//				addSamples();
//			}
//		}.begin();
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
		MakePublicDialog m = new MakePublicDialog(new ArrayList(CheckedSamples));
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
		final ArrayList<String> cookiedColumns;
		if (Cookies.getCookie(cookieString) != null) {
			cookiedColumns = new ArrayList<String>(Arrays.asList(Cookies
					.getCookie(cookieString).split(",")));
			for (int i=0; i<cookiedColumns.size(); i++) {
				boolean visible = Integer.parseInt(cookiedColumns.get(i)) > 0;
				list.getTableDefinition().setColumnVisible(list.getTableDefinition().getColumnDefinition(i), visible);
			}
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
