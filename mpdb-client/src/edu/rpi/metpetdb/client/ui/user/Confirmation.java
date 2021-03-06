package edu.rpi.metpetdb.client.ui.user;

import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.model.User;
import edu.rpi.metpetdb.client.ui.MpDb;
import edu.rpi.metpetdb.client.ui.TokenSpace;
import edu.rpi.metpetdb.client.ui.commands.ServerOp;

public class Confirmation extends FlowPanel implements ClickListener {

	private final TextBox confirmationCode;
	private final Button confirm;
	private final Label instructions;

	public Confirmation() {
		instructions = new Label(
				"Enter your confirmation code and click Confirm");
		confirmationCode = new TextBox();
		confirm = new Button("Confirm", this);
		add(instructions);
		
		if (!MpDb.currentUser().getEnabled()) {
			add(confirmationCode);
			add(confirm);
			
		} else {
			instructions
					.setText("Your account is already enabled - you cannot confirm it.");
		}
		
		
	}

	public Confirmation fill(final String uuid) {
		confirmationCode.setText(uuid);
		return this;
	}

	public void onClick(final Widget sender) {
		if (sender == confirm) {
			new ServerOp<User>() {

				@Override
				public void begin() {
					MpDb.user_svc.confirmUser(confirmationCode.getText(), this);
				}

				public void onSuccess(User result) {
					MpDb.setCurrentUser(result);
					instructions.setText("Your account is now enabled");
					History.newItem(TokenSpace.editProfile.makeToken(null));
					
				}
			}.begin();
		}
		
	}

}
