package edu.rpi.metpetdb.client.ui.dialogs;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.locale.LocaleHandler;
import edu.rpi.metpetdb.client.ui.commands.ServerOp;
import edu.rpi.metpetdb.client.ui.input.Submit;

public class ConfirmationDialogBox extends MDialogBox implements ClickListener,
		KeyboardListener {
	private final Button submit;
	private final Button cancel;
	private final Button no;
	private final Label msg;

	// cancel = false will display just a button that says "continue"
	// cancel = true will display two buttons, "yes", and "cancel"
	// threeButtons = true will display three buttons, "yes", "no", and "cancel"
	public ConfirmationDialogBox(final String msg, final boolean cancel) {
		this(msg,cancel,false);
	}
	
	public ConfirmationDialogBox(final String msg, final boolean cancel, final boolean threeButtons) {
		this.msg = new Label(msg);
		if (cancel)
			this.submit = new Submit(LocaleHandler.lc_text.buttonYes(), this);
		else
			this.submit = new Submit(LocaleHandler.lc_text.buttonContinue(),
					this);
		if (threeButtons) {
			this.no = new Submit("No", this);
		} else this.no = null;

		this.cancel = new Button(LocaleHandler.lc_text.buttonCancel(), this);

		final VerticalPanel vp = new VerticalPanel();

		vp.add(this.msg);

		final HorizontalPanel hp = new HorizontalPanel();
		hp.add(submit);
		if (threeButtons)
			hp.add(this.no);
		if (cancel)
			hp.add(this.cancel);

		hp.setSpacing(5);
		vp.add(hp);
		vp.setCellHorizontalAlignment(hp, HasHorizontalAlignment.ALIGN_CENTER);
		this.setWidget(vp);
	}

	public void onClick(final Widget sender) {
		if (cancel == sender) {
			onCancel();
		}
		else if (submit == sender) {
			onSubmit();
		} else if (no == sender) {
			onNo();
		}
		hide();
	}
	public void onKeyPress(final Widget sender, final char kc, final int mod) {
		if (kc == KEY_ENTER) {
			onSubmit();
		}
		hide();
	}

	public void onKeyDown(final Widget sender, final char kc, final int mod) {
	}

	public void onKeyUp(final Widget sender, final char kc, final int mod) {
	}

	public void onCancel() {};

	public void onSubmit() {};
	
	public void onNo() {};

}
