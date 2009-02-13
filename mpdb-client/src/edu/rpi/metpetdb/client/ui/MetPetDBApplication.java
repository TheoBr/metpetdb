package edu.rpi.metpetdb.client.ui;

import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.error.LoginRequiredException;
import edu.rpi.metpetdb.client.locale.LocaleHandler;
import edu.rpi.metpetdb.client.model.ResumeSessionResponse;
import edu.rpi.metpetdb.client.model.User;
import edu.rpi.metpetdb.client.service.MpDbConstants;
import edu.rpi.metpetdb.client.ui.dialogs.LoginDialog;
import edu.rpi.metpetdb.client.ui.dialogs.UnknownErrorDialog;
import edu.rpi.metpetdb.client.ui.user.UsesCurrentUser;
import edu.rpi.metpetdb.client.ui.widgets.MHtmlList;
import edu.rpi.metpetdb.client.ui.widgets.MLink;
import edu.rpi.metpetdb.client.ui.widgets.MMenuBar;

/** Main application entry point. */
public class MetPetDBApplication implements EntryPoint {

	private static RootPanel logbar;
	private static MHtmlList logbarLinks = new MHtmlList();
	private static MLink loginLink;
	private static MLink registerLink;
	private static MLink editProfileLink;
	private static MLink logoutLink;
	private static MLink reportBugLink;
	
	private static MMenuBar hdrnav;
	private static RootPanel breadcrumbsBar;
	private static RootPanel inPageNotice;
	private static RootPanel contentContainer;
	private static RootPanel noticeContainer;
	private static HashSet<Widget> pageChangeWatchers;
	private static RootPanel footerContainer;
	
	{
		loginLink = new MLink(LocaleHandler.lc_text.buttonLogin(),
				new ClickListener() {
					public void onClick(final Widget sender) {
						new LoginDialog(null).show();
					}
				});
		loginLink.addStyleName(CSS.LIGHT_LINK);
		registerLink = new MLink(LocaleHandler.lc_text.buttonRegister(),
				TokenSpace.register);
		registerLink.addStyleName(CSS.LIGHT_LINK);
		reportBugLink = new MLink("Report a Bug",new ClickListener() {
			public void onClick(final Widget sender) {
				Window.open(MpDb.BUG_REPORT_URL, "mpdb_trac", "");
			}
		});
		reportBugLink.addStyleName("report-bug");
		editProfileLink = new MLink(LocaleHandler.lc_text.tools_EditProfile(),
				TokenSpace.editProfile);
		editProfileLink.addStyleName(CSS.LIGHT_LINK);
		logoutLink = new MLink(LocaleHandler.lc_text.buttonLogout(),
				new ClickListener() {
					public void onClick(Widget sender) {
						MpDb.setCurrentUser(null);
						TokenSpace.home.execute();
						History.newItem(TokenSpace.home.getName());
					}
				});
		logoutLink.addStyleName("logout");
	}

	public void onModuleLoad() {
		GWT.setUncaughtExceptionHandler(ErrorHandler.INSTANCE);
		History.addHistoryListener(TokenSpace.INSTANCE);

		logbar = RootPanel.get(CSS.LOGBAR_ID);
		breadcrumbsBar = RootPanel.get(CSS.BREADCRUMBS_ID);
		inPageNotice = RootPanel.get(CSS.IN_PAGE_NOTICE_ID);
		hdrnav = new MMenuBar();
		RootPanel.get(CSS.HDRNAV_ID).add(hdrnav);
		contentContainer = RootPanel.get(CSS.CONTENT_ID);
		noticeContainer = RootPanel.get(CSS.NOTICE_ID);
		footerContainer = RootPanel.get(CSS.FOOTER_ID);

		pageChangeWatchers = new HashSet<Widget>();

		appenBreadCrumbs(new Breadcrumbs());

		// Try to restore the user's current session.
		MpDb.user_svc.resumeSession(new AsyncCallback<ResumeSessionResponse>() {
			public void onFailure(final Throwable caught) {
				// Dead. As a doornail. We cannot let the user
				// continue as we have no ObjectConstraints!
				new UnknownErrorDialog(caught, false).show();
			}

			public void onSuccess(final ResumeSessionResponse result) {
				final ResumeSessionResponse r = (ResumeSessionResponse) result;
				MpDb.doc = r.databaseObjectConstraints;
				MpDb.oc = r.objectConstraints;
				MpDb.setCurrentUser(r.user);
				finishOnModuleLoad();
				if (r.user == null) {
					new ServerOp<User>() {
						public void begin() {
							MpDb.mpdbGeneric_svc.getAutomaticLoginUser(this);
						}

						public void onSuccess(final User result) {
							if (result != null) {
								MpDb.setCurrentUser((User) result);
							}
						}
					}.begin();
				}
			}
		});

	}

