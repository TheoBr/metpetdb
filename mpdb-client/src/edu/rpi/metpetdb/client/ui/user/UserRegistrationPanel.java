package edu.rpi.metpetdb.client.ui.user;

import com.claudiushauptmann.gwt.recaptcha.client.RecaptchaWidget;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.error.validation.InvalidCaptchaException;
import edu.rpi.metpetdb.client.locale.LocaleHandler;
import edu.rpi.metpetdb.client.model.User;
import edu.rpi.metpetdb.client.model.UserWithPassword;
import edu.rpi.metpetdb.client.model.interfaces.MObject;
import edu.rpi.metpetdb.client.ui.CSS;
import edu.rpi.metpetdb.client.ui.MpDb;
import edu.rpi.metpetdb.client.ui.TokenSpace;
import edu.rpi.metpetdb.client.ui.commands.FormOp;
import edu.rpi.metpetdb.client.ui.input.CurrentMessage;
import edu.rpi.metpetdb.client.ui.input.DetailsPanel;
import edu.rpi.metpetdb.client.ui.input.FocusSupport;
import edu.rpi.metpetdb.client.ui.input.OnEnterPanel;
import edu.rpi.metpetdb.client.ui.input.Submit;
import edu.rpi.metpetdb.client.ui.input.attributes.GenericAttribute;
import edu.rpi.metpetdb.client.ui.input.attributes.PasswordAttribute;
import edu.rpi.metpetdb.client.ui.input.attributes.TextAreaAttribute;
import edu.rpi.metpetdb.client.ui.input.attributes.TextAttribute;
import edu.rpi.metpetdb.client.ui.widgets.panels.MPagePanel;

public class UserRegistrationPanel extends MPagePanel implements ClickListener {

	private static final TextAreaAttribute contributorRequestAttribute = new TextAreaAttribute(
			MpDb.doc.User_researchInterests, "") {
		protected MObject resolve(final MObject obj) {

			return ((UserWithPassword) obj).getUser();
		}

	};

	private static final GenericAttribute[] mainAttributes = {
			new TextAttribute(MpDb.doc.User_name) {
				protected MObject resolve(final MObject obj) {
					return ((UserWithPassword) obj).getUser();
				}
			}, new TextAttribute(MpDb.doc.User_emailAddress) {
				protected MObject resolve(final MObject obj) {
					return ((UserWithPassword) obj).getUser();
				}
			}, new PasswordAttribute(MpDb.doc.UserWithPassword_newPassword),
			new PasswordAttribute(MpDb.doc.UserWithPassword_vrfPassword),
			new TextAttribute(MpDb.doc.User_address) {
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
			},

			contributorRequestAttribute

			, new TextAttribute(MpDb.doc.User_professionalUrl) {
				protected MObject resolve(final MObject obj) {
					return ((UserWithPassword) obj).getUser();
				}
			}

	};

	private final UserWithPassword newbie;
	private final Button register;
	private final DetailsPanel<UserWithPassword> p_main;
	private final RecaptchaWidget rw;

	public UserRegistrationPanel() {
		setStyleName(CSS.REGISTER);
		setPageTitle("Register");
		setPageDescription(LocaleHandler.lc_text.message_WhyRegister());
		newbie = new UserWithPassword(new User());
		register = new Submit(LocaleHandler.lc_text.buttonRegister(), this);

		// TODO: Externalize Recaptcha key SCB
		rw = new RecaptchaWidget("6LeCaL0SAAAAAD-dKyj9t3PTOqdW8j9svbfHn9P2");

		p_main = new DetailsPanel<UserWithPassword>(mainAttributes,
				new Widget[] {
						rw, register
				});

		p_main.edit(newbie);

		add(new OnEnterPanel(p_main) {
			public void onEnter() {

				try {
					doRegister();
				} catch (InvalidCaptchaException bce) {
					bce.printStackTrace();
				}
			}
		});
	}

	protected void onLoad() {
		super.onLoad();
		FocusSupport.requestFocus(p_main);

	}

	public void onClick(final Widget sender) {
		if (sender == register) {
			try {
				doRegister();
			} catch (InvalidCaptchaException bce) {
				Window.alert("Invalid captcha entered");

				throw new RuntimeException("Invalid captcha entered");
			}
		}

	}

	private CurrentMessage getCurrentMessage(final GenericAttribute attr) {

		return p_main.getCurrentMessage(attr);
	}

	protected void doRegister() throws InvalidCaptchaException {
		new FormOp<User>(p_main) {

			protected void onSubmit() {

				MpDb.user_svc.registerNewUser(newbie, rw.getChallenge(), rw
						.getResponse(), this);
			}

			public void onSuccess(final User result) {
				MpDb.setCurrentUser((User) result);
				History.newItem(TokenSpace.home.makeToken(null));
			}
		}.begin();
	}
}
