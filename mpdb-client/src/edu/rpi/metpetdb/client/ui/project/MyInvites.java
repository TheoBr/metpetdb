package edu.rpi.metpetdb.client.ui.project;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.model.Invite;
import edu.rpi.metpetdb.client.model.Project;
import edu.rpi.metpetdb.client.ui.MpDb;
import edu.rpi.metpetdb.client.ui.commands.ServerOp;
import edu.rpi.metpetdb.client.ui.widgets.MLink;
import edu.rpi.metpetdb.client.ui.widgets.panels.MPagePanel;

public class MyInvites extends MPagePanel {
	
	public MyInvites(){
		
	}
	
	private void addHeader(){
		this.setPageTitle("My Invites");	
	}
	
	private void listInvite(final Project p, Invite i){
		final HorizontalPanel panel = new HorizontalPanel();
		panel.setSpacing(15);
		
		panel.add(new Label(p.getName()));
		panel.add(new MLink("Accept", new ClickListener(){
			public void onClick(Widget sender){
				new ServerOp(){
					@Override
					public void begin(){
						Invite i = new Invite();
						i.setUser_id(MpDb.currentUser().getId());
						i.setProject_id(p.getId());
						MpDb.project_svc.acceptInvite(i, this);
					}
					public void onSuccess(Object result){
						removeInviteListing(panel);
					}
				}.begin();
			}
		}));
		panel.add(new MLink("Reject", new ClickListener(){
			public void onClick(Widget sender){
				new ServerOp(){
					@Override
					public void begin(){
						Invite i = new Invite();
						i.setUser_id(MpDb.currentUser().getId());
						i.setProject_id(p.getId());
						MpDb.project_svc.rejectInvite(i, this);
					}
					public void onSuccess(Object result){
						removeInviteListing(panel);
					}
				}.begin();
			}
		}));
		this.add(panel);
	}
	
	private void noInvitesLabel(){
		this.add(new Label("You currently have no invites"));
	}
	
	private void removeInviteListing(Widget w){
		this.remove(w);
	}

	public MyInvites showById(long id) {
		
		addHeader();
		
		new ServerOp(){
			@Override
			public void begin() {
				MpDb.project_svc.getInvitesForUser(MpDb.currentUser().getId(), this);
			}
			public void onSuccess(final Object projects) {
				new ServerOp(){
					public void begin() {
						MpDb.project_svc.inviteDetails((List<Project>) projects, MpDb.currentUser().getId(), this);
					}
					public void onSuccess(Object inviteMap){
						Map<Project,Invite> invites = (Map<Project, Invite>) inviteMap;
						System.out.println("out test");
					}
				}.begin();
				
				
				/*List<Project> invites = (List<Project>) result;
				for(Project p: invites){
					listInvite(p);
				}
				if(invites.size() == 0){
					noInvitesLabel();
				}*/
			}
			
		}.begin();
		
		return this;
	}

}
