package edu.rpi.metpetdb.client.ui.user;

import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.locale.LocaleHandler;
import edu.rpi.metpetdb.client.model.MObjectDTO;
import edu.rpi.metpetdb.client.model.UserDTO;
import edu.rpi.metpetdb.client.model.UserWithPasswordDTO;
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
				protected MObjectDTO resolve(final MObjectDTO obj) {
					return ((UserWithPasswordDTO) obj).getUser();
				}
			},
			new TextAttribute(MpDb.doc.User_firstName) {
				protected MObjectDTO resolve(final MObjectDTO obj) {
					return ((UserWithPasswordDTO) obj).getUser();
				}
			},
			new TextAttribute(MpDb.doc.User_lastName) {
				protected MObjectDTO resolve(final MObjectDTO obj) {
					return ((UserWithPasswordDTO) obj).getUser();
				}
			},
			new TextAttribute(MpDb.doc.User_address) {
				protected MObjectDTO resolve(final MObjectDTO obj) {
					return ((UserWithPasswordDTO) obj).getUser();
				}
			},
			new TextAttribute(MpDb.doc.User_city) {
				protected MObjectDTO resolve(final MObjectDTO obj) {
					return ((UserWithPasswordDTO) obj).getUser();
				}
			},
			new TextAttribute(MpDb.doc.User_province) {
				protected MObjectDTO resolve(final MObjectDTO obj) {
					return ((UserWithPasswordDTO) obj).getUser();
				}
			},
			new TextAttribute(MpDb.doc.User_country) {
				protected MObjectDTO resolve(final MObjectDTO obj) {
					return ((UserWithPasswordDTO) obj).getUser();
				}
			},
			new TextAttribute(MpDb.doc.User_postalCode) {
				protected MObjectDTO resolve(final MObjectDTO obj) {
					return ((UserWithPasswordDTO) obj).getUser();
				}
			},
			new TextAttribute(MpDb.doc.User_institution) {
				protected MObjectDTO resolve(final MObjectDTO obj) {
					return ((UserWithPasswordDTO) obj).getUser();
				}
			},
			new TextAttribute(MpDb.doc.User_referenceEmail) {
				protected MObjectDTO resolve(final MObjectDTO obj) {
					return ((UserWithPasswordDTO) obj).getUser();
				}
			},
			new PasswordAttribute(MpDb.doc.UserWithPassword_newPassword),
			new PasswordAttribute(MpDb.doc.UserWithPassword_vrfPassword),
	};

	private final UserWithPasswordDTO newbie;
	private final Button register;
	private final Button toggle;
	private final DetailsPanel<UserWithPasswordDTO> p_main;

	public UserRegistrationPanel() {
		newbie = new UserWithPasswordDTO(new UserDTO());
		register = new Submit(LocaleHandler.lc_text.buttonRegister(), this);

		toggle = new Button("Show");
		toggle.addClickListener(this);

		p_main = new DetailsPanel<UserWithPasswordDTO>(mainAttributes,
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
		new FormOp<UserDTO>(p_main) {
			protected void onSubmit() {
				MpDb.user_svc.registerNewUser(newbie, this);
				Window.alert("You've been registered as "
						+ newbie.getUser().getEmailAddress());
			}
			public void onSuccess(final UserDTO result) {
				MpDb.setCurrentUser((UserDTO) result);
				History.newItem(TokenSpace.introduction.makeToken(null));
			}
		}.begin();
	}
}
