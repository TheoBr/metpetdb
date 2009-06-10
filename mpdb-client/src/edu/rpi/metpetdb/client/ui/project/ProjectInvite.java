package edu.rpi.metpetdb.client.ui.project;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.locale.LocaleHandler;
import edu.rpi.metpetdb.client.model.Invite;
import edu.rpi.metpetdb.client.model.Project;
import edu.rpi.metpetdb.client.model.User;
import edu.rpi.metpetdb.client.ui.MpDb;
import edu.rpi.metpetdb.client.ui.commands.ServerOp;
import edu.rpi.metpetdb.client.ui.input.ObjectEditorPanel;
import edu.rpi.metpetdb.client.ui.input.OnEnterPanel;
import edu.rpi.metpetdb.client.ui.input.attributes.GenericAttribute;
import edu.rpi.metpetdb.client.ui.input.attributes.TextAreaAttribute;
import edu.rpi.metpetdb.client.ui.input.attributes.TextAttribute;
import edu.rpi.metpetdb.client.ui.objects.details.ProjectDetails;

public class ProjectInvite extends FlowPanel {
	private static GenericAttribute[] InviteAtts = {
		new TextAttribute(MpDb.doc.User_emailAddress)
	};
	
	private final ObjectEditorPanel<User> p_user;
	private Project p;

	public ProjectInvite() {
		p_user = new ObjectEditorPanel<User>(InviteAtts, LocaleHandler.lc_text
				.inviteMember(), LocaleHandler.lc_text.inviteMemberDescription()) {
			private boolean savedNew;

			protected void loadBean(final AsyncCallback<User> ac) {}
			protected void saveBean(final AsyncCallback<User> ac) {
				// true when saved the first time or saved after editing
				savedNew = true;
				final User u = (User) getBean();
				final Invite i = new Invite();
				i.setProject_id(p.getId());
				new ServerOp<User>() {
					public void begin() {
						MpDb.user_svc.details(u.getEmailAddress(), this);
					}
					public void onSuccess(final User result) {
						if(result != null){
							i.setMember_id(result.getId());
							MpDb.project_svc.saveInvite(i, ac);
						}
						else{
							//TODO throw an error about the user not existing
						}
					}
				}.begin();
			}
			
			protected void deleteBean(final AsyncCallback<Object> ac) {}
			
			protected void onSaveCompletion(final Project result) {
				/*if (savedNew)
					MpDb.currentUser().getProjects().add((Project) result);
				this.show(result);*/
			}
		};
		Button save = p_user.getSaveButton();
		save.setText("Send Invite");
		add(new OnEnterPanel.ObjectEditor(p_user));
	}

	public ProjectInvite newInvite(Project p) {
		this.p = p;
		User u = new User();
		p_user.edit(u);
		return this;
	}

}