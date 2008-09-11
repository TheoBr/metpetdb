package edu.rpi.metpetdb.client.ui.user;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.model.UserDTO;
import edu.rpi.metpetdb.client.ui.MpDb;
import edu.rpi.metpetdb.client.ui.ServerOp;

public class Confirmation extends FlowPanel implements ClickListener {
	
	private final TextBox confirmationCode;
	private final Button confirm;
	private final Label instructions;
	
	public Confirmation() {
		instructions = new Label("Enter your confirmation code and click Confirm");
		confirmationCode = new TextBox();
		confirm = new Button("Confirm", this);
		
		add(instructions);
		add(confirmationCode);
		add(confirm);
	}
	
	public Confirmation fill(final String uuid) {
		confirmationCode.setText(uuid);
		return this;
	}
	
	public void onClick(final Widget sender) {
		if (sender == confirm) {
			new ServerOp<UserDTO>() {

				@Override
				public void begin() {
					MpDb.user_svc.confirmUser(confirmationCode.getText(), this);
				}

				public void onSuccess(UserDTO result) {
					MpDb.setCurrentUser(result);
					instructions.setText("Your account is now enabled");
				}
				
			}.begin();
		}
	}

}
