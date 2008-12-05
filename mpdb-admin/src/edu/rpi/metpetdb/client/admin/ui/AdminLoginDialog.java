package edu.rpi.metpetdb.client.admin.ui;

import edu.rpi.metpetdb.client.model.User;
import edu.rpi.metpetdb.client.ui.FormOp;
import edu.rpi.metpetdb.client.ui.ServerOp;
import edu.rpi.metpetdb.client.ui.dialogs.LoginDialog;

public class AdminLoginDialog extends LoginDialog {

	public AdminLoginDialog(ServerOp<?> r) {
		super(r);
		//Remove forgot password tab
		tabs.remove(p_emailIdx);
	}
	
	protected void doLogin() {
		new FormOp<User>(p_main) {
			protected void onSubmit() {
				MpDb.adminUser_svc.startSession(ssr, this);
			}
			public void onFailure(final Throwable e) {
				ssr.setPassword(null);
				p_main.edit(ssr);
				super.onFailure(e);
			}
			public void onSuccess(final User result) {
				MpDb.setCurrentUser(result);
				hide();
				if (continuation != null)
					continuation.begin();
			}
		}.begin();
	}

}
