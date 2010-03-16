package edu.rpi.metpetdb.client.ui.objects.list;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.locale.LocaleHandler;
import edu.rpi.metpetdb.client.model.ChemicalAnalysis;
import edu.rpi.metpetdb.client.model.Sample;
import edu.rpi.metpetdb.client.model.Subsample;
import edu.rpi.metpetdb.client.ui.MpDb;
import edu.rpi.metpetdb.client.ui.commands.ServerOp;
import edu.rpi.metpetdb.client.ui.commands.VoidServerOp;
import edu.rpi.metpetdb.client.ui.dialogs.ConfirmationDialogBox;
import edu.rpi.metpetdb.client.ui.dialogs.MDialogBox;
import edu.rpi.metpetdb.client.ui.dialogs.MakePublicDialog;
import edu.rpi.metpetdb.client.ui.widgets.MLink;
import edu.rpi.metpetdb.client.ui.widgets.paging.DataList;

public class ChemicalAnalysisListActions extends FlowPanel implements ClickListener {

	private final DataList<ChemicalAnalysis> list;

	private enum SelectOption {

		EMPTY("----") {
			public void doAction(final DataList<ChemicalAnalysis> list) {
			// do nothing for empty
			}
		},
		NONE("None") {
			public void doAction(final DataList<ChemicalAnalysis> list) {
				list.getDataTable().deselectAllRows();
			}
		},
		PRIVATE("Private") {
			public void doAction(final DataList<ChemicalAnalysis> list) {
				list.selectAllRows(false);
			}
		},
		PUBLIC("Public") {
			public void doAction(final DataList<ChemicalAnalysis> list) {
				list.selectAllRows(true);
			}
		},
		ALL("All on this page") {
			public void doAction(final DataList<ChemicalAnalysis> list) {
				list.selectAllPageRows();
			}
		},
		ALL_TABLE("All") {
			public void doAction(final DataList<ChemicalAnalysis> list) {
				list.selectAllRows();
			}	
		};;

		final String display;

		public abstract void doAction(final DataList<ChemicalAnalysis> list);

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

	private void setupSelect(final DataList<ChemicalAnalysis> list) {
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

	private final MLink remove;
	
	public ChemicalAnalysisListActions(final DataList<ChemicalAnalysis> list) {
		this.list = list;
		setupSelect(list);

		remove = new MLink("Remove", this);
		remove.addStyleName("item");
		
		add(remove);
		setStylePrimaryName("scrolltable-actions");
	}

	private void deleteSelected(final List<Integer> CheckedSubsamples) {
		new VoidServerOp() {
			@Override
			public void begin() {			
				MpDb.chemicalAnalysis_svc.deleteAll(CheckedSubsamples, this);

			}
			public void onSuccess() {
				list.getScrollTable().reloadPage();
			}
		}.begin();
	}
	
	private boolean checkDeletePermissions(List<ChemicalAnalysis> checkedAnalyses){
		//check to see if any of the samples are not owned by the current user
		Iterator<ChemicalAnalysis> itr = checkedAnalyses.iterator();
		while(itr.hasNext()){
			ChemicalAnalysis current = itr.next();
			if(!MpDb.isCurrentUser(current.getSubsample().getOwner()))
				return true;
		}
		return false;
	}
	
	private void noPermissionToDelete(){
		final MDialogBox box = new MDialogBox();
		final FlowPanel container = new FlowPanel();
		container.add(new Label("You do not have permission to delete one or more of these analyses"));
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
		if (sender == remove) {
			final ArrayList<Integer> checkedAnalysesIds = new ArrayList<Integer>();
			for (Object id : list.getSelectedValues()){
				checkedAnalysesIds.add((Integer) id);
			}
			if (checkedAnalysesIds.size() == 0){
				noAnalysesSelected();
			} else {
				new ServerOp<List<ChemicalAnalysis>>() {
					public void begin(){
						MpDb.chemicalAnalysis_svc.details(checkedAnalysesIds, this);
					}

					public void onSuccess(List<ChemicalAnalysis> result2) {
						if(checkDeletePermissions(result2)) {
							noPermissionToDelete();
						} else {
							new ServerOp<Boolean>() {
								public void begin() {
									new ConfirmationDialogBox(LocaleHandler.lc_text
											.confirmation_Delete_Analysis(), true, this);
								}
				
								public void onSuccess(final Boolean result) {
									if (result)
										deleteSelected(checkedAnalysesIds);
								}
							}.begin();
						}
					}
				}.begin();
			}
		}
	}
	
	private void noAnalysesSelected(){
		final MDialogBox noAnalysesBox = new MDialogBox();
		final FlowPanel container = new FlowPanel();
		container.add(new Label("No analyses selected"));
		Button ok = new Button("Ok");
		ok.addClickListener(new ClickListener(){
			public void onClick(final Widget sender){
				noAnalysesBox.hide();
			}
		});
		container.add(ok);
		noAnalysesBox.setWidget(container);
		noAnalysesBox.show();
	}
}