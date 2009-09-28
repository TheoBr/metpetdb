package edu.rpi.metpetdb.client.ui.dialogs;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.error.MpDbException;
import edu.rpi.metpetdb.client.locale.LocaleHandler;
import edu.rpi.metpetdb.client.ui.commands.ServerOp;

public class MpDbExceptionDialog extends MDialogBox implements ClickListener {
	private final Button close;
	private final ServerOp continuation;

	public MpDbExceptionDialog(final MpDbException err, final ServerOp r) {
		// TODO create message 'Something went wrong' add button to expand full error
		close = new Button(LocaleHandler.lc_text.buttonClose(), this);
		continuation = r;

		final FlowPanel p = new FlowPanel();
		p.add(new Label("Sorry, something went wrong. If this problem is affecting the")); 
		p.add(new Label("functionality of the system, please try reloading this page.")); 
		p.add(new Label("You can see the details of the error message below."));
		final Label hideLabel = new Label("Hide");
		final DisclosurePanel dp = new DisclosurePanel(hideLabel,false);
		p.add(dp);
		final FlowPanel container = new FlowPanel();
		container.add(new Label(LocaleHandler.lc_text.errorTitle_InvalidData()));
		container.add(new Label(err.format()));
		dp.add(container);
		dp.setStylePrimaryName("criteria-collapse");
		dp.setAnimationEnabled(true);
		p.add(close);
		setWidget(p);
	}

	public void onClick(final Widget sender) {
		if (close == sender)
			hide();
		if (continuation != null)
			continuation.cancel();
	}
}
