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
import edu.rpi.metpetdb.client.ui.left.side.UsesLeftColumn;
import edu.rpi.metpetdb.client.ui.user.UsesCurrentUser;
import edu.rpi.metpetdb.client.ui.widgets.MLink;
import edu.rpi.metpetdb.client.ui.widgets.MMenuBar;
import edu.rpi.metpetdb.client.ui.widgets.MText;

/** Main application entry point. */
public class MetPetDBApplication implements EntryPoint {

	private static final String WIKI_URL = "http://trinity.db.cs.rpi.edu/xwiki/bin/view/Main/WebHome";
	private static final String GIT_URL = "http://cgi2.cs.rpi.edu/~pearcs/gitweb.cgi?p=MetPetDB.git";
	private static final String SVN_URL = "http://www.cs.rpi.edu/~watera2/svn/index.php";
	private static final String JAVADOC_URL = "http://samana.cs.rpi.edu:8080/watera2/api/index.html";
	private static final String JUNIT_URL = "http://samana.cs.rpi.edu:8080/watera2/reports/index.html";

	private static RootPanel loginBar;
	private static MMenuBar hdrnav;
	private static RootPanel breadcrumbsBar;
	// private static MenuBar datePanel;
	private static RootPanel contentContainer;
	private static RootPanel noticeContainer;
	private static RootPanel leftContainer;
	private static HashSet<Widget> pageChangeWatchers;
	private static RootPanel footerContainer;

	// public static Html introduction;
	// public static Hyperlink logoLink;

	public void onModuleLoad() {
		GWT.setUncaughtExceptionHandler(ErrorHandler.INSTANCE);
		History.addHistoryListener(TokenSpace.INSTANCE);

		loginBar = RootPanel.get(CSS.LOGBAR_ID);
		breadcrumbsBar = RootPanel.get(CSS.BREADCRUMBS_ID);
		hdrnav = new MMenuBar();
		RootPanel.get(CSS.HDRNAV_ID).add(hdrnav);
		contentContainer = RootPanel.get(CSS.CONTENT_ID);
		noticeContainer = RootPanel.get(CSS.NOTICE_ID);
		leftContainer = RootPanel.get(CSS.LEFTCOL_ID);
		footerContainer = RootPanel.get(CSS.FOOTER_ID);

		// make MPDB logo a link to the introduction screen
		/*
		 * logoLink = MpDb.factory.getLogoLink();
		 * logoLink.setTargetHistoryToken(
		 * TokenSpace.introduction.makeToken(null));
		 * logoLink.addMouseEventListener(new MouseEventAdapter() { public void
		 * onClick(MouseClickEvent event) { TokenSpace.introduction.execute(); }
		 * });
		 */

		// setupIntroduction();
		pageChangeWatchers = new HashSet<Widget>();

		appenBreadCrumbs(new Breadcrumbs());

		// Try to restore the user's current session.
		//
		MpDb.user_svc.resumeSession(new AsyncCallback<ResumeSessionResponse>() {
			public void onFailure(final Throwable caught) {
				// Dead. As a doornail. We cannot let the user
				// continue as we have no ObjectConstraints!
				//
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
			createLoginBarLoggedOut();

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
		loginBar.clear();
		if (n != null)
			createLoginBarLoggedIn();
		else
			createLoginBarLoggedOut();
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
			if (w instanceof UsesLeftColumn) {
				((UsesLeftColumn) w).onPageChanged();
			}
		}
	}

	public static void registerPageWatcher(final Widget w) {
		pageChangeWatchers.add(w);
	}

	public static void removePageWatcher(final Widget w) {
		pageChangeWatchers.remove(w);
	}

	private static void createLoginBarLoggedOut() {
		loginBar.add(new MLink(LocaleHandler.lc_text.buttonLogin(),
				new ClickListener() {
					public void onClick(final Widget sender) {
						new LoginDialog(null).show();
					}
				}));
		loginBar.add(new MLink(LocaleHandler.lc_text.buttonRegister(),
				TokenSpace.register));
		Cookies.setCookie(MpDbConstants.USERID_COOKIE, "", new Date());
	}

	private static void createLoginBarLoggedIn() {
		MText userName = new MText(MpDb.currentUser().getEmailAddress());
		userName.setStyleName("logbar-username");
		loginBar.add(userName);
		loginBar.add(new MLink(LocaleHandler.lc_text.tools_EditProfile(),
				TokenSpace.editProfile));
		loginBar.add(new MLink(LocaleHandler.lc_text.buttonLogout(),
				new ClickListener() {
					public void onClick(Widget sender) {
						MpDb.setCurrentUser(null);
						TokenSpace.home.execute();
						History.newItem(TokenSpace.home.getName());
					}
				}));
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
		footer(new MLink(text != null ? text : "", ""));
	}

	public static void footer(final Widget w) {
		footerContainer.clear();
		footerContainer.add(w);
	}

	public static void left(final Widget w) {
		leftContainer.clear();
		leftContainer.add(w);
	}

	public static void appenLeft(final Widget w) {
		leftContainer.add(w);
	}

	public static void removeFromLeft(final Widget w) {
		if (w != null)
			leftContainer.remove(w);
	}

	public static int getFromLeft(final Widget w) {
		return leftContainer.getWidgetIndex(w);
	}

	public static Widget getFromLeft(final int index) {
		return leftContainer.getWidget(index);
	}

	public static void resetLeftSide() {
		leftContainer.clear();
		final Element sidebarElem = DOM.getElementById("sidebar-Default");
		final HTML sidebar = new HTML();
		// sidebar.setHTML(sidebarElem.toString());
		leftContainer.add(sidebar);
	}

	public static void clearLeftSide() {
		leftContainer.clear();
	}

	public static int getLeftCount() {
		return leftContainer.getWidgetCount();
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
		// about.addItem(LocaleHandler.lc_text.aboutMenu_VersionControl(),
		// new Command() {
		// public void execute() {
		// Window.open(GIT_URL, "mpdb_git", "");
		// }
		// });
		// about.addItem("SVN Version Control", new Command() {
		// public void execute() {
		// Window.open(SVN_URL, "mpdb_svn", "");
		// }
		// });
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
		dev.addItem("JavaDocs", new Command() {
			public void execute() {
				Window.open(JAVADOC_URL, "mpdb_javadoc", "");
			}
		});
		dev.addItem("JUnit Results", new Command() {
			public void execute() {
				Window.open(JUNIT_URL, "mpdb_junit", "");
			}
		});

		// hdrnav.addItem(LocaleHandler.lc_text.homeMenu(),
		// TokenSpace.introduction);
		hdrnav.addItem("My Samples", TokenSpace.samplesForUser);
		hdrnav.addItem(LocaleHandler.lc_text.projectMenu(), projects);
		hdrnav.addItem("Search", TokenSpace.search);
		hdrnav.addItem("Wiki", new Command() {
			public void execute() {
				Window.open(WIKI_URL, "mpdb_wiki", "");
			}
		});
		hdrnav.addItem("Developers", dev);

	}

	/**
	 * Creates the introduction screen and assigns click listeners to necessary
	 * links
	 */
	private static void setupIntroduction() {
		// introduction = MpDb.factory.getIntroduction();
	}
}
