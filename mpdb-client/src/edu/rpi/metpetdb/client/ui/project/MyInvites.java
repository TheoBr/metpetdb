package edu.rpi.metpetdb.client.ui.project;

import java.util.HashMap;
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
import edu.rpi.metpetdb.client.model.properties.ProjectProperty;
import edu.rpi.metpetdb.client.ui.MetPetDBApplication;
import edu.rpi.metpetdb.client.ui.MpDb;
import edu.rpi.metpetdb.client.ui.TokenSpace;
import edu.rpi.metpetdb.client.ui.commands.ServerOp;
import edu.rpi.metpetdb.client.ui.widgets.MLink;
import edu.rpi.metpetdb.client.ui.widgets.panels.MPagePanel;

public class MyInvites extends MPagePanel {
	
	public MyInvites(){
		
	}
	
	private void addHeader(){
		this.setPageTitle("My Invites");	
	}
	
	private void listInvite(final Invite i, final Project p){
		final HorizontalPanel panel = new HorizontalPanel();
		panel.setSpacing(15);
		
		Label projectName = new Label(p.getName());
		projectName.setWidth("150px");
		MLink ownerName = new MLink(p.getOwner().getName(), 
				TokenSpace.detailsOf(p.getOwner()));
		ownerName.setWidth("150px");
		panel.add(projectName);
		panel.add(ownerName);
		
		if(i.getStatus().equals("New")){
			MLink acceptLink = new MLink("Accept", new ClickListener(){
				public void onClick(Widget sender){
					new ServerOp(){
						@Override
						public void begin(){
							MpDb.project_svc.acceptInvite(i, this);
						}
						public void onSuccess(Object result){
							refreshInviteListing(panel, i, p);
							MetPetDBApplication.populateLogbar(MpDb.currentUser());
						}
					}.begin();
				}
			});
			acceptLink.setWidth("100px");
			panel.add(acceptLink);
			MLink rejectLink = new MLink("Reject", new ClickListener(){
				public void onClick(Widget sender){
					new ServerOp(){
						@Override
						public void begin(){
							MpDb.project_svc.rejectInvite(i, this);
						}
						public void onSuccess(Object result){
							refreshInviteListing(panel, i, p);
							MetPetDBApplication.populateLogbar(MpDb.currentUser());
						}
					}.begin();
				}
			});
			rejectLink.setWidth("100px");
			panel.add(rejectLink);
		} else {
			Label statusLabel = new Label(i.getStatus());
			statusLabel.setWidth("100px");
			panel.add(statusLabel);
		}
		
		Label timeLabel = new Label(i.timeAsString());
		timeLabel.setWidth("150px");
		Label descriptionLabel = new Label(p.getDescription());
		descriptionLabel.setWidth("250px");
		panel.add(timeLabel);
		panel.add(descriptionLabel);
		this.add(panel);
	}
	
	private void noInvitesLabel(){
		this.add(new Label("You currently have no invites"));
	}
	
	private void refreshInviteListing(Widget w, final Invite i, final Project p){
		this.remove(w);
		new ServerOp<Invite>(){
			public void begin() {
				MpDb.project_svc.inviteDetails(i.getId(), this);
			}
			public void onSuccess(final Invite invite){
				listInvite(invite, p);
			}
		}.begin();
	}

	public MyInvites showById(long id) {
		
		addHeader();
		
		new ServerOp<List<Invite>>(){
			@Override
			public void begin() {
				MpDb.project_svc.getInvitesForUser(MpDb.currentUser().getId(), this);
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
								Map.Entry pairs = (Map.Entry) itr.next();
								listInvite((Invite) pairs.getKey(), (Project) pairs.getValue());
							}
						}
					}
				}.begin();
			}
			
		}.begin();
		
		return this;
	}

}
