package edu.rpi.metpetdb.client.ui.user;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.error.LoginRequiredException;
import edu.rpi.metpetdb.client.error.validation.InvalidProfileRequestException;
import edu.rpi.metpetdb.client.locale.LocaleHandler;
import edu.rpi.metpetdb.client.model.User;
import edu.rpi.metpetdb.client.model.UserWithPassword;
import edu.rpi.metpetdb.client.model.interfaces.MObject;
import edu.rpi.metpetdb.client.ui.MetPetDBApplication;
import edu.rpi.metpetdb.client.ui.MpDb;
import edu.rpi.metpetdb.client.ui.TokenSpace;
import edu.rpi.metpetdb.client.ui.commands.FormOp;
import edu.rpi.metpetdb.client.ui.input.DetailsPanel;
import edu.rpi.metpetdb.client.ui.input.OnEnterPanel;
import edu.rpi.metpetdb.client.ui.input.Submit;
import edu.rpi.metpetdb.client.ui.input.attributes.CheckBoxAttribute;
import edu.rpi.metpetdb.client.ui.input.attributes.GenericAttribute;
import edu.rpi.metpetdb.client.ui.input.attributes.PasswordAttribute;
import edu.rpi.metpetdb.client.ui.input.attributes.TextAreaAttribute;
import edu.rpi.metpetdb.client.ui.input.attributes.TextAttribute;
import edu.rpi.metpetdb.client.ui.widgets.MText;
import edu.rpi.metpetdb.client.ui.widgets.panels.MPagePanel;

public class EditUserProfile extends MPagePanel implements UsesCurrentUser {
	private static User user;

	private static TextAreaAttribute contributorRequestAttribute = new TextAreaAttribute(
			MpDb.doc.User_researchInterests, "");

	private static CheckBoxAttribute requestContributorAttribute = new CheckBoxAttribute(
			MpDb.doc.User_requestContributor);

	public EditUserProfile(final User whoToEdit) {
		setPageTitle("Edit Profile");
		
		Label label1 = new Label(LocaleHandler.lc_text.editProfile_description_1());
		
		this.add(label1);
		DOM.appendChild(label1.getElement(), DOM.createElement("br") );
		DOM.appendChild(label1.getElement(), DOM.createElement("br") );

		Label label2 = new Label(LocaleHandler.lc_text.editProfile_description_2());
		this.add(label2);
		DOM.appendChild(label2.getElement(), DOM.createElement("br"));
		DOM.appendChild(label2.getElement(), DOM.createElement("br") );

		Label label3 = new Label(LocaleHandler.lc_text.editProfile_description_3());		
		this.add(label3);
		DOM.appendChild(label3.getElement(), DOM.createElement("br"));
		DOM.appendChild(label3.getElement(), DOM.createElement("br") );
		
		
		user = whoToEdit;

		displayMemberStatus();

		if (MpDb.isCurrentUser(user)) {
			add(new PasswordChanger(user));
			add(new InfoChanger(user));
		}
	}

	private void displayMemberStatus() {
		if (MpDb.isCurrentUser(user) && user.getContributorEnabled()) {
			add(new MText("You are a Contributor"));
		} else if (MpDb.isCurrentUser(user) && user.getEnabled()) {
			add(new MText("You are a Member"));
		} else {
			add(new MText("You are not a confirmed user"));
		}
	}

	public void onCurrentUserChanged(final User whoIsIt)
			throws LoginRequiredException {
		if (whoIsIt == null)
			throw new LoginRequiredException();
	}

	private static void setUser(User u) {
		user = u;
	}

