package edu.rpi.metpetdb.client.ui.dialogs;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SourcesTabEvents;
import com.google.gwt.user.client.ui.TabListener;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.error.NoSuchObjectException;
import edu.rpi.metpetdb.client.error.UnableToSendEmailException;
import edu.rpi.metpetdb.client.locale.LocaleHandler;
import edu.rpi.metpetdb.client.model.StartSessionRequest;
import edu.rpi.metpetdb.client.model.User;
import edu.rpi.metpetdb.client.ui.FormOp;
import edu.rpi.metpetdb.client.ui.MpDb;
import edu.rpi.metpetdb.client.ui.ServerOp;
import edu.rpi.metpetdb.client.ui.input.DetailsPanel;
import edu.rpi.metpetdb.client.ui.input.FocusSupport;
import edu.rpi.metpetdb.client.ui.input.Submit;
import edu.rpi.metpetdb.client.ui.input.attributes.GenericAttribute;
import edu.rpi.metpetdb.client.ui.input.attributes.PasswordAttribute;
import edu.rpi.metpetdb.client.ui.input.attributes.TextAttribute;
import edu.rpi.metpetdb.client.ui.widgets.MTabPanel;

public class LoginDialog extends MDialogBox implements ClickListener,
		KeyboardListener, TabListener {
	private static final GenericAttribute[] mainAttributes = {
			new TextAttribute(MpDb.doc.StartSessionRequest_emailAddress),
			new PasswordAttribute(MpDb.doc.StartSessionRequest_password),
	};
	private static final GenericAttribute[] emailAttributes = {
		new TextAttribute(MpDb.doc.StartSessionRequest_emailAddress),
	};

	protected final ServerOp<?> continuation;
	protected final StartSessionRequest ssr;
	protected final MTabPanel tabs;
	protected final DetailsPanel<StartSessionRequest> p_main;
	private final DetailsPanel<StartSessionRequest> p_email;
	private final int p_mainIdx;
	protected final int p_emailIdx;
	private final Button loginC;
	private final Button login;
	private final Button emailC;
	private final Button email;

	public LoginDialog(final ServerOp<?> r) {
		continuation = r;
		ssr = new StartSessionRequest();
		setText("Please Login");

		loginC = new Button(LocaleHandler.lc_text.buttonCancel(), this);
		login = new Submit(LocaleHandler.lc_text.buttonLogin(), this);

		emailC = new Button(LocaleHandler.lc_text.buttonCancel(), this);
		email = new Submit(LocaleHandler.lc_text.buttonEmailPassword(), this);

		p_main = new DetailsPanel<StartSessionRequest>(mainAttributes,
				new Button[] {
						login, loginC
				});
		p_main.edit(ssr);

		p_email = new DetailsPanel<StartSessionRequest>(emailAttributes,
				new Button[] {
						email, emailC
				});

		tabs = new MTabPanel();
		{
			final FlowPanel p = new FlowPanel();
			p.add(p_main);
			tabs.add(p, LocaleHandler.lc_text.tab_Login());
			p_mainIdx = tabs.getDeckPanel().getWidgetIndex(p);
		}
		{
			final FlowPanel p = new FlowPanel();
			p.add(p_email);
			tabs.add(p, LocaleHandler.lc_text.tab_ForgotPassword());
			p_emailIdx = tabs.getDeckPanel().getWidgetIndex(p);
		}
		tabs.selectTab(p_mainIdx);
		tabs.addTabListener(this);

		final FocusPanel f = new FocusPanel();
		f.addKeyboardListener(this);
		f.setWidget(tabs);

		setWidget(f);
		this.addStyleName("mpdb-login");
	}

	protected void onLoad() {
		super.onLoad();
		FocusSupport.requestFocus(p_main);
	}

	public boolean onBeforeTabSelected(final SourcesTabEvents s, final int idx) {
		return loginC.isEnabled() && emailC.isEnabled();
	}

	public void onTabSelected(final SourcesTabEvents s, final int idx) {
		if (p_mainIdx == idx) {
			p_email.validateEdit();
			p_main.edit(ssr);
		} else if (p_emailIdx == idx) {
			p_main.validateEdit();
			p_email.edit(ssr);
		}
		FocusSupport.requestFocus(tabs.getDeckPanel().getWidget(idx));
	}

	public void onClick(final Widget sender) {
		if (login == sender)
			doLogin();
		else if (email == sender)
			doEmailPassword();
		else if (loginC == sender || emailC == sender)
			cancel();
	}

	public void onKeyPress(final Widget sender, final char kc, final int mod) {
		if (kc == KEY_ENTER) {
			final int activeIdx = tabs.getDeckPanel().getVisibleWidget();
			if (login.isEnabled() && activeIdx == p_mainIdx)
				doLogin();
			else if (email.isEnabled() && activeIdx == p_emailIdx)
				doEmailPassword();
		} else if (kc == KEY_ESCAPE && loginC.isEnabled() && emailC.isEnabled())
			cancel();
	}

	public void onKeyDown(final Widget sender, final char kc, final int mod) {
	}

	public void onKeyUp(final Widget sender, final char kc, final int mod) {
	}

	private void cancel() {
		hide();
		if (continuation != null)
			continuation.cancel();
	}

	private void doEmailPassword() {
		new FormOp<Void>(p_email) {
			protected void onSubmit() {
				MpDb.user_svc.emailPassword(ssr.getEmailAddress(), this);
			}
			public void onFailure(final Throwable e) {
				if (e instanceof UnableToSendEmailException) {
					enable(true);
					super.onFailure(e);
				} else if (e instanceof NoSuchObjectException) {
					enable(true);
					// p_email.showValidationException(new
					// LoginFailureException(
					// MpDb.doc.StartSessionRequest_username));
				} else {
					super.onFailure(e);
				}
			}
			public void onSuccess(final Void result) {
				enable(true);
				final Widget old = getWidget();
				final Button close = new Button(LocaleHandler.lc_text
						.buttonClose(), new ClickListener() {
					public void onClick(final Widget sender) {
						setWidget(old);
						tabs.selectTab(p_mainIdx);
					}
				});
				final VerticalPanel p = new VerticalPanel();
				p.add(new Label(LocaleHandler.lc_text.message_NewPasswordSet(),
						true));
				p.add(close);
				p.setCellHorizontalAlignment(close, VerticalPanel.ALIGN_CENTER);
				setWidget(p);
			}
		}.begin();
	}

	protected void doLogin() {
		new FormOp<User>(p_main) {
			protected void onSubmit() {
				MpDb.user_svc.startSession(ssr, this);
			}
			public void onFailure(final Throwable e) {
				ssr.setPassword(null);
				p_main.edit(ssr);
				super.onFailure(e);
			}
			public void onSuccess(final User result) {
				MpDb.setCurrentUser((User) result);
				hide();
				if (continuation != null)
					continuation.begin();
			}
		}.begin();
	}
}
