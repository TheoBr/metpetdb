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
		NONE("None") {
			public void doAction(final DataList<Subsample> list) {
				list.getDataTable().deselectAllRows();
			}
		},
		PRIVATE("Private") {
			public void doAction(final DataList<Subsample> list) {
				for (int i = 0; i < list.getDataTable().getRowCount(); i++)
					if (!list.getRowValue(i).isPublicData())
						list.getDataTable().selectRow(i, false);
					else
						list.getDataTable().deselectRow(i);
			}
		},
		PUBLIC("Public") {
			public void doAction(final DataList<Subsample> list) {
				for (int i = 0; i < list.getDataTable().getRowCount(); i++)
					if (list.getRowValue(i).isPublicData())
						list.getDataTable().selectRow(i, false);
					else
						list.getDataTable().deselectRow(i);
			}
		},
		ALL("All on this page") {
			public void doAction(final DataList<Subsample> list) {
				list.getDataTable().selectAllRows();
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

	private void deleteSelected(final List<Subsample> CheckedSubsamples) {
		new VoidServerOp() {
			@Override
			public void begin() {			
				Iterator<Subsample> itr = CheckedSubsamples.iterator();
				final ArrayList<Long> ids = new ArrayList<Long>();
				while (itr.hasNext()) {
					ids.add(itr.next().getId());
				}
				
				MpDb.subsample_svc.deleteAll(ids, this);

			}
			public void onSuccess() {
				list.getScrollTable().reloadPage();
			}
		}.begin();
	}

	public void onClick(Widget sender) {
		if (sender == makePublic) {
			if(list.getSelectedValues().size() == 0){
				noSubsamplesSelected();
			}
			if(list.getSelectedValues().size() > 0){
				new ServerOp<Sample>(){
					public void begin() {
						MpDb.sample_svc.details(list.getSelectedValues().get(0).getSampleId(), this);
					}
					public void onSuccess(final Sample result){
						if(!result.isPublicData()){
							sampleNotPublic();
						} else{
							MakePublicDialog m = new MakePublicDialog(list.getSelectedValues(), 
								list, false, result);
							m.show();
						}
					}	
				}.begin();
			}
		} else if (sender == remove) {
			final List<Subsample> checkedSubsamples = list.getSelectedValues();
			new ServerOp<Boolean>() {
				public void begin() {
					new ConfirmationDialogBox(LocaleHandler.lc_text
							.confirmation_Delete(), true, this);
				}

				public void onSuccess(final Boolean result) {
					if (result)
						deleteSelected(checkedSubsamples);
				}
			}.begin();
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
