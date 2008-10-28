package edu.rpi.metpetdb.client.ui.left.side;

import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.model.User;
import edu.rpi.metpetdb.client.ui.TokenSpace;
import edu.rpi.metpetdb.client.ui.dialogs.LoginDialog;
import edu.rpi.metpetdb.client.ui.widgets.MLink;

public class UserInfo extends LeftColWidget implements UsesLeftColumn {
	private FlexTable ft;
	private static final String LOGIN_ID = "login-link";

	public UserInfo(final User user) {
		super("");
		this.setStyleName("lcol-MyProjects");
		// MetPetDBApplication.registerPageWatcher(this);
		ft = new FlexTable();
		ft.setCellSpacing(3);
		if (user == null) {
			final MLink login = new MLink("login", new ClickListener() {
				public void onClick(final Widget sender) {
					new LoginDialog(null).show();
				}
			});
			final MLink register = new MLink("register", TokenSpace.register);
			HTMLPanel notLoggedIn = new HTMLPanel(
					"<p class=\"notice\">You are not logged in.</p>"+
					"<p>Please <span id=\""+ LOGIN_ID + "\"></span> to gain full access to MetPetDB's features:</p>"+
					"<ul class=\"bullet\">" +
					"<li><strong>Upload sample data.</strong></li>" +
					"<li><strong>Save searches.</strong></li>" +
					"<li><strong>Create/Join projects.</strong></li>" +
					"</ul>");
			if (notLoggedIn.getElementById(LOGIN_ID) != null)
				notLoggedIn.addAndReplaceElement(login, LOGIN_ID);
			add(notLoggedIn);
		} else {
			Label username = new Label("Welcome, " + user.getName());
			add(username);
		}
	}
	public void onPageChanged() {
		// MetPetDBApplication.removePageWatcher(this);
		// MetPetDBApplication.removeFromLeft(this);
	}

}
