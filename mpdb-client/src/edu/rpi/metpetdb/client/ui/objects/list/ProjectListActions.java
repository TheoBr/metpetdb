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
import edu.rpi.metpetdb.client.model.Sample;
import edu.rpi.metpetdb.client.ui.MpDb;
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
		remove.addStyleName("item");
		
		add(remove);
		setStylePrimaryName("scrolltable-actions");
	}

	private void deleteSelected(final List<Project> checkedProjects) {
		new VoidServerOp() {
			@Override
			public void begin() {		
				Iterator<Project> itr = checkedProjects.iterator();
				final ArrayList<Integer> ids = new ArrayList<Integer>();
				while (itr.hasNext()) {
					ids.add(itr.next().getId());
				}
				
				MpDb.project_svc.deleteAll(ids, this);

			}
			public void onSuccess() {
				list.getScrollTable().reloadPage();
			}
		}.begin();
	}
	
	private boolean checkDeletePermissions(List<Project> checkedProjects){
		//check to see if any of the projects are not owned by the current user
		Iterator<Project> itr = checkedProjects.iterator();
		while(itr.hasNext()){
			Project current = itr.next();
			if(!MpDb.isCurrentUser(current.getOwner()))
				return true;
		}
		return false;
	}
	
	private void noPermissionToDelete(){
		final MDialogBox box = new MDialogBox();
		final FlowPanel container = new FlowPanel();
		container.add(new Label("You do not haver permission to delete one or more of these projects"));
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
			final List<Project> checkedProjects = list.getSelectedValues();
			if (checkedProjects.size() == 0){
				noProjectsSelected();
			} else if(checkDeletePermissions(checkedProjects)) {
				noPermissionToDelete();
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