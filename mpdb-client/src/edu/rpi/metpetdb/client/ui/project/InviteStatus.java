package edu.rpi.metpetdb.client.ui.project;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.model.Invite;
import edu.rpi.metpetdb.client.model.Project;
import edu.rpi.metpetdb.client.model.User;
import edu.rpi.metpetdb.client.ui.MpDb;
import edu.rpi.metpetdb.client.ui.TokenSpace;
import edu.rpi.metpetdb.client.ui.commands.ServerOp;
import edu.rpi.metpetdb.client.ui.widgets.MLink;
import edu.rpi.metpetdb.client.ui.widgets.panels.MPagePanel;

public class InviteStatus extends MPagePanel {
	
	private void addHeader(){
		this.setPageTitle("Status of Invites");	
	}
	
	private void listInvite(final Invite i, final Project p, final User user){
		final HorizontalPanel panel = new HorizontalPanel();
		panel.setSpacing(15);
		
		MLink userLink = new MLink(user.getName(), 
				TokenSpace.detailsOf(user));
		userLink.setWidth("150px");
		panel.add(userLink);
		
		if(i.getStatus().equals("New")){
			Label pendingLabel = new Label("Pending action");
			pendingLabel.setWidth("100px");
			panel.add(pendingLabel);
		} else {
			Label statusLabel = new Label(i.getStatus());
			statusLabel.setWidth("100px");
			panel.add(statusLabel);
		}
		
		Label timeLabel = new Label(i.timeAsString());
		timeLabel.setWidth("150px");
		panel.add(timeLabel);
		this.add(panel);
	}
	
	private void noInvitesLabel(){
		this.add(new Label("This project has not had any invites sent"));
	}

	public InviteStatus showById(final long id) {
		
		addHeader();
		
		new ServerOp<List<Invite>>(){
			@Override
			public void begin() {
				MpDb.project_svc.getInvitesForProject(id, this);
			}
			public void onSuccess(final List<Invite> invites) {
				new ServerOp<Map<Invite,Project>>(){
					public void begin() {
						MpDb.project_svc.getProjectsForInvites(invites, this);
					}
					public void onSuccess(Map<Invite,Project> inviteMap){
						Iterator itr = inviteMap.entrySet().iterator();
						if(inviteMap.size() == 0) {
							noInvitesLabel();
						} else {
							while(itr.hasNext()){
								final Map.Entry pairs = (Map.Entry) itr.next();
								
								//fetch the name of the user that the invite was sent to
								new ServerOp<User>(){
									public void begin() {
										MpDb.user_svc.userById(((Invite) pairs.getKey()).getUser_id(), this);
									}
									public void onSuccess(User result) {
										listInvite((Invite) pairs.getKey(), (Project) pairs.getValue(), result);
									}
								}.begin();
							}
						}
					}
				}.begin();
			}
			
		}.begin();
		
		return this;
	}

}