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
import edu.rpi.metpetdb.client.model.Project;
import edu.rpi.metpetdb.client.ui.commands.ServerOp;
import edu.rpi.metpetdb.client.ui.commands.VoidServerOp;
import edu.rpi.metpetdb.client.ui.dialogs.ConfirmationDialogBox;
import edu.rpi.metpetdb.client.ui.dialogs.MDialogBox;
import edu.rpi.metpetdb.client.ui.widgets.MLink;
import edu.rpi.metpetdb.client.ui.widgets.paging.DataList;

public class ProjectListActions extends FlowPanel implements ClickListener {

	private final DataList<Project> list;

	private enum SelectOption {

		EMPTY("----") {
			public void doAction(final DataList<Project> list) {
			// do nothing for empty
			}
		},
		NONE("None") {
			public void doAction(final DataList<Project> list) {
				list.getDataTable().deselectAllRows();
			}
		},
		ALL("All on this page") {
			public void doAction(final DataList<Project> list) {
				list.getDataTable().selectAllRows();
			}
		};

		final String display;

		public abstract void doAction(final DataList<Project> list);

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

	private void setupSelect(final DataList<Project> list) {
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
	
	public ProjectListActions(final DataList<Project> list) {
		this.list = list;
		setupSelect(list);

		remove = new MLink("Remove", this);
		//remove.addStyleName("item");
		remove.addStyleName("beta");
		
		add(remove);
		setStylePrimaryName("scrolltable-actions");
	}

	private void deleteSelected(final List<Project> checkedProjects) {
		new VoidServerOp() {
			@Override
			public void begin() {		
				//TODO delete projects
				/*Iterator<Project> itr = checkedProjects.iterator();
				final ArrayList<Long> ids = new ArrayList<Long>();
				while (itr.hasNext()) {
					ids.add(itr.next().getId());
				}
				
				MpDb.subsample_svc.deleteAll(ids, this);*/

			}
			public void onSuccess() {
				list.getScrollTable().reloadPage();
			}
		}.begin();
	}

	public void onClick(Widget sender) {
		if (sender == remove) {
			final List<Project> checkedProjects = list.getSelectedValues();
			if (checkedProjects.size() == 0){
				noProjectsSelected();
			} else {
				new ServerOp<Boolean>() {
					public void begin() {
						new ConfirmationDialogBox(LocaleHandler.lc_text
								.confirmation_Delete_Project(), true, this);
					}
	
					public void onSuccess(final Boolean result) {
						if (result)
							deleteSelected(checkedProjects);
					}
				}.begin();
			}
		}
	}
	
	private void noProjectsSelected(){
		final MDialogBox noProjectsBox = new MDialogBox();
		final FlowPanel container = new FlowPanel();
		container.add(new Label("No Projects selected"));
		Button ok = new Button("Ok");
		ok.addClickListener(new ClickListener(){
			public void onClick(final Widget sender){
				noProjectsBox.hide();
			}
		});
		container.add(ok);
		noProjectsBox.setWidget(container);
		noProjectsBox.show();
	}
	
}