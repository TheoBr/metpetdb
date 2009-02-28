package edu.rpi.metpetdb.client.ui.dialogs;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
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
		close = new Button(LocaleHandler.lc_text.buttonClose(), this);
		continuation = r;

		final FlowPanel p = new FlowPanel();
		p.add(new Label(LocaleHandler.lc_text.errorTitle_InvalidData()));
		p.add(new Label(err.format()));
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