	static class InfoChanger extends FlowPanel implements ClickListener {
		private static final GenericAttribute[] userAttributes = {
				new TextAttribute(MpDb.doc.User_name),
				new TextAttribute(MpDb.doc.User_emailAddress),
				new TextAttribute(MpDb.doc.User_address),
				new TextAttribute(MpDb.doc.User_city),
				new TextAttribute(MpDb.doc.User_province),
				new TextAttribute(MpDb.doc.User_country),
				new TextAttribute(MpDb.doc.User_postalCode),
				new TextAttribute(MpDb.doc.User_institution),
				contributorRequestAttribute,
				new TextAttribute(MpDb.doc.User_professionalUrl),
				requestContributorAttribute
		};
		private User u;
		private final DetailsPanel<User> p_main;
		private final FlowPanel infoContainer = new FlowPanel();
		private final MText infoHeader = new MText("Change your information",
				"h2");
		private final Button changeInfo;


		private void setUser(User u) {
			this.u = u;
		}

		InfoChanger(final User whoToEdit) {
			u = whoToEdit;
			changeInfo = new Submit(LocaleHandler.lc_text.buttonUpdateInfo());

			changeInfo.addClickListener(this);

			p_main = new DetailsPanel<User>(userAttributes, new Button[] {
				changeInfo
			});

			p_main.edit(u);

			if (p_main.getBean().getRequestContributor()
					|| p_main.getBean().getContributorEnabled()) {
				for (int i = 0; i < p_main.getWidgetCount(); i++) {
					Widget currWidget = p_main.getWidget(i);

					if (currWidget instanceof CheckBox) {
						CheckBox cb = (CheckBox) currWidget;
						cb.setEnabled(false);
					}
				}
			}

			infoContainer.add(infoHeader);
			infoContainer.add(p_main);

			add(new OnEnterPanel(infoContainer) {
				public void onEnter() {
					
					try
					{
						doChangeInfo();
					}
					catch (Exception e)
					{
						throw new RuntimeException(e.getMessage());						
					}
				}
			});
		}

		public void onClick(final Widget sender) {
			if (sender == changeInfo)
				{
				try
				{
				doChangeInfo();
				}
				catch (Exception e)
				{
					throw new RuntimeException(e.getMessage());
				}
				}

		}

		
		
		protected void doChangeInfo() throws InvalidProfileRequestException {
			new FormOp<User>(p_main) {
				protected void onSubmit()  {


					CheckBox requestContributorRequest = null;

					for (int i = 0; i < p_main.getWidgetCount(); i++) {
						Widget currWidget = p_main.getWidget(i);

						if (currWidget instanceof CheckBox) {
							requestContributorRequest = (CheckBox) currWidget;
							break;
						}
					}

					TextArea researchInterests = (TextArea) p_main
							.getEditWidgets(contributorRequestAttribute)[0];

					if (requestContributorRequest.getValue().equals(Boolean.TRUE)) {
						MpDb.user_svc.sendContributorCode(u, researchInterests
								.getText(), this);
						
					}
					else
					{
						MpDb.user_svc.save(u, this);
					}

				}
				public void onFailure(final Throwable e) {
					p_main.edit(u);
					super.onFailure(e);
				}
				public void onSuccess(final User result) {
					setUser(result);
					u = result;
					enable(true);
					// p_main.edit(u);
					MetPetDBApplication.notice(LocaleHandler.lc_text
							.notice_InfoChanged(u.getEmailAddress()));

					// Go Home
					History.newItem(TokenSpace.home.makeToken(null));
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
		private final MText passwordHeader = new MText("Change your password",
				"h2");
		private final Button changePassword;

		PasswordChanger(final User whoToEdit) {
			uwp = new UserWithPassword(whoToEdit);

			changePassword = new Submit(LocaleHandler.lc_text
					.buttonChangePassword());
			changePassword.addClickListener(this);

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
			new FormOp<MObject>(p_password) {
				protected void onSubmit() {
					MpDb.user_svc.changePassword(uwp, this);
				}
				public void onFailure(final Throwable e) {
					uwp.setOldPassword(null);
					p_password.edit(uwp);
					super.onFailure(e);
				}
				public void onSuccess(final MObject result) {
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
