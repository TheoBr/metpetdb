package edu.rpi.metpetdb.client.admin.ui;

import com.google.gwt.core.client.GWT;

import edu.rpi.metpetdb.client.model.User;
import edu.rpi.metpetdb.client.service.admin.AdminUserService;
import edu.rpi.metpetdb.client.service.admin.AdminUserServiceAsync;

public class MpDb extends edu.rpi.metpetdb.client.ui.MpDb {
	
	public static final AdminUserServiceAsync adminUser_svc;
	
	static {
		adminUser_svc = (AdminUserServiceAsync) bindService(
				GWT.create(AdminUserService.class), "adminUser");
	}
	
	public static void setCurrentUser(final User user) {
		currentUser = user;
	}

}