	private void finishOnModuleLoad() {
		createHdrNav();
		
		// If there is no user we didn't receive a user change event,
		// as null (initial user) == null (current user).
		if (!MpDb.isLoggedIn())
			populateLogbar(null);

		// Throw away the loading message that users see while GWT
		// starts up and is able to finish loading its resources.
		final Element lm = RootPanel.get(CSS.LOADINGMESSAGE_ID).getElement();
		DOM.removeChild(DOM.getParent(lm), lm);

		// If we were given a state to jump to, go there. Otherwise
		// go to the introduction state, which displays some pretty
		// message about what we are all about.
		new ServerOp<String>() {
			public void begin() {
				MpDb.mpdbGeneric_svc.getBuildDate(this);
			}

			public void onSuccess(final String result) {
				footer("Last Update: " + (String) result);
			}
		}.begin();
		final String state = History.getToken();
		if (state.length() > 0) {
			GWT.log("Resume application at " + state, null);
			TokenSpace.dispatch(state);
			((Breadcrumbs) getFromBreadCrumbs()).update(state);
		} else {
			// notice(MpDb.lc_text.notice_Welcome());
			DeferredCommand.addCommand(TokenSpace.home);
		}
	}

	static void onCurrentUserChanged(final User n) {
		populateLogbar(n);
		inPageNotice.clear();
		if (n != null && !n.getEnabled()) {
			HTMLPanel p = new HTMLPanel("Your account is not confirmed. Please click the confirmation link in your email. <span id=\"resend-email\"></span>");
			p.setStyleName("confirm-account-msg");
			MLink resend = new MLink("Resend confirmation email", new ClickListener() {
				public void onClick(Widget sender) {
					new VoidServerOp() {
						@Override
						public void onSuccess() {
							inPageNotice.clear();
							inPageNotice.add(new Label("email send to your email address"));
						}

						@Override
						public void begin() {
							MpDb.user_svc.sendConfirmationCode(MpDb.currentUser(), this);
						}
					}.begin();
				}
			});
			resend.addStyleName(CSS.LIGHT_LINK);
			p.addAndReplaceElement(resend, "resend-email");
			inPageNotice.add(p);
		}
		dispatchCurrentUserChanged(contentContainer, n);
	}

	private static void dispatchCurrentUserChanged(final Widget w, final User u) {
		if (w instanceof UsesCurrentUser) {
			try {
				((UsesCurrentUser) w).onCurrentUserChanged(u);
			} catch (LoginRequiredException lre) {
				TokenSpace.home.execute();
				return;
			}
		}
		if (w instanceof Panel) {
			final Iterator<Widget> i = ((Panel) w).iterator();
			while (i.hasNext())
				dispatchCurrentUserChanged((Widget) i.next(), u);
		}
	}

	public static void dispatchCurrentPageChanged() {
		final Iterator<Widget> itr = pageChangeWatchers.iterator();
		while (itr.hasNext()) {
			final Widget w = (Widget) itr.next();
			if (w instanceof PageChangeListener) {
				((PageChangeListener) w).onPageChanged();
			}
		}
	}

