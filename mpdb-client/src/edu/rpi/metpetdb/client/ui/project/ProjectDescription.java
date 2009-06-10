package edu.rpi.metpetdb.client.ui.project;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Label;

import edu.rpi.metpetdb.client.model.Project;
import edu.rpi.metpetdb.client.model.User;
import edu.rpi.metpetdb.client.paging.PaginationParameters;
import edu.rpi.metpetdb.client.paging.Results;
import edu.rpi.metpetdb.client.ui.MpDb;
import edu.rpi.metpetdb.client.ui.TokenSpace;
import edu.rpi.metpetdb.client.ui.commands.ServerOp;
import edu.rpi.metpetdb.client.ui.objects.list.ProjectMemberList;
import edu.rpi.metpetdb.client.ui.objects.list.ProjectMemberListEx;
import edu.rpi.metpetdb.client.ui.widgets.MLink;
import edu.rpi.metpetdb.client.ui.widgets.panels.MPagePanel;

public class ProjectDescription extends MPagePanel{
	//TODO: make page more awesome	
	private ProjectMemberListEx list;
	
	public ProjectDescription() {	
		
	}
	
	public void reload() {

	}

	public ProjectDescription showById(final int id) {
		new ServerOp<Project>() {
			public void begin() {
				MpDb.project_svc.details(id, this);
			}
			public void onSuccess(final Project result) {
				setPageTitle(result.getName());
				add(new Label("Creator: " + result.getOwner().getName()));
				add(new Label(result.getDescription()));
				addPageActionItem(new MLink("Invite Member", TokenSpace.sendNewInvite(result)));
				
				list = new ProjectMemberListEx() {
					public void update(final PaginationParameters p,
							final AsyncCallback<Results<User>> ac) {
						MpDb.project_svc.allProjectMembers(p, id, ac);
					}
				};
				add(list);
				
				add(new Label("Comments will go here"));
				
			}
		}.begin();
		
		return this;
	}
}
