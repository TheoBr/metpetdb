package edu.rpi.metpetdb.client.ui.user;

import java.util.Collection;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.model.RoleChange;
import edu.rpi.metpetdb.client.ui.MpDb;
import edu.rpi.metpetdb.client.ui.commands.ServerOp;
import edu.rpi.metpetdb.client.ui.commands.VoidServerOp;

/**
 * An interface for a sponsor to review role changes that they have pending
 * 
 * NOTE THIS IS JUST A PROOF OF CONCEPT
 * 
 * @author anthony
 * 
 */
public class ReviewRoleChanges extends FlexTable {

	public ReviewRoleChanges() {
		new ServerOp<Collection<RoleChange>>() {
			@Override
			public void begin() {
				MpDb.user_svc.getSponsorRoleChanges(MpDb.currentUser().getId(),
						this);
			}
			public void onSuccess(Collection<RoleChange> roleChanges) {
				clear();
				setText(0, 0, "Requesting User");
				setText(0, 1, "Requesting Role");
				setText(0, 2, "Request Date");
				setText(0, 3, "Reason");
				setText(0, 4, "Grant?");
				int row = 1;
				for (final RoleChange rc : roleChanges) {
					setText(row, 0, rc.getUser().toString());
					setText(row, 1, rc.getRole().toString());
					setText(row, 2, rc.getRequestReason());
					setText(row, 3, rc.getRequestDate().toString());
					final FlowPanel actions = new FlowPanel();
					actions.add(new Button("Yes", new ClickListener() {
						public void onClick(final Widget sender) {
							new VoidServerOp() {
								public void begin() {
									MpDb.user_svc.approveRoleChange(rc, this);
								}
								@Override
								public void onSuccess() {
								}
							}.begin();
						}
					}));
					actions.add(new Button("No", new ClickListener() {
						public void onClick(final Widget sender) {
							new VoidServerOp() {
								public void begin() {
									MpDb.user_svc.denyRoleChange(rc, this);
								}
								@Override
								public void onSuccess() {
								}
							}.begin();
						}
					}));
					setWidget(row, 4, actions);
					++row;
				}
			}
		}.begin();
	}
}
