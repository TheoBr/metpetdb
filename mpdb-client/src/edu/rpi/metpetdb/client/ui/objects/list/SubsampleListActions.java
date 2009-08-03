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
import edu.rpi.metpetdb.client.model.Subsample;
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

public class SubsampleListActions extends FlowPanel implements ClickListener {

	private final DataList<Subsample> list;

	private enum SelectOption {


		EMPTY("----") {
			public void doAction(final DataList<Subsample> list) {
			// do nothing for empty
			}
		},
		NONE_PAGE("None on this page") {
			public void doAction(final DataList<Subsample> list) {
				list.deselectAllPageRows();
			}
		},
		NONE_TABLE("None") {
			public void doAction(final DataList<Subsample> list) {
				list.deselectAllRows();
			}
		},
		PRIVATE("Private") {
			public void doAction(final DataList<Subsample> list) {
				list.selectAllRows(false);
			}
		},
		PUBLIC("Public") {
			public void doAction(final DataList<Subsample> list) {
				list.selectAllRows(true);
			}
		},
		ALL_PAGE("All on this page") {
			public void doAction(final DataList<Subsample> list) {
				list.selectAllPageRows();
			}
		},
		ALL_TABLE("All") {
			public void doAction(final DataList<Subsample> list) {
				list.selectAllRows();
			}	
		};


		final String display;

		public abstract void doAction(final DataList<Subsample> list);

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

	private void setupSelect(final DataList<Subsample> list) {
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

	private final MLink makePublic;
	private final MLink remove;
	
	public SubsampleListActions(final DataList<Subsample> list) {
		this.list = list;
		setupSelect(list);

		makePublic = new MLink("Make Public", this);
		makePublic.addStyleName("item");

		remove = new MLink("Remove", this);
		remove.addStyleName("item");
		
		add(makePublic);
		add(remove);
		setStylePrimaryName("scrolltable-actions");
	}

	private void deleteSelected(final List<Long> CheckedSubsamples) {
		new VoidServerOp() {
			@Override
			public void begin() {			
				MpDb.subsample_svc.deleteAll(CheckedSubsamples, this);

			}
			public void onSuccess() {
				list.getScrollTable().reloadPage();
			}
		}.begin();
	}
	
	private boolean checkDeletePermissions(List<Subsample> checkedSubsamples){
		//check to see if any of the subsamples are not owned by the current user
		Iterator<Subsample> itr = checkedSubsamples.iterator();
		while(itr.hasNext()){
			Subsample current = itr.next();
			if(!MpDb.isCurrentUser(current.getOwner()))
				return true;
		}
		return false;
	}
	
	private void noPermissionToDelete(){
		final MDialogBox box = new MDialogBox();
		final FlowPanel container = new FlowPanel();
		container.add(new Label("You do not haver permission to delete one or more of these subsamples"));
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
		if (sender == makePublic) {
			if(list.getSelectedValues().size() == 0){
				noSubsamplesSelected();
			}
			if(list.getSelectedValues().size() > 0){
				new ServerOp<Sample>(){
					public void begin() {
						MpDb.sample_svc.details((Long)list.getRowValue(0).getSampleId(), this);
					}
					public void onSuccess(final Sample result){
						if(!result.isPublicData()){
							sampleNotPublic();
						} else{
							new ServerOp<List<Subsample>>() {
								public void begin(){
									List<Long> ids = new ArrayList<Long>();
									for (Object o :  list.getSelectedValues()){
										ids.add((Long) o);
									}
									MpDb.subsample_svc.details(ids, this);
								}

								public void onSuccess(List<Subsample> result2) {
									MakePublicDialog m = new MakePublicDialog((ArrayList) result2, list, false, result);
									m.show();	
								};
							}.begin();
						}
					}	
				}.begin();
			}
		} else if (sender == remove) {
			final ArrayList<Long> checkedSubsampleIds = new ArrayList<Long>();
			for (Object id : list.getSelectedValues()){
				checkedSubsampleIds.add((Long) id);
			}
			if (checkedSubsampleIds.size() == 0){
				noSubsamplesSelected();
			} else {
				new ServerOp<List<Subsample>>() {
					public void begin(){
						List<Long> ids = new ArrayList<Long>();
						for (Object o :  list.getSelectedValues()){
							ids.add((Long) o);
						}
						MpDb.subsample_svc.details(ids, this);
					}

					public void onSuccess(List<Subsample> result2) {
						if(checkDeletePermissions(result2)){
							noPermissionToDelete();
						} else {
							new ServerOp<Boolean>() {
								public void begin() {
									new ConfirmationDialogBox(LocaleHandler.lc_text
											.confirmation_Delete_Subsample(), true, this);
								}
				
								public void onSuccess(final Boolean result) {
									if (result)
										deleteSelected(checkedSubsampleIds);
								}
							}.begin();
						}	
					};
				}.begin();
			}
		}
	}
	
	private void noSubsamplesSelected(){
		final MDialogBox noSubsamplesBox = new MDialogBox();
		final FlowPanel container = new FlowPanel();
		container.add(new Label("No Subsamples selected"));
		Button ok = new Button("Ok");
		ok.addClickListener(new ClickListener(){
			public void onClick(final Widget sender){
				noSubsamplesBox.hide();
			}
		});
		container.add(ok);
		noSubsamplesBox.setWidget(container);
		noSubsamplesBox.show();
	}
	
	private void sampleNotPublic(){
		final MDialogBox notPublicBox = new MDialogBox();
		final FlowPanel container = new FlowPanel();
		container.add(new Label("This sample is not public."));
		container.add(new Label("Subsamples cannot be made public unless they belong to a public sample."));
		Button ok = new Button("Ok");
		ok.addClickListener(new ClickListener(){
			public void onClick(final Widget sender){
				notPublicBox.hide();
			}
		});
		container.add(ok);
		notPublicBox.setWidget(container);
		notPublicBox.show();
	}
}
