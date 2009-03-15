package edu.rpi.metpetdb.client.ui.user;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.error.LoginRequiredException;
import edu.rpi.metpetdb.client.locale.LocaleHandler;
import edu.rpi.metpetdb.client.model.User;
import edu.rpi.metpetdb.client.model.UserWithPassword;
import edu.rpi.metpetdb.client.ui.MetPetDBApplication;
import edu.rpi.metpetdb.client.ui.MpDb;
import edu.rpi.metpetdb.client.ui.commands.FormOp;
import edu.rpi.metpetdb.client.ui.input.DetailsPanel;
import edu.rpi.metpetdb.client.ui.input.OnEnterPanel;
import edu.rpi.metpetdb.client.ui.input.Submit;
import edu.rpi.metpetdb.client.ui.input.attributes.GenericAttribute;
import edu.rpi.metpetdb.client.ui.input.attributes.PasswordAttribute;
import edu.rpi.metpetdb.client.ui.input.attributes.TextAttribute;
import edu.rpi.metpetdb.client.ui.widgets.MText;
import edu.rpi.metpetdb.client.ui.widgets.panels.MPagePanel;

public class EditUserProfile extends MPagePanel implements UsesCurrentUser {
	private final User user;

	public EditUserProfile(final User whoToEdit) {
		setPageTitle("Edit Profile");
		user = whoToEdit;
		if (MpDb.isCurrentUser(user)){
			add(new PasswordChanger(user));
			add(new InfoChanger(user));
		}
	}

	public void onCurrentUserChanged(final User whoIsIt)
			throws LoginRequiredException {
		if (whoIsIt == null)
			throw new LoginRequiredException();
	}
	
	static class InfoChanger extends FlowPanel implements ClickListener{
		private static final GenericAttribute[] userAttributes = {
			new TextAttribute(MpDb.doc.User_name),
			new TextAttribute(MpDb.doc.User_emailAddress),
			new TextAttribute(MpDb.doc.User_address), 
			new TextAttribute(MpDb.doc.User_city), 
			new TextAttribute(MpDb.doc.User_province),
			new TextAttribute(MpDb.doc.User_country),
			new TextAttribute(MpDb.doc.User_postalCode), 
			new TextAttribute(MpDb.doc.User_institution), 
			new TextAttribute(MpDb.doc.User_referenceEmail)
		};
		private final User u;
		private final DetailsPanel<User> p_main;
		private final FlowPanel infoContainer = new FlowPanel();
		private final MText infoHeader = new MText("Change your information", "h2");
		private final Button changeInfo;
		
		InfoChanger(final User whoToEdit) {
			u = whoToEdit;
			changeInfo = new Submit(LocaleHandler.lc_text
					.buttonUpdateInfo());
			changeInfo.addClickListener(this);
			p_main = new DetailsPanel<User>(userAttributes,
			new Button[] {
					changeInfo
			});

			p_main.edit(u);
			infoContainer.add(infoHeader);
			infoContainer.add(p_main);

			add(new OnEnterPanel(infoContainer) {
				public void onEnter() {					
					doChangeInfo();
				}
			});
		}	
		
		public void onClick(final Widget sender) {
			if (sender == changeInfo)
				doChangeInfo();
		}
		
		protected void doChangeInfo() {
			new FormOp<User>(p_main) {
				protected void onSubmit() {
					MpDb.user_svc.save(u, this);
				}
				public void onFailure(final Throwable e) {
					p_main.edit(u);
					super.onFailure(e);
				}
				public void onSuccess(final User result) {
					enable(true);
					p_main.edit(u);
					MetPetDBApplication.notice(LocaleHandler.lc_text
							.notice_InfoChanged(u.getEmailAddress()));
				}
			}.begin();
		}
	}

	static class PasswordChanger extends FlowPanel implements ClickListener {
		private static final GenericAttribute[] passwordAttributes = {
				new PasswordAttribute(MpDb.doc.UserWithPassword_oldPassword),
				new PasswordAttribute(MpDb.doc.UserWithPassword_newPassword),
				new PasswordAttribute(MpDb.doc.UserWithPassword_vrfPassword),
		};
		
		private final UserWithPassword uwp;
		
		private final DetailsPanel<UserWithPassword> p_password;
		
		private final FlowPanel passwordContainer = new FlowPanel();
		private final MText passwordHeader = new MText("Change your password", "h2");
		private final Button changePassword;
			
		PasswordChanger(final User whoToEdit) {
			uwp = new UserWithPassword(whoToEdit);

			changePassword = new Submit(LocaleHandler.lc_text
					.buttonChangePassword());
			changePassword.addClickListener(this);
			
			

			final String n = uwp.getUser().getEmailAddress();
			p_password = new DetailsPanel<UserWithPassword>(passwordAttributes,
					new Button[] {
						changePassword
			});
			p_password.edit(uwp);
			
			passwordContainer.add(passwordHeader);
			passwordContainer.add(p_password);

			add(new OnEnterPanel(passwordContainer) {
				public void onEnter() {
					
					doChangePassword();
				}
			});
			
		}

		public void onClick(final Widget sender) {
			if (sender == changePassword)
				doChangePassword();
		}

		protected void doChangePassword() {
			new FormOp<Object>(p_password) {
				protected void onSubmit() {
					MpDb.user_svc.changePassword(uwp, this);
				}
				public void onFailure(final Throwable e) {
					uwp.setOldPassword(null);
					p_password.edit(uwp);
					super.onFailure(e);
				}
				public void onSuccess(final Object result) {
					enable(true);
					uwp.setOldPassword(null);
					uwp.setNewPassword(null);
					uwp.setVrfPassword(null);
					p_password.edit(uwp);
					MetPetDBApplication.notice(LocaleHandler.lc_text
							.notice_PasswordChanged(uwp.getUser()
									.getEmailAddress()));
				}
			}.begin();
		}
	}
}
