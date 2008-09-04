package edu.rpi.metpetdb.client.ui.user;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.error.LoginRequiredException;
import edu.rpi.metpetdb.client.locale.LocaleHandler;
import edu.rpi.metpetdb.client.model.UserDTO;
import edu.rpi.metpetdb.client.model.UserWithPasswordDTO;
import edu.rpi.metpetdb.client.ui.FormOp;
import edu.rpi.metpetdb.client.ui.MetPetDBApplication;
import edu.rpi.metpetdb.client.ui.MpDb;
import edu.rpi.metpetdb.client.ui.input.DetailsPanel;
import edu.rpi.metpetdb.client.ui.input.OnEnterPanel;
import edu.rpi.metpetdb.client.ui.input.Submit;
import edu.rpi.metpetdb.client.ui.input.attributes.GenericAttribute;
import edu.rpi.metpetdb.client.ui.input.attributes.PasswordAttribute;

public class EditUserProfile extends FlowPanel implements UsesCurrentUser {
	private final UserDTO user;

	public EditUserProfile(final UserDTO whoToEdit) {
		user = whoToEdit;
		if (MpDb.isCurrentUser(user))
			add(new PasswordChanger(user));
	}

	public void onCurrentUserChanged(final UserDTO whoIsIt)
			throws LoginRequiredException {
		if (whoIsIt == null)
			throw new LoginRequiredException();
	}

	static class PasswordChanger extends FlowPanel implements ClickListener {
		private static final GenericAttribute[] passwordAttributes = {
				new PasswordAttribute(MpDb.doc.UserWithPassword_oldPassword),
				new PasswordAttribute(MpDb.doc.UserWithPassword_newPassword),
				new PasswordAttribute(MpDb.doc.UserWithPassword_vrfPassword),
		};
		private final UserWithPasswordDTO uwp;
		private final DetailsPanel<UserWithPasswordDTO> p_password;
		private final Button changePassword;

		PasswordChanger(final UserDTO whoToEdit) {
			uwp = new UserWithPasswordDTO(whoToEdit);

			changePassword = new Submit(LocaleHandler.lc_text
					.buttonChangePassword());
			changePassword.addClickListener(this);

			final String n = uwp.getUser().getEmailAddress();
			p_password = new DetailsPanel<UserWithPasswordDTO>(
					passwordAttributes, new Button[] {
						changePassword
					});
			p_password.setLegend(LocaleHandler.lc_text
					.title_ChangeAccountPassword(n));
			p_password.edit(uwp);

			add(new OnEnterPanel(p_password) {
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
					MetPetDBApplication
							.notice(LocaleHandler.lc_text
									.notice_PasswordChanged(uwp.getUser()
											.getEmailAddress()));
				}
			}.begin();
		}
	}
}
