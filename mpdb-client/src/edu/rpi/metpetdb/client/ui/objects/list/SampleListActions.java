package edu.rpi.metpetdb.client.ui.objects.list;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
		NONE_PAGE("None on this page") {
			public void doAction(final DataList<Sample> list) {
				list.deselectAllPageRows();
			}
		},
		NONE_TABLE("None") {
			public void doAction(final DataList<Sample> list) {
				list.deselectAllRows();
			}
		},
		PRIVATE("Private") {
			public void doAction(final DataList<Sample> list) {
				list.selectAllRows(false);
			}
		},
		PUBLIC("Public") {
			public void doAction(final DataList<Sample> list) {
				list.selectAllRows(true);
			}
		},
		ALL_PAGE("All on this page") {
			public void doAction(final DataList<Sample> list) {
				list.selectAllPageRows();
			}
		},
		ALL_TABLE("All") {
			public void doAction(final DataList<Sample> list) {
				list.selectAllRows();
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
								final Set<Object> checkedSampleIds =  list
										.getSelectedValues();
								final List<Long> checkedSamples = new ArrayList<Long>();
								for (Object id : checkedSampleIds){
									checkedSamples.add((Long) id);
								}
								if (checkedSampleIds.size() > 0) {
									//confirm that the user would like to add these samples to the project
									new ConfirmationDialogBox(
										LocaleHandler.lc_text.confirmation_AddToProject(),
											true) {
										public void onSubmit() {
											final int projectId = Integer.parseInt(projectListBox
												.getValue(projectListBox
												.getSelectedIndex()));
											final Project proj = projects
												.get(projectId);
											//Add selected samples to the project selected
											new ServerOp<Project>() {
												public void begin() {
													MpDb.project_svc.addSamples(projectId, checkedSamples, this);
												}
												public void onSuccess(final Project result) {
													projects.put(projectId,
															result);
													//reset the combo box by setting the index to 0
													projectListBox.setSelectedIndex(0);
													list.deselectAllRows();
												}
											}.begin();
										}
									}.show(); 
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
	private final MLink removeFromProject = new MLink("Remove from project", this);;
	
	public SampleListActions(final DataList<Sample> list, boolean isForProject){
		this(list);
		if(isForProject){
			//Get rid of the remove sample link
			remove(remove);
			
			//Put in a remove from project link
			removeFromProject.addStyleName("item");
			add(removeFromProject);
		}
	}

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
				final ArrayList<Long> checkedSampleIds = new ArrayList<Long>();
				for (Object id : list.getSelectedValues()){
					checkedSampleIds.add((Long) id);
				}
				MpDb.sample_svc.details(checkedSampleIds, this);
			}
			public void onSuccess(List<Sample> result) {
				if (result != null && result.size() > 0) {
					earthPopup.createUI(result);
					earthPopup.show();
				} else {
					new ConfirmationDialogBox("There are no samples to view",false).show();
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
				final ArrayList<Long> checkedSampleIds = new ArrayList<Long>();
				for (Object id : list.getSelectedValues()){
					checkedSampleIds.add((Long) id);
				}
				MpDb.sample_svc.details(checkedSampleIds, this);
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
					Hidden url = new Hidden(urlParameter,  "#"
							+ LocaleHandler.lc_entity.TokenSpace_Sample_Details()
							+ LocaleHandler.lc_text.tokenSeparater());
					hp.add(url);
					fp.add(hp);
					fp.setAction("excel.svc?");
					fp.setVisible(false);
					add(fp);
					fp.submit();
				} else {
					new ConfirmationDialogBox("There are no samples to export",false).show();
				}
			}
		}.begin();
	}

	private void doExportGoogleEarth() {
		new ServerOp<List<Sample>>() {
			@Override
			public void begin() {
				final ArrayList<Long> checkedSampleIds = new ArrayList<Long>();
				for (Object id : list.getSelectedValues()){
					checkedSampleIds.add((Long) id);
				}
				MpDb.sample_svc.details(checkedSampleIds, this);
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
					Hidden url = new Hidden(urlParameter,  "#"
							+ LocaleHandler.lc_entity.TokenSpace_Sample_Details()
							+ LocaleHandler.lc_text.tokenSeparater());
					hp.add(url);
					fp.add(hp);
					fp.setAction("./BasicKML.kml?");
					fp.setVisible(false);
					add(fp);
					fp.submit();
				} else {
					new ConfirmationDialogBox("There are no samples to export",false).show();
				}
			}
		}.begin();
	}

	private void deleteSelected(final List<Long> CheckedSamples) {
		new VoidServerOp() {
			@Override
			public void begin() {			
				MpDb.sample_svc.deleteAll(CheckedSamples, this);

			}
			public void onSuccess() {
				list.getScrollTable().reloadPage();
			}
		}.begin();
	}
	
	private void removeFromProject(final List<Long> checkedSamples, final long projectId){
		new VoidServerOp() {
			public void begin() {
				MpDb.project_svc.removeFromProject(checkedSamples, projectId, this);
			}
			public void onSuccess(){
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
		container.add(new Label("You do not have permission to delete one or more of these samples"));
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
			new ServerOp<List<Sample>>() {
				public void begin(){
					List<Long> ids = new ArrayList<Long>();
					for (Object o :  list.getSelectedValues()){
						ids.add((Long) o);
					}
					MpDb.sample_svc.details(ids, this);
				}

				public void onSuccess(List<Sample> result) {
					MakePublicDialog m = new MakePublicDialog((ArrayList) result, list, true, null);
					m.show();	
				};
			}.begin();
			
		} else if (sender == remove) {
			final ArrayList<Long> checkedSampleIds = new ArrayList<Long>();
			for (Object id : list.getSelectedValues()){
				checkedSampleIds.add((Long) id);
			}
			if(checkedSampleIds.size() == 0){
				noSamplesSelected();
			} else {
				new ServerOp<List<Sample>>() {
					public void begin(){
						List<Long> ids = new ArrayList<Long>();
						for (Object o :  list.getSelectedValues()){
							ids.add((Long) o);
						}
						MpDb.sample_svc.details(ids, this);
					}

					public void onSuccess(List<Sample> result2) {
						if(checkDeletePermissions(result2)){
							noPermissionToDelete();
						} else {
							new ConfirmationDialogBox(LocaleHandler.lc_text.confirmation_Delete(), true) {
								public void onCancel() {}

								public void onSubmit() {
									deleteSelected(checkedSampleIds);
								}		
							}.show();
				
						}	
					};
				}.begin();
			}
		} else if (sender == removeFromProject){
			final long projectId = ((ProjectSampleList)list).getProjectId();
			final ArrayList<Long> checkedSampleIds = new ArrayList<Long>();
			for (Object id : list.getSelectedValues()){
				checkedSampleIds.add((Long) id);
			}
			if(checkedSampleIds.size() == 0){
				noSamplesSelected();
			} else {
				new ServerOp<List<Sample>>(){
					public void begin(){
						List<Long> ids = new ArrayList<Long>();
						for (Object o :  list.getSelectedValues()){
							ids.add((Long) o);
						}
						MpDb.sample_svc.details(ids, this);
					}
					
					public void onSuccess(List<Sample> result){
								new ConfirmationDialogBox(LocaleHandler.lc_text.confirmation_Remove_From_Project(), true) {
									public void onSubmit() {
										removeFromProject(checkedSampleIds, projectId);
									}
							}.show();
					}
					
				}.begin();	
			}
			
		}
	}
}
