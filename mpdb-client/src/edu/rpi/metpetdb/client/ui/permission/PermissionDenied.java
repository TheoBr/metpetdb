package edu.rpi.metpetdb.client.ui.permission;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;

import edu.rpi.metpetdb.client.locale.LocaleHandler;
import edu.rpi.metpetdb.client.ui.MetPetDBApplication;

public class PermissionDenied extends FlowPanel {
	// public PermissionDenied(final String reason) {
	public PermissionDenied() {
		MetPetDBApplication.clearLeftSide();
		final Label msg = new Label(LocaleHandler.lc_text.Permission_Denied());
		final Label msgReason = new Label(LocaleHandler.lc_text
				.Permission_NotYours());
		msg.setStyleName("h1");
		msgReason.setStyleName("h2");
		this.add(msg);
		this.add(msgReason);
	}
}
