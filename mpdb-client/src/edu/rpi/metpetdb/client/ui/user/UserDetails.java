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
			new TextAttribute(MpDb.doc.User_emailAddress),
			new TextAttribute(MpDb.doc.User_name),
	};

	private final DetailsPanel<UserDTO> p_user;

	public UserDetails(final String username) {
		p_user = new DetailsPanel<UserDTO>(mainAttributes);
		add(p_user);
		new ServerOp<UserDTO>() {
			public void begin() {
				MpDb.user_svc.details(username, this);
			}
			public void onSuccess(final UserDTO result) {
				p_user.show((UserDTO) result);
			}
		}.begin();
	}
}
