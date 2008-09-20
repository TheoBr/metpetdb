package edu.rpi.metpetdb.client.ui.user;

import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.locale.LocaleHandler;
import edu.rpi.metpetdb.client.model.MObject;
import edu.rpi.metpetdb.client.model.User;
import edu.rpi.metpetdb.client.model.UserWithPassword;
import edu.rpi.metpetdb.client.ui.FormOp;
import edu.rpi.metpetdb.client.ui.MpDb;
import edu.rpi.metpetdb.client.ui.TokenSpace;
import edu.rpi.metpetdb.client.ui.input.DetailsPanel;
import edu.rpi.metpetdb.client.ui.input.FocusSupport;
import edu.rpi.metpetdb.client.ui.input.OnEnterPanel;
import edu.rpi.metpetdb.client.ui.input.Submit;
import edu.rpi.metpetdb.client.ui.input.attributes.GenericAttribute;
import edu.rpi.metpetdb.client.ui.input.attributes.PasswordAttribute;
import edu.rpi.metpetdb.client.ui.input.attributes.TextAttribute;
import edu.rpi.metpetdb.client.ui.widgets.MText;

public class UserRegistrationPanel extends FlowPanel implements ClickListener {
	private static final GenericAttribute[] mainAttributes = {
			new TextAttribute(MpDb.doc.User_emailAddress) {
				protected MObject resolve(final MObject obj) {
					return ((UserWithPassword) obj).getUser();
				}
			}, new TextAttribute(MpDb.doc.User_name) {
				protected MObject resolve(final MObject obj) {
					return ((UserWithPassword) obj).getUser();
				}
			}, new TextAttribute(MpDb.doc.User_address) {
				protected MObject resolve(final MObject obj) {
					return ((UserWithPassword) obj).getUser();
				}
			}, new TextAttribute(MpDb.doc.User_city) {
				protected MObject resolve(final MObject obj) {
					return ((UserWithPassword) obj).getUser();
				}
			}, new TextAttribute(MpDb.doc.User_province) {
				protected MObject resolve(final MObject obj) {
					return ((UserWithPassword) obj).getUser();
				}
			}, new TextAttribute(MpDb.doc.User_country) {
				protected MObject resolve(final MObject obj) {
					return ((UserWithPassword) obj).getUser();
				}
			}, new TextAttribute(MpDb.doc.User_postalCode) {
				protected MObject resolve(final MObject obj) {
					return ((UserWithPassword) obj).getUser();
				}
			}, new TextAttribute(MpDb.doc.User_institution) {
				protected MObject resolve(final MObject obj) {
					return ((UserWithPassword) obj).getUser();
				}
			}, new TextAttribute(MpDb.doc.User_referenceEmail) {
				protected MObject resolve(final MObject obj) {
					return ((UserWithPassword) obj).getUser();
				}
			}, new PasswordAttribute(MpDb.doc.UserWithPassword_newPassword),
			new PasswordAttribute(MpDb.doc.UserWithPassword_vrfPassword),
	};

	private final UserWithPassword newbie;
	private final Button register;
	private final Button toggle;
	private final DetailsPanel<UserWithPassword> p_main;

	public UserRegistrationPanel() {
		newbie = new UserWithPassword(new User());
		register = new Submit(LocaleHandler.lc_text.buttonRegister(), this);

		toggle = new Button("Show");
		toggle.addClickListener(this);

		p_main = new DetailsPanel<UserWithPassword>(mainAttributes,
				new Button[] {
						toggle, register
				});
		p_main.setLegend(LocaleHandler.lc_text.title_RegisterAccountInfo());
		p_main.edit(newbie);

		add(new MText(LocaleHandler.lc_text.buttonRegister(), "h1"));
		add(new MText(LocaleHandler.lc_text.message_WhyRegister(), "p"));
		add(new OnEnterPanel(p_main) {
			public void onEnter() {
				doRegister();
			}
		});
	}

	protected void onLoad() {
		super.onLoad();
		FocusSupport.requestFocus(p_main);
	}

	public void onClick(final Widget sender) {
		if (sender == register)
			doRegister();
		else if (sender == toggle && "show".equals(toggle.getText())) {
			p_main.validateEdit();
			p_main.show(newbie);
			toggle.setText("edit");
			register.setVisible(false);
		} else if (sender == toggle && "edit".equals(toggle.getText())) {
			p_main.edit(newbie);
			toggle.setText("show");
			register.setVisible(true);
		}
	}

	protected void doRegister() {
		new FormOp<User>(p_main) {
			protected void onSubmit() {
				MpDb.user_svc.registerNewUser(newbie, this);
			}
			public void onSuccess(final User result) {
				MpDb.setCurrentUser((User) result);
				History.newItem(TokenSpace.introduction.makeToken(null));
			}
		}.begin();
	}
}
