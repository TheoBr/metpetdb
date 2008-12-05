package edu.rpi.metpetdb.client.admin.ui;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;

import edu.rpi.metpetdb.client.model.ResumeSessionResponse;
import edu.rpi.metpetdb.client.model.User;
import edu.rpi.metpetdb.client.ui.MpDb;
import edu.rpi.metpetdb.client.ui.ServerOp;
import edu.rpi.metpetdb.client.ui.dialogs.UnknownErrorDialog;

public class MetPetDBAdminApplication implements EntryPoint {

	public void onModuleLoad() {
		RootPanel.get("loadingmessage").setVisible(false);
		
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
				finishOnModuleLoad();
				if (r.user == null) {
					new ServerOp<User>() {
						public void begin() {
							MpDb.mpdbGeneric_svc.getAutomaticLoginUser(this);
						}

						public void onSuccess(final User result) {
							if (result != null) {
								MpDb.setCurrentUser(result);
							}
						}
					}.begin();
				}
			}
		});
	}
	
	public void finishOnModuleLoad() {
		new ServerOp<Void>() {
			@Override
			public void begin() {
				final AdminLoginDialog loginDialog = new AdminLoginDialog(this);
				loginDialog.show();
			}

			public void onSuccess(Void result) {
				RootPanel.get().add(new Label("you have perms"));
			}
		}.begin();
	}

}
