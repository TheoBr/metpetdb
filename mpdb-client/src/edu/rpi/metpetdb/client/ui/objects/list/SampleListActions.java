package edu.rpi.metpetdb.client.ui.objects.list;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Button;
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
import edu.rpi.metpetdb.client.ui.MpDb;
import edu.rpi.metpetdb.client.ui.commands.ServerOp;
import edu.rpi.metpetdb.client.ui.commands.VoidServerOp;
import edu.rpi.metpetdb.client.ui.dialogs.ConfirmationDialogBox;
import edu.rpi.metpetdb.client.ui.dialogs.MDialogBox;
import edu.rpi.metpetdb.client.ui.dialogs.MakePublicDialog;
import edu.rpi.metpetdb.client.ui.excel.ExcelUtil;
import edu.rpi.metpetdb.client.ui.widgets.MCheckBox;
import edu.rpi.metpetdb.client.ui.widgets.MGoogleEarthPopUp;
import edu.rpi.metpetdb.client.ui.widgets.MLink;
import edu.rpi.metpetdb.client.ui.widgets.paging.DataList;

public class SampleListActions extends FlowPanel implements ClickListener {

	private static final String samplesParameter = "Samples";
	private static final String urlParameter = "url";
	private final DataList<Sample> list;
	/** maps a project id to the actual project */
	private final Map<Integer, Project> projects;
	private MGoogleEarthPopUp earthPopup = new MGoogleEarthPopUp();

	private enum SelectOption {

		EMPTY("----") {
			public void doAction(final DataList<Sample> list) {
			// do nothing for empty
			}
		},
		NONE("None") {
			public void doAction(final DataList<Sample> list) {
				list.getDataTable().deselectAllRows();
			}
		},
		PRIVATE("Private") {
			public void doAction(final DataList<Sample> list) {
				for (int i = 0; i < list.getDataTable().getRowCount(); i++)
					if (!list.getRowValue(i).isPublicData())
						list.getDataTable().selectRow(i, false);
					else
						list.getDataTable().deselectRow(i);
			}
		},
		PUBLIC("Public") {
			public void doAction(final DataList<Sample> list) {
				for (int i = 0; i < list.getDataTable().getRowCount(); i++)
					if (list.getRowValue(i).isPublicData())
						list.getDataTable().selectRow(i, false);
					else
						list.getDataTable().deselectRow(i);
			}
		},
		ALL("All on this page") {
			public void doAction(final DataList<Sample> list) {
				list.getDataTable().selectAllRows();
			}
		};

		final String display;

		public abstract void doAction(final DataList<Sample> list);

		SelectOption(final String display) {
			this.display = display;
		}

		public String getDisplay() {
			return display;
		}

		public static SelectOption get(final String display) {
			for (SelectOption so : SelectOption.values()) {
				if (so.getDisplay().equals(display))
					return so;
			}
			return SelectOption.EMPTY;
		}
	}

	private void setupSelect(final DataList<Sample> list) {
		final InlineLabel selectLabel = new InlineLabel("Select:");
		selectLabel.setStylePrimaryName("label");
		selectLabel.addStyleName("item");
		final ListBox selectListBox = new ListBox();
		for (SelectOption so : SelectOption.values()) {
			selectListBox.addItem(so.getDisplay());
		}
		selectListBox.addChangeListener(new ChangeListener() {
			public void onChange(Widget sender) {
				final SelectOption opt = SelectOption.get(selectListBox
						.getItemText(selectListBox.getSelectedIndex()));
				opt.doAction(list);
				selectListBox.setItemSelected(0, true);
			}
		});

		add(selectLabel);
		add(selectListBox);
	}

