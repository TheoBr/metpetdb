package edu.rpi.metpetdb.client.ui.left.side;

import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.model.UserDTO;
import edu.rpi.metpetdb.client.ui.TokenSpace;
import edu.rpi.metpetdb.client.ui.dialogs.LoginDialog;
import edu.rpi.metpetdb.client.ui.widgets.MLink;

public class UserInfo extends LeftColWidget implements UsesLeftColumn {
	private FlexTable ft;

	public UserInfo(final UserDTO user) {
		super("User Information");
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
			HTML notLoggedIn = new HTML(
					"<p class='notice'>You are not logged in.</p>");
			FlexTable pleaseLogIn = new FlexTable();
			pleaseLogIn.setWidget(0, 0, new Label("Please"));
			pleaseLogIn.setWidget(0, 1, login);
			pleaseLogIn.setWidget(0, 2, new Label("or"));
			pleaseLogIn.setWidget(0, 3, register);
			pleaseLogIn.setWidget(0, 4, new Label("to gain"));
			Label pleaseLogIn2 = new Label("full access to MetPetDB's");
			pleaseLogIn.setWidget(1, 0, pleaseLogIn2);
			pleaseLogIn.setWidget(2, 0, new Label("features:"));
			pleaseLogIn.getFlexCellFormatter().setColSpan(1, 0, 5);
			pleaseLogIn.getFlexCellFormatter().setColSpan(2, 0, 5);
			pleaseLogIn.getFlexCellFormatter().setWidth(0, 2, "16px");
			pleaseLogIn.setWidth("196px");

			HTML features = new HTML(
					"<p><ul class='bullet'><li><strong>Upload sample data.</strong></li><li><strong>Save searches.</strong></li><li><strong>Create/Join projects.</strong></li></ul></p>");
			ft.setWidget(0, 0, notLoggedIn);
			ft.setWidget(1, 0, pleaseLogIn);
			ft.setWidget(2, 0, features);
		} else {
			Label username = new Label("Welcome, " + user.getFirstName());
			ft.setWidget(0, 0, username);
		}

		this.add(ft);
	}
	public void onPageChanged() {
		// MetPetDBApplication.removePageWatcher(this);
		// MetPetDBApplication.removeFromLeft(this);
	}

}
