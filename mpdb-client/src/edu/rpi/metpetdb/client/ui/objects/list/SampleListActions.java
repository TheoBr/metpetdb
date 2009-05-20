package edu.rpi.metpetdb.client.ui.objects.list;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.Hidden;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.locale.LocaleHandler;
import edu.rpi.metpetdb.client.model.Project;
import edu.rpi.metpetdb.client.model.Sample;
import edu.rpi.metpetdb.client.ui.MpDb;
import edu.rpi.metpetdb.client.ui.commands.ServerOp;
import edu.rpi.metpetdb.client.ui.commands.VoidServerOp;
import edu.rpi.metpetdb.client.ui.dialogs.ConfirmationDialogBox;
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
		ALL("All") {
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
				int id = (int) (MpDb.currentUser().getId());
				List<Sample> checked = list.getSelectedValues();
				if (checked.size() == 0)
					MpDb.sample_svc.allSamplesForUser(id, this);
				else {
					onSuccess(checked);
				}
			}
			public void onSuccess(List<Sample> result) {
				earthPopup.createUI(result);
				earthPopup.show();
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
				long id = (long) (MpDb.currentUser().getId());
				List<Sample> checked = list.getSelectedValues();
				if (checked.size() == 0)
					MpDb.sample_svc.allSamplesForUser(id, this);
				else {
					onSuccess(checked);
				}
			}
			public void onSuccess(List<Sample> result) {
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
			}
		}.begin();
	}

	private void doExportGoogleEarth() {
		new ServerOp<List<Sample>>() {
			@Override
			public void begin() {
				int id = (int) (MpDb.currentUser().getId());
				List<Sample> checked = list.getSelectedValues();
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
			}
		}.begin();
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
				list.getScrollTable().reloadPage();
			}
		}.begin();
	}

	public void onClick(Widget sender) {
		if (sender == viewGoogleEarth) {
			doViewGoogleEarth();
		} else if (sender == exportExcel) {
			doExportExcel();
		} else if (sender == exportGoogleEarth) {
			doExportGoogleEarth();
		} else if (sender == makePublic) {
			MakePublicDialog m = new MakePublicDialog(list.getSelectedValues());
			m.show();
		} else if (sender == remove) {
			final List<Sample> checkedSamples = list.getSelectedValues();
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