	private void setupProjecks() {
		if (MpDb.currentUser() != null) {
			new ServerOp<List<Project>>() {
				@Override
				public void begin() {
					MpDb.project_svc.all(MpDb.currentUser().getId(), this);
				}
				public void onSuccess(List<Project> projectsList) {
					final ListBox projectListBox = new ListBox();
					final InlineLabel adProjectLabel = new InlineLabel(
							"Add to:");
					adProjectLabel.setStylePrimaryName("item");
					adProjectLabel.addStyleName("label");

					projectListBox.addItem("Choose Project...");
					Iterator<Project> it = projectsList.iterator();
					while (it.hasNext()) {
						final Project project = it.next();
						projectListBox.addItem(project.getName(), String
								.valueOf(project.getId()));
						projects.put(project.getId(), project);
					}
					projectListBox.addChangeListener(new ChangeListener() {
						public void onChange(Widget sender) {
							if (projectListBox.getSelectedIndex() > 0) {
								final List<Sample> checkedSamples = list
										.getSelectedValues();
								if (checkedSamples.size() > 0) {
									new ServerOp<Boolean>() {
										public void begin() {
											new ConfirmationDialogBox(
													LocaleHandler.lc_text
															.confirmation_AddToProject(),
													true, this);
										}

										public void onSuccess(
												final Boolean result) {
											if (result) {
												final int projectId = Integer
														.parseInt(projectListBox
																.getValue(projectListBox
																		.getSelectedIndex()));
												final Project proj = projects
														.get(projectId);
												proj
														.setSamples(new HashSet<Sample>(
																checkedSamples));
												new ServerOp<Project>() {
													public void begin() {
														MpDb.project_svc
																.saveProject(
																		proj,
																		this);
													}

													public void onSuccess(
															Project result) {
														projects.put(projectId,
																result);
													}

												}.begin();

											}
										}
									}.begin();
								}
							}
						}
					});
					if (!projectsList.isEmpty()) {
						add(adProjectLabel);
						add(projectListBox);
					}
				}
			}.begin();
		}
	}

	private final MLink exportExcel;
	private final MLink exportGoogleEarth;
	private final MLink viewGoogleEarth;
	private final MLink makePublic;
	private final MLink remove;

	public SampleListActions(final DataList<Sample> list) {
		projects = new HashMap<Integer, Project>();
		this.list = list;
		setupSelect(list);
		setupProjecks();

		final InlineLabel exportLabel = new InlineLabel("Export:");
		exportLabel.setStylePrimaryName("item");
		exportLabel.addStyleName("label");

		exportExcel = new MLink(LocaleHandler.lc_text.buttonExportExcel(), this);
		exportExcel.addStyleName("subitem");
		exportGoogleEarth = new MLink(LocaleHandler.lc_text.buttonExportKML(),
				this);
		exportGoogleEarth.addStyleName("subitem");
		viewGoogleEarth = new MLink(LocaleHandler.lc_text
				.search_viewGoogleEarth(), this);
		viewGoogleEarth.addStyleName("subitem");

		makePublic = new MLink("Make Public", this);
		makePublic.addStyleName("item");

		remove = new MLink("Remove", this);
		remove.addStyleName("item");

		add(viewGoogleEarth);
		add(exportLabel);
		add(exportExcel);
		add(exportGoogleEarth);
		add(makePublic);
		add(remove);
		setStylePrimaryName("scrolltable-actions");
	}

	private void doViewGoogleEarth() {
		new ServerOp<List<Sample>>() {
			@Override
			public void begin() {
				List<Sample> checked = list.getSelectedValues();
				if (checked.size() == 0)
					onSuccess(list.getAllValuesForPage());
				else {
					onSuccess(checked);
				}
			}
			public void onSuccess(List<Sample> result) {
				if (result != null && result.size() > 0) {
					earthPopup.createUI(result);
					earthPopup.show();
				} else {
					new ConfirmationDialogBox("There are no samples to view",false,null);
				}
			}
		}.begin();
	}

	private void doExportExcel() {
		final FormPanel fp = new FormPanel();
		fp.setMethod(FormPanel.METHOD_GET);
		fp.setEncoding(FormPanel.ENCODING_URLENCODED);
		final HorizontalPanel hp = new HorizontalPanel();

		new ServerOp<List<Sample>>() {
			@Override
			public void begin() {
				List<Sample> checked = list.getSelectedValues();
				if (checked.size() == 0)
					onSuccess(list.getAllValuesForPage());
				else {
					onSuccess(checked);
				}
			}
			public void onSuccess(List<Sample> result) {
				if (result != null && result.size() > 0) {
					for (String columnHeader : ExcelUtil.columnHeaders) {
						hp.add(new Hidden(ExcelUtil.columnHeaderParameter,
								columnHeader));
					}
					for (int i = 0; i < result.size(); i++) {
						Hidden sample = new Hidden(samplesParameter, String
								.valueOf(result.get(i).getId()));
						hp.add(sample);
					}
					Hidden url = new Hidden(urlParameter, GWT.getModuleBaseURL()
							+ "#"
							+ LocaleHandler.lc_entity.TokenSpace_Sample_Details()
							+ LocaleHandler.lc_text.tokenSeparater());
					hp.add(url);
					fp.add(hp);
					fp.setAction(GWT.getModuleBaseURL() + "excel.svc?");
					fp.setVisible(false);
					add(fp);
					fp.submit();
				} else {
					new ConfirmationDialogBox("There are no samples to export",false,null);
				}
			}
		}.begin();
	}

