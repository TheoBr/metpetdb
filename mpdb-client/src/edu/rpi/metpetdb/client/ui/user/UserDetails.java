package edu.rpi.metpetdb.client.ui.user;

import com.google.gwt.user.client.ui.FlowPanel;

import edu.rpi.metpetdb.client.model.UserDTO;
import edu.rpi.metpetdb.client.ui.MpDb;
import edu.rpi.metpetdb.client.ui.ServerOp;
import edu.rpi.metpetdb.client.ui.input.DetailsPanel;
import edu.rpi.metpetdb.client.ui.input.attributes.GenericAttribute;
import edu.rpi.metpetdb.client.ui.input.attributes.TextAttribute;

public class UserDetails extends FlowPanel {
	private static final GenericAttribute[] mainAttributes = {
			new TextAttribute(MpDb.doc.User_username),
			new TextAttribute(MpDb.doc.User_emailAddress),};
	// new TextAttribute(MpDb.oc.Project_name)

	private final DetailsPanel p_user;

	public UserDetails(final String username) {
		p_user = new DetailsPanel(mainAttributes);
		add(p_user);
		new ServerOp() {
			public void begin() {
				MpDb.user_svc.details(username, this);
			}
			public void onSuccess(final Object result) {
				p_user.show((UserDTO) result);
			}
		}.begin();
	}
}
