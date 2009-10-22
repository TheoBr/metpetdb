package edu.rpi.metpetdb.client.ui.project;

import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.locale.LocaleHandler;
import edu.rpi.metpetdb.client.model.Invite;
import edu.rpi.metpetdb.client.model.Project;
import edu.rpi.metpetdb.client.model.User;
import edu.rpi.metpetdb.client.ui.MpDb;
import edu.rpi.metpetdb.client.ui.TokenSpace;
import edu.rpi.metpetdb.client.ui.commands.ServerOp;
import edu.rpi.metpetdb.client.ui.dialogs.MDialogBox;
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
							i.setUser_id(result.getId());
							new ServerOp<Invite>() {
								public void begin() {	
									i.setStatus("New");
									MpDb.project_svc.saveInvite(i, this);
								}
								public void onSuccess(final Invite result){
									new ServerOp<Project> () {
										public void begin() {
											AsyncCallback<Project> ac;
											MpDb.project_svc.details(result.getProject_id(), this);
										}
										public void onSuccess(final Project result){
											History.newItem(TokenSpace.descriptionOf((Project) result));
										}
									}.begin();
								}
							}.begin();
							
						}
					}
					@Override
					public void onFailure(final Throwable e) {
						userNotFound();
					}
				}.begin();
			}
			
			protected void deleteBean(final AsyncCallback<Object> ac) {}
		};
		Button save = p_user.getSaveButton();
		save.setText("Send Invite");
		add(new OnEnterPanel.ObjectEditor(p_user));
	}
	
	private void userNotFound(){
		final MDialogBox notFoundBox = new MDialogBox();
		final FlowPanel container = new FlowPanel();
		container.add(new Label("This user has not yet registered at MetPetDB"));
		Button ok = new Button("Ok");
		ok.addClickListener(new ClickListener(){
			public void onClick(final Widget sender){
				notFoundBox.hide();
				User u = new User();
				p_user.edit(u);
			}
		});
		container.add(ok);
		notFoundBox.setWidget(container);
		notFoundBox.show();
	}

	public ProjectInvite newInvite(Project p) {
		this.p = p;
		User u = new User();
		p_user.edit(u);
		return this;
	}

}
