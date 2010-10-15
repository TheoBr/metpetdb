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

public class Contribution extends FlowPanel implements ClickListener {

	private final TextBox contributionCode;
	private final Button contribute;
	private final Label instructions;

	public Contribution() {
		instructions = new Label(
				"Enter your confirmation code and click Approve");
		contributionCode = new TextBox();
		contribute = new Button("Approve", this);
		add(instructions);
		add(contributionCode);
		add(contribute);

	}

	public Contribution fill(final String uuid) {
		contributionCode.setText(uuid);
		return this;
	}

	public void onClick(final Widget sender) {
		if (sender == contribute) {
			new ServerOp<User>() {

				@Override
				public void begin() {
					MpDb.user_svc.confirmContributor(contributionCode.getText(), this);
				}

				public void onSuccess(User result) {

					if (result != null)
					{
					instructions.setText(result.getName() + "is now a contributor");
					History.newItem(TokenSpace.editProfile.makeToken(null));
					}
				}
			}.begin();
		}
	}

}