	private void doExportGoogleEarth() {
		new ServerOp<List<Sample>>() {
			@Override
			public void begin() {
				List<Sample> checked = list.getSelectedValues();
				if (checked.size() == 0)
					onSuccess(list.getAllValuesForPage());
				else {
					onSuccess(checked);
				}
			}
			public void onSuccess(List<Sample> result) {
				if (result != null && result.size() > 0) {
					final FormPanel fp = new FormPanel();
					fp.setMethod(FormPanel.METHOD_GET);
					fp.setEncoding(FormPanel.ENCODING_URLENCODED);
					final HorizontalPanel hp = new HorizontalPanel();
					for (Sample s : result) {
						Hidden sample = new Hidden(samplesParameter, String
								.valueOf(s.getId()));
						hp.add(sample);
					}
					Hidden url = new Hidden(urlParameter, GWT.getModuleBaseURL()
							+ "#"
							+ LocaleHandler.lc_entity.TokenSpace_Sample_Details()
							+ LocaleHandler.lc_text.tokenSeparater());
					hp.add(url);
					fp.add(hp);
					fp.setAction(GWT.getModuleBaseURL() + "BasicKML.kml?");
					fp.setVisible(false);
					add(fp);
					fp.submit();
				} else {
					new ConfirmationDialogBox("There are no samples to export",false,null);
				}
			}
		}.begin();
	}

	private void deleteSelected(final List<Sample> CheckedSamples) {
		new VoidServerOp() {
			@Override
			public void begin() {			
				Iterator<Sample> itr = CheckedSamples.iterator();
				final ArrayList<Long> ids = new ArrayList<Long>();
				while (itr.hasNext()) {
					ids.add(itr.next().getId());
				}
				MpDb.sample_svc.deleteAll(ids, this);

			}
			public void onSuccess() {
				list.getScrollTable().reloadPage();
			}
		}.begin();
	}
	
	private void noSamplesSelected(){
		final MDialogBox noSamplesBox = new MDialogBox();
		final FlowPanel container = new FlowPanel();
		container.add(new Label("No Samples selected"));
		Button ok = new Button("Ok");
		ok.addClickListener(new ClickListener(){
			public void onClick(final Widget sender){
				noSamplesBox.hide();
			}
		});
		container.add(ok);
		noSamplesBox.setWidget(container);
		noSamplesBox.show();
	}
	
	private boolean checkDeletePermissions(List<Sample> checkedSamples){
		//check to see if any of the samples are not owned by the current user
		Iterator<Sample> itr = checkedSamples.iterator();
		while(itr.hasNext()){
			Sample current = itr.next();
			if(!MpDb.isCurrentUser(current.getOwner()))
				return true;
		}
		return false;
	}
	
	private void noPermissionToDelete(){
		final MDialogBox box = new MDialogBox();
		final FlowPanel container = new FlowPanel();
		container.add(new Label("You do not haver permission to delete one or more of these samples"));
		Button ok = new Button("Ok");
		ok.addClickListener(new ClickListener(){
			public void onClick(final Widget sender){
				box.hide();
			}
		});
		container.add(ok);
		box.setWidget(container);
		box.show();
	}

	public void onClick(Widget sender) {
		if (sender == viewGoogleEarth) {
			doViewGoogleEarth();
		} else if (sender == exportExcel) {
			doExportExcel();
		} else if (sender == exportGoogleEarth) {
			doExportGoogleEarth();
		} else if (sender == makePublic) {
			MakePublicDialog m = new MakePublicDialog(list.getSelectedValues(), list, true, null);
			m.show();
		} else if (sender == remove) {
			final List<Sample> checkedSamples = list.getSelectedValues();
			if(checkedSamples.size() == 0){
				noSamplesSelected();
			} else if(checkDeletePermissions(checkedSamples)) {
				noPermissionToDelete();
			} else {
				new ServerOp<Boolean>() {
					public void begin() {
						new ConfirmationDialogBox(LocaleHandler.lc_text
								.confirmation_Delete(), true, this);
					}
	
					public void onSuccess(final Boolean result) {
						if (result)
							deleteSelected(checkedSamples);
					}
				}.begin();
			}
		}
	}
}