	public static void registerPageWatcher(final Widget w) {
		pageChangeWatchers.add(w);
	}

	public static void removePageWatcher(final Widget w) {
		pageChangeWatchers.remove(w);
	}

	private static void populateLogbar(final User n) {
		logbar.clear();
		logbarLinks.clear();
		if (n != null) {
			HTML loggedIn = new HTML("Logged in as <span>" + MpDb.currentUser().getEmailAddress() + "</span>");
			loggedIn.setStyleName("identity");
			logbarLinks.add(loggedIn);
			logbarLinks.add(editProfileLink);
			logbarLinks.add(logoutLink);
			
		} else {
			logbarLinks.add(loginLink);
			logbarLinks.add(registerLink);
			Cookies.setCookie(MpDbConstants.USERID_COOKIE, "", new Date());
		}
		logbar.add(logbarLinks);
		logbar.add(reportBugLink);
	}

	public static void show(final Widget w) {
		contentContainer.clear();
		contentContainer.add(w);
		contentContainer.setHeight("100%");
	}

	public static void notice(final String text) {
		notice(new Label(text != null ? text : ""));
	}

	public static void notice(final Widget w) {
		noticeContainer.clear();
		noticeContainer.add(w);
	}

	public static void footer(final String text) {
		footer(new Label(text != null ? text : ""));
	}

	public static void footer(final Widget w) {
		footerContainer.clear();
		footerContainer.add(w);
	}

	public static void appenBreadCrumbs(final Widget w) {
		breadcrumbsBar.add(w);
	}

	public static Widget getFromBreadCrumbs() {
		return breadcrumbsBar.getWidget(0);
	}

	private void createHdrNav() {
		hdrnav.setAutoOpen(true);

		final MMenuBar projects = new MMenuBar(true);
		projects.addItem("My Projects", TokenSpace.allProjects);
		projects.addItem(LocaleHandler.lc_text.projectsMenu_NewProject(),
				TokenSpace.newProject);

		final MMenuBar dev = new MMenuBar(true);
		dev.addItem("Regenerate Constraints", new Command() {
			public void execute() {
				new ServerOp<ResumeSessionResponse>() {
					public void begin() {
						MpDb.mpdbGeneric_svc.regenerateConstraints(this);
					}

					public void onSuccess(ResumeSessionResponse result) {
						MpDb.oc = result.objectConstraints;
						MpDb.doc = result.databaseObjectConstraints;
					}
				}.begin();
			}
		});
		dev.addItem("Regenerate Search Indecies", new Command() {

			public void execute() {
				new ServerOp<Void>() {
					public void begin() {
						MpDb.search_svc.rebuildSearchIndex(this);
					}

					public void onSuccess(Void result) {
						Window.alert("done regenerating indicies");
					}
				}.begin();
			}

		});
		dev.addItem("JavaDocs", new Command() {
			public void execute() {
				Window.open(MpDb.JAVADOC_URL, "mpdb_javadoc", "");
			}
		});
		dev.addItem("JUnit Results", new Command() {
			public void execute() {
				Window.open(MpDb.JUNIT_URL, "mpdb_junit", "");
			}
		});

		hdrnav.addItem("My Samples", TokenSpace.samplesForUser);
		hdrnav.addItem(LocaleHandler.lc_text.projectMenu(), projects);
		hdrnav.addItem("Search", TokenSpace.search);
		hdrnav.addItem("Upload Data", TokenSpace.bulkUpload);
		hdrnav.addItem("Wiki", new Command() {
			public void execute() {
				Window.open(MpDb.WIKI_URL, "mpdb_wiki", "");
			}
		});
		if (!GWT.getHostPageBaseURL().contains("metpetweb"))
			hdrnav.addItem("Developers", dev);

	}
}
